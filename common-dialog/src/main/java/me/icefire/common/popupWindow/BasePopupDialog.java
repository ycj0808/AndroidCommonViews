package me.icefire.common.popupWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import java.lang.ref.WeakReference;

import me.icefire.common.utils.Utils;

/**
 * popwindow基类
 * @author yangchj
 * @email yangchj@icefire.me
 * @date 2019/1/11
 */
public abstract class BasePopupDialog extends PopupWindow{

    private Context mContext;
    private View contentView;
    private SparseArray<WeakReference<View>> mViews;
    public BasePopupDialog(Context context){
        super(context);
        mContext=context;
        init();
        initData(contentView);
    }

    private void init(){
        mViews=new SparseArray<>();
        contentView= LayoutInflater.from(mContext).inflate(getLayoutId(),null);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(false);
        setFocusable(true);
        setTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable());
    }

    protected abstract int getLayoutId();

    protected abstract void initData(View contentView);

    /**
     * 设置屏幕透明度
     * @param alpha
     */
    public void setBgAlpha(float alpha){
        Utils.setBackgroundAlpha((Activity) mContext,alpha);
    }

    public void setDelayedMsDismiss(long time){
        mHandler.postDelayed(delayedRun,time);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Utils.setBackgroundAlpha((Activity) mContext,1f);
        if (mHandler!=null){
            if (delayedRun!=null){
                mHandler.removeCallbacks(delayedRun);
            }else{
                mHandler.removeCallbacksAndMessages(null);
            }
            mHandler=null;
        }
    }

    public int getWidth(){
        if (contentView==null) return 0;
        contentView.measure(0,0);
        return contentView.getMeasuredWidth();
    }

    public int getHeight(){
        if(contentView==null){
            return 0;
        }
        contentView.measure(0   ,0);
        return contentView.getMeasuredHeight();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        View view = null;
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        if (null != viewWeakReference) {
            view = viewWeakReference.get();
        }
        if (null == view) {
            view = contentView.findViewById(viewId);
            if (null != view)
                mViews.put(viewId, new WeakReference<View>(view));
        }
        return (T) view;
    }

    public void setOnClick(@IdRes int viewId, final View.OnClickListener listener){
        View view=getView(viewId);
        if (view!=null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        listener.onClick(v);
                    }
                }
            });
        }
    }


    private Handler mHandler = new Handler();

    private Runnable delayedRun=new Runnable() {
        @Override
        public void run() {
            dismiss();
        }
    };
}
