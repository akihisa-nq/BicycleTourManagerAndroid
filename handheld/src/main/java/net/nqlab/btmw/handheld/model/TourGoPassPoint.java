package net.nqlab.btmw.handheld.model;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import android.support.annotation.Nullable;

@Table
public class TourGoPassPoint {

    @PrimaryKey
    public long _id;

    @Column(indexed = true)
    public Long tour_go_id;

    @Column(indexed = true)
    public long tour_plan_point_id;

    @Column
    public String passed_on;

}
