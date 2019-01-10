package me.icefire.ratio.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/8
 */
public class RatioRelativeLayout extends RelativeLayout{

    private float ratio = 0f;

    public RatioRelativeLayout(Context context) {
        this(context,null);
    }

    public RatioRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RatioRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleAttrs(context,attrs);
    }

    private void handleAttrs(Context context,AttributeSet attrs){
        if (attrs!=null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.RatioView);
            ratio=typedArray.getFloat(R.styleable.RatioView_viewRatio,0f);
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = getMeasuredWidth();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode==MeasureSpec.EXACTLY&&ratio>0&&measuredWidth!=0){
            int measureHeight= (int) (measuredWidth*ratio);
            heightMeasureSpec=MeasureSpec.makeMeasureSpec(measureHeight,MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
