package net.nqlab.btmw.handheld.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import net.nqlab.btmw.api.TourGoApi;
import net.nqlab.btmw.api.TourGoCreateResult;
import net.nqlab.btmw.api.TourGoEvent;
import net.nqlab.btmw.api.TourGoUpdateResult;
import net.nqlab.btmw.api.TourPlanList;
import net.nqlab.btmw.handheld.R;
import net.nqlab.btmw.handheld.model.BtmwApi;
import net.nqlab.btmw.handheld.model.TourGo;
import net.nqlab.btmw.handheld.model.TourGoPassPoint;
import net.nqlab.btmw.handheld.view.ListLocalGoListViewAdapter;
import net.nqlab.btmw.handheld.view.ListOnlineListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListLocalGoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_local_go);
        getBtmwApplication().setupNavigation(this, R.id.drawer_layout_list_local_go);

        ListView listView = (ListView)findViewById(R.id.listView);
        final ListLocalGoListViewAdapter adapter = new ListLocalGoListViewAdapter(
                this,
                getBtmwApplication().getSaveData(),
                getBtmwApplication().getApi().getSerDes()
        );
        listView.setAdapter(adapter);
        adapter.setOnButtonListener(new ListLocalGoListViewAdapter.OnButtonListener() {
            @Override
            public void onUploadClicked(final View v, final TourGo go) {
                getBtmwApplication().getApi().uploadTourGo(go, new BtmwApi.UploadTourGoListerner() {
                    @Override
                    public void onBegin() {
                        ((Button)v).setText("Uploading...");
                    }

                    @Override
                    public void onError() {
                        ((Button)v).setText("Upload Failure");
                    }

                    @Override
                    public void onDone() {
                        ((Button)v).setText("Upload");
                    }
                });
            }

            @Override
            public void onDeleteClicked(View v, TourGo go) {
                getBtmwApplication().getSaveData().deleteTourGo(go);
                adapter.updateList();
            }
        });
    }

    private BtmwApplication getBtmwApplication() {
        return (BtmwApplication) getApplicationContext();
    }
}
