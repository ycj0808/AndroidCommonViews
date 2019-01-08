package me.icefire.adapter.rv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.icefire.adapter.rv.base.ItemViewDelegate;
import me.icefire.adapter.rv.base.ItemViewDelegateManager;
import me.icefire.adapter.rv.base.ViewHolder;

/**
 * @author yangchj
 * @email yangchj@icefire.me
 * @date 2019/1/2
 */
public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext;
    protected List<T> mDatas;

    protected OnItemClickListener mOnItemClickListener;
    protected OnChildItemClickListener mOnChildItemClickListener;
    protected ItemViewDelegateManager mItemViewDelegateManager;

    public MultiItemTypeAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.mDatas = datas;
        if (mDatas==null){
            throw new IllegalArgumentException("mDatas not allow null");
        }
        mItemViewDelegateManager=new ItemViewDelegateManager();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemViewDelegate itemViewDelegate=mItemViewDelegateManager.getItemViewDelegate(i);
        int layoutId=itemViewDelegate.getItemViewLayoutId();
        ViewHolder holder=ViewHolder.createViewHolder(mContext,viewGroup,layoutId);
        onViewHolderCreated(holder,holder.getConvertView());
        setListener(viewGroup,holder,i);
        return holder;
    }

    public void onViewHolderCreated(ViewHolder holder,View itemView){

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        convert(viewHolder,mDatas.get(i));
    }

    public void convert(ViewHolder holder,T t){
        mItemViewDelegateManager.convert(holder,t,holder.getAdapterPosition());
    }

    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, final int viewType){
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener!=null){
                    int position=viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v,viewHolder,position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener!=null){
                    int position=viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v,viewHolder,position);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (!useItemViewDelegateManager()) return super.getItemViewType(position);
        return mItemViewDelegateManager.getItemViewType(mDatas.get(position),position);
    }

    @Override
    public int getItemCount() {
        int itemCount=mDatas.size();
        return itemCount;
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    public List<T> getDatas(){
        return mDatas;
    }

    /**
     * 设置数据
     * @param datas
     */
    public void setDatas(List<T> datas){
        if (mDatas!=null&&datas!=null){
            mDatas.clear();
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    /**
     * 在末端添加数据
     * @param datas
     */
    public void addDatas(List<T> datas){
        if (mDatas!=null&&datas!=null){
            mDatas.addAll(mDatas.size(),datas);
            notifyDataSetChanged();
        }
    }

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate){
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int viewType,ItemViewDelegate<T> itemViewDelegate){
        mItemViewDelegateManager.addDelegate(viewType,itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager(){
        return mItemViewDelegateManager.getItemViewDelegateCount()>0;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public interface OnChildItemClickListener {
        void onChildItemClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnChildItemClickListener(OnChildItemClickListener mOnChildItemClickListener) {
        this.mOnChildItemClickListener = mOnChildItemClickListener;
    }
}
