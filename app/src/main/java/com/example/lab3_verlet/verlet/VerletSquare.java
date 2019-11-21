package com.example.lab3_verlet.verlet;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.example.lab3_verlet.engine.IGameObject;

public class VerletSquare implements IGameObject {
    private PointF position;

    private VerletStick[] sticks;
    private VerletPoint[] points;

    private Paint paint;
    private Path boxBody;

    private int size;

    private final int FILL_COLOR = Color.BLACK;
    private final int BOX_SIZE = 200;

    public VerletSquare(int x, int y) {
        init(x, y);

        points[0] = new VerletPoint(x - size / 2, y - size / 2, position);
        points[1] = new VerletPoint(x - size / 2, y + size / 2, position);
        points[2] = new VerletPoint(x + size / 2, y - size / 2, position);
        points[3] = new VerletPoint(x + size / 2, y + size / 2, position);

        CreateSticks();
    }

    public VerletSquare(int x, int y, int xOld, int yOld) {
        init(x, y);

        points[0] = new VerletPoint(x - size / 2, y - size / 2, xOld - size / 2, yOld - size / 2, position);
        points[1] = new VerletPoint(x - size / 2, y + size / 2, xOld - size / 2, yOld + size / 2, position);
        points[2] = new VerletPoint(x + size / 2, y - size / 2, xOld + size / 2, yOld - size / 2, position);
        points[3] = new VerletPoint(x + size / 2, y + size / 2, xOld + size / 2, yOld + size / 2, position);

        CreateSticks();
    }

    private void init(int x, int y) {
        position = new PointF(x, y);

        points = new VerletPoint[4];
        sticks = new VerletStick[5];

        paint = new Paint();
        paint.setColor(FILL_COLOR);
        paint.setStyle(Paint.Style.FILL);
        boxBody = new Path();

        size = BOX_SIZE;
    }

    private void CreateSticks() {
        sticks[0] = new VerletStick(points[0], points[1]);
        sticks[1] = new VerletStick(points[1], points[3]);
        sticks[2] = new VerletStick(points[3], points[2]);
        sticks[3] = new VerletStick(points[2], points[0]);
        sticks[4] = new VerletStick(points[0], points[3]);
    }


    public PointF getPosition() {
        return position;
    }

    @Override
    public void render(Canvas canvas) {

        boxBody.moveTo(points[0].getPosition().x, points[0].getPosition().y);
        boxBody.lineTo(points[1].getPosition().x, points[1].getPosition().y);
        boxBody.lineTo(points[3].getPosition().x, points[3].getPosition().y);
        boxBody.lineTo(points[2].getPosition().x, points[2].getPosition().y);
        boxBody.close();

        canvas.drawPath(boxBody, paint);
        boxBody.rewind();


        for (VerletPoint point : points) {
            point.render(canvas);
        }

        for (VerletStick stick : sticks) {
            stick.render(canvas);
        }
    }

    @Override
    public void update() {

        for (VerletPoint point : points) {
            point.update();
        }

        for (int i = 0; i < 3; i++) {
            for (VerletStick stick : sticks) {
                stick.update();
            }

            for (VerletPoint point : points) {
                point.ConstrainToScreen();
            }
        }

        position.x = points[0].getPosition().x + (points[3].getPosition().x - points[0].getPosition().x) / 2;
        position.y = points[0].getPosition().y + (points[3].getPosition().y - points[0].getPosition().y) / 2;
    }
}
