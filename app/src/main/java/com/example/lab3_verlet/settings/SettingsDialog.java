package com.example.lab3_verlet.settings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.lab3_verlet.R;
import com.example.lab3_verlet.engine.IGameManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsDialog extends DialogFragment {

    private CheckBox spawnBox;
    private EditText launchSpeed, accelerationX, accelerationY, bounceFriction, drag, angularSpeed;

    private IGameManager gameManager;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.fragment_settings_dialog, null))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //save settings...
                        EnvironmentSettings.SPAWN_BOX = spawnBox.isChecked();
                        EnvironmentSettings.MAX_LAUNCH_SPEED = Integer.parseInt(launchSpeed.getText().toString());
                        EnvironmentSettings.ACCELERATION.x = Float.parseFloat(accelerationX.getText().toString());
                        EnvironmentSettings.ACCELERATION.y = Float.parseFloat(accelerationY.getText().toString());
                        EnvironmentSettings.BOUNCE_FRICTION = Float.parseFloat(bounceFriction.getText().toString());
                        EnvironmentSettings.DRAG = Float.parseFloat(drag.getText().toString());
                        EnvironmentSettings.ANGULAR_SPEED = Integer.parseInt(angularSpeed.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getDialog().cancel();
                    }
                })
                .setNeutralButton("Clear view", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("clear view!");
                        gameManager.onClearView();
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        gameManager = ((IGameManager) context);
    }

    @Override
    public void onStart() {
        super.onStart();

        spawnBox = getDialog().findViewById(R.id.spawnBox);
        launchSpeed = getDialog().findViewById(R.id.laucnSpeed);
        accelerationX = getDialog().findViewById(R.id.accelerationX);
        accelerationY = getDialog().findViewById(R.id.accelerationY);
        bounceFriction = getDialog().findViewById(R.id.bounceFriction);
        drag = getDialog().findViewById(R.id.drag);
        angularSpeed = getDialog().findViewById(R.id.angularSpeed);

        spawnBox.setChecked(EnvironmentSettings.SPAWN_BOX);
        launchSpeed.setText(String.valueOf(EnvironmentSettings.MAX_LAUNCH_SPEED));
        accelerationX.setText(String.valueOf(EnvironmentSettings.ACCELERATION.x));
        accelerationY.setText(String.valueOf(EnvironmentSettings.ACCELERATION.y));
        bounceFriction.setText(String.valueOf(EnvironmentSettings.BOUNCE_FRICTION));
        drag.setText(String.valueOf(EnvironmentSettings.DRAG));
        angularSpeed.setText(String.valueOf(EnvironmentSettings.ANGULAR_SPEED));
    }

}
