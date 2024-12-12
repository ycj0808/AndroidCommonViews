package me.icefire.common.toast;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import me.icefire.common.dialog.R;
import me.icefire.common.utils.Utils;

/**
 * @author yangchj
 * email yangchj@icefire.me
 * date 2019/1/8
 */
public final class ToastCompat {

    private static Context mContext;

    public static void init(@NonNull final Context context) {
        mContext = context;
    }

    private ToastCompat() {
        throw new UnsupportedOperationException("u can't instantiate me..");
    }

    /**
     * 检查上下文不能为空，必须先进行性初始化操作
     */
    private static void checkContext() {
        if (mContext == null) {
            throw new NullPointerException("ToastCompat context is not null,please first init");
        }
    }


    private static Toast toast;

    /**
     * 吐司工具类    避免点击多次导致吐司多次，最后导致Toast就长时间关闭不掉了
     * 注意：这里如果传入context会报内存泄漏；传递activity..getApplicationContext()
     *
     * @param content
     */
    public static void showToast(String content) {
        checkContext();
        if (toast == null) {
            toast = Toast.makeText(mContext, content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }

    /**
     * 显示自定义toast--类似系统自带toast
     * 检查Utils.isEnableNotification(context);
     * 替代SnackBarUtils.showSnack(context,content);
     * 圆角 屏幕中间 不显示顶部图标
     * @param content
     */
    public static void showRoundToast(String content){
        showRoundToast(content,Gravity.CENTER,0);
    }

    /**
     * 显示自定义Toast
     * @param content
     * @param gravity
     */
    public static void showRoundToast(String content,int gravity,int offset){
        showRoundToast(content,"",gravity,0,false,0,offset);
    }

    /**
     * 显示自定义toast
     * @param showTop
     * @param content
     */
    public static void showRoundToast(String content,boolean showTop){
        showRoundToast(content,"",Gravity.CENTER,0,showTop,0,0);
    }

    /**
     * 显示自定义toast
     * @param title
     * @param content
     */
    public static void showRoundToast(String title,String content){
        showRoundToast(title,content,Gravity.CENTER,0,false,0,0);
    }

    /**
     * 显示自定义Toast
     * @param title
     * @param content
     * @param bgColor
     */
    public static void showRoundToast(String title,String content,int bgColor){
        showRoundToast(title,content,Gravity.CENTER,bgColor,false,0,0);
    }

    /**
     * 自定义布局的toast
     * @param layoutId
     */
    public static void showRoundToast(@LayoutRes int layoutId){
        checkContext();
        if (layoutId==0) return;
        new Builder(mContext)
                .setDuration(Toast.LENGTH_SHORT)
                .setFill(false)
                .setGravity(Gravity.CENTER)
                .setOffset(0)
                .setLayout(layoutId)
                .build()
                .show();
    }
    /**
     * 构建通用的toast
     * @param title
     * @param content
     * @param bgResId
     * @param showTop
     * @param drawTop
     */
    public static void showRoundToast(String title,String content,int gravity,int bgResId,boolean showTop,int drawTop,int offset){
        checkContext();
        if (TextUtils.isEmpty(title)) return;
        new Builder(mContext)
                .setDuration(Toast.LENGTH_SHORT)
                .setFill(false)
                .setGravity(gravity)
                .setOffset(Utils.dp2px(mContext,offset))
                .setTitle(title)
                .setDesc(content)
                .setShowDrawableTop(showTop)
                .setDrawableTop(drawTop)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(bgResId)
                .setRadius(Utils.dp2px(mContext,10))
                .setElevation(0)
                .build()
                .show();
    }

    public static final class Builder {
        private Context context;
        private CharSequence title;
        private CharSequence desc;
        private int gravity = Gravity.TOP;
        private boolean isFill;
        private int yOffset;
        private int duration = Toast.LENGTH_SHORT;
        private int textColor = Color.WHITE;
        private int backgroundColor = Color.BLACK;
        private int drawableTop;
        private boolean showDrawableTop;
        private float radius;
        private int elevation;
        private int layout;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setDesc(CharSequence desc) {
            this.desc = desc;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setFill(boolean fill) {
            isFill = fill;
            return this;
        }

        public Builder setOffset(int yOffset) {
            this.yOffset = yOffset;
            return this;
        }

        public Builder setDuration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setDrawableTop(int drawableTop) {
            this.drawableTop = drawableTop;
            return this;
        }

        public Builder setShowDrawableTop(boolean showDrawableTop) {
            this.showDrawableTop = showDrawableTop;
            return this;
        }

        public Builder setRadius(float radius) {
            this.radius = radius;
            return this;
        }

        public Builder setElevation(int elevation) {
            this.elevation = elevation;
            return this;
        }

        public Builder setLayout(int layout) {
            this.layout = layout;
            return this;
        }

        public Toast build() {
            if (toast == null) {
                toast = new Toast(context);
            }
            if (isFill) {
                toast.setGravity(gravity | Gravity.FILL_HORIZONTAL, 0, yOffset);
            } else {
                toast.setGravity(gravity, 0, yOffset);
            }

            toast.setDuration(duration);
            toast.setMargin(0, 0);
            if (layout == 0) {
                CardView rootView = (CardView) LayoutInflater.from(context).inflate(R.layout.view_toast_default, null);
                TextView txtTitle = rootView.findViewById(R.id.txtTitle);
                ImageView ivInfo = rootView.findViewById(R.id.ivInfo);
                TextView txtContent = rootView.findViewById(R.id.txtContent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    rootView.setCardElevation(elevation);
                }
                rootView.setRadius(radius);
                if (backgroundColor!=0){
                    rootView.setCardBackgroundColor(backgroundColor);
                }
                txtTitle.setTextColor(textColor);
                txtTitle.setText(title);
                if (TextUtils.isEmpty(desc)) {
                    txtContent.setVisibility(View.GONE);
                } else {
                    txtContent.setVisibility(View.VISIBLE);
                    txtContent.setText(desc);
                }
                if (drawableTop != 0) {
                    ivInfo.setImageResource(drawableTop);
                }
                if (showDrawableTop) {
                    ivInfo.setVisibility(View.VISIBLE);
                } else {
                    ivInfo.setVisibility(View.GONE);
                }
                toast.setView(rootView);
            } else {
                View view = LayoutInflater.from(context).inflate(layout, null);
                toast.setView(view);
            }
            return toast;
        }
    }
}
