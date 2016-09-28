package net.nqlab.btmw.handheld.model;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import android.support.annotation.Nullable;

@Table
public class Token {

    @PrimaryKey
    public long _id;

    @Column(indexed = true)
    public String key;

    @Column
    public String data;
}

