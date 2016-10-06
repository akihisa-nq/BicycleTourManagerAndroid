package net.nqlab.btmw.handheld.model;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

@Table
public class TourGoSound {
    @PrimaryKey
    public long _id;

    @Column(indexed = true)
    public Long tour_go_id;

    @Column
    public byte[] sound_data;

    @Column
    public String recorded_on;
}
