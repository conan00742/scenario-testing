package krot.sample.com.meshchat.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.VideoView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

/**
 * Created by Krot on 5/20/18.
 */

public class CustomVideoView extends VideoView {

    private int width;
    private int height;

    public CustomVideoView(Context context) {
        super(context);
        initVideoView();

    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVideoView();
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initVideoView();
    }


    private void initVideoView() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        height = metrics.widthPixels / 2;
        width = (height * 16) / 9;

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
}
