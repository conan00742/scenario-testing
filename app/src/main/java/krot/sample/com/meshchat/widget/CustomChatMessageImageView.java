package krot.sample.com.meshchat.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Krot on 1/3/18.
 */

public class CustomChatMessageImageView extends android.support.v7.widget.AppCompatImageView {

    private int width = 16;
    private int height = 9;

    public CustomChatMessageImageView(Context context) {
        super(context);
    }

    public CustomChatMessageImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomChatMessageImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImageWidth(int width) {
        this.width = width;
    }

    public void setImageHeight(int height) {
        this.height = height;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int customWidth = MeasureSpec.getSize(widthMeasureSpec);
        int customHeight = Math.round(((customWidth - getPaddingLeft() - getPaddingRight()) * height) / width) + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(customWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(customHeight, MeasureSpec.EXACTLY));
    }


}
