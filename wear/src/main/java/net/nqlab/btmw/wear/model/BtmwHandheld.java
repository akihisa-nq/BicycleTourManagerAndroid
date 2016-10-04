package net.nqlab.btmw.wear.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.DataApi;

import net.nqlab.btmw.api.TourPlanSchedulePoint;
import net.nqlab.btmw.api.SerDes;
import net.nqlab.btmw.model.WearProtocol;

import java.util.Date;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BtmwHandheld {
	public interface BtmwHandheldListener {
		public void onSetPoint(String base, String start, TourPlanSchedulePoint pointPrevious, TourPlanSchedulePoint pointCurrent, int pointType);
	}

	private GoogleApiClient mGoogleApiClient;
	private SerDes mSerDes;
	private BtmwHandheldListener mListener;
	private Handler mHandler; 

	public BtmwHandheld(Context context, BtmwHandheldListener listener) {
		mSerDes = new SerDes();
		mListener = listener;
		mHandler = new Handler();

		mGoogleApiClient = new GoogleApiClient.Builder(context)
			.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle connectionHint) {
                    BtmwHandheld.this.recieveTourPlanSchedule();

					Wearable.DataApi.addListener(mGoogleApiClient, new DataApi.DataListener() {
		                @Override
						public void onDataChanged(DataEventBuffer dataEvents) {
							for (DataEvent event : dataEvents) {
								// event.getDataItem().getUri()
								if (event.getType() == DataEvent.TYPE_DELETED) {
									// nothing to do

								} else if (event.getType() == DataEvent.TYPE_CHANGED) {
                                    if (event.getDataItem().getUri().getPath().equals(WearProtocol.REQUEST_POINT)) {
                                        DataMap dataMap = DataMap.fromByteArray(event.getDataItem().getData());
										BtmwHandheld.this.notifyDataRecieved(dataMap);
                                    }
								}
							}
						}
					});
                }

                @Override
                public void onConnectionSuspended(int cause) {
                }
			})
			.addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult result) {
                }
			})
			.addApi(Wearable.API)
			.build();
	}

	private void notifyDataRecieved(DataMap dataMap) {
		String jsonPrevious = dataMap.getString(WearProtocol.REQUEST_POINT_PARAM_PREVIOUS);
		final TourPlanSchedulePoint pointPrevious = (TourPlanSchedulePoint)mSerDes.fromJson(jsonPrevious, TourPlanSchedulePoint.class);
		String json = dataMap.getString(WearProtocol.REQUEST_POINT_PARAM_DATA);
		final TourPlanSchedulePoint point = (TourPlanSchedulePoint)mSerDes.fromJson(json, TourPlanSchedulePoint.class);
		final String base = dataMap.getString(WearProtocol.REQUEST_POINT_PARAM_BASE_DATE);
		final String start = dataMap.getString(WearProtocol.REQUEST_POINT_PARAM_START_DATE);
		final int pointType = dataMap.getInt(WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE);
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mListener.onSetPoint(base, start, pointPrevious, point, pointType);
			}
		});
	}

	public void connect() {
		if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();
		}
	}

	public void disconnect() {
		if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}	
	}

    private void recieveTourPlanSchedule() {
        PendingResult<DataItemBuffer> dataItems = Wearable.DataApi.getDataItems(mGoogleApiClient);
        dataItems.setResultCallback(new ResultCallback<DataItemBuffer>() {
            @Override
            public void onResult(DataItemBuffer dataItems) {
                for (DataItem dataItem : dataItems) {
                    if (dataItem.getUri().getPath().equals(WearProtocol.REQUEST_POINT)) {
                        DataMap dataMap = DataMap.fromByteArray(dataItem.getData());
						BtmwHandheld.this.notifyDataRecieved(dataMap);
                    }
                }
            }
        });
    }

	public void requestNextPoint() {
		Observable
			.create(new Observable.OnSubscribe<MessageApi.SendMessageResult>() {
				@Override
				public void call(final Subscriber<? super MessageApi.SendMessageResult> subscriber) {
					NodeApi.GetConnectedNodesResult nodeResult = Wearable.NodeApi
							.getConnectedNodes(BtmwHandheld.this.mGoogleApiClient).await();
					for (Node node : nodeResult.getNodes()) {
						PendingResult<MessageApi.SendMessageResult> result =
							Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), WearProtocol.REQUEST_NEXT, "".getBytes());
						result.setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
							@Override
							public void onResult(@NonNull MessageApi.SendMessageResult sendMessageResult) {
								subscriber.onNext(sendMessageResult);
							}
						});
					}

					subscriber.onCompleted();
				}
			})
			.subscribeOn(Schedulers.newThread())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Observer<MessageApi.SendMessageResult>() {
				@Override
				public void onCompleted() {
					// 処理完了コールバック
				}

				@Override
				public void onError(Throwable e) {
					// 処理内で例外が発生すると自動的にonErrorが呼ばれる
				}

				@Override
				public void onNext(MessageApi.SendMessageResult result) {
				}
			});
	}

	public void sendSoundData(byte[] data) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(WearProtocol.REQUEST_SOUND);
        putDataMapReq.getDataMap().putByteArray(WearProtocol.REQUEST_SOUND_PARAM_DATA, data);
        putDataMapReq.getDataMap().putString(WearProtocol.REQUEST_SOUND_PARAM_DATE, mSerDes.fromDateToString(new Date()));
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();

        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(DataApi.DataItemResult dataItemResult) {
                Log.d("Handheld", "" + dataItemResult.getStatus());
            }
        });
	}

	public SerDes getSerDes() {
        return mSerDes;
    }
}
