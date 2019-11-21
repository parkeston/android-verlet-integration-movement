package com.example.lab3_verlet.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public class LineRenderer {
    private PointF start;
    private PointF end;

    private Paint paint;

    public LineRenderer(int color) {
        paint = new Paint();
        paint.setColor(color);
    }

    private boolean needToDraw;

    public void render(Canvas canvas) {
        if (needToDraw) {
            canvas.drawLine(start.x, start.y, end.x, end.y, paint);
        }
    }

    public void setPoints(PointF start, PointF end) {
        this.start = start;
        this.end = end;

        needToDraw = true;
    }

    public void clear() {
        needToDraw = false;
    }
}
