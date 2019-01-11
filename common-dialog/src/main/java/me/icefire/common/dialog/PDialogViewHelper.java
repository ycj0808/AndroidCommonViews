package me.icefire.common.dialog;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * 加载布局,查找控件和绑定控件
 */

public class PDialogViewHelper {
    private Context mContext;
    private View mContentView;
    private SparseArray<WeakReference<View>> mViews;
    private OnPopDialogClickListener mOnPopDialogClickListener;
    public PDialogViewHelper(Context context, int viewLayoutResId) {
        mViews = new SparseArray<>();
        mContentView = LayoutInflater.from(context).inflate(viewLayoutResId, null);
    }

    public PDialogViewHelper(Context context, View view) {
        mViews = new SparseArray<>();
        mContentView = view;
    }

    public void setText(@IdRes int viewId, CharSequence charSequence) {
        TextView textView = getView(viewId);
        if (null != textView) {
            textView.setText(charSequence);
        }
    }

    public void setOnClick(final int position, @IdRes final int viewId) {
        View view = getView(viewId);
        if (null != view) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnPopDialogClickListener){
                        mOnPopDialogClickListener.onClick(v,position,viewId);
                    }
                }
            });
        }
    }


    public View getContentView() {
        return mContentView;
    }

    @SuppressWarnings("unchecked")
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


    public void setOnJAlertDialogClickListener(OnPopDialogClickListener popDialogClickListener) {
        mOnPopDialogClickListener = popDialogClickListener;
    }
}
