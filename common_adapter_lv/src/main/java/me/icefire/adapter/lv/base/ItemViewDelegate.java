package me.icefire.adapter.lv.base;

import me.icefire.adapter.lv.ViewHolder;

/**
 * author yangchj
 * email yangchj@icefire.me
 * date 2019-06-25
 */
public interface ItemViewDelegate<T> {
    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);
}
