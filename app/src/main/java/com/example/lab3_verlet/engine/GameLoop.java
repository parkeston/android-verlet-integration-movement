package com.example.lab3_verlet.engine;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameLoop extends Thread {
    public static final int MAX_FPS = 60;

    private SurfaceHolder surfaceHolder;
    private GameView gameView;

    private boolean running;

    public GameLoop(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        long frameStartTime;
        long frameTime = 0;
        long frameTargetTime = 1000 / MAX_FPS;
        long frameWaitTime;
        long timeMillis;

        Canvas canvas = null;

        while (running) {
            frameStartTime = System.nanoTime();

            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    gameView.update();
                    gameView.onDraw(canvas);
                }
            } catch (Exception | Error e) {
                e.printStackTrace();
            } finally {
                if (canvas != null)
                    surfaceHolder.unlockCanvasAndPost(canvas);
            }

            timeMillis = (System.nanoTime() - frameStartTime) / 1000000; //passed frame time in milliseconds
            frameWaitTime = frameTargetTime - timeMillis;

            if (frameWaitTime > 0) {
                try {
                    sleep(frameWaitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            frameTime = System.nanoTime() - frameStartTime;
            Time.deltaTime = frameTime / 1000000000f; //sync?
        }
    }
}
