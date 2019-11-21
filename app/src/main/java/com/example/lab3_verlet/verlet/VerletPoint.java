package com.example.lab3_verlet.verlet;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import com.example.lab3_verlet.engine.GameView;
import com.example.lab3_verlet.engine.IGameObject;
import com.example.lab3_verlet.engine.Time;
import com.example.lab3_verlet.settings.EnvironmentSettings;

public class VerletPoint implements IGameObject {
    private PointF position;
    private PointF oldPosition;
    private Paint paint;
    private PointF rotationCenter;

    private final int POINT_COLOR = Color.RED;
    private final int POINT_SIZE = 20;

    public VerletPoint(int x, int y) {
        position = new PointF();
        position.x = x;
        position.y = y;
        oldPosition = new PointF(position.x, position.y);
        rotationCenter = null;

        paint = new Paint();
        paint.setColor(POINT_COLOR);
    }

    public VerletPoint(int x, int y, PointF rotationCenter) {
        this(x, y);
        this.rotationCenter = rotationCenter;
    }

    public VerletPoint(int x, int y, int xOld, int yOld) {
        this(x, y);

        double magnitude = Math.sqrt((x - xOld) * (x - xOld) + (y - yOld) * (y - yOld));

        xOld = x + (int) (((x - xOld) / magnitude) * EnvironmentSettings.MAX_LAUNCH_SPEED);
        yOld = y + (int) (((y - yOld) / magnitude) * EnvironmentSettings.MAX_LAUNCH_SPEED);

        oldPosition.x = xOld;
        oldPosition.y = yOld;
    }

    public VerletPoint(int x, int y, int xOld, int yOld, PointF rotationCenter) {
        this(x, y, xOld, yOld);
        this.rotationCenter = rotationCenter;
    }


    @Override
    public void render(Canvas canvas) {
        canvas.drawCircle(position.x, position.y, POINT_SIZE / 2, paint);
    }

    @Override
    public void update() {
        float vx = (position.x - oldPosition.x) * EnvironmentSettings.DRAG;
        float vy = (position.y - oldPosition.y) * EnvironmentSettings.DRAG;

        //2*pos - oldPos + adt^2 = pos + pos - olpod + adt^2

        if (EnvironmentSettings.ANGULAR_SPEED != 0 && rotationCenter != null)
            position = ApplyRotation(EnvironmentSettings.ANGULAR_SPEED * Time.deltaTime);

        oldPosition.x = position.x;
        oldPosition.y = position.y;

        position.offset(vx + EnvironmentSettings.ACCELERATION.x * Time.deltaTime * Time.deltaTime,
                vy + EnvironmentSettings.ACCELERATION.y * Time.deltaTime * Time.deltaTime);

        ConstrainToScreen();
    }

    private PointF ApplyRotation(double angle) {
        angle = Math.toRadians(angle);
        float dx = (float) (Math.cos(angle) * (position.x - rotationCenter.x) - Math.sin(angle) * (position.y - rotationCenter.y));
        float dy = (float) (Math.sin(angle) * (position.x - rotationCenter.x) + Math.cos(angle) * (position.y - rotationCenter.y));

        return new PointF(rotationCenter.x + dx, rotationCenter.y + dy);
    }

    @Override
    public PointF getPosition() {
        return position;
    }

    public void ConstrainToScreen() {
        float vx = (position.x - oldPosition.x) * EnvironmentSettings.DRAG;
        float vy = (position.y - oldPosition.y) * EnvironmentSettings.DRAG;

        float halfSize = POINT_SIZE / 2f;
        float bounceFriction = EnvironmentSettings.BOUNCE_FRICTION;

        if (position.x > GameView.screenWidth - halfSize) {
            position.x = GameView.screenWidth - halfSize;
            oldPosition.x = position.x + vx * bounceFriction;
        } else if (position.x < halfSize) {
            position.x = halfSize;
            oldPosition.x = position.x + vx * bounceFriction;
        }

        if (position.y > GameView.screenHeight - halfSize) {
            position.y = GameView.screenHeight - halfSize;
            oldPosition.y = position.y + vy * bounceFriction;
            rotationCenter = null;
        } else if (position.y < halfSize) {
            position.y = halfSize;
            oldPosition.y = position.y + vy * bounceFriction;
        }
    }
}
