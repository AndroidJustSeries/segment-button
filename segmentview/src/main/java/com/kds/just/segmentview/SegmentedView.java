package com.kds.just.segmentview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
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

    EnhancedHalper.FontType mFontType = null;

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
        mFontType = EnhancedHalper.getFontType(context,attrs);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.HORIZONTAL);
        setWillNotDraw(false);

        setPadding(mStrokeWidth/2,0,mStrokeWidth/2,0);
        initPaint();

    }

    private Paint mBGNormalPaint;
    private Paint mBGNormalStrokePaint;

    private Paint mBGSelectPaint;
    private Paint mBGSelectStrokePaint;

    private Paint mMainPaint;
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
        mBGNormalStrokePaint.setDither(true);
        mBGNormalStrokePaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want

        mBGSelectPaint = new Paint();
        mBGSelectPaint.setStyle(Paint.Style.FILL);
        mBGSelectPaint.setColor(mBGColorSelected);
        mBGSelectPaint.setAntiAlias(true);

        mBGSelectStrokePaint = new Paint();
        mBGSelectStrokePaint.setStyle(Paint.Style.STROKE);
        mBGSelectStrokePaint.setColor(mStrokeColorSelected);
        mBGSelectStrokePaint.setStrokeWidth(mStrokeWidth);
        mBGSelectStrokePaint.setAntiAlias(true);
        mBGSelectStrokePaint.setDither(true);
        mBGSelectStrokePaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want

        mMainPaint = new Paint();
        mMainPaint.setAntiAlias(true);
    }

    public void setFont(EnhancedHalper.FontType fonttype) {
        mFontType = fonttype;
        for (int i=0;i<getChildCount();i++){
            View v = getChildAt(i);
            if (v instanceof TextView) {
                EnhancedHalper.setFont((TextView) v,mFontType);
            }
        }
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
        tv.setPadding(mStrokeWidth/2,mStrokeWidth,mStrokeWidth/2,mStrokeWidth);
        if (mFontType != null) {
            EnhancedHalper.setFont(tv,mFontType);
        }
        addView(tv);
    }

    public void setSelected(int index) {
        mSelectIndex = index;
        for (int i=0;i<getChildCount();i++) {
            TextView tv = (TextView) getChildAt(i);
            tv.setSelected(((int)tv.getTag()) == index);
        }
        invalidate();
    }

    public int getSelectedIndex() {
        return mSelectIndex;
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

    private int mStrokePadding = 0;
    @Override
    protected void onDraw(Canvas canvas) {
        int childCount = getChildCount();
        mStrokePadding = mStrokeWidth / 2;
        Log.e(TAG,"KDS3393_TEST_mStrokeWidth + " + mStrokeWidth + " StrokePadding = " + mStrokePadding);
        RectF rectF = new RectF(
                mStrokePadding, // left
                mStrokePadding, // top
                canvas.getWidth() - mStrokePadding, // right
                canvas.getHeight() - mStrokePadding  // bottom
        );

        //draw background
        canvas.drawRoundRect(rectF,mRoundRadius,mRoundRadius,mBGNormalPaint);
        //draw round stroke
        canvas.drawRoundRect(rectF,mRoundRadius,mRoundRadius,mBGNormalStrokePaint);

        drawDivider(canvas, childCount);
        drawSelectRect(canvas, rectF,childCount);
    }

    private void drawDivider(Canvas canvas, int childCount) {
        //draw divider line
        int w = canvas.getWidth() - mStrokeWidth;
        int h = canvas.getHeight();
        for (int i=1;i<childCount;i++) {
            int x = w/childCount * i + (mStrokeWidth / 2);
            canvas.drawLine(x,0,x,h,mBGNormalStrokePaint);
        }
    }

    private void drawSelectRect(Canvas canvas, RectF rectF, int childCount) {
        //draw select item
        if (mSelectIndex >= 0) {
            if (getChildCount() > 1) {
                View v = getChildAt(mSelectIndex);
                RectF itemRectf = new RectF(v.getLeft() ,v.getTop() + mStrokePadding,v.getRight(),v.getHeight() - mStrokePadding);
                if (mSelectIndex == 0) {    //first item
                    Path path = getDrawRoundPath(itemRectf,mRoundRadius,0,mRoundRadius,0);
                    canvas.drawPath(path, mBGSelectPaint);
                    canvas.drawPath(path, mBGSelectStrokePaint);
                } else if (mSelectIndex == childCount - 1) {    //list Item
                    Path path = getDrawRoundPath(itemRectf,0,mRoundRadius,0,mRoundRadius);
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

    private Path getDrawRoundPath(RectF rectF, int lt, int rt, int lb, int rb) {
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
