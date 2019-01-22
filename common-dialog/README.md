## 简介

该库包含了如下内容：
* 自定义对话框，可以自定义对话框UI，添加自定义点击事件（包括确认对话框，更新对话框，广告弹窗等）

* 自定义底部弹窗

* 默认的加载框

* 自定义popupwindow

* 封装的ToastCompat,添加了更改背景样式，设置图片，以及添加自定义布局

* 封装了SnackBarCompat，增加了自定义的设置，使用了设计模式中的Builder构建模式


## 引用
```
implementation 'me.icefire:common-dialog:1.0.1'
```

## 判断通知权限
```
Utils.requestMsgPermission(this);
```

## 自定义弹出确认对话框
```
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
        popDialog.show();

```
## 自定义的底部菜单
```
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
```


## Loading加载对话框
```
ViewLoading.show(this);
ViewLoading.show(this,"加载中...");
ViewLoading.show(mContext, "加载中...", true);//可取消（返回键可取消）

//结束loading
ViewLoading.dismiss(this);
```



## 自定义Toast
采用builder构造者模式，链式变成，一行代码可设置吐司Toast

```
//设置最简单吐司，设置吐司内容（圆角背景）
ToastCompat.showRoundToast("自定义吐司");

//显示自定义布局的吐司
ToastCompat.showRoundToast(R.layout.view_layout_custom_toast);

//采用builder模式创建
ToastCompat.Builder builder=new ToastCompat.Builder(this.getApplicationContext())
    .setDuration(Toast.LENGTH_SHORT)
             .setFill(false)
             .setGravity(Gravity.CENTER)
             .setOffset(0)
             .setDesc("内容内容")
             .setTitle("标题")
             .setTextColor(Color.WHITE)
             .setBackgroundColor(this.getResources().getColor(R.color.blackText))
                     .build();
    builder.show();
```

## 自定义Snackbar

```
 //简单显示自定义的Snackbar
 SnackBarCompat.showSnackBar(this,"测试SnackBar");

 //带action的SnackBarCompat
 SnackBarCompat.showSnackBar(this, "测试SnackBar", "关闭", new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         showToast();
     }
   });
```

## 自定义简易型PopupWindow
只需要继承BasePopupDialog即可，实现其中的两个抽象方法
```
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
```

使用CustomPop
```
final CustomPop customPop=new CustomPop(this);
customPop.setOnClick(R.id.item01,new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           ToastCompat.showRoundToast("哈哈哈哈");
           customPop.dismiss();
       }
     });
customPop.showAtLocation(btn1, Gravity.TOP, 0, 100);
```

## 自定义PopupWindow，builder模式
```
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
```





