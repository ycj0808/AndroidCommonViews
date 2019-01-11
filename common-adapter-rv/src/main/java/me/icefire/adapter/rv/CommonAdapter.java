package me.icefire.adapter.rv;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

import me.icefire.adapter.rv.base.ItemViewDelegate;
import me.icefire.adapter.rv.base.ViewHolder;

/**
 * @author yangchj
 *  email yangchj@icefire.me
 *date 2019/1/7
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T>{

    protected Context mContext;
    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public CommonAdapter(Context context,final int layoutId, List<T> datas) {
        super(context, datas);
        mContext=context;
        mInflater=LayoutInflater.from(mContext);
        mLayoutId=layoutId;
        mDatas=datas;
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
                CommonAdapter.this.convert(holder,t,position);
            }
        });
    }
    protected abstract void convert(ViewHolder holder, T t, int position);
}
