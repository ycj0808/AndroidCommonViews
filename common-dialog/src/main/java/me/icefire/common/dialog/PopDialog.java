package me.icefire.common.dialog;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author yangchj
 *  email yangchj@icefire.me
 *date 2019/1/9
 */
public class PopDialog extends Dialog {

    private PopController mAlert;

    public PopDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mAlert=new PopController(this,getWindow());
    }

    public static class Builder {
        private final PopController.AlertParams mAlertParams;

        public Builder(Context context){
            this(context,R.style.PopDialogStyle);
        }

        public Builder(Context context, @StyleRes int themeRes){
            mAlertParams=new PopController.AlertParams(context,themeRes);
        }

        public Builder setContentView(View view){
            mAlertParams.mView=view;
            mAlertParams.mViewLayoutResId=0;
            return this;
        }

        public Builder setContentView(int layoutResId){
            mAlertParams.mView=null;
            mAlertParams.mViewLayoutResId=layoutResId;
            return this;
        }

        public Builder setCancelable(boolean cancelable){
            mAlertParams.mCancelable=cancelable;
            return this;
        }

        public Builder setText(@IdRes int viewId,CharSequence text){
            mAlertParams.mTextArr.put(viewId,text);
            return this;
        }

        public Builder setFromBottom(){
            mAlertParams.mGravity= Gravity.BOTTOM;
            return this;
        }

        public Builder setAnimation(@StyleRes int styleAnim){
            mAlertParams.mAnimation=styleAnim;
            return this;
        }

        public Builder setHasAnimation(boolean hasAnimation){
            mAlertParams.mHasAnimation=hasAnimation;
            return this;
        }

        public Builder setFullWidth(){
            mAlertParams.mWidth= ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        public Builder setWidthAndHeight(int width,int height){
            mAlertParams.mWidth=width;
            mAlertParams.mHeight=height;
            return this;
        }

        public Builder setOnClick(@IdRes int viewId){
            mAlertParams.mClickArr.put(mAlertParams.mClickArr.size(),viewId);
            return this;
        }

        public Builder setOnPopDialogClickListener(OnPopDialogClickListener listener){
            mAlertParams.mOnPopDialogClickListener=listener;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener){
            mAlertParams.mOnCancelListener=onCancelListener;
            return this;
        }

        public Builder setOnDimissListener(OnDismissListener onDimissListener){
            mAlertParams.mOnDismissListener=onDimissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener){
            mAlertParams.mOnKeyListener=onKeyListener;
            return this;
        }

        public PopDialog create(){
            final PopDialog dialog=new PopDialog(mAlertParams.mContext,mAlertParams.mThemeRes);
            mAlertParams.apply(dialog.mAlert);
            if (mAlertParams.mCancelable){
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(mAlertParams.mOnCancelListener);
            dialog.setOnDismissListener(mAlertParams.mOnDismissListener);
            if (mAlertParams.mOnKeyListener!=null){
                dialog.setOnKeyListener(mAlertParams.mOnKeyListener);
            }
            return dialog;
        }

        public PopDialog show(){
            PopDialog dialog=create();
            dialog.show();
            return dialog;
        }
    }
}
