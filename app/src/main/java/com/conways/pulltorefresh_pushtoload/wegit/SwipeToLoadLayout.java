package com.conways.pulltorefresh_pushtoload.wegit;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Scroller;

import com.conways.pulltorefresh_pushtoload.R;

public class SwipeToLoadLayout extends ViewGroup {


    private static final int DEFAULT_SWIPING_TO_REFRESH_TO_DEFAULT_SCROLLING_DURATION = 200;

    private static final int DEFAULT_RELEASE_TO_REFRESHING_SCROLLING_DURATION = 200;

    private static final int DEFAULT_REFRESH_COMPLETE_DELAY_DURATION = 300;

    private static final int DEFAULT_REFRESH_COMPLETE_TO_DEFAULT_SCROLLING_DURATION = 500;

    private static final int DEFAULT_DEFAULT_TO_REFRESHING_SCROLLING_DURATION = 500;

    private static final int DEFAULT_SWIPING_TO_LOAD_MORE_TO_DEFAULT_SCROLLING_DURATION = 200;

    private static final int DEFAULT_RELEASE_TO_LOADING_MORE_SCROLLING_DURATION = 200;

    private static final int DEFAULT_LOAD_MORE_COMPLETE_DELAY_DURATION = 300;

    private static final int DEFAULT_LOAD_MORE_COMPLETE_TO_DEFAULT_SCROLLING_DURATION = 300;

    private static final int DEFAULT_DEFAULT_TO_LOADING_MORE_SCROLLING_DURATION = 300;

    private static final float DEFAULT_DRAG_RATIO = 0.5f;

    private static final int INVALID_POINTER = -1;

    private static final int INVALID_COORDINATE = -1;

    private AutoScroller mAutoScroller;

    private OnRefreshListener mRefreshListener;

    private OnLoadMoreListener mLoadMoreListener;

    private View mHeaderView;

    private View mTargetView;

    private View mFooterView;

    private int mHeaderHeight;

    private int mFooterHeight;



    private float mDragRatio = DEFAULT_DRAG_RATIO;

    private boolean mAutoLoading;

    private final int mTouchSlop;

    private int mStatus = STATUS.STATUS_DEFAULT;

    private int mHeaderOffset;

    private int mTargetOffset;

    private int mFooterOffset;

    private float mInitDownY;

    private float mInitDownX;

    private float mLastY;

    private float mLastX;

    private int mActivePointerId;

    private boolean mRefreshEnabled = true;

    private boolean mLoadMoreEnabled = true;

    private int mStyle = STYLE.CLASSIC;

    private float mRefreshTriggerOffset;

    private float mLoadMoreTriggerOffset;

    private float mRefreshFinalDragOffset;

    private float mLoadMoreFinalDragOffset;

    private int mSwipingToRefreshToDefaultScrollingDuration = DEFAULT_SWIPING_TO_REFRESH_TO_DEFAULT_SCROLLING_DURATION;

    private int mReleaseToRefreshToRefreshingScrollingDuration = DEFAULT_RELEASE_TO_REFRESHING_SCROLLING_DURATION;

    private int mRefreshCompleteDelayDuration = DEFAULT_REFRESH_COMPLETE_DELAY_DURATION;

    private int mRefreshCompleteToDefaultScrollingDuration = DEFAULT_REFRESH_COMPLETE_TO_DEFAULT_SCROLLING_DURATION;

    private int mDefaultToRefreshingScrollingDuration = DEFAULT_DEFAULT_TO_REFRESHING_SCROLLING_DURATION;

    private int mReleaseToLoadMoreToLoadingMoreScrollingDuration = DEFAULT_RELEASE_TO_LOADING_MORE_SCROLLING_DURATION;


    private int mLoadMoreCompleteDelayDuration = DEFAULT_LOAD_MORE_COMPLETE_DELAY_DURATION;

    private int mLoadMoreCompleteToDefaultScrollingDuration = DEFAULT_LOAD_MORE_COMPLETE_TO_DEFAULT_SCROLLING_DURATION;

    private int mSwipingToLoadMoreToDefaultScrollingDuration = DEFAULT_SWIPING_TO_LOAD_MORE_TO_DEFAULT_SCROLLING_DURATION;

    private int mDefaultToLoadingMoreScrollingDuration = DEFAULT_DEFAULT_TO_LOADING_MORE_SCROLLING_DURATION;

    public static final class STYLE {
        public static final int CLASSIC = 0;
        public static final int ABOVE = 1;
        public static final int BLEW = 2;
        public static final int SCALE = 3;
    }

    public SwipeToLoadLayout(Context context) {
        this(context, null);
    }

