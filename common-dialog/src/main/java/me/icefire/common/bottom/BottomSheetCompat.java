package me.icefire.common.bottom;

import android.content.Context;
import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import android.view.View;

/**
 * @author yangchj
 *  email yangchj@icefire.me
 *date 2019/1/10
 */
public class BottomSheetCompat {

    private final Builder builder;

    private BottomSheetCompat() {
        throw new UnsupportedOperationException("u can't instantiate me..");
    }

    private BottomSheetCompat(Builder builder) {
        this.builder = builder;
    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public static Builder builder(Context context, int theme) {
        return new Builder(context, theme);
    }

    public static class Builder {
        private Context context;
        private int theme;
        private BottomViewHelper bottomViewHelper;
        public Builder(Context context) {
            this.context = context;
            bottomViewHelper = new BottomViewHelper(context);
        }

        public Builder(Context context, int theme) {
            this.context = context;
            this.theme = theme;
            bottomViewHelper = new BottomViewHelper(context);
        }

        public Builder setContentView(int layoutResId) {
            bottomViewHelper.setContentView(layoutResId);
            return this;
        }

        public Builder setContentView(View view) {
            bottomViewHelper.setContentView(view);
            return this;
        }

        public Builder setTheme(int theme) {
            this.theme = theme;
            return this;
        }

        public Builder setOnBottomClickListener(BottomDialogClickListener clickListener) {
            bottomViewHelper.setBottomDialogClickListener(clickListener);
            bottomViewHelper.apply();
            return this;
        }

        public Builder setOnClick(@IdRes int viewId) {
            bottomViewHelper.setOnClick(viewId);
            return this;
        }

        public Builder setText(@IdRes int viewId, CharSequence charSequence){
            bottomViewHelper.setText(viewId,charSequence);
            return this;
        }

        public Builder setTextColor(@IdRes int viewId, @ColorInt int textColor){
            bottomViewHelper.setTextColor(viewId,textColor);
            return this;
        }

        public View getView(int viewId) {
            return bottomViewHelper.getView(viewId);
        }

        public View getContentView() {
            return bottomViewHelper.getContentView();
        }

        public BottomDialog build() {
            if (this.getContentView() == null) {
                throw new IllegalStateException("must set an view before make a bottom dialog");
            }
            BottomDialog bottomDialog;
            if (this.theme == 0) {
                bottomDialog = new BottomDialog(this.context);
            } else {
                bottomDialog = new BottomDialog(this.context, this.theme);
            }
            bottomDialog.setContentView(this.getContentView());
            return bottomDialog;
        }

        public BottomDialog show(){
            BottomDialog bottomDialog=build();
            bottomDialog.show();
            return bottomDialog;
        }
    }
}
