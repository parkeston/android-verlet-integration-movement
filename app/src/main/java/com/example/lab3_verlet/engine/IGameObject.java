package com.example.lab3_verlet.engine;

import android.graphics.Canvas;
import android.graphics.PointF;

public interface IGameObject
{
    public void render(Canvas canvas);
    public void update();
    public PointF getPosition();
}
