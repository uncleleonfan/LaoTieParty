package com.leon.laotieparty;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Leon on 2017/4/9.
 */

public class PartyRoomLayout extends ViewGroup {

    private static final String TAG = "PartyRoomLayout";

    private GestureDetector mGestureDetector;

    //四六分屏模式
    private static int DISPLAY_MODE_SPLIT = 0;
    //上下分屏模式
    private static int DISPLAY_MODE_TOP_BOTTOM = 1;
    //显示模式的变量，默认是四六分屏
    private int mDisplayMode = DISPLAY_MODE_SPLIT;
    //上下分屏时上面View的下标
    private int mTopViewIndex = -1;

    public PartyRoomLayout(Context context) {
        this(context, null);
    }

    public PartyRoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, mOnGestureListener);
    }

    private GestureDetector.SimpleOnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            handleDoubleTap(e);//处理双击事件
            return true;
        }

        private void handleDoubleTap(MotionEvent e) {
            //遍历所有的孩子
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                //获取孩子view的矩形
                Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                if (rect.contains((int)e.getX(), (int)e.getY())) {//找到双击位置的孩子是谁
                    if (mTopViewIndex == i) {//如果点击的位置就是上面的view, 则切换成四六分屏模式
                        mDisplayMode = DISPLAY_MODE_SPLIT;
                        mTopViewIndex = -1;//重置上面view的下标
                    } else {
                        //切换成上下分屏模式，
                        mTopViewIndex = i;//保存双击位置的下标，即上面View的下标
                        mDisplayMode = DISPLAY_MODE_TOP_BOTTOM;
                    }
                    requestLayout();//请求重新布局
                    break;
                }
            }
        }
    };


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        if (childCount == 1) {
            measureOneChild(widthMeasureSpec, heightMeasureSpec);
        } else if (childCount == 2) {
            measureTwoChild(widthMeasureSpec, heightMeasureSpec);
        } else {
            if (mDisplayMode == DISPLAY_MODE_SPLIT) {
                measureMoreChildSplit(widthMeasureSpec, heightMeasureSpec);
            } else {
                measureMoreChildTopBottom(widthMeasureSpec, heightMeasureSpec);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 上下分屏模式的测量
     */
    private void measureMoreChildTopBottom(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            if (i == mTopViewIndex) {
                //测量上面View
                measureTopChild(widthMeasureSpec, heightMeasureSpec);
            } else {
                //测量下面View
                measureBottomChild(i, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    /**
     * 上下分屏模式时底部View的测量
     */
    private void measureBottomChild(int i, int widthMeasureSpec, int heightMeasureSpec) {
        //除去顶部孩子后还剩的孩子个数
        int childCountExcludeTop = getChildCount() - 1;
        //当底部孩子个数小于等于3时
        if (childCountExcludeTop <= 3) {
            //平分孩子宽度
            int childWidth = MeasureSpec.getSize(widthMeasureSpec) / childCountExcludeTop;
            int size = MeasureSpec.getSize(heightMeasureSpec);
            //高度为PartyRoomLayout的一半
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(size / 2, MeasureSpec.EXACTLY);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            getChildAt(i).measure(childWidthMeasureSpec, childHeightMeasureSpec);
        } else if (childCountExcludeTop == 4) {//当底部孩子个数为4个时
            int childWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;//宽度为PartyRoomLayout的一半
            int childHeight = MeasureSpec.getSize(heightMeasureSpec) / 4;//高度为PartyRoomLayout的1/4
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            getChildAt(i).measure(childWidthMeasureSpec, childHeightMeasureSpec);
        } else {//当底部孩子大于4个时
            //计算行的个数
            int row = childCountExcludeTop / 3;
            if (row  % 3 != 0) {
                row ++;
            }
            //孩子的宽度为PartyRoomLayout宽度的1/3
            int childWidth = MeasureSpec.getSize(widthMeasureSpec) / 3;
            //底部孩子平分PartyRoomLayout一半的高度
            int childHeight = (MeasureSpec.getSize(heightMeasureSpec) / 2) / row;
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            getChildAt(i).measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    /**
     *  上下分屏模式时上面View的测量
     */
    private void measureTopChild(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(heightMeasureSpec);
        //高度为PartyRoomLayout的一半
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(size / 2, MeasureSpec.EXACTLY);
        getChildAt(mTopViewIndex).measure(widthMeasureSpec, childHeightMeasureSpec);
    }

    /**
     * 四六分屏的测量
     */
    private void measureMoreChildSplit(int widthMeasureSpec, int heightMeasureSpec) {
        //列数为两列，计算行数
        int row = getChildCount() / 2;
        if (getChildCount() % 2 != 0) {
            row = row + 1;
        }
        int childHeight = MeasureSpec.getSize(heightMeasureSpec) / row;
        int childWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    /**
     *  拦截所有的事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    /**
     * 让GestureDetector处理触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * 二分屏时的测量
     */
    private void measureTwoChild(int widthMeasureSpec, int heightMeasureSpec) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int size = MeasureSpec.getSize(heightMeasureSpec);
            //孩子高度为父容器高度的一半
            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(size / 2, MeasureSpec.EXACTLY);
            child.measure(widthMeasureSpec, childHeightMeasureSpec);
        }
    }

    /**
     * 测量一个孩子的情况，孩子的宽高和父容器即PartyRoomLayout一样
     */
    private void measureOneChild(int widthMeasureSpec, int heightMeasureSpec) {
        View child = getChildAt(0);
        child.measure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount == 1) {
            layoutOneChild();
        } else if (childCount == 2) {
            layoutTwoChild();
        } else {
            if (mDisplayMode == DISPLAY_MODE_SPLIT) {
                layoutMoreChildSplit();//四六分屏模式布局
            } else {
                layoutMoreChildTopBottom();//上下分屏模式布局
            }
        }
    }

    /**
     * 上下分屏模式的布局
     */
    private void layoutMoreChildTopBottom() {
        //布局上面View
        View topView = getChildAt(mTopViewIndex);
        topView.layout(0, 0, topView.getMeasuredWidth(), topView.getMeasuredHeight());
        int left = 0;
        int top = topView.getMeasuredHeight();
        for (int i = 0; i < getChildCount(); i++) {
            //上面已经布局过上面的View, 这里就跳过
            if (i == mTopViewIndex) {
                continue;
            }
            View view = getChildAt(i);
            int right = left + view.getMeasuredWidth();
            int bottom = top + view.getMeasuredHeight();
            //布局下面的一个View
            view.layout(left, top, right, bottom);
            left = left + view.getMeasuredWidth();
            if (left >= getWidth()) {//满足换行条件则换行
                left = 0;
                top += view.getMeasuredHeight();
            }
        }
    }

    /**
     * 四六分屏布局
     */
    private void layoutMoreChildSplit() {
        int left = 0;
        int top = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int right = left + child.getMeasuredWidth();
            int bottom = top + child.getMeasuredHeight();
            child.layout(left, top, right, bottom);
            if ( (i + 1 )% 2 == 0) {//满足换行条件，更新left和top，布局下一行
                left = 0;
                top += child.getMeasuredHeight();
            } else {
                //不满足换行条件，更新left值，继续布局一行中的下一个孩子
                left += child.getMeasuredWidth();
            }
        }
    }

    /**
     * 二分屏模式的布局
     */
    private void layoutTwoChild() {
        int left = 0;
        int top = 0;
        int right = getMeasuredWidth();
        int bottom = getChildAt(0).getMeasuredHeight();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.layout(left, top, right, bottom);
            top += child.getMeasuredHeight();
            bottom += child.getMeasuredHeight();
        }
    }

    /**
     * 布局一个孩子的情况
     */
    private void layoutOneChild() {
        View child = getChildAt(0);
        child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
    }
}
