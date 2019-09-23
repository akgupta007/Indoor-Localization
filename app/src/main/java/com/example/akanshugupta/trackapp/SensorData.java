package com.example.akanshugupta.trackapp;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.SensorListener;
import android.provider.SyncStateContract;
import android.util.Log;

import com.example.akanshugupta.trackapp.BottomSectionFragment;

import java.lang.*;
import static android.content.ContentValues.TAG;
import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by hp on 04-04-2018.
 */

public class SensorData implements SensorEventListener {
    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor magnometer;
    private Sensor countSensor;
    private Sensor countSensor1;
    private float[] accelOutput;
    private float[] magOutput;
    private float mSteps;
    private float mStepsIn=0;
    private int mStepsDetect=0;
    private double strideLengh=0.73;
    private static double mXc_value=0;
    private static double mYc_value=0;
    private float[] orientation = new float[3];
    public float[] getOrientation() {
        return orientation;
    }

    private float[] startOrientation = null;
    public float[] getStartOrientation() {
        return startOrientation;
    }
    public void newGame() {
        startOrientation = null;
    }

    public SensorData(Activity activity) {
        manager = (SensorManager)activity.getSystemService(SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        countSensor= manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        countSensor1= manager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
    }



    public void register() {
        mXc_value=BottomSectionFragment.mGetX_init();
        mYc_value=BottomSectionFragment.mGetY_init();
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this, countSensor1, SensorManager.SENSOR_DELAY_GAME);
    }

    public void pause() {
        BottomSectionFragment.mMakeX_init(mXc_value,mYc_value);
        manager.unregisterListener(this);
        mSteps=0;
        mStepsIn=0;
        mStepsDetect=0;
        BottomSectionFragment.mSteps.setText(0+"");
        BottomSectionFragment.mStepsDetect.setText(""+0);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //  Log.d(TAG, "onSensorChanged: ");
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accelOutput = event.values;
        else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magOutput = event.values;
        else if(event.sensor.getType()==Sensor.TYPE_STEP_DETECTOR) {
            mStepsDetect++;
            BottomSectionFragment.mStepsDetect.setText(""+mStepsDetect);
        }
        else if(event.sensor.getType()== Sensor.TYPE_STEP_COUNTER) {
            if(mStepsIn==0) {
                mStepsIn= event.values[0];
            }
            mSteps= event.values[0];
            BottomSectionFragment.mSteps.setText(String.valueOf(mSteps-mStepsIn));

            Log.d(TAG, "onSensorChanged: step sensor called");
            BottomSectionFragment.mUpdate();
            mXc_value=mXc_value+strideLengh*Math.sin(orientation[0]);
            mYc_value=mYc_value+strideLengh*Math.cos(orientation[0]);
            BottomSectionFragment.show(Double.toString(mXc_value),Double.toString(mYc_value));
            BottomSectionFragment.mXc.setText(""+mXc_value);
            BottomSectionFragment.mYc.setText(""+mYc_value);
            BottomSectionFragment.mDistance.setText(""+(mSteps-mStepsIn)*strideLengh);

        }

        if(accelOutput != null && magOutput != null) {
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, accelOutput, magOutput);
            if(success) {
                SensorManager.getOrientation(R, orientation);
                Log.d(TAG, "onSensorChanged: "+orientation[0]+" "+orientation[1]+" "+orientation[2]);
                if(startOrientation == null) {
                    startOrientation = new float[orientation.length];
                    System.arraycopy(orientation, 0, startOrientation, 0, orientation.length);
                }

            }
        }
    }
}
