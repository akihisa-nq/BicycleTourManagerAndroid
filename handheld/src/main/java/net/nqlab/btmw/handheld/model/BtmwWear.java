package net.nqlab.btmw.handheld.model;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.wearable.Wearable;

public class BtmwWear {
	private GoogleApiClient mApi;

	public BtmwWear(Context context) {
		mApi = new GoogleApiClient.Builder(context)
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
		if (mApi != null && !mApi.isConnected()) {
			mApi.connect();
		}
	}

	public void disconnect() {
		if (mApi != null && mApi.isConnected()) {
			mApi.disconnect();
		}	
	}
}
