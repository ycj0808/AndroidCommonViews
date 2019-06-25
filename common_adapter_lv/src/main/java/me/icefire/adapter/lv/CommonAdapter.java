package me.icefire.adapter.lv;

import android.content.Context;

import java.util.List;

import me.icefire.adapter.lv.base.ItemViewDelegate;

/**
 * author yangchj
 * email yangchj@icefire.me
 * date 2019-06-25
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {

    public CommonAdapter(Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }
            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }
            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }

    protected abstract void convert(ViewHolder viewHolder, T item, int position);
}
