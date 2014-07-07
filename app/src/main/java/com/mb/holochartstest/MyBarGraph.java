package com.mb.holochartstest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class MyBarGraph extends View {

    private ArrayList<Bar> points = new ArrayList<Bar>();

    private Paint paint = new Paint();
    private Rect barRect;
    private Path path = new Path();
    private int indexSelected = -1;
    private OnBarClickedListener listener;
    private Bitmap fullImage;
    private boolean shouldUpdate = false;
    private Boolean append = false;

    private boolean showBarText = true;
    private String unit = "$";
    private Rect r2 = new Rect();
    private Rect r3 = new Rect();

    public MyBarGraph(Context context) {
        super(context);
    }

    public MyBarGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setShowBarText(boolean show) {
        showBarText = show;
    }

    public void setBars(ArrayList<Bar> points) {
        this.points = points;
        postInvalidate();
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return this.unit;
    }

    public void appendUnit(Boolean doAppend) {
        this.append = doAppend;
    }

    public Boolean isAppended() {
        return this.append;
    }

    public ArrayList<Bar> getBars() {
        return this.points;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("debug", "WidthSpec: " + widthMeasureSpec + ", htSpec: " + heightMeasureSpec);

        int mViewWidth = measureWidth(widthMeasureSpec);
        int mViewHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mViewWidth,mViewHeight);
    }

    private int measureWidth(int measureSpec){
        int preferred = 0;
        return getMeasurement(measureSpec, preferred);
    }

    private int measureHeight(int measureSpec){
        int preferred = 600;
        return getMeasurement(measureSpec, preferred);
    }

    private int getMeasurement(int measureSpec, int preferred){
        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement;
        switch(MeasureSpec.getMode(measureSpec)){
            case MeasureSpec.EXACTLY:
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }
        return measurement;
    }


    // Draw horizontal bar graph

    @Override
    public void onDraw(Canvas ca) {

        if (fullImage == null || shouldUpdate) {
            fullImage = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(fullImage);
            canvas.drawColor(Color.TRANSPARENT);
//            NinePatchDrawable popup = (NinePatchDrawable) this.getResources().getDrawable(R.drawable.popup_black);

//            float maxValue = 0;
            float padding = 15;
            int selectPadding = 4;
            float leftPadding = 20;

            float usableWidth = getWidth() - leftPadding;

//            if (showBarText) {
//                this.paint.setTextSize(40);
//                this.paint.getTextBounds(unit, 0, 1, r3);
//                usableHeight = getHeight() - bottomPadding - Math.abs(r3.top - r3.bottom) - 26;
//            } else {
//                usableWidth = getWidth() - leftPadding;
//            }

            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(2);
            paint.setAlpha(50);
            paint.setAntiAlias(true);

            canvas.drawLine(leftPadding, 0, leftPadding, getHeight(), paint);

            //float barHeight = (getHeight() - ((padding * 2) * points.size())) / points.size();
            float barHeight = 56.0f;
            float perUnitWidth = usableWidth / 7;

//            for (Bar p : points) {
//                maxValue += p.getValue();
//            }

            barRect = new Rect();

            path.reset();

            int count = 0;
            for (Bar barToDraw : points) {
                int left = (int) leftPadding;
                int top = (int) ((padding * 2) * count + padding + barHeight * count);
//                int right = (int) (usableWidth * (barToDraw.getValue() / maxValue));
                int right = (int) (perUnitWidth * (barToDraw.getValue() % 8));
                int bottom = (int) (top + barHeight);

                barRect.set(left, top, right, bottom);

                Path barPath = new Path();
                barPath.addRect(new RectF(barRect.left - selectPadding, barRect.top - selectPadding, barRect.right + selectPadding, barRect.bottom + selectPadding), Path.Direction.CW);
                barToDraw.setPath(barPath);
                barToDraw.setRegion(new Region(barRect.left - selectPadding, barRect.top - selectPadding, barRect.right + selectPadding, barRect.bottom + selectPadding));

                this.paint.setColor(barToDraw.getColor());
                this.paint.setAlpha(125);
                canvas.drawRect(barRect, this.paint);

                this.paint.setColor(Color.DKGRAY);
                this.paint.setTextSize(20);
                String traitTitleAndValueStr = barToDraw.getName() + "  (" + (int) barToDraw.getValue() + ")";
                canvas.drawText(traitTitleAndValueStr, (int) ((getWidth() - leftPadding) / 2) - (this.paint.measureText(traitTitleAndValueStr) / 2), barRect.top + (barHeight / 2), this.paint);
                this.paint.setColor(barToDraw.getColor());

                // Below code is to show the bubble on top of the bar displaying bar value
//                if (showBarText) {
//                    this.paint.setTextSize(40);
//                    this.paint.setColor(Color.WHITE);
//                    this.paint.getTextBounds(unit + barToDraw.getValue(), 0, 1, r2);
//                    if (popup != null)
//                        popup.setBounds((int) (((barRect.left + barRect.right) / 2) - (this.paint.measureText(unit + barToDraw.getValue()) / 2)) - 14,
//                                        barRect.top + (r2.top - r2.bottom) - 26,
//                                        (int) (((barRect.left + barRect.right) / 2) + (this.paint.measureText(unit + barToDraw.getValue()) / 2)) + 14,
//                                        barRect.top);
//                    popup.draw(canvas);
//                    if (isAppended())
//                        canvas.drawText(barToDraw.getValue() + unit, (int) (((barRect.left + barRect.right) / 2) - (this.paint.measureText(unit + barToDraw.getValue()) / 2)), barRect.top - 20, this.paint);
//                    else
//                        canvas.drawText(unit + barToDraw.getValue(), (int) (((barRect.left + barRect.right) / 2) - (this.paint.measureText(unit + barToDraw.getValue()) / 2)), barRect.top - 20, this.paint);
//                }

                if (indexSelected == count && listener != null) {
                    this.paint.setColor(Color.parseColor("#33B5E5"));
                    this.paint.setAlpha(100);
                    canvas.drawPath(barToDraw.getPath(), this.paint);
                    this.paint.setAlpha(255);
                }
                count++;
            }
            shouldUpdate = false;
        }

        ca.drawBitmap(fullImage, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        Point point = new Point();
        point.x = (int) event.getX();
        point.y = (int) event.getY();

        int count = 0;
        for (Bar bar : points) {
            Region region = new Region();
            region.setPath(bar.getPath(), bar.getRegion());

            if (region.contains(point.x, point.y) && event.getAction() == MotionEvent.ACTION_DOWN) {
                indexSelected = count;
                break;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (region.contains(point.x, point.y) && listener != null) {
//                    listener.onClick(indexSelected);
                    listener.onClickDisplay(bar.getName());
                }
                indexSelected = -1;
            }

            count++;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
            shouldUpdate = true;
            postInvalidate();
        }

        return true;
    }

    public void setOnBarClickedListener(OnBarClickedListener listener) {
        this.listener = listener;
    }

    public interface OnBarClickedListener {
        void onClick(int index);
        void onClickDisplay(String barName);
    }

}


