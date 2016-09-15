package net.nqlab.simple.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import net.nqlab.simple.R;

public class GoDirectionView extends View {
    private String mSource;
    private String mDestination;
    private boolean mRoadNw;
    private boolean mRoadN;
    private boolean mRoadNe;
    private boolean mRoadW;
    private boolean mRoadE;
    private boolean mRoadSw;
    private boolean mRoadS;
    private boolean mRoadSe;

    public GoDirectionView(Context context) {
        super(context);
        initialize();
    }

    public GoDirectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public GoDirectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public GoDirectionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize() {
        mSource = "";
        mDestination = "";

        mRoadNw = false;
        mRoadN = false;
        mRoadNe = false;
        mRoadW = false;
        mRoadE = false;
        mRoadSw = false;
        mRoadS = false;
        mRoadSe = false;
    }

    private void drawRoad(Canvas canvas, int id) {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), id);
        canvas.drawBitmap(
                bitmap,
                new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()),
                new Rect(0, 0, canvas.getWidth(), canvas.getHeight()),
                null
        );
    }

    private boolean isSourceOrDestinationMatch(String dir) {
        return dir.equals(mSource) || dir.equals(mDestination);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRoadNw || isSourceOrDestinationMatch("nw")) { drawRoad(canvas, R.drawable.ic_normal_nw); }
        if (mRoadN  || isSourceOrDestinationMatch("n" )) { drawRoad(canvas, R.drawable.ic_normal_n ); }
        if (mRoadNe || isSourceOrDestinationMatch("ne")) { drawRoad(canvas, R.drawable.ic_normal_ne); }
        if (mRoadW  || isSourceOrDestinationMatch("w" )) { drawRoad(canvas, R.drawable.ic_normal_w ); }
        if (mRoadE  || isSourceOrDestinationMatch("e" )) { drawRoad(canvas, R.drawable.ic_normal_e ); }
        if (mRoadSw || isSourceOrDestinationMatch("sw")) { drawRoad(canvas, R.drawable.ic_normal_sw); }
        if (mRoadS  || isSourceOrDestinationMatch("s" )) { drawRoad(canvas, R.drawable.ic_normal_s ); }
        if (mRoadSe || isSourceOrDestinationMatch("se")) { drawRoad(canvas, R.drawable.ic_normal_se); }

        if (mSource.equals("nw")) { drawRoad(canvas, R.drawable.ic_orig_nw); }
        else if (mSource.equals("n")) { drawRoad(canvas, R.drawable.ic_orig_n); }
        else if (mSource.equals("ne")) { drawRoad(canvas, R.drawable.ic_orig_ne); }
        else if (mSource.equals("w")) { drawRoad(canvas, R.drawable.ic_orig_w); }
        else if (mSource.equals("e")) { drawRoad(canvas, R.drawable.ic_orig_e); }
        else if (mSource.equals("sw")) { drawRoad(canvas, R.drawable.ic_orig_sw); }
        else if (mSource.equals("s")) { drawRoad(canvas, R.drawable.ic_orig_s); }
        else if (mSource.equals("se")) { drawRoad(canvas, R.drawable.ic_orig_se); }

        if (mDestination.equals("nw")) { drawRoad(canvas, R.drawable.ic_dest_nw); }
        else if (mDestination.equals("n")) { drawRoad(canvas, R.drawable.ic_dest_n); }
        else if (mDestination.equals("ne")) { drawRoad(canvas, R.drawable.ic_dest_ne); }
        else if (mDestination.equals("w")) { drawRoad(canvas, R.drawable.ic_dest_w); }
        else if (mDestination.equals("e")) { drawRoad(canvas, R.drawable.ic_dest_e); }
        else if (mDestination.equals("sw")) { drawRoad(canvas, R.drawable.ic_dest_sw); }
        else if (mDestination.equals("s")) { drawRoad(canvas, R.drawable.ic_dest_s); }
        else if (mDestination.equals("se")) { drawRoad(canvas, R.drawable.ic_dest_se); }
    }

    public void setRoadNw(boolean val) {
        mRoadNw = val;
    }

    public void setRoadN(boolean val) {
        mRoadN = val;
    }

    public void setRoadNe(boolean val) {
        mRoadNe = val;
    }

    public void setRoadW(boolean val) {
        mRoadW = val;
    }

    public void setRoadE(boolean val) {
        mRoadE = val;
    }

    public void setRoadSw(boolean val) {
        mRoadSw = val;
    }

    public void setRoadS(boolean val) {
        mRoadS = val;
    }

    public void setRoadSe(boolean val) {
        mRoadSe = val;
    }

    public void setSource(String src) {
        mSource = src;
    }

    public void setDestination(String dst) {
        mDestination = dst;
    }
}
