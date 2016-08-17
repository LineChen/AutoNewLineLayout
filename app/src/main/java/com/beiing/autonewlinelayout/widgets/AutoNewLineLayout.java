package com.beiing.autonewlinelayout.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenliu on 2016/8/17.<br/>
 * 描述：自动换行Layout
 * </br>
 */
public class AutoNewLineLayout extends ViewGroup {
    public AutoNewLineLayout(Context context) {
        super(context);
    }

    public AutoNewLineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoNewLineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //AT_MOST
        int width = 0;
        int height = 0;
        int rawWidth = 0;//当前行总宽度
        int rawHeight = 0;// 当前行高

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if(child.getVisibility() == GONE){
                if(i == count - 1){
                    //最后一个child
                    height += rawHeight;
                    width = Math.max(width, rawWidth);
                }
                continue;
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth()  + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if(rawWidth + childWidth > widthSpecSize - getPaddingLeft() - getPaddingRight()){
                //换行
                width = Math.max(width, rawWidth);
                rawWidth = childWidth;
                height += rawHeight;
                rawHeight = childHeight;
            } else {
                rawWidth += childWidth;
                rawHeight = Math.max(rawHeight, childHeight);
            }

            if(i == count - 1){
                width = Math.max(rawWidth, width);
                height += rawHeight;
            }
        }

        setMeasuredDimension(
                widthSpecMode == MeasureSpec.EXACTLY ? widthSpecSize : width + getPaddingLeft() + getPaddingRight(),
                heightSpecMode == MeasureSpec.EXACTLY ? heightSpecSize : height + getPaddingTop() + getPaddingBottom()
        );
    }

    /**
     * 存储所有的View
     */
    private List<List<View>> mAllViews = new ArrayList<>();

    /**
     * 每一行的高度
     */
    private List<Integer> mLineHeight = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<>();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            if(childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight()){
                //需要换行
                //记录这一行的行高
                mLineHeight.add(lineHeight);
                //记录这一行的View
                mAllViews.add(lineViews);

                //重置行宽和行高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;

                lineViews = new ArrayList<>();
            }

            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }

        //处理最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(lineViews);


        //设置子View位置
        int left = getPaddingLeft();
        int top = getPaddingTop();

        //行数
        int lineNum = mAllViews.size();
        for (int i = 0; i < lineNum; i++) {
            lineViews = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            for(int j = 0; j< lineViews.size(); j++){
                View child = lineViews.get(j);
                if(child.getVisibility() == GONE){
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc ,tc, rc, bc);

                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            //下一行
            left = getPaddingLeft();
            top += lineHeight;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }


}

