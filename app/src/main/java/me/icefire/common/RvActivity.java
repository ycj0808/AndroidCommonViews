package me.icefire.common;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.icefire.adapter.rv.CommonAdapter;
import me.icefire.adapter.rv.base.ViewHolder;
import me.icefire.adapter.rv.decoration.DividerItemDecoration;
import me.icefire.adapter.rv.utils.Utils;
import me.icefire.adapter.rv.wrapper.EmptyWrapper;
import me.icefire.adapter.rv.wrapper.HeaderAndFooterWrapper;
import me.icefire.adapter.rv.wrapper.LoadMoreWrapper;

/**
 * @author yangchj
 * @email yangchj@icefire.me
 * @date 2019/1/7
 */
public class RvActivity extends AppCompatActivity {

    RecyclerView rvList;
    private List<String> mDatas = new ArrayList<>();
    CommonAdapter commonAdapter;

    private EmptyWrapper mEmptyWrapper;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private LoadMoreWrapper mLoadMoreWrapper;
    private Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_list);
        mContext=this;
        initData();
        rvList = findViewById(R.id.rvList);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(OrientationHelper.HORIZONTAL, Color.parseColor("#ff0000"), Utils.dp2px(mContext,2),Utils.dp2px(mContext,16),Utils.dp2px(mContext,16));
        itemDecoration.setHeadCount(2);
        itemDecoration.setDrawHeaderFooter(false);
        rvList.addItemDecoration(itemDecoration);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        commonAdapter = new CommonAdapter<String>(this, R.layout.item_list, mDatas) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.setText(R.id.id_item_list_title, s + "  位置：" + holder.getAdapterPosition());
            }
        };
        initHeaderAndFooter();
//        initEmptyView();
//        rvList.setAdapter(mEmptyWrapper);

        mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);
        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            mDatas.add("Add:" + i);
                        }
                        mLoadMoreWrapper.notifyDataSetChanged();
                    }
                }, 3000);
            }
        });

        rvList.setAdapter(mLoadMoreWrapper);
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            mDatas.add("test" + i);
        }
    }

    private void initEmptyView() {
        mEmptyWrapper = new EmptyWrapper(mHeaderAndFooterWrapper);
        View view = LayoutInflater.from(this).inflate(R.layout.empty_view, rvList, false);
        TextView txtNoData = view.findViewById(R.id.txtNoData);
        txtNoData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
                mEmptyWrapper.notifyDataSetChanged();
            }
        });
        mEmptyWrapper.setEmptyView(view);
    }

    private void initHeaderAndFooter() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(commonAdapter);
        TextView t1 = new TextView(this);
        t1.setText("Header 1");
        TextView t2 = new TextView(this);
        t2.setText("Header 2");
        mHeaderAndFooterWrapper.addHeaderView(t1);
        mHeaderAndFooterWrapper.addHeaderView(t2);
    }
}
