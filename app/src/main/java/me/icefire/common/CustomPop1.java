package me.icefire.common;

import android.content.Context;
import android.view.View;

import me.icefire.common.popupWindow.BasePopupDialog;

/**
 * @author yangchj
 * @email yangchj@icefire.me
 * @date 2019/1/11
 */
public class CustomPop1 extends BasePopupDialog {


    public CustomPop1(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_qq;
    }

    @Override
    protected void initData(View contentView) {
    }
}
