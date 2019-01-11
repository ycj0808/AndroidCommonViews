package me.icefire.common.bottom;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * @author yangchj
 *  email yangchj@icefire.me
 *date 2019/1/10
 */
public class BottomViewHelper {

    private Context mContext;
    private View mContentView;
    private SparseArray<WeakReference<View>> mViews;
    private BottomDialogClickListener bottomDialogClickListener;
    public SparseIntArray mClickArr = new SparseIntArray();

    public BottomViewHelper(Context context) {
        mViews = new SparseArray<>();
        this.mContext = context;
    }

    public void setContentView(View mContentView) {
        this.mContentView = mContentView;
    }

    public void setContentView(@LayoutRes int layoutId) {
        View view = View.inflate(mContext, layoutId, null);
        this.mContentView = view;
    }

    public void setOnClick(@IdRes final int viewId) {
        View view = getView(viewId);
        if (null != view) {
            mClickArr.put(mClickArr.size(), viewId);
        }
    }

    public void setText(@IdRes int viewId, CharSequence charSequence){
        TextView textView = getView(viewId);
        if (null!=textView){
            textView.setText(charSequence);
        }
    }

    public void setTextColor(@IdRes int viewId, @ColorInt int textColor){
        TextView textView = getView(viewId);
        if (null!=textView){
            textView.setTextColor(textColor);
        }
    }

    public void apply() {
        for (int i = 0, len = mClickArr.size(); i < len; i++) {
            if (bottomDialogClickListener != null) {
                final int viewId=mClickArr.valueAt(i);
                View view = getView(viewId);
                if (view != null) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomDialogClickListener.onClick(v, viewId);
                        }
                    });
                }
            }
        }
    }

    public View getContentView() {
        return mContentView;
    }

    public <T extends View> T getView(int viewId) {
        View view = null;
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        if (null != viewWeakReference) {
            view = viewWeakReference.get();
        }
        if (null == view) {
            view = mContentView.findViewById(viewId);
            if (null != view)
                mViews.put(viewId, new WeakReference<View>(view));
        }
        return (T) view;
    }

    public void setBottomDialogClickListener(BottomDialogClickListener bottomDialogClickListener) {
        this.bottomDialogClickListener = bottomDialogClickListener;
    }
}
