package me.icefire.common;

import android.content.Context;
import android.view.View;

import me.icefire.common.popupWindow.BasePopupDialog;

/**
 * @author yangchj
 *  email yangchj@icefire.me
 *date 2019/1/11
 */
public class CustomPop extends BasePopupDialog {


    public CustomPop(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_pop_custom;
    }

    @Override
    protected void initData(View contentView) {
    }
}
