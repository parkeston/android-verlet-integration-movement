package com.example.lab3_verlet;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.lab3_verlet.engine.GameView;
import com.example.lab3_verlet.engine.IGameManager;
import com.example.lab3_verlet.settings.SettingsDialog;

public class MainActivity extends AppCompatActivity implements IGameManager {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        gameView = findViewById(R.id.gameView);


        View settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getSupportFragmentManager();
                SettingsDialog settingsDialog = new SettingsDialog();
                settingsDialog.show(manager, "dialog");
            }
        });
    }

    @Override
    public void onClearView() {
        gameView.clearView();
    }
}
