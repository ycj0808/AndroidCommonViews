package me.icefire.adapter.rv.wrapper;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import me.icefire.adapter.rv.base.ViewHolder;

/**
 * @author yangchj
 * @email yangchj@icefire.me
 * @date 2019/1/7
 */
public class HeaderAndFooterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter mInnerAdpter;

    public HeaderAndFooterWrapper(RecyclerView.Adapter adapter) {
        this.mInnerAdpter = adapter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mHeaderViews.get(i) != null) {
            ViewHolder holder = ViewHolder.createViewHolder(viewGroup.getContext(), mHeaderViews.get(i));
            return holder;
        } else if (mFootViews.get(i) != null) {
            ViewHolder holder = ViewHolder.createViewHolder(viewGroup.getContext(), mFootViews.get(i));
            return holder;
        }
        return mInnerAdpter.onCreateViewHolder(viewGroup, i);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (isHeadViewPos(i)) {
            return;
        }

        if (isFooterViewPos(i)) {
            return;
        }
        mInnerAdpter.onBindViewHolder(viewHolder, i - getHeadersCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeadViewPos(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - getHeadersCount() - getRealItemCount());
        }
        return mInnerAdpter.getItemViewType(position - getHeadersCount());
    }

    private boolean isHeadViewPos(int pos) {
        return pos < getHeadersCount();
    }

    private boolean isFooterViewPos(int pos) {
        return pos >= getHeadersCount() + getRealItemCount();
    }

    @Override
    public int getItemCount() {
        return getHeadersCount() + getRealItemCount() + getFootersCount();
    }

    public int getRealItemCount() {
        return mInnerAdpter.getItemCount();
    }

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public int getFootersCount() {
        return mFootViews.size();
    }

    public void addHeaderView(View view){
        mHeaderViews.put(mHeaderViews.size()+BASE_ITEM_TYPE_HEADER,view);
    }

    public void addFootView(View view){
        mFootViews.put(mFootViews.size()+BASE_ITEM_TYPE_FOOTER,view);
    }
}
