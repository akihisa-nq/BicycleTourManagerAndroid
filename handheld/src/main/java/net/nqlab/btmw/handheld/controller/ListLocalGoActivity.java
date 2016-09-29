package net.nqlab.btmw.handheld.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.nqlab.btmw.api.TourGoApi;
import net.nqlab.btmw.api.TourGoCreateResult;
import net.nqlab.btmw.api.TourGoEvent;
import net.nqlab.btmw.api.TourGoUpdateResult;
import net.nqlab.btmw.api.TourPlanList;
import net.nqlab.btmw.handheld.R;
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

        ListView listView = (ListView)findViewById(R.id.listView);
        final ListLocalGoListViewAdapter adapter = new ListLocalGoListViewAdapter(
                this,
                getBtmwApplication().getSaveData(),
                getBtmwApplication().getApi().getSerDes()
        );
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TourGo go = (TourGo)adapter.getItem(position);
                TourGoApi api = getBtmwApplication().getApi().getTourGoApi();

                List<TourGoEvent> listEvents = new ArrayList<TourGoEvent>();
                for (TourGoPassPoint passPoint : ListLocalGoActivity.this.getBtmwApplication().getSaveData().getTourGoPassPoints(go._id)) {
                    TourGoEvent event = new TourGoEvent();
                    event.setEventType("pass_point");
                    event.setOccuredOn(passPoint.passed_on);
                    event.setTourPlanPointId((int)passPoint.tour_plan_point_id);
                    listEvents.add(event);
                }

                net.nqlab.btmw.api.TourGo goApi = new net.nqlab.btmw.api.TourGo();
                goApi.setStartTime(go.start_time);
                goApi.setTourPlanId((int)go.tour_plan_shedule_id);
                goApi.setTourGoEvents(listEvents);

                if (go.tour_go_id.longValue() == 0) {
                    api.create(goApi)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TourGoCreateResult>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(TourGoCreateResult result) {
                                go.tour_go_id = result.getId().longValue();
                                getBtmwApplication().getSaveData().updateTourGoId(go);
                            }
                        });
                } else {
                    api.update(go.tour_go_id.intValue(), goApi)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TourGoUpdateResult>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(TourGoUpdateResult result) {
                            }
                        });
                }
            }
        });
    }

    private BtmwApplication getBtmwApplication() {
        return (BtmwApplication) getApplicationContext();
    }
}
