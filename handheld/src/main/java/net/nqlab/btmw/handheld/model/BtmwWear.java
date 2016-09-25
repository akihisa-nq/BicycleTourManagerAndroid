package net.nqlab.btmw.handheld.model;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.DataApi;

import net.nqlab.btmw.api.TourPlanSchedulePoint;
import net.nqlab.btmw.model.WearProtocol;

public class BtmwWear {
	private GoogleApiClient mGoogleApiClient;
	private BtmwApi mBtmwApi;

	public BtmwWear(Context context, BtmwApi api) {
		mBtmwApi = api;

		mGoogleApiClient = new GoogleApiClient.Builder(context)
			.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle connectionHint) {
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

	public void sendPoint(TourPlanSchedulePoint point) {
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            return;
        }

        String strJson = mBtmwApi.toJson(point);

        PutDataMapRequest putDataMapReq = PutDataMapRequest.create(WearProtocol.REQUEST_POINT_SET);
        putDataMapReq.getDataMap().putString(WearProtocol.REQUEST_POINT_SET_PARAM_DATA, strJson);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
    }
}
