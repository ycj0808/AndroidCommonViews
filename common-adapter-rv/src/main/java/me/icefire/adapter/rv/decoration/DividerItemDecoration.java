package me.icefire.adapter.rv.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * 万能的分割线
 *
 * @author yangchj
 * @email yangchj@icefire.me
 * @date 2019/1/7
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private Paint mPaint;
    private Drawable mDividerDrawable;
    private int mDividerHeight;//默认1px;
    private int mOrientation;//列表方向
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    public static final int HORIZONTAL_LIST = RecyclerView.HORIZONTAL;//水平0
    public static final int VERTICAL_LIST = RecyclerView.VERTICAL;//垂直1
    public static final int BOTH_SET = 2;//水平+垂直

    public int mPaddingStart;
    public int mPaddingEnd;
    private boolean mDrawLastItem = true;
    private boolean mDrawHeaderFooter = false;
    private int headCount;
    private int footCount;

    /**
     * 默认分割线，高度2px,颜色为灰色
     *
     * @param context
     * @param orientation
     */
    public DividerItemDecoration(Context context, int orientation, int height, int start, int end) {
        this.setOrientation(orientation);
        this.mDividerHeight = height;
        this.mPaddingStart = start;
        this.mPaddingEnd = end;
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        if (a != null) {
            mDividerDrawable = a.getDrawable(0);
            a.recycle();
        }
    }

    /**
     * 默认分割线 默认纵向列表
     *
     * @param context
     * @param height
     * @param start
     * @param end
     */
    public DividerItemDecoration(Context context, int height, int start, int end) {
        this(context, VERTICAL_LIST, height, start, end);
    }

    /**
     * 自定义分割线
     *
     * @param orientation 列表方向
     * @param drawable    自定义drawable
     * @param start
     * @param end
     */
    public DividerItemDecoration(int orientation, Drawable drawable, int start, int end) {
        this.setOrientation(orientation);
        mDividerDrawable = drawable;
        this.mPaddingStart = start;
        this.mPaddingEnd = end;
        if (mDividerDrawable != null) {
            mDividerHeight = mDividerDrawable.getIntrinsicHeight();
        }
    }

    /**
     * 自定义分割线 默认纵向列表
     *
     * @param drawable 自定义drawable
     * @param start
     * @param end
     */
    public DividerItemDecoration(Drawable drawable, int start, int end) {
        this(VERTICAL_LIST, drawable, start, end);
    }

    /**
     * 自定义分割线
     *
     * @param orientation
     * @param dividerColor 自定义分割线颜色
     * @param height
     * @param start
     * @param end
     */
    public DividerItemDecoration(int orientation, int dividerColor, int height, int start, int end) {
        this.setOrientation(orientation);
        mDividerDrawable = new ColorDrawable(dividerColor);
        this.mDividerHeight = height;
        this.mPaddingStart = start;
        this.mPaddingEnd = end;
    }

    /**
     * 自定义分割线 默认纵向列表
     *
     * @param dividerColor 自定义分割线颜色
     * @param height
     * @param start
     * @param end
     */
    public DividerItemDecoration(int dividerColor, int height, int start, int end) {
        this(VERTICAL_LIST, dividerColor, height, start, end);
    }


    /**
     * 设置方向
     *
     * @param orientation
     */
    public void setOrientation(int orientation) {
        if (orientation < 0 || orientation > 2) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    public void setDrawLastItem(boolean mDrawLastItem) {
        this.mDrawLastItem = mDrawLastItem;
    }

    public void setDrawHeaderFooter(boolean mDrawHeaderFooter) {
        this.mDrawHeaderFooter = mDrawHeaderFooter;
    }

    /**
     * 绘制分割线之后，需要留出一个外边框item间的间距要换一下
     *
     * @param outRect outRect.set(0, 0, 0, 0);的四个参数理解成margin就好了
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //获取layoutParams参数
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        //当前位置
        int itemPosition = layoutParams.getViewLayoutPosition();
        if (parent.getAdapter() == null) return;
        int childCount = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        if (position >= headCount && position < childCount - footCount || mDrawHeaderFooter) {
            switch (mOrientation) {
                case BOTH_SET:
                    int spanCount = this.getSpanCount(parent);
                    if (mDrawLastItem) {//绘制最后一行一列
                        outRect.set(0, 0, mDividerHeight, mDividerHeight);
                    } else {
                        if (isLastRaw(parent, itemPosition, spanCount, childCount)) {//是最后一行，不需要绘制底部
                            outRect.set(0, 0, mDividerHeight, 0);
                        } else if (isLastColum(parent, itemPosition, spanCount, childCount)) {//是最后一列，不需要绘制右边
                            outRect.set(0, 0, 0, mDividerHeight);
                        } else {
                            outRect.set(0, 0, mDividerHeight, mDividerHeight);
                        }
                    }
                    break;
                case VERTICAL_LIST:
                    if (mDrawLastItem) {
                        outRect.set(0, 0, mDividerHeight, 0);
                    } else {
                        //水平布局右侧留Margin,如果是最后一列,就不要留Margin了
                        childCount -= 1;
                        outRect.set(0, 0, (itemPosition != childCount) ? mDividerHeight : 0, 0);
                    }
                    break;
                case HORIZONTAL_LIST:
                    if (mDrawLastItem) {
                        outRect.set(0, 0, 0, mDividerHeight);
                    } else {
                        //垂直布局底部留边，最后一行不留
                        childCount -= 1;
                        outRect.set(0, 0, 0, (itemPosition != childCount) ? mDividerHeight : 0);
                    }
                    break;
            }
        }
    }


    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (parent.getAdapter() == null) return;
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else if (mOrientation == HORIZONTAL_LIST) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
            drawHorizontal(c, parent);
        }
    }

    /**
     * 绘制横向Item分割线
     *
     * @param canvas
     * @param parent
     */
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        final int x = parent.getPaddingLeft() + mPaddingStart;
        final int width = parent.getMeasuredWidth() - parent.getPaddingRight() - mPaddingEnd;
        final int childSize = parent.getChildCount();
        int dataCount = parent.getAdapter().getItemCount();
        int startPos = headCount;
        int endPos = headCount + dataCount;
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (position >= startPos && position < endPos - 1
                    || (position == endPos - 1 && mDrawLastItem)
                    || (!(position >= startPos && position < endPos) && mDrawHeaderFooter)) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                //Item底部的Y轴坐标+margin值
                final int y = child.getBottom() + layoutParams.bottomMargin;
                final int height = y + mDividerHeight;
                if (mDividerDrawable != null) {
                    mDividerDrawable.setBounds(x, y, width, height);
                    mDividerDrawable.draw(canvas);
                }
            }
        }
    }

    /**
     * 绘制纵向Item分割线
     *
     * @param canvas
     * @param parent
     */
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop() + mPaddingStart;
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom() - mPaddingEnd;
        final int childSize = parent.getChildCount();
        int dataCount = parent.getAdapter().getItemCount();
        int startPos = headCount;
        int endPos = headCount + dataCount;
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);
            if (position >= startPos && position < endPos - 1
                    || (position == endPos - 1 && mDrawLastItem)
                    || (!(position >= startPos && position < endPos) && mDrawHeaderFooter)) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                final int left = child.getRight() + layoutParams.rightMargin;
                final int right = left + mDividerHeight;
                if (mDividerDrawable != null) {
                    mDividerDrawable.setBounds(left, top, right, bottom);
                    mDividerDrawable.draw(canvas);
                }
            }
        }
    }

    /**
     * 获取列数
     *
     * @param parent
     * @return
     */
    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    /**
     * 判断是否是最后一列
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            int orientation = ((GridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是最后一列，则不需要绘制右边
                if ((pos + 1) % spanCount == 0)
                    return true;
            } else {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一列，则不需要绘制右边
                if (pos >= childCount)
                    return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是最后一列，则不需要绘制右边
                if ((pos + 1) % spanCount == 0)
                    return true;
            } else {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一列，则不需要绘制右边
                if (pos >= childCount)
                    return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是最后一行
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        int orientation;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            orientation = ((GridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是最后一行，则不需要绘制底部
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)
                    return true;
            } else {// StaggeredGridLayoutManager 横向滚动
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0)
                    return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是最后一行，则不需要绘制底部
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount)
                    return true;
            } else {// StaggeredGridLayoutManager 横向滚动
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0)
                    return true;
            }
        }
        return false;
    }

    public void setHeadCount(int headCount) {
        this.headCount = headCount;
    }

    public void setFootCount(int footCount) {
        this.footCount = footCount;
    }
}
