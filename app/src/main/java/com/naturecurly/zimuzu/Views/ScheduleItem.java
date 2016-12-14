package com.naturecurly.zimuzu.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.naturecurly.zimuzu.Listeners.OnSwitchWatchedListener;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.Utils.DensityUtil;

import java.util.Set;

/**
 * Created by leveyleonhardt on 12/6/16.
 */

public class ScheduleItem extends View {
    private final static int NUMBER_IN_A_ROW = 3;
    private int cellNumber;
    private Paint cellPaint;
    private Paint cellBackground;
    private Paint cellText;
    private Paint divider;
    private Paint seasonText;
    private Context context;
    private Cell[] cells;
    private float downX;
    private float downY;
    private float upX;
    private float upY;
    private int topMargin;
    private int sideMargin;
    private int dividerMargin;
    private String seasonNumber;
    private int cellHeight;
    private OnSwitchWatchedListener onSwitchWatchedListener;
    private int screenWidth;
    private int cellWidth;
    private int margin;

    public ScheduleItem(Context context) {
        super(context);
    }

    public ScheduleItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray attrArray = context.obtainStyledAttributes(attrs, R.styleable.ScheduleItem);
        cellNumber = attrArray.getInt(R.styleable.ScheduleItem_episodeNum, 0);
        seasonNumber = attrArray.getString(R.styleable.ScheduleItem_seasonNum);
        attrArray.recycle();
//        cells = new Cell[cellNumber];
        topMargin = DensityUtil.dip2px(context, 30);
        sideMargin = DensityUtil.dip2px(context, 16);
        cellPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        cellPaint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        cellText = new Paint(Paint.ANTI_ALIAS_FLAG);
        cellText.setColor(Color.WHITE);
        seasonText = new Paint(Paint.ANTI_ALIAS_FLAG);
        seasonText.setColor(Color.WHITE);
        seasonText.setTextSize(DensityUtil.sp2px(context, 15));
        divider = new Paint(Paint.ANTI_ALIAS_FLAG);
        divider.setColor(Color.WHITE);
//        cellBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
//        cellBackground.setColor(ContextCompat.getColor(context, R.color.colorAccent));
        initialCells();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (Integer.parseInt(seasonNumber)) {
            case 0:
                canvas.drawText("前传", sideMargin, topMargin, seasonText);
                break;
            case 101:
                canvas.drawText("单剧", sideMargin, topMargin, seasonText);
                break;
            case 102:
                canvas.drawText("迷你剧", sideMargin, topMargin, seasonText);
                break;
            case 103:
                canvas.drawText("周边资源", sideMargin, topMargin, seasonText);
                break;
            default:
                canvas.drawText("第" + seasonNumber + "季", sideMargin, topMargin, seasonText);
                break;
        }
        canvas.drawLine(sideMargin, topMargin + dividerMargin, context.getResources().getDisplayMetrics().widthPixels - sideMargin, topMargin + dividerMargin + 2, divider);
        for (int i = 0; i < cellNumber; i++) {
            canvas.drawRect(cells[i].getRect(), cells[i].getPaintBackground());
            drawRectText(cells[i].getEpisode() + "", canvas, cells[i].getRect());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                Log.i("view", "down" + downX + " " + downY);

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                upY = event.getY();
                switchWatched(upX, upY);
                Log.i("view", "up" + upX + " " + upY);
                break;
        }
        return true;
    }

    private void switchWatched(float x, float y) {
        for (Cell c : cells) {
            if (c.rect.contains((int) x, (int) y)) {
                Log.i("view", "in");
                if (c.isWatched()) {
                    c.setPaintBackground(ContextCompat.getColor(context, R.color.episode_unchecked));
                    c.setWatched(false);
                    if (onSwitchWatchedListener != null) {
                        onSwitchWatchedListener.unWatched(c);
                    }
                } else {
                    c.setPaintBackground(ContextCompat.getColor(context, R.color.colorPrimary));
                    c.setWatched(true);
                    if (onSwitchWatchedListener != null) {
                        onSwitchWatchedListener.watched(c);
                    }
                }
                invalidate();
                break;
            }
        }
    }

    private void initialCells() {
        cells = new Cell[cellNumber];
        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        screenWidth = screen.widthPixels;
        cellWidth = screenWidth / 5;
        cellHeight = cellWidth * 3 / 5;
        margin = screenWidth / 10;
        dividerMargin = margin / 2;
        int row = 0;
        int col = 0;
        for (int count = 0; count < cellNumber; count++) {
            if (count % NUMBER_IN_A_ROW == 0 && count != 0) {
                col = 0;
                row++;
            }
            Rect rect = new Rect((col + 1) * margin + col * cellWidth, (row + 1) * margin + row * cellHeight + topMargin, (col + 1) *
                    (margin + cellWidth), (row + 1) * (margin + cellHeight) + topMargin);

            cells[count] = new Cell((count + 1) + "", rect);
            col++;
        }
    }

    public class Cell {
        private String episode;
        private Rect rect;
        private Paint paintBackground;
        private Paint paintText;
        private boolean watched;
        private String season;

        public Cell(String episode, Rect rect) {
            this.episode = episode;
            this.rect = rect;
            this.watched = false;
            paintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
            paintBackground.setColor(ContextCompat.getColor(context, R.color.episode_unchecked));
            season = seasonNumber;
        }

        public void setPaintBackground(int color) {
            paintBackground.setColor(color);
        }

        public Paint getPaintBackground() {
            return paintBackground;
        }

        public boolean isWatched() {
            return watched;
        }

        public void setWatched(boolean watched) {
            this.watched = watched;
            if (watched == true) {
                setPaintBackground(ContextCompat.getColor(context, R.color.colorPrimary));
            } else {
                setPaintBackground(ContextCompat.getColor(context, R.color.episode_unchecked));
            }
        }

        public String getEpisode() {
            return episode;
        }

        public Rect getRect() {
            return rect;
        }

        public String getSeason() {
            return season;
        }
    }

    public void setCellNumber(int cellNumber) {
        this.cellNumber = cellNumber;
        initialCells();
        requestLayout();
        invalidate();
    }

    public void setSeasonNumber(String seasonNumber) {
        this.seasonNumber = seasonNumber;
        invalidate();
    }

    private void drawRectText(String text, Canvas canvas, Rect r) {

        cellText.setTextSize(r.height() / 3);
        cellText.setTextAlign(Paint.Align.CENTER);
        int width = r.width();

        int numOfChars = cellText.breakText(text, true, width, null);
        int start = (text.length() - numOfChars) / 2;
        canvas.drawText(text, start, start + numOfChars, r.exactCenterX(), r.exactCenterY() + r.height() / 6, cellText);
    }

    public void setOnSwitchWatchedListener(OnSwitchWatchedListener onSwitchWatchedListener) {
        this.onSwitchWatchedListener = onSwitchWatchedListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (cellNumber > 0) {
            int rowNum = ((cellNumber - 1) / NUMBER_IN_A_ROW) + 1;
            setMeasuredDimension(widthMeasureSpec, (rowNum + 1) * (cellHeight + margin));
        } else if (cellNumber == 0) {
            setMeasuredDimension(widthMeasureSpec, 0);

        }
    }

    public void setWatched(Set<String> set) {
        for (Cell cell : cells) {
            if (set.contains(cell.getEpisode())) {
                cell.setWatched(true);
            }
        }
        invalidate();
    }
}
