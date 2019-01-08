package me.icefire.adapter.rv.base;

/**
 * @author yangchj
 * @email yangchj@icefire.me
 * @date 2019/1/2
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position);
}
