package com.mb.holochartstest;

import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class BarGraph extends View {

    private ArrayList<Bar> points = new ArrayList<Bar>();

    private Paint paint = new Paint();
    private Paint p = new Paint();

    private Rect barRect;
    private Rect r;

    private Path path = new Path();
    private boolean showBarText = true;
    private int indexSelected = -1;
    private OnBarClickedListener listener;
    private Bitmap fullImage;
    private boolean shouldUpdate = false;
    private String unit = "$";
    private Boolean append = false;
    private Rect r2 = new Rect();
    private Rect r3 = new Rect();

    public BarGraph(Context context) {
        super(context);
    }

    public BarGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setShowBarText(boolean show) {
        showBarText = show;
    }

    public void setBars(ArrayList<Bar> points) {
        this.points = points;
        postInvalidate();
        removeCallbacks(animator);

        post(animator);
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

    public void onDraw(Canvas ca) {

//        if (fullImage == null || shouldUpdate) {
            fullImage = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(fullImage);
            canvas.drawColor(Color.TRANSPARENT);
//            NinePatchDrawable popup = (NinePatchDrawable) this.getResources().getDrawable(R.drawable.popup_black);

//            float maxValue = 0;
            float padding = 5;
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

            float barHeight = (getHeight() - ((padding * 2) * points.size())) / points.size();
//            float barHeight = 56.0f;
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
//                int right = (int) (perUnitWidth * (barToDraw.getValue() % 8));
                int right = left + (int) (perUnitWidth * (barToDraw.getCurrentValue()));
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
                this.paint.setTextSize(15);
                String traitTitleAndValueStr = barToDraw.getName() + "  (" + (int) barToDraw.getValue() + ")";
                canvas.drawText(traitTitleAndValueStr, (int) ((getWidth() - leftPadding) / 2) - (this.paint.measureText(traitTitleAndValueStr) / 2), barRect.top + (barHeight / 2) + 5, this.paint);
                this.paint.setColor(barToDraw.getColor());

                if (indexSelected == count && listener != null) {
                    this.paint.setColor(Color.parseColor("#33B5E5"));
                    this.paint.setAlpha(100);
                    canvas.drawPath(barToDraw.getPath(), this.paint);
                    this.paint.setAlpha(255);
                }
                count++;
            }
            shouldUpdate = false;
//        }

        ca.drawBitmap(fullImage, 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
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
                    //listener.onClick(indexSelected);
                    listener.onClickDisplay(bar.getName());
                    indexSelected = -1; break;
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
        void onClickDisplay(String name);
    }

    private Runnable animator = new Runnable() {
        @Override
        public void run() {
            boolean needNewFrame = false;
            float changeValue = 0.1f;
            for (Bar bar : points) {
                if (bar.getCurrentValue() < bar.getValue()) {
                    bar.setCurrentValue(bar.getCurrentValue() + changeValue);
                    needNewFrame = true;
                } else if (bar.getCurrentValue() > bar.getValue()) {
                    bar.setCurrentValue(bar.getValue());
//                    bar.setCurrentValue(bar.getCurrentValue() - 0.02f);
                    needNewFrame = true;
                }

                if (Math.abs(bar.getValue() - bar.getCurrentValue()) < 0.02f) {
                    bar.setCurrentValue(bar.getValue());
                }
            }
            if (needNewFrame) {
                postDelayed(this, 20);
            }
            invalidate();
        }
    };


}
