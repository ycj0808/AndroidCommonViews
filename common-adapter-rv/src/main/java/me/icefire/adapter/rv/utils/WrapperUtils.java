package me.icefire.adapter.rv.utils;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

/**
 * @author yangchj
 *  email yangchj@icefire.me
 *date 2019/1/7
 */
public class WrapperUtils {

    public interface SpanSizeCallback{
        int getSpanSize(GridLayoutManager layoutManager,GridLayoutManager.SpanSizeLookup lookup,int pos);
    }

    public static void onAttachedToRecyclerView(RecyclerView.Adapter innerAdapter,RecyclerView recyclerView,final SpanSizeCallback callback){
        innerAdapter.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager= (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int i) {
                    return callback.getSpanSize(gridLayoutManager,spanSizeLookup,i);
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    public static void setFullSpan(RecyclerView.ViewHolder holder){
        ViewGroup.LayoutParams lp=holder.itemView.getLayoutParams();
        if (lp!=null&&lp instanceof StaggeredGridLayoutManager.LayoutParams){
            StaggeredGridLayoutManager.LayoutParams p= (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }
}
