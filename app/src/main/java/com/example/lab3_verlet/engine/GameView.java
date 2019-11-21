package com.example.lab3_verlet.engine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.lab3_verlet.settings.EnvironmentSettings;
import com.example.lab3_verlet.utils.LineRenderer;
import com.example.lab3_verlet.verlet.VerletPoint;
import com.example.lab3_verlet.verlet.VerletSquare;

import java.util.ArrayList;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameLoop gameLoop;
    private LineRenderer lineRenderer;

    private IGameObject previewItem;
    private ArrayList<IGameObject> gameObjects;

    public static int screenWidth;
    public static int screenHeight;

    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        getHolder().addCallback(this);
        setFocusable(true);

        lineRenderer = new LineRenderer(Color.RED);

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        gameObjects = new ArrayList<>();
    }

    public void clearView() {
        gameObjects.clear();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        gameLoop = new GameLoop(getHolder(), this);

        gameLoop.setRunning(true);
        gameLoop.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;

        while (retry) {
            try {
                gameLoop.setRunning(false);
                gameLoop.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            }

            retry = false;
        }
    }

    public void update() {
        for (IGameObject gameObject : gameObjects)
            gameObject.update();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        if (previewItem != null)
            previewItem.render(canvas);

        lineRenderer.render(canvas);

        for (IGameObject gameObject : gameObjects)
            gameObject.render(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                previewItem = EnvironmentSettings.SPAWN_BOX ?
                        new VerletSquare((int) event.getX(), (int) event.getY())
                        : new VerletPoint((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                lineRenderer.setPoints(previewItem.getPosition(), new PointF(event.getX(), event.getY()));
                break;
            case MotionEvent.ACTION_UP:
                if (EnvironmentSettings.SPAWN_BOX)
                    gameObjects.add(new VerletSquare((int) previewItem.getPosition().x, (int) previewItem.getPosition().y,
                            (int) event.getX(), (int) event.getY()));
                else
                    gameObjects.add(new VerletPoint((int) previewItem.getPosition().x, (int) previewItem.getPosition().y,
                            (int) event.getX(), (int) event.getY()));

                previewItem = null;
                lineRenderer.clear();
                break;
        }
        return true;
    }
}
