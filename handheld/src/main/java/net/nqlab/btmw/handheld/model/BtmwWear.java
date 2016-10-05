package net.nqlab.btmw.handheld.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.DataApi;

import net.nqlab.btmw.api.TourPlanSchedulePoint;
import net.nqlab.btmw.model.WearProtocol;

public class BtmwWear {
    public interface BtmwWearListener {
        void onGoNext();

        void onConnect();

        void onSoundRecorded(String date, byte[] data);
    }

    private GoogleApiClient mGoogleApiClient;
	private BtmwApi mBtmwApi;
    private BtmwWearListener mListener;
	private String mBaseTime;
	private String mStartTime;

	public BtmwWear(Context context, BtmwApi api, BtmwWearListener listener) {
		mBtmwApi = api;
        mListener = listener;
		final Handler handler = new Handler(); 

		mGoogleApiClient = new GoogleApiClient.Builder(context)
			.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle connectionHint) {
                    Wearable.MessageApi.addListener(mGoogleApiClient, new MessageApi.MessageListener() {
                        @Override
                        public void onMessageReceived(MessageEvent messageEvent) {
							handler.post(new Runnable() {
								@Override
								public void run() {
		                            BtmwWear.this.mListener.onGoNext();
								}
							});
                        }
                    });

                    Wearable.DataApi.addListener(mGoogleApiClient, new DataApi.DataListener() {
                        @Override
                        public void onDataChanged(DataEventBuffer dataEvents) {
                            for (DataEvent event : dataEvents) {
                                // event.getDataItem().getUri()
                                if (event.getType() == DataEvent.TYPE_DELETED) {
                                    // nothing to do

                                } else if (event.getType() == DataEvent.TYPE_CHANGED) {
                                    BtmwWear.this.notifyDataRecieved(event.getDataItem());
                                }
                            }
                        }
                    });

                    BtmwWear.this.mListener.onConnect();
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

    private void notifyDataRecieved(DataItem item) {
        DataMap dataMap = DataMap.fromByteArray(item.getData());
        if (item.getUri().getPath().equals(WearProtocol.REQUEST_SOUND)) {
            byte[] sound = dataMap.getByteArray(WearProtocol.REQUEST_SOUND_PARAM_DATA);
            String date = dataMap.getString(WearProtocol.REQUEST_SOUND_PARAM_DATE);
            mListener.onSoundRecorded(date, sound);
        }
    }

    public void connect() {
		if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
		}
	}

	public void disconnect() {
		if (isConnected()) {
			mGoogleApiClient.disconnect();
		}	
	}

    public boolean isConnected() {
        return mGoogleApiClient != null && mGoogleApiClient.isConnected();
    }

	public void setBaseTime(String time) {
		mBaseTime = time;
	}

	public void setStartTime(String time) {
		mStartTime = time;
	}

	public void sendPoint(TourPlanSchedulePoint pointPrev, TourPlanSchedulePoint pointCurrent, int pointType) {
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            return;
        }

        String strJsonPrev = mBtmwApi.toJson(pointPrev);
        String strJsonCurrent = mBtmwApi.toJson(pointCurrent);

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(WearProtocol.REQUEST_POINT);
        putDataMapReq.getDataMap().putString(WearProtocol.REQUEST_POINT_PARAM_PREVIOUS, strJsonPrev);
        putDataMapReq.getDataMap().putString(WearProtocol.REQUEST_POINT_PARAM_DATA, strJsonCurrent);
        putDataMapReq.getDataMap().putString(WearProtocol.REQUEST_POINT_PARAM_BASE_DATE, mBaseTime);
        putDataMapReq.getDataMap().putString(WearProtocol.REQUEST_POINT_PARAM_START_DATE, mStartTime);
        putDataMapReq.getDataMap().putInt(WearProtocol.REQUEST_POINT_PARAM_POINT_TYPE, pointType);

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(DataApi.DataItemResult dataItemResult) {
                Log.d("Wear", "" + dataItemResult.getStatus());
            }
        });
    }
}
