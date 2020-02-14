package com.kds.just.segmentview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class SegmentedView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "SegmentedView";

    private int mTextColorNormal = Color.parseColor("#d8d8d8");
    private int mTextColorSelected = Color.parseColor("#727af2");
    private int mTextSize = dp2px(10);

    private int mBGColorNormal = Color.WHITE;
    private int mBGColorSelected = Color.parseColor("#727af2");
    private int mStrokeColorNormal = Color.parseColor("#d8d8d8");
    private int mStrokeColorSelected = Color.parseColor("#727af2");
    private int mRoundRadius = dp2px(10);
    private int mStrokeWidth = dp2px(1);

    private int mSelectIndex = -1;

    private OnSelectedItemListener mOnSelectedItemListener;
    public void setOnSelectedItemListener(OnSelectedItemListener l) {
        mOnSelectedItemListener = l;
    }
    public interface OnSelectedItemListener {
        void onSelected(SegmentedView v, View selectedView, int index);
    }

    public SegmentedView(Context context) {
        super(context);
        init();
    }

    public SegmentedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mTextColorNormal = context.obtainStyledAttributes(attrs, R.styleable.SegmentedView)
                .getColor(R.styleable.SegmentedView_textColorNormal,mTextColorNormal);
        mTextColorSelected = context.obtainStyledAttributes(attrs, R.styleable.SegmentedView)
                .getColor(R.styleable.SegmentedView_textColorSelected,mTextColorSelected);
        mStrokeColorNormal = context.obtainStyledAttributes(attrs, R.styleable.SegmentedView)
                .getColor(R.styleable.SegmentedView_BgStrokeColorNormal,mStrokeColorNormal);
        mStrokeColorSelected = context.obtainStyledAttributes(attrs, R.styleable.SegmentedView)
                .getColor(R.styleable.SegmentedView_BgStrokeColorSelected,mStrokeColorSelected);
        mBGColorNormal = context.obtainStyledAttributes(attrs, R.styleable.SegmentedView)
                .getColor(R.styleable.SegmentedView_BgColorNormal,Color.WHITE);
        mBGColorSelected = context.obtainStyledAttributes(attrs, R.styleable.SegmentedView)
                .getColor(R.styleable.SegmentedView_BgColorSelected,Color.parseColor("#727af2"));
        mRoundRadius = context.obtainStyledAttributes(attrs, R.styleable.SegmentedView)
                .getDimensionPixelSize(R.styleable.SegmentedView_BgRadius,mRoundRadius);
        mStrokeWidth = context.obtainStyledAttributes(attrs, R.styleable.SegmentedView)
                .getDimensionPixelSize(R.styleable.SegmentedView_StrokeWidth,mStrokeWidth);
        mTextSize = context.obtainStyledAttributes(attrs, R.styleable.SegmentedView)
                .getDimensionPixelSize(R.styleable.SegmentedView_textSize,mStrokeWidth);

        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setWillNotDraw(false);

        initPaint();
    }

    private Paint mBGNormalPaint;
    private Paint mBGNormalStrokePaint;

    private Paint mBGSelectPaint;
    private Paint mBGSelectStrokePaint;

    private void initPaint() {
        mBGNormalPaint = new Paint();
        mBGNormalPaint.setStyle(Paint.Style.FILL);
        mBGNormalPaint.setColor(mBGColorNormal);
        mBGNormalPaint.setAntiAlias(true);

        mBGNormalStrokePaint = new Paint();
        mBGNormalStrokePaint.setStyle(Paint.Style.STROKE);
        mBGNormalStrokePaint.setColor(mStrokeColorNormal);
        mBGNormalStrokePaint.setStrokeWidth(mStrokeWidth);
        mBGNormalStrokePaint.setAntiAlias(true);

        mBGSelectPaint = new Paint();
        mBGSelectPaint.setStyle(Paint.Style.FILL);
        mBGSelectPaint.setColor(mBGColorSelected);
        mBGSelectPaint.setAntiAlias(true);


        mBGSelectStrokePaint = new Paint();
        mBGSelectStrokePaint.setStyle(Paint.Style.STROKE);
        mBGSelectStrokePaint.setColor(mStrokeColorSelected);
        mBGSelectStrokePaint.setStrokeWidth(mStrokeWidth);
        mBGSelectStrokePaint.setAntiAlias(true);

    }

    public void addItem(String text) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        params.weight = 1;
        tv.setLayoutParams(params);
        tv.setTextColor(makeTextColorSelector(mTextColorNormal,mTextColorSelected));
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize);
        tv.setGravity(Gravity.CENTER);
        tv.setTag(getChildCount());
        tv.setOnClickListener(this);
        addView(tv);
    }

    @Override
    public void onClick(View v) {
        int selectIndex = (int) v.getTag();
        if (mSelectIndex != selectIndex) {
            mSelectIndex = selectIndex;
            for (int i=0;i<getChildCount();i++) {
                View item = getChildAt(i);
                item.setSelected(v == item);
            }
            invalidate();
        }
        if (mOnSelectedItemListener != null) {
            mOnSelectedItemListener.onSelected(this, v,selectIndex);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        RectF rectF = new RectF(
                0, // left
                0, // top
                canvas.getWidth(), // right
                canvas.getHeight() // bottom
        );
        //draw background
        canvas.drawRoundRect(rectF,mRoundRadius,mRoundRadius,mBGNormalPaint);
        //draw round stroke
        canvas.drawRoundRect(rectF,mRoundRadius,mRoundRadius,mBGNormalStrokePaint);

        //draw divider line
        int childCount = getChildCount();
        int w = canvas.getWidth();
        int h = canvas.getHeight();
        for (int i=1;i<childCount;i++) {
            int x = w/childCount * i;
            canvas.drawLine(x,0,x,h,mBGNormalStrokePaint);
        }

        //draw select item
        if (mSelectIndex >= 0) {
            if (getChildCount() > 1) {
                View v = getChildAt(mSelectIndex);
                RectF itemRectf = new RectF(v.getLeft(),v.getTop(),v.getRight(),v.getHeight());
                if (mSelectIndex == 0) {    //first item
                    Path path = getDrawRoundPath(canvas,itemRectf,mRoundRadius,0,mRoundRadius,0);
                    canvas.drawPath(path, mBGSelectPaint);
                    canvas.drawPath(path, mBGSelectStrokePaint);
                } else if (mSelectIndex == childCount - 1) {    //list Item
                    Path path = getDrawRoundPath(canvas,itemRectf,0,mRoundRadius,0,mRoundRadius);
                    canvas.drawPath(path, mBGSelectPaint);
                    canvas.drawPath(path, mBGSelectStrokePaint);
                } else {
                    canvas.drawRoundRect(itemRectf,0,0,mBGSelectPaint);
                    canvas.drawRoundRect(itemRectf,0,0,mBGSelectStrokePaint);
                }
            } else if (getChildCount() == 1) {
                //draw background
                canvas.drawRoundRect(rectF,mRoundRadius,mRoundRadius,mBGSelectPaint);
                //draw round stroke
                canvas.drawRoundRect(rectF,mRoundRadius,mRoundRadius,mBGSelectStrokePaint);
            }
        }
    }


    private Path getDrawRoundPath(Canvas canvas, RectF rectF, int lt, int rt, int lb, int rb) {
        int topLeftRadius = lt;
        int topRightRadius = rt;
        int bottomLeftRadius = lb;
        int bottomRightRadius = rb;

        RectF topLeftArcBound = new RectF();
        RectF topRightArcBound = new RectF();
        RectF bottomLeftArcBound = new RectF();
        RectF bottomRightArcBound = new RectF();

        topRightArcBound.set(rectF.right - topRightRadius * 2, rectF.top, rectF.right, rectF.top + topRightRadius * 2);
        bottomRightArcBound.set(rectF.right - bottomRightRadius * 2, rectF.bottom - bottomRightRadius * 2, rectF.right, rectF.bottom);
        bottomLeftArcBound.set(rectF.left, rectF.bottom - bottomLeftRadius * 2, rectF.left + bottomLeftRadius * 2, rectF.bottom);
        topLeftArcBound.set(rectF.left, rectF.top, rectF.left + topLeftRadius * 2, rectF.top + topLeftRadius * 2);

        Path path = new Path();
        path.reset();
        path.moveTo(rectF.left + topLeftRadius, rectF.top);
//draw top horizontal line
        path.lineTo(rectF.right - topRightRadius, rectF.top);
//draw top-right corner
        path.arcTo(topRightArcBound, -90, 90);
//draw right vertical line
        path.lineTo(rectF.right, rectF.bottom - bottomRightRadius);
//draw bottom-right corner
        path.arcTo(bottomRightArcBound, 0, 90);
//draw bottom horizontal line
        path.lineTo(rectF.left + bottomLeftRadius, rectF.bottom);
//draw bottom-left corner
        path.arcTo(bottomLeftArcBound, 90, 90);
//draw left vertical line
        path.lineTo(rectF.left, rectF.top + topLeftRadius);
//draw top-left corner
        path.arcTo(topLeftArcBound, 180, 90);
        path.close();
        return path;
    }

    public static int dp2px(float dp) {
        Resources resources = Resources.getSystem();
        float px = dp * resources.getDisplayMetrics().density;
        return (int) Math.ceil(px);
    }

    private static ColorStateList makeTextColorSelector(int normal, int selected) {
        if (selected == 0) {
            selected = normal;
        }

        int[][] states = new int[][] {
                new int[] { android.R.attr.state_pressed}, // press
                new int[] { android.R.attr.state_selected}, // selected
                new int[] {-android.R.attr.state_empty}  // unpressed
        };

        int[] colors = new int[] {
                selected,
                selected,
                normal
        };

        return new ColorStateList(states, colors);
    }

}
