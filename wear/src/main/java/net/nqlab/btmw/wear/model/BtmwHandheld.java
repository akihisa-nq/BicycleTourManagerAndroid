package net.nqlab.btmw.wear.model;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.DataApi;

import net.nqlab.btmw.api.TourPlanSchedulePoint;
import net.nqlab.btmw.api.SerDes;
import net.nqlab.btmw.model.WearProtocol;

public class BtmwHandheld {
	private GoogleApiClient mGoogleApiClient;
	private SerDes mSerDes;

	public BtmwHandheld(Context context) {
		mSerDes = new SerDes();
	
		mGoogleApiClient = new GoogleApiClient.Builder(context)
			.addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle connectionHint) {
					Wearable.DataApi.addListener(mGoogleApiClient, new DataApi.DataListener() {
		                @Override
						public void onDataChanged(DataEventBuffer dataEvents) {
							for (DataEvent event : dataEvents) {
								// event.getDataItem().getUri()
								if (event.getType() == DataEvent.TYPE_DELETED) {
									// nothing to do

								} else if (event.getType() == DataEvent.TYPE_CHANGED) {
									DataMap dataMap = DataMap.fromByteArray(event.getDataItem().getData());
									String json = dataMap.getString(WearProtocol.REQUEST_POINT_SET_PARAM_DATA);
									TourPlanSchedulePoint point = (TourPlanSchedulePoint)mSerDes.fromJson(json, TourPlanSchedulePoint.class);
									// notify to activity
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

}
