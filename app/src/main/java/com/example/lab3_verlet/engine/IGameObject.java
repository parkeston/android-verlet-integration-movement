package com.example.lab3_verlet.engine;

import android.graphics.Canvas;
import android.graphics.PointF;

public interface IGameObject {
    void render(Canvas canvas);

    void update();

    PointF getPosition();
}
