package me.icefire.ratio.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * ImageView 以宽为基准，按照ratio=高/宽的比例设置高度
 * @author yangchj
 * @email yangchj@icefire.me
 * @date 2019/1/8
 */
public class RatioImageView extends AppCompatImageView {

    private float ratio = 0f;

    public RatioImageView(Context context) {
        this(context,null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
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

