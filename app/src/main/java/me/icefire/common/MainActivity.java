package me.icefire.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import me.icefire.common.bottom.BottomDialog;
import me.icefire.common.bottom.BottomDialogClickListener;
import me.icefire.common.bottom.BottomSheetCompat;
import me.icefire.common.dialog.OnPopDialogClickListener;
import me.icefire.common.dialog.PopDialog;
import me.icefire.common.loading.ViewLoading;
import me.icefire.common.popupWindow.PopupWindowCompat;
import me.icefire.common.snackbar.SnackBarCompat;
import me.icefire.common.toast.ToastCompat;
import me.icefire.common.utils.Utils;

public class MainActivity extends AppCompatActivity {

    Button btn1;
    private Context mContext;
    PopDialog popDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,UpdateActivity.class));
                //startActivity(new Intent(MainActivity.this,RatioViewActivity.class));

//                showToast();
//                showSnackbar();
//                showPopDialog();
//                showLoading();
//                showBottomDialog();
//                showBuildBottomDialog();
//                showCustomPop();
//                showPop();
//                showPopCompat();
            }
        });
    }

    private void showToast() {
//        ToastCompat.showRoundToast("我的自定义Toast");
//        ToastCompat.showRoundToast("我是Title""我的自定义Toast");
        ToastCompat.showRoundToast("我的自定义Toast", Gravity.BOTTOM, 40);
    }

    private void showSnackbar() {
//        SnackBarCompat.showSnackBar(this,"测试SnackBar");
        SnackBarCompat.showSnackBar(this, "测试SnackBar", "关闭", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast();
            }
        });
    }

    private void showPopDialog() {
        hidePopDialog();
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_confirm, null);
        PopDialog.Builder builder = new PopDialog.Builder(mContext)
                .setAnimation(R.style.ScaleAnimation)
                .setCancelable(false)
                .setContentView(view)
                .setOnClick(R.id.txtCancel)
                .setOnClick(R.id.txtConfirm)
                .setText(R.id.txtCancel, "取消")
                .setText(R.id.txtConfirm, "确定")
                .setText(R.id.txtTitle, "温馨提示")
                .setText(R.id.txtContent, "这是一个自定义的确认框")
                .setOnPopDialogClickListener(new OnPopDialogClickListener() {
                    @Override
                    public void onClick(View view, int position, int viewId) {
                        switch (viewId) {
                            case R.id.txtCancel:
                                ToastCompat.showRoundToast("取消");
                                break;
                            case R.id.txtConfirm:
                                ToastCompat.showRoundToast("确定");
                                break;
                        }
                        popDialog.dismiss();
                    }
                });
        popDialog = builder.create();
        if (isLiving()) {
            popDialog.show();
        }
    }

    private void hidePopDialog() {
        if (popDialog != null) {
            if (popDialog.isShowing()) {
                popDialog.dismiss();
            }
            popDialog = null;
        }
    }

    protected boolean isLiving() {
        if (this.isFinishing()) {
            return false;
        }
        return true;
    }

    private void showLoading() {
        ViewLoading.show(mContext, "加载中...", true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoading();
            }
        }, 3000);
    }

    private void hideLoading() {
        ViewLoading.dismiss(mContext);
    }

    private void showBottomDialog() {
        final BottomDialog bottomDialog = new BottomDialog(mContext);
        bottomDialog.setContentView(R.layout.view_bottom);
        bottomDialog.setOnClickListener(R.id.txt3, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastCompat.showRoundToast("取消");
                bottomDialog.dismiss();
            }
        });

        bottomDialog.setOnClickListener(R.id.txt1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastCompat.showRoundToast("第一条数据");
                bottomDialog.dismiss();
            }
        });

        bottomDialog.show();
    }

    BottomDialog dialog;

    private void showBuildBottomDialog() {
        BottomSheetCompat.Builder builder = new BottomSheetCompat.Builder(this)
                .setContentView(R.layout.view_bottom)
                .setOnClick(R.id.txt1)
                .setOnClick(R.id.txt2)
                .setOnClick(R.id.txt3)
                .setOnBottomClickListener(new BottomDialogClickListener() {
                    @Override
                    public void onClick(View view, int viewId) {
                        switch (viewId) {
                            case R.id.txt1:
                                ToastCompat.showRoundToast("点击了第一个");
                                break;
                            case R.id.txt2:
                                ToastCompat.showRoundToast("点击了第二个");
                                break;
                            case R.id.txt3:
                                ToastCompat.showRoundToast("点击了第二个");
                                break;
                        }
                        dialog.dismiss();
                    }
                });
        dialog = builder.show();
    }

    private void showCustomPop() {
        final CustomPop customPop = new CustomPop(this);
        customPop.setDelayedMsDismiss(2500);
        customPop.setBgAlpha(0.5f);
        customPop.setOnClick(R.id.tv_pop, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastCompat.showRoundToast("哈哈哈哈");
                customPop.dismiss();
            }
        });
        customPop.showAsDropDown(btn1, 0, 0);
    }

    private void showPop() {
        final CustomPop1 customPop = new CustomPop1(this);
        customPop.setOnClick(R.id.tv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastCompat.showRoundToast("哈哈哈哈");
                customPop.dismiss();
            }
        });
        customPop.showAtLocation(btn1, Gravity.TOP, 0, 100);
    }

    PopupWindow popupWindow;
    private void showPopCompat() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_pop_custom, null);
        popupWindow = new PopupWindowCompat.Builder(mContext)
                .setContentView(view)
                .setFocusable(true)
                .enableBackgroundDark(false)
                .setBgDarkAlpha(0.5f)
                .setOutsideTouchable(false)
                .setTouchable(true)
                .setAnimationStyle(R.style.popWindowStyle)
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Utils.setBackgroundAlpha((Activity) mContext,1f);
                    }
                })
                .setOnClick(R.id.tv_pop, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastCompat.showRoundToast("点击了头部");
                        popupWindow.dismiss();
                    }
                })
                .build();
        popupWindow.showAsDropDown(btn1, 0, 10);

    }
}
