package net.nqlab.btmw.handheld.model;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import android.support.annotation.Nullable;

@Table
public class TourPlan {

    @PrimaryKey(auto = false)
    public long _id;

    @Column
    public String name;

    @Column
    public String json;
}