    public SwipeToLoadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeToLoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeToLoadLayout, defStyleAttr, 0);
        try {
            final int N = a.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.SwipeToLoadLayout_refresh_enabled) {
                    setRefreshEnabled(a.getBoolean(attr, true));

                } else if (attr == R.styleable.SwipeToLoadLayout_load_more_enabled) {
                    setLoadMoreEnabled(a.getBoolean(attr, true));

                } else if (attr == R.styleable.SwipeToLoadLayout_swipe_style) {
                    setSwipeStyle(a.getInt(attr, STYLE.CLASSIC));

                } else if (attr == R.styleable.SwipeToLoadLayout_drag_ratio) {
                    setDragRatio(a.getFloat(attr, DEFAULT_DRAG_RATIO));

                } else if (attr == R.styleable.SwipeToLoadLayout_refresh_final_drag_offset) {
                    setRefreshFinalDragOffset(a.getDimensionPixelOffset(attr, 0));

                } else if (attr == R.styleable.SwipeToLoadLayout_load_more_final_drag_offset) {
                    setLoadMoreFinalDragOffset(a.getDimensionPixelOffset(attr, 0));

                } else if (attr == R.styleable.SwipeToLoadLayout_refresh_trigger_offset) {
                    setRefreshTriggerOffset(a.getDimensionPixelOffset(attr, 0));

                } else if (attr == R.styleable.SwipeToLoadLayout_load_more_trigger_offset) {
                    setLoadMoreTriggerOffset(a.getDimensionPixelOffset(attr, 0));

                } else if (attr == R.styleable.SwipeToLoadLayout_swiping_to_refresh_to_default_scrolling_duration) {
                    setSwipingToRefreshToDefaultScrollingDuration(a.getInt(attr, DEFAULT_SWIPING_TO_REFRESH_TO_DEFAULT_SCROLLING_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_release_to_refreshing_scrolling_duration) {
                    setReleaseToRefreshingScrollingDuration(a.getInt(attr, DEFAULT_RELEASE_TO_REFRESHING_SCROLLING_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_refresh_complete_delay_duration) {
                    setRefreshCompleteDelayDuration(a.getInt(attr, DEFAULT_REFRESH_COMPLETE_DELAY_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_refresh_complete_to_default_scrolling_duration) {
                    setRefreshCompleteToDefaultScrollingDuration(a.getInt(attr, DEFAULT_REFRESH_COMPLETE_TO_DEFAULT_SCROLLING_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_default_to_refreshing_scrolling_duration) {
                    setDefaultToRefreshingScrollingDuration(a.getInt(attr, DEFAULT_DEFAULT_TO_REFRESHING_SCROLLING_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_swiping_to_load_more_to_default_scrolling_duration) {
                    setSwipingToLoadMoreToDefaultScrollingDuration(a.getInt(attr, DEFAULT_SWIPING_TO_LOAD_MORE_TO_DEFAULT_SCROLLING_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_release_to_loading_more_scrolling_duration) {
                    setReleaseToLoadingMoreScrollingDuration(a.getInt(attr, DEFAULT_RELEASE_TO_LOADING_MORE_SCROLLING_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_load_more_complete_delay_duration) {
                    setLoadMoreCompleteDelayDuration(a.getInt(attr, DEFAULT_LOAD_MORE_COMPLETE_DELAY_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_load_more_complete_to_default_scrolling_duration) {
                    setLoadMoreCompleteToDefaultScrollingDuration(a.getInt(attr, DEFAULT_LOAD_MORE_COMPLETE_TO_DEFAULT_SCROLLING_DURATION));

                } else if (attr == R.styleable.SwipeToLoadLayout_default_to_loading_more_scrolling_duration) {
                    setDefaultToLoadingMoreScrollingDuration(a.getInt(attr, DEFAULT_DEFAULT_TO_LOADING_MORE_SCROLLING_DURATION));

                }
            }
        } finally {
            a.recycle();
        }

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mAutoScroller = new AutoScroller();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final int childNum = getChildCount();
        if (childNum == 0) {
            return;
        } else if (0 < childNum && childNum < 4) {
            mHeaderView = findViewById(R.id.swipe_refresh_header);
            mTargetView = findViewById(R.id.swipe_target);
            mFooterView = findViewById(R.id.swipe_load_more_footer);
        } else {
            throw new IllegalStateException("Children num must equal or less than 3");
        }
        if (mTargetView == null) {
            return;
        }
        if (mHeaderView != null && mHeaderView instanceof SwipeTrigger) {
            mHeaderView.setVisibility(GONE);
        }
        if (mFooterView != null && mFooterView instanceof SwipeTrigger) {
            mFooterView.setVisibility(GONE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            final View headerView = mHeaderView;
            measureChildWithMargins(headerView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = ((MarginLayoutParams) headerView.getLayoutParams());
            mHeaderHeight = headerView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (mRefreshTriggerOffset < mHeaderHeight) {
                mRefreshTriggerOffset = mHeaderHeight;
            }
        }
        if (mTargetView != null) {
            final View targetView = mTargetView;
            measureChildWithMargins(targetView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        if (mFooterView != null) {
            final View footerView = mFooterView;
            measureChildWithMargins(footerView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = ((MarginLayoutParams) footerView.getLayoutParams());
            mFooterHeight = footerView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if (mLoadMoreTriggerOffset < mFooterHeight) {
                mLoadMoreTriggerOffset = mFooterHeight;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChildren();

    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                onActivePointerUp();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                mInitDownY = mLastY = getMotionEventY(event, mActivePointerId);
                mInitDownX = mLastX = getMotionEventX(event, mActivePointerId);

                if (STATUS.isSwipingToRefresh(mStatus) || STATUS.isSwipingToLoadMore(mStatus) ||
                        STATUS.isReleaseToRefresh(mStatus) || STATUS.isReleaseToLoadMore(mStatus)) {
                    mAutoScroller.abortIfRunning();
                }

                if (STATUS.isSwipingToRefresh(mStatus) || STATUS.isReleaseToRefresh(mStatus)
                        || STATUS.isSwipingToLoadMore(mStatus) || STATUS.isReleaseToLoadMore(mStatus)) {
                    return true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                float y = getMotionEventY(event, mActivePointerId);
                float x = getMotionEventX(event, mActivePointerId);
                final float yInitDiff = y - mInitDownY;
                final float xInitDiff = x - mInitDownX;
                mLastY = y;
                mLastX = x;
                boolean moved = Math.abs(yInitDiff) > Math.abs(xInitDiff)
                        && Math.abs(yInitDiff) > mTouchSlop;
                boolean triggerCondition =
                        (yInitDiff > 0 && moved && onCheckCanRefresh()) ||
                                (yInitDiff < 0 && moved && onCheckCanLoadMore());
                if (triggerCondition) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP: {
                onSecondaryPointerUp(event);
                mInitDownY = mLastY = getMotionEventY(event, mActivePointerId);
                mInitDownX = mLastX = getMotionEventX(event, mActivePointerId);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER;
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                return true;

            case MotionEvent.ACTION_MOVE:
                final float y = getMotionEventY(event, mActivePointerId);
                final float x = getMotionEventX(event, mActivePointerId);

                final float yDiff = y - mLastY;
                final float xDiff = x - mLastX;
                mLastY = y;
                mLastX = x;

                if (Math.abs(xDiff) > Math.abs(yDiff) && Math.abs(xDiff) > mTouchSlop) {
                    return false;
                }

                if (STATUS.isStatusDefault(mStatus)) {
                    if (yDiff > 0 && onCheckCanRefresh()) {
                        mRefreshCallback.onPrepare();
                        setStatus(STATUS.STATUS_SWIPING_TO_REFRESH);
                    } else if (yDiff < 0 && onCheckCanLoadMore()) {
                        mLoadMoreCallback.onPrepare();
                        setStatus(STATUS.STATUS_SWIPING_TO_LOAD_MORE);
                    }
                } else if (STATUS.isRefreshStatus(mStatus)) {
                    if (mTargetOffset <= 0) {
                        setStatus(STATUS.STATUS_DEFAULT);
                        fixCurrentStatusLayout();
                        return false;
                    }
                } else if (STATUS.isLoadMoreStatus(mStatus)) {
                    if (mTargetOffset >= 0) {
                        setStatus(STATUS.STATUS_DEFAULT);
                        fixCurrentStatusLayout();
                        return false;
                    }
                }

                if (STATUS.isRefreshStatus(mStatus)) {
                    if (STATUS.isSwipingToRefresh(mStatus) || STATUS.isReleaseToRefresh(mStatus)) {
                        if (mTargetOffset >= mRefreshTriggerOffset) {
                            setStatus(STATUS.STATUS_RELEASE_TO_REFRESH);
                        } else {
                            setStatus(STATUS.STATUS_SWIPING_TO_REFRESH);
                        }
                        fingerScroll(yDiff);
                    }
                } else if (STATUS.isLoadMoreStatus(mStatus)) {
                    if (STATUS.isSwipingToLoadMore(mStatus) || STATUS.isReleaseToLoadMore(mStatus)) {
                        if (-mTargetOffset >= mLoadMoreTriggerOffset) {
                            setStatus(STATUS.STATUS_RELEASE_TO_LOAD_MORE);
                        } else {
                            setStatus(STATUS.STATUS_SWIPING_TO_LOAD_MORE);
                        }
                        fingerScroll(yDiff);
                    }
                }
                return true;

            case MotionEvent.ACTION_POINTER_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);
                if (pointerId != INVALID_POINTER) {
                    mActivePointerId = pointerId;
                }
                mInitDownY = mLastY = getMotionEventY(event, mActivePointerId);
                mInitDownX = mLastX = getMotionEventX(event, mActivePointerId);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                onSecondaryPointerUp(event);
                mInitDownY = mLastY = getMotionEventY(event, mActivePointerId);
                mInitDownX = mLastX = getMotionEventX(event, mActivePointerId);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                mActivePointerId = INVALID_POINTER;
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }


    public boolean isRefreshEnabled() {
        return mRefreshEnabled;
    }

    public void setRefreshEnabled(boolean enable) {
        this.mRefreshEnabled = enable;
    }

    public boolean isLoadMoreEnabled() {
        return mLoadMoreEnabled;
    }

    public void setLoadMoreEnabled(boolean enable) {
        this.mLoadMoreEnabled = enable;
    }

    public boolean isRefreshing() {
        return STATUS.isRefreshing(mStatus);
    }

    public boolean isLoadingMore() {
        return STATUS.isLoadingMore(mStatus);
    }

    public void setRefreshHeaderView(View view) {
        if (view instanceof SwipeRefreshTrigger) {
            if (mHeaderView != null && mHeaderView != view) {
                removeView(mHeaderView);
            }
            if (mHeaderView != view) {
                this.mHeaderView = view;
                addView(view);
            }
        }
    }

    public void setLoadMoreFooterView(View view) {
        if (view instanceof SwipeLoadMoreTrigger) {
            if (mFooterView != null && mFooterView != view) {
                removeView(mFooterView);
            }
            if (mFooterView != view) {
                this.mFooterView = view;
                addView(mFooterView);
            }
        }
    }

    public void setSwipeStyle(int style) {
        this.mStyle = style;
        requestLayout();
    }

    public void setDragRatio(float dragRatio) {
        this.mDragRatio = dragRatio;
    }

    public void setRefreshTriggerOffset(int offset) {
        mRefreshTriggerOffset = offset;
    }

    public void setLoadMoreTriggerOffset(int offset) {
        mLoadMoreTriggerOffset = offset;
    }

    public void setRefreshFinalDragOffset(int offset) {
        mRefreshFinalDragOffset = offset;
    }

    public void setLoadMoreFinalDragOffset(int offset) {
        mLoadMoreFinalDragOffset = offset;
    }

    public void setSwipingToRefreshToDefaultScrollingDuration(int duration) {
        this.mSwipingToRefreshToDefaultScrollingDuration = duration;
    }
    public void setReleaseToRefreshingScrollingDuration(int duration) {
        this.mReleaseToRefreshToRefreshingScrollingDuration = duration;
    }

    public void setRefreshCompleteDelayDuration(int duration) {
        this.mRefreshCompleteDelayDuration = duration;
    }

    public void setRefreshCompleteToDefaultScrollingDuration(int duration) {
        this.mRefreshCompleteToDefaultScrollingDuration = duration;
    }

    public void setDefaultToRefreshingScrollingDuration(int duration) {
        this.mDefaultToRefreshingScrollingDuration = duration;
    }

    public void setSwipingToLoadMoreToDefaultScrollingDuration(int duration) {
        this.mSwipingToLoadMoreToDefaultScrollingDuration = duration;
    }

    public void setReleaseToLoadingMoreScrollingDuration(int duration) {
        this.mReleaseToLoadMoreToLoadingMoreScrollingDuration = duration;
    }

    public void setLoadMoreCompleteDelayDuration(int duration) {
        this.mLoadMoreCompleteDelayDuration = duration;
    }

    public void setLoadMoreCompleteToDefaultScrollingDuration(int duration) {
        this.mLoadMoreCompleteToDefaultScrollingDuration = duration;
    }

    public void setDefaultToLoadingMoreScrollingDuration(int duration) {
        this.mDefaultToLoadingMoreScrollingDuration = duration;
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        this.mRefreshListener = listener;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mLoadMoreListener = listener;
    }

    public void setRefreshing(boolean refreshing) {
        if (!isRefreshEnabled() || mHeaderView == null) {
            return;
        }
        this.mAutoLoading = refreshing;
        if (refreshing) {
            if (STATUS.isStatusDefault(mStatus)) {
                setStatus(STATUS.STATUS_SWIPING_TO_REFRESH);
                scrollDefaultToRefreshing();
            }
        } else {
            if (STATUS.isRefreshing(mStatus)) {
                mRefreshCallback.onComplete();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollRefreshingToDefault();
                    }
                }, mRefreshCompleteDelayDuration);
            }
        }
    }

    public void setLoadingMore(boolean loadingMore) {
        if (!isLoadMoreEnabled() || mFooterView == null) {
            return;
        }
        this.mAutoLoading = loadingMore;
        if (loadingMore) {
            if (STATUS.isStatusDefault(mStatus)) {
                setStatus(STATUS.STATUS_SWIPING_TO_LOAD_MORE);
                scrollDefaultToLoadingMore();
            }
        } else {
            if (STATUS.isLoadingMore(mStatus)) {
                mLoadMoreCallback.onComplete();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollLoadingMoreToDefault();
                    }
                }, mLoadMoreCompleteDelayDuration);
            }
        }
    }

    protected boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTargetView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTargetView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTargetView, -1) || mTargetView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTargetView, -1);
        }
    }

    protected boolean canChildScrollDown() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTargetView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTargetView;
                return absListView.getChildCount() > 0
                        && (absListView.getLastVisiblePosition() < absListView.getChildCount() - 1
                        || absListView.getChildAt(absListView.getChildCount() - 1).getBottom() > absListView.getPaddingBottom());
            } else {
                return ViewCompat.canScrollVertically(mTargetView, 1) || mTargetView.getScrollY() < 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTargetView, 1);
        }
    }

    private void layoutChildren() {
        final int width = getMeasuredWidth();
        final int height = getMeasuredHeight();

        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();

        if (mTargetView == null) {
            return;
        }

        if (mHeaderView != null) {
            final View headerView = mHeaderView;
            MarginLayoutParams lp = (MarginLayoutParams) headerView.getLayoutParams();
            final int headerLeft = paddingLeft + lp.leftMargin;
            final int headerTop;
            switch (mStyle) {
                case STYLE.CLASSIC:
                    headerTop = paddingTop + lp.topMargin - mHeaderHeight + mHeaderOffset;
                    break;
                case STYLE.ABOVE:
                    headerTop = paddingTop + lp.topMargin - mHeaderHeight + mHeaderOffset;
                    break;
                case STYLE.BLEW:
                    headerTop = paddingTop + lp.topMargin;
                    break;
                case STYLE.SCALE:
                    headerTop = paddingTop + lp.topMargin - mHeaderHeight / 2 + mHeaderOffset / 2;
                    break;
                default:
                    headerTop = paddingTop + lp.topMargin - mHeaderHeight + mHeaderOffset;
                    break;
            }
            final int headerRight = headerLeft + headerView.getMeasuredWidth();
            final int headerBottom = headerTop + headerView.getMeasuredHeight();
            headerView.layout(headerLeft, headerTop, headerRight, headerBottom);
        }


        if (mTargetView != null) {
            final View targetView = mTargetView;
            MarginLayoutParams lp = (MarginLayoutParams) targetView.getLayoutParams();
            final int targetLeft = paddingLeft + lp.leftMargin;
            final int targetTop;

            switch (mStyle) {
                case STYLE.CLASSIC:
                    targetTop = paddingTop + lp.topMargin + mTargetOffset;
                    break;
                case STYLE.ABOVE:
                    targetTop = paddingTop + lp.topMargin;
                    break;
                case STYLE.BLEW:
                    targetTop = paddingTop + lp.topMargin + mTargetOffset;
                    break;
                case STYLE.SCALE:
                    targetTop = paddingTop + lp.topMargin + mTargetOffset;
                    break;
                default:
                    targetTop = paddingTop + lp.topMargin + mTargetOffset;
                    break;
            }
            final int targetRight = targetLeft + targetView.getMeasuredWidth();
            final int targetBottom = targetTop + targetView.getMeasuredHeight();
            targetView.layout(targetLeft, targetTop, targetRight, targetBottom);
        }

        if (mFooterView != null) {
            final View footerView = mFooterView;
            MarginLayoutParams lp = (MarginLayoutParams) footerView.getLayoutParams();
            final int footerLeft = paddingLeft + lp.leftMargin;
            final int footerBottom;
            switch (mStyle) {
                case STYLE.CLASSIC:
                    footerBottom = height - paddingBottom - lp.bottomMargin + mFooterHeight + mFooterOffset;
                    break;
                case STYLE.ABOVE:
                    footerBottom = height - paddingBottom - lp.bottomMargin + mFooterHeight + mFooterOffset;
                    break;
                case STYLE.BLEW:
                    footerBottom = height - paddingBottom - lp.bottomMargin;
                    break;
                case STYLE.SCALE:
                    footerBottom = height - paddingBottom - lp.bottomMargin + mFooterHeight / 2 + mFooterOffset / 2;
                    break;
                default:
                    footerBottom = height - paddingBottom - lp.bottomMargin + mFooterHeight + mFooterOffset;
                    break;
            }
            final int footerTop = footerBottom - footerView.getMeasuredHeight();
            final int footerRight = footerLeft + footerView.getMeasuredWidth();

            footerView.layout(footerLeft, footerTop, footerRight, footerBottom);
        }

        if (mStyle == STYLE.CLASSIC
                || mStyle == STYLE.ABOVE) {
            if (mHeaderView != null) {
                mHeaderView.bringToFront();
            }
            if (mFooterView != null) {
                mFooterView.bringToFront();
            }
        } else if (mStyle == STYLE.BLEW || mStyle == STYLE.SCALE) {
            if (mTargetView != null) {
                mTargetView.bringToFront();
            }
        }
    }

    private void fixCurrentStatusLayout() {
        if (STATUS.isRefreshing(mStatus)) {
            mTargetOffset = (int) (mRefreshTriggerOffset + 0.5f);
            mHeaderOffset = mTargetOffset;
            mFooterOffset = 0;
            layoutChildren();
            invalidate();
        } else if (STATUS.isStatusDefault(mStatus)) {
            mTargetOffset = 0;
            mHeaderOffset = 0;
            mFooterOffset = 0;
            layoutChildren();
            invalidate();
        } else if (STATUS.isLoadingMore(mStatus)) {
            mTargetOffset = -(int) (mLoadMoreTriggerOffset + 0.5f);
            mHeaderOffset = 0;
            mFooterOffset = mTargetOffset;
            layoutChildren();
            invalidate();
        }
    }

    /**
     * scrolling by physical touch with your fingers
     *
     * @param yDiff
     */
    private void fingerScroll(final float yDiff) {
        float ratio = mDragRatio;
        float yScrolled = yDiff * ratio;

        // make sure (targetOffset>0 -> targetOffset=0 -> default status)
        // or (targetOffset<0 -> targetOffset=0 -> default status)
        // forbidden fling (targetOffset>0 -> targetOffset=0 ->targetOffset<0 -> default status)
        // or (targetOffset<0 -> targetOffset=0 ->targetOffset>0 -> default status)
        // I am so smart :)

        float tmpTargetOffset = yScrolled + mTargetOffset;
        if ((tmpTargetOffset > 0 && mTargetOffset < 0)
                || (tmpTargetOffset < 0 && mTargetOffset > 0)) {
            yScrolled = -mTargetOffset;
        }


        if (mRefreshFinalDragOffset >= mRefreshTriggerOffset && tmpTargetOffset > mRefreshFinalDragOffset) {
            yScrolled = mRefreshFinalDragOffset - mTargetOffset;
        } else if (mLoadMoreFinalDragOffset >= mLoadMoreTriggerOffset && -tmpTargetOffset > mLoadMoreFinalDragOffset) {
            yScrolled = -mLoadMoreFinalDragOffset - mTargetOffset;
        }

        if (STATUS.isRefreshStatus(mStatus)) {
            mRefreshCallback.onMove(mTargetOffset, false, false);
        } else if (STATUS.isLoadMoreStatus(mStatus)) {
            mLoadMoreCallback.onMove(mTargetOffset, false, false);
        }
        updateScroll(yScrolled);
    }

    private void autoScroll(final float yScrolled) {

        if (STATUS.isSwipingToRefresh(mStatus)) {
            mRefreshCallback.onMove(mTargetOffset, false, true);
        } else if (STATUS.isReleaseToRefresh(mStatus)) {
            mRefreshCallback.onMove(mTargetOffset, false, true);
        } else if (STATUS.isRefreshing(mStatus)) {
            mRefreshCallback.onMove(mTargetOffset, true, true);
        } else if (STATUS.isSwipingToLoadMore(mStatus)) {
            mLoadMoreCallback.onMove(mTargetOffset, false, true);
        } else if (STATUS.isReleaseToLoadMore(mStatus)) {
            mLoadMoreCallback.onMove(mTargetOffset, false, true);
        } else if (STATUS.isLoadingMore(mStatus)) {
            mLoadMoreCallback.onMove(mTargetOffset, true, true);
        }
        updateScroll(yScrolled);
    }

    private void updateScroll(final float yScrolled) {
        if (yScrolled == 0) {
            return;
        }
        mTargetOffset += yScrolled;

        if (STATUS.isRefreshStatus(mStatus)) {
            mHeaderOffset = mTargetOffset;
            mFooterOffset = 0;
        } else if (STATUS.isLoadMoreStatus(mStatus)) {
            mFooterOffset = mTargetOffset;
            mHeaderOffset = 0;
        }

        layoutChildren();
        invalidate();
    }

    private void onActivePointerUp() {
        if (STATUS.isSwipingToRefresh(mStatus)) {
            scrollSwipingToRefreshToDefault();

        } else if (STATUS.isSwipingToLoadMore(mStatus)) {
            scrollSwipingToLoadMoreToDefault();

        } else if (STATUS.isReleaseToRefresh(mStatus)) {
            mRefreshCallback.onRelease();
            scrollReleaseToRefreshToRefreshing();

        } else if (STATUS.isReleaseToLoadMore(mStatus)) {
            mLoadMoreCallback.onRelease();
            scrollReleaseToLoadMoreToLoadingMore();

        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    private void scrollDefaultToRefreshing() {
        mAutoScroller.autoScroll((int) (mRefreshTriggerOffset + 0.5f), mDefaultToRefreshingScrollingDuration);
    }

    private void scrollDefaultToLoadingMore() {
        mAutoScroller.autoScroll(-(int) (mLoadMoreTriggerOffset + 0.5f), mDefaultToLoadingMoreScrollingDuration);
    }

    private void scrollSwipingToRefreshToDefault() {
        mAutoScroller.autoScroll(-mHeaderOffset, mSwipingToRefreshToDefaultScrollingDuration);
    }

    private void scrollSwipingToLoadMoreToDefault() {
        mAutoScroller.autoScroll(-mFooterOffset, mSwipingToLoadMoreToDefaultScrollingDuration);
    }

    private void scrollReleaseToRefreshToRefreshing() {
        mAutoScroller.autoScroll(mHeaderHeight - mHeaderOffset, mReleaseToRefreshToRefreshingScrollingDuration);
    }

    private void scrollReleaseToLoadMoreToLoadingMore() {
        mAutoScroller.autoScroll(-mFooterOffset - mFooterHeight, mReleaseToLoadMoreToLoadingMoreScrollingDuration);
    }

    private void scrollRefreshingToDefault() {
        mAutoScroller.autoScroll(-mHeaderOffset, mRefreshCompleteToDefaultScrollingDuration);
    }

    private void scrollLoadingMoreToDefault() {
        mAutoScroller.autoScroll(-mFooterOffset, mLoadMoreCompleteToDefaultScrollingDuration);
    }

    private void autoScrollFinished() {
        int mLastStatus = mStatus;

        if (STATUS.isReleaseToRefresh(mStatus)) {
            setStatus(STATUS.STATUS_REFRESHING);
            fixCurrentStatusLayout();
            mRefreshCallback.onRefresh();

        } else if (STATUS.isRefreshing(mStatus)) {
            setStatus(STATUS.STATUS_DEFAULT);
            fixCurrentStatusLayout();
            mRefreshCallback.onReset();

        } else if (STATUS.isSwipingToRefresh(mStatus)) {
            if (mAutoLoading) {
                mAutoLoading = false;
                setStatus(STATUS.STATUS_REFRESHING);
                fixCurrentStatusLayout();
                mRefreshCallback.onRefresh();
            } else {
                setStatus(STATUS.STATUS_DEFAULT);
                fixCurrentStatusLayout();
                mRefreshCallback.onReset();
            }
        } else if (STATUS.isStatusDefault(mStatus)) {

        } else if (STATUS.isSwipingToLoadMore(mStatus)) {
            if (mAutoLoading) {
                mAutoLoading = false;
                setStatus(STATUS.STATUS_LOADING_MORE);
                fixCurrentStatusLayout();
                mLoadMoreCallback.onLoadMore();
            } else {
                setStatus(STATUS.STATUS_DEFAULT);
                fixCurrentStatusLayout();
                mLoadMoreCallback.onReset();
            }
        } else if (STATUS.isLoadingMore(mStatus)) {
            setStatus(STATUS.STATUS_DEFAULT);
            fixCurrentStatusLayout();
            mLoadMoreCallback.onReset();
        } else if (STATUS.isReleaseToLoadMore(mStatus)) {
            setStatus(STATUS.STATUS_LOADING_MORE);
            fixCurrentStatusLayout();
            mLoadMoreCallback.onLoadMore();
        } else {
            throw new IllegalStateException("illegal state: " + STATUS.getStatus(mStatus));
        }

    }

    private boolean onCheckCanRefresh() {

        return mRefreshEnabled && !canChildScrollUp() && mHeaderView!=null && mRefreshTriggerOffset
                > 0;
    }

    private boolean onCheckCanLoadMore() {

        return mLoadMoreEnabled && !canChildScrollDown() && mFooterView!=null &&
                mLoadMoreTriggerOffset > 0;
    }

    private float getMotionEventY(MotionEvent event, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(event, activePointerId);
        if (index < 0) {
            return INVALID_COORDINATE;
        }
        return MotionEventCompat.getY(event, index);
    }

    private float getMotionEventX(MotionEvent event, int activePointId) {
        final int index = MotionEventCompat.findPointerIndex(event, activePointId);
        if (index < 0) {
            return INVALID_COORDINATE;
        }
        return MotionEventCompat.getX(event, index);
    }

    RefreshCallback mRefreshCallback = new RefreshCallback() {
        @Override
        public void onPrepare() {
            if (mHeaderView != null && mHeaderView instanceof SwipeTrigger && STATUS.isStatusDefault(mStatus)) {
                mHeaderView.setVisibility(VISIBLE);
                ((SwipeTrigger) mHeaderView).onPrepare();
            }
        }

        @Override
        public void onMove(int y, boolean isComplete, boolean automatic) {
            if (mHeaderView != null && mHeaderView instanceof SwipeTrigger && STATUS.isRefreshStatus(mStatus)) {
                if (mHeaderView.getVisibility() != VISIBLE) {
                    mHeaderView.setVisibility(VISIBLE);
                }
                ((SwipeTrigger) mHeaderView).onMove(y, isComplete, automatic);
            }
        }

        @Override
        public void onRelease() {
            if (mHeaderView != null && mHeaderView instanceof SwipeTrigger && STATUS.isReleaseToRefresh(mStatus)) {
                ((SwipeTrigger) mHeaderView).onRelease();
            }
        }

        @Override
        public void onRefresh() {
            if (mHeaderView != null && STATUS.isRefreshing(mStatus)) {
                if (mHeaderView instanceof SwipeRefreshTrigger) {
                    ((SwipeRefreshTrigger) mHeaderView).onRefresh();
                }
                if (mRefreshListener != null) {
                    mRefreshListener.onRefresh();
                }
            }
        }

        @Override
        public void onComplete() {
            if (mHeaderView != null && mHeaderView instanceof SwipeTrigger) {
                ((SwipeTrigger) mHeaderView).onComplete();
            }
        }

        @Override
        public void onReset() {
            if (mHeaderView != null && mHeaderView instanceof SwipeTrigger && STATUS.isStatusDefault(mStatus)) {
                ((SwipeTrigger) mHeaderView).onReset();
                mHeaderView.setVisibility(GONE);
            }
        }
    };

    LoadMoreCallback mLoadMoreCallback = new LoadMoreCallback() {

        @Override
        public void onPrepare() {
            if (mFooterView != null && mFooterView instanceof SwipeTrigger && STATUS.isStatusDefault(mStatus)) {
                mFooterView.setVisibility(VISIBLE);
                ((SwipeTrigger) mFooterView).onPrepare();
            }
        }

        @Override
        public void onMove(int y, boolean isComplete, boolean automatic) {
            if (mFooterView != null && mFooterView instanceof SwipeTrigger && STATUS.isLoadMoreStatus(mStatus)) {
                if (mFooterView.getVisibility() != VISIBLE) {
                    mFooterView.setVisibility(VISIBLE);
                }
                ((SwipeTrigger) mFooterView).onMove(y, isComplete, automatic);
            }
        }

        @Override
        public void onRelease() {
            if (mFooterView != null && mFooterView instanceof SwipeTrigger && STATUS.isReleaseToLoadMore(mStatus)) {
                ((SwipeTrigger) mFooterView).onRelease();
            }
        }

        @Override
        public void onLoadMore() {
            if (mFooterView != null && STATUS.isLoadingMore(mStatus)) {
                if (mFooterView instanceof SwipeLoadMoreTrigger) {
                    ((SwipeLoadMoreTrigger) mFooterView).onLoadMore();
                }
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore();
                }
            }
        }

        @Override
        public void onComplete() {
            if (mFooterView != null && mFooterView instanceof SwipeTrigger) {
                ((SwipeTrigger) mFooterView).onComplete();
            }
        }

        @Override
        public void onReset() {
            if (mFooterView != null && mFooterView instanceof SwipeTrigger && STATUS.isStatusDefault(mStatus)) {
                ((SwipeTrigger) mFooterView).onReset();
                mFooterView.setVisibility(GONE);
            }
        }
    };

    abstract class RefreshCallback implements SwipeTrigger, SwipeRefreshTrigger {
    }

    abstract class LoadMoreCallback implements SwipeTrigger, SwipeLoadMoreTrigger {
    }

    private class AutoScroller implements Runnable {

        private Scroller mScroller;

        private int mmLastY;

        private boolean mRunning = false;

        private boolean mAbort = false;

        public AutoScroller() {
            mScroller = new Scroller(getContext());
        }

        @Override
        public void run() {
            boolean finish = !mScroller.computeScrollOffset() || mScroller.isFinished();
            int currY = mScroller.getCurrY();
            int yDiff = currY - mmLastY;
            if (finish) {
                finish();
            } else {
                mmLastY = currY;
                SwipeToLoadLayout.this.autoScroll(yDiff);
                post(this);
            }
        }

        private void finish() {
            mmLastY = 0;
            mRunning = false;
            removeCallbacks(this);
            if (!mAbort) {
                autoScrollFinished();
            }
        }

        public void abortIfRunning() {
            if (mRunning) {
                if (!mScroller.isFinished()) {
                    mAbort = true;
                    mScroller.forceFinished(true);
                }
                finish();
                mAbort = false;
            }
        }

        private void autoScroll(int yScrolled, int duration) {
            removeCallbacks(this);
            mmLastY = 0;
            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }
            mScroller.startScroll(0, 0, 0, yScrolled, duration);
            post(this);
            mRunning = true;
        }
    }

    private void setStatus(int status) {
        mStatus = status;
    }

    private final static class STATUS {
        private static final int STATUS_REFRESHING = -3;
        private static final int STATUS_RELEASE_TO_REFRESH = -2;
        private static final int STATUS_SWIPING_TO_REFRESH = -1;
        private static final int STATUS_DEFAULT = 0;
        private static final int STATUS_SWIPING_TO_LOAD_MORE = 1;
        private static final int STATUS_RELEASE_TO_LOAD_MORE = 2;
        private static final int STATUS_LOADING_MORE = 3;
        private static final int STATUS_LOAD_MORE_RETURNING = 4;

        private static boolean isRefreshing(final int status) {
            return status == STATUS.STATUS_REFRESHING;
        }

        private static boolean isLoadingMore(final int status) {
            return status == STATUS.STATUS_LOADING_MORE;
        }

        private static boolean isReleaseToRefresh(final int status) {
            return status == STATUS.STATUS_RELEASE_TO_REFRESH;
        }

        private static boolean isReleaseToLoadMore(final int status) {
            return status == STATUS.STATUS_RELEASE_TO_LOAD_MORE;
        }

        private static boolean isSwipingToRefresh(final int status) {
            return status == STATUS.STATUS_SWIPING_TO_REFRESH;
        }

        private static boolean isSwipingToLoadMore(final int status) {
            return status == STATUS.STATUS_SWIPING_TO_LOAD_MORE;
        }

        private static boolean isRefreshStatus(final int status) {
            return status < STATUS.STATUS_DEFAULT;
        }

        public static boolean isLoadMoreStatus(final int status) {
            return status > STATUS.STATUS_DEFAULT;
        }

        private static boolean isStatusDefault(final int status) {
            return status == STATUS.STATUS_DEFAULT;
        }

        private static String getStatus(int status) {
            final String statusInfo;
            switch (status) {
                case STATUS_REFRESHING:
                    statusInfo = "status_refreshing";
                    break;
                case STATUS_RELEASE_TO_REFRESH:
                    statusInfo = "status_release_to_refresh";
                    break;
                case STATUS_SWIPING_TO_REFRESH:
                    statusInfo = "status_swiping_to_refresh";
                    break;
                case STATUS_DEFAULT:
                    statusInfo = "status_default";
                    break;
                case STATUS_SWIPING_TO_LOAD_MORE:
                    statusInfo = "status_swiping_to_load_more";
                    break;
                case STATUS_RELEASE_TO_LOAD_MORE:
                    statusInfo = "status_release_to_load_more";
                    break;
                case STATUS_LOADING_MORE:
                    statusInfo = "status_loading_more";
                    break;
                case STATUS_LOAD_MORE_RETURNING:
                    statusInfo = "status_load_more_returning";
                    break;
                default:
                    statusInfo = "status_illegal!";
                    break;
            }
            return statusInfo;
        }

    }



    public interface SwipeTrigger{
        void onPrepare();

        void onMove(int y, boolean isComplete, boolean automatic);

        void onRelease();

        void onComplete();

        void onReset();
    }


    public interface SwipeRefreshTrigger {
        void onRefresh();
    }


    public interface SwipeLoadMoreTrigger {
        void onLoadMore();
    }


    public interface OnRefreshListener {
         void onRefresh();
    }


    public interface OnLoadMoreListener {
         void onLoadMore();
    }
}
