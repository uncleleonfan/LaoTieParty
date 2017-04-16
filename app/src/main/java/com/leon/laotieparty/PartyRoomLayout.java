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

    private static int DISPLAY_MODE_SPLIT = 0;
    private static int DISPLAY_MODE_TOP_BOTTOM = 1;

    private int mDisplayMode = DISPLAY_MODE_SPLIT;
    private int mTopViewIndex = 0;

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
            Log.d(TAG, "onDoubleTap: ");
            findClickView(e);
            mDisplayMode = DISPLAY_MODE_TOP_BOTTOM;
            requestLayout();
            return true;
        }

        private void findClickView(MotionEvent e) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                Rect rect = new Rect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                if (rect.contains((int)e.getX(), (int)e.getY())) {
                    mTopViewIndex = i;
                    Log.d(TAG, "findClickView: " + mTopViewIndex);
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

    private void measureMoreChildTopBottom(int widthMeasureSpec, int heightMeasureSpec) {
        int childCountExcludeTop = getChildCount() - 1;
        if (childCountExcludeTop <= 3) {
            for (int j = 0; j < getChildCount(); j++) {
                if (j == mTopViewIndex) {
                    measureTopView(widthMeasureSpec, heightMeasureSpec);
                } else {
                    int childWidth = MeasureSpec.getSize(widthMeasureSpec) / childCountExcludeTop;
                    int size = MeasureSpec.getSize(heightMeasureSpec);
                    int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(size / 2, MeasureSpec.EXACTLY);
                    int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                    getChildAt(j).measure(childWidthMeasureSpec, childHeightMeasureSpec);
                }
            }
        } else if (childCountExcludeTop == 4) {
            for (int k = 0; k < getChildCount(); k++) {
                if (k == mTopViewIndex) {
                    measureTopView(widthMeasureSpec, heightMeasureSpec);
                } else {
                    int childHeight = MeasureSpec.getSize(heightMeasureSpec) / 4;
                    int childWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
                    int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
                    int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                    getChildAt(k).measure(childWidthMeasureSpec, childHeightMeasureSpec);
                }
            }
        } else {
            for (int m = 0; m < getChildCount(); m++) {
                if (m == mTopViewIndex) {
                    measureTopView(widthMeasureSpec, heightMeasureSpec);
                } else {
                    int row = childCountExcludeTop / 3;
                    if (row  % 3 != 0) {
                        row ++;
                    }
                    int childWidth = MeasureSpec.getSize(widthMeasureSpec) / 3;
                    int childHeight = MeasureSpec.getSize(heightMeasureSpec) / row;
                    int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
                    int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                    View child = getChildAt(m);
                    child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                }
            }
        }

    }

    private void measureTopView(int widthMeasureSpec, int heightMeasureSpec) {
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(size / 2, MeasureSpec.EXACTLY);
        getChildAt(mTopViewIndex).measure(widthMeasureSpec, childHeightMeasureSpec);
    }

    /**
     * 四分屏或者六分屏的测量
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

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
                layoutMoreChildSplit();
            } else {
                layoutMoreChildTopBottom();
            }
        }
    }

    private void layoutMoreChildTopBottom() {
        //layout top child
        View topView = getChildAt(mTopViewIndex);
        topView.layout(0, 0, topView.getMeasuredWidth(), topView.getMeasuredHeight());
        int left = 0;
        int top = topView.getMeasuredHeight();
        for (int i = 0; i < getChildCount(); i++) {
            if (i == mTopViewIndex) {
                return;
            }
            View view = getChildAt(i);
            int right = view.getMeasuredWidth();
            int bottom = top + view.getMeasuredHeight();
            view.layout(left, top, right, bottom);
            left = left + view.getMeasuredWidth();
            if (left >= getWidth()) {
                left = 0;
                top += view.getMeasuredHeight();
            }

        }
    }

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
