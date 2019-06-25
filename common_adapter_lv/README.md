## 简介
Android万能Adapter for ListView，支持多种Item类型

## 引用
```

implementation 'me.icefire:common_adapter_lv:1.0.1'
```

## 使用

```
//最常用的单种Item的书写方式
lvList.setAdapter(new CommonAdapter<String>(this,R.layout.item_list,mDatas){
    @Override
    public void convert(ViewHolder holder,String s){
        holder.setText(R.id.item01,s);
    }
});

还可以自定义Adapter继承CommonAdapter，复习convert方法，简化了大量代码
1.通过holder.getView(id)拿到任何控件
2.ViewHolder中封装了大量的常用的方法,比如：
 public ViewHolder setText(int viewId, String text);
 public ViewHolder setTextColor(int viewId, int textColor);
 public ViewHolder setBackgroundColor(int viewId, int bgColor);
 public ViewHolder setBackgroundRes(int viewId, int resId);
 public ViewHolder setVisible(int viewId, boolean visible);
 public ViewHolder setAlpha(int viewId, float value);
 public ViewHolder setImageResource(int viewId, int resId);
 ...
```

## 多种ItemViewType
```
MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(this,mDatas);
adapter.addItemViewDelegate(new MsgSendItemDelagate());
adapter.addItemViewDelegate(new MsgComingItemDelagate());

//每种Item类型对应一个ItemViewDelegete
public class MsgComingItemDelagate implements ItemViewDelegate<ChatMessage>{

    @Override
    public int getItemViewLayoutId()
    {
        return R.layout.main_chat_from_msg;
    }

    @Override
    public boolean isForViewType(ChatMessage item, int position)
    {
        return item.isComMeg();
    }

    @Override
    public void convert(ViewHolder holder, ChatMessage chatMessage, int position)
    {
        holder.setText(R.id.chat_from_content, chatMessage.getContent());
        holder.setText(R.id.chat_from_name, chatMessage.getName());
        holder.setImageResource(R.id.chat_from_icon, chatMessage.getIcon());
    }
}
```

## 添加HeaderView,FooterView

```
private void initHeaderAndFooter() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(commonAdapter);
        TextView t1 = new TextView(this);
        t1.setText("Header 1");
        TextView t2 = new TextView(this);
        t2.setText("Header 2");
        mHeaderAndFooterWrapper.addHeaderView(t1);
        mHeaderAndFooterWrapper.addHeaderView(t2);
}

rvList.setAdapter(mHeaderAndFooterWrapper);
mHeaderAndFooterWrapper.notifyDataSetChanged();
```

## 添加EmptyView
```
mEmptyWrapper = new EmptyWrapper(mAdapter);
mEmptyWrapper.setEmptyView(R.layout.empty_view);
rvList.setAdapter(mEmptyWrapper);
```

## 万能分割线DividerItemDecoration
```
DividerItemDecoration itemDecoration=new DividerItemDecoration(OrientationHelper.HORIZONTAL, Color.parseColor("#ff0000"), Utils.dp2px(mContext,2),Utils.dp2px(mContext,16),Utils.dp2px(mContext,16));
        itemDecoration.setHeadCount(2);//设置Header的数目（如果没有可不设置）
        itemDecoration.setDrawHeaderFooter(false);//是否绘制绘制header和footer分割线
        itemDecoration.setDrawLastItem(true);//是否绘制最后一个Item的分割线
        rvList.addItemDecoration(itemDecoration);
```