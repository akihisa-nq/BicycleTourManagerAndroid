package net.nqlab.btmw.handheld.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
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

	public void setBaseTime(String time) {
		mBaseTime = time;
	}

	public void setStartTime(String time) {
		mStartTime = time;
	}

	public void sendPoint(TourPlanSchedulePoint point) {
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            return;
        }

        String strJson = mBtmwApi.toJson(point);

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(WearProtocol.REQUEST_POINT);
        putDataMapReq.getDataMap().putString(WearProtocol.REQUEST_POINT_PARAM_DATA, strJson);
        putDataMapReq.getDataMap().putString(WearProtocol.REQUEST_POINT_PARAM_START_DATE, mStartTime);

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
