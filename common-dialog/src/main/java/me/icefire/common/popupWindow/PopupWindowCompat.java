package me.icefire.common.popupWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 构建自定义PopupWindow
 * @author yangchj
 *  email yangchj@icefire.me
 *date 2019/1/11
 */
public final class PopupWindowCompat{


    private PopupWindowCompat() {
    }

    public static final class Builder  {
        private Context mContext;
        private int mResLayoutId;
        private View mContentView;
        private int mAnimationStyle;
        private int mWidth;
        private int mHeight;
        private boolean mIsFocusable;
        private boolean mIsOutside;
        private PopupWindow mPopupWindow;
        private boolean mClippEnable;
        private boolean mIgnoreCheekPress;
        private int mInputMode;
        private int mSoftInputMode;
        private boolean mTouchable;
        private boolean mIsBackgroundDark;
        private float mBackgroundDrakValue;
        private View.OnTouchListener mOnTouchListener;
        private PopupWindow.OnDismissListener mOnDismissListener;
        private Window mWindow;

        public Builder(Context context){
            this.mContext=context;
        }

        public Builder setContentView(@LayoutRes int layoutId){
            mResLayoutId=layoutId;
            mContentView=null;
            return this;
        }

        public Builder setContentView(View view){
            mResLayoutId=-1;
            mContentView=view;
            return this;
        }

        public Builder setAnimationStyle(int animationStyle){
            mAnimationStyle=animationStyle;
            return this;
        }

        public Builder size(int width,int height){
            mWidth=width;
            mHeight=height;
            return this;
        }

        public Builder setFocusable(boolean mIsFocusable) {
            this.mIsFocusable = mIsFocusable;
            return this;
        }

        public Builder setOutsideTouchable(boolean mIsOutside) {
            this.mIsOutside = mIsOutside;
            return this;
        }

        public Builder setClippingEnable(boolean flag){
            this.mClippEnable=flag;
            return this;
        }

        public Builder setIgnoreCheekPress(boolean flag){
            this.mIgnoreCheekPress=flag;
            return this;
        }

        public Builder setInputMethodMode(int mode){
            this.mInputMode=mode;
            return this;
        }

        public Builder setOnDissmissListener(PopupWindow.OnDismissListener dissmissListener){
            this.mOnDismissListener=dissmissListener;
            return this;
        }

        public Builder setSoftInputMode(int softInputMode){
            this.mSoftInputMode=softInputMode;
            return this;
        }

        public Builder setTouchable(boolean touchable) {
            this.mTouchable=touchable;
            return this;
        }

        public Builder setTouchIntercept(View.OnTouchListener touchIntercepter){
            this.mOnTouchListener=touchIntercepter;
            return this;
        }

        public Builder enableBackgroundDark(boolean isDark){
            this.mIsBackgroundDark=isDark;
            return this;
        }

        public Builder setBgDarkAlpha(float darkValue){
            this.mBackgroundDrakValue=darkValue;
            return this;
        }

        public Builder setOnClick(@IdRes int viewId, final View.OnClickListener listener){
            View view=getView(viewId);
            if (view!=null){
                view.setOnClickListener(listener);
            }
            return this;
        }

        private  <T extends View> T getView(int viewId) {
            if (mContentView == null) return null;
            View view = mContentView.findViewById(viewId);
            return (T) view;
        }

        public PopupWindow build(){
            if (mContentView==null&&mResLayoutId==-1)
                throw new IllegalArgumentException("u must set contentView first");
            if (mContentView==null){
                mContentView= LayoutInflater.from(mContext).inflate(mResLayoutId,null);
            }

            //获取Activity对象
            Activity activity = (Activity)this.mContentView.getContext();
            if(activity != null && this.mIsBackgroundDark) {
                //设置背景透明度
                float alpha = this.mBackgroundDrakValue > 0.0F && this.mBackgroundDrakValue < 1.0F?this.mBackgroundDrakValue:0.7F;
                this.mWindow = activity.getWindow();
                WindowManager.LayoutParams params = this.mWindow.getAttributes();
                params.alpha = alpha;
                this.mWindow.setAttributes(params);
            }

            //设置宽高
            if(this.mWidth != 0 && this.mHeight != 0) {
                this.mPopupWindow = new PopupWindow(this.mContentView, this.mWidth, this.mHeight);
            } else {
                this.mPopupWindow = new PopupWindow(this.mContentView, -2, -2);
            }

            //设置动画
            if(this.mAnimationStyle != -1) {
                this.mPopupWindow.setAnimationStyle(this.mAnimationStyle);
            }

            this.apply(this.mPopupWindow);
            //设置是否捕获焦点，默认为true
            this.mPopupWindow.setFocusable(this.mIsFocusable);
            //设置背景，默认是全透明
            this.mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
            //设置是否可以点击外部，默认是true
            this.mPopupWindow.setOutsideTouchable(this.mIsOutside);
            if(this.mWidth == 0 || this.mHeight == 0) {
                this.mPopupWindow.getContentView().measure(0, 0);
                this.mWidth = this.mPopupWindow.getContentView().getMeasuredWidth();
                this.mHeight = this.mPopupWindow.getContentView().getMeasuredHeight();
            }
            //实现关闭监听
            this.mPopupWindow.setOnDismissListener(mOnDismissListener);
            this.mPopupWindow.update();
            return this.mPopupWindow;
        }

        private void apply(PopupWindow mPopupWindow) {
            mPopupWindow.setClippingEnabled(this.mClippEnable);
            if(this.mIgnoreCheekPress) {
                mPopupWindow.setIgnoreCheekPress();
            }

            if(this.mInputMode != -1) {
                mPopupWindow.setInputMethodMode(this.mInputMode);
            }

            if(this.mSoftInputMode != -1) {
                mPopupWindow.setSoftInputMode(this.mSoftInputMode);
            }

            if(this.mOnDismissListener != null) {
                mPopupWindow.setOnDismissListener(this.mOnDismissListener);
            }

            if(this.mOnTouchListener != null) {
                mPopupWindow.setTouchInterceptor(this.mOnTouchListener);
            }

            mPopupWindow.setTouchable(this.mTouchable);
        }

    }
}
