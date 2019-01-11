package me.icefire.common.bottom;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.ViewGroup;

import me.icefire.common.utils.Utils;

/**
 * @author yangchj
 * @email yangchj@icefire.me
 * @date 2019/1/10
 */
public class BottomDialog extends BottomSheetDialog {

    protected Context mContext;
    private View viewRoot;
    public BottomDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public BottomDialog(@NonNull Context context, int theme) {
        super(context, theme);
        mContext = context;
    }

    protected BottomDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getOwnerActivity() == null) return;
        int sH = Utils.getScreenHeight(getOwnerActivity());
        int sbH = Utils.getStatusBarHeight(getContext());
        int dialogH = sH - sbH;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogH == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogH);
    }


    @Override
    public void setContentView(int layoutResId) {
        viewRoot = View.inflate(mContext,layoutResId,null);
        setContentView(viewRoot);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        viewRoot=view;
    }

    public <T extends View> T getView(int viewId) {
        if (viewRoot==null) return null;
        View view = viewRoot.findViewById(viewId);
        return (T) view;
    }

    public void setOnClickListener(@IdRes int viewId, View.OnClickListener listener){
        View view=getView(viewId);
        if (view!=null) {
            view.setOnClickListener(listener);
        }
    }
}
