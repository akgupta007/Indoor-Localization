package com.example.akanshugupta.trackapp;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class BottomSectionFragment extends Fragment {
    private static SensorData mSensorData;
    private static TextView mAzimuthal;
    public static TextView mXc;
    public static TextView mYc;
    boolean running=false;
    private Button mStop;
    private Button mStart;
    public static TextView mSteps;
    public static TextView mStepsDetect;
    private Button mMap;
    private static EditText X_init;
    private static EditText Y_init;
    public static TextView mDistance;

    public static void mUpdate(){
        if (mSensorData.getOrientation() != null) {
            double azimuth = mSensorData.getOrientation()[0]*180/3.14;
            mAzimuthal.setText("" + azimuth);
        }
    }
    public static double mGetX_init(){
        if(X_init.getText().toString() == ""){
            return 0;
        }
        return Double.valueOf(X_init.getText().toString());
    }
    public static void mMakeX_init(double x,double y) {
        X_init.setText(x+"");
        Y_init.setText(y+"");
    }
    public static double mGetY_init() {
        if(Y_init.getText().toString()==""){
            return 0;
        }
        return Double.valueOf(Y_init.getText().toString());
    }

    static BottomSectionListener activityCommander;

    public interface BottomSectionListener{
        public void showmap(String x,String y);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            activityCommander = (BottomSectionListener) activity;

        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


            View view = inflater.inflate(R.layout.bottom_section_fragment,container,false);


        X_init=(EditText)view.findViewById(R.id.x_initial);
        Y_init=(EditText)view.findViewById(R.id.y_initial);
        mDistance=(TextView)view.findViewById(R.id.distance);
        mXc=(TextView) view.findViewById(R.id.x_current);
        mYc=(TextView)view.findViewById(R.id.y_current);
        mAzimuthal=(TextView)view.findViewById(R.id.tangle);
        mSteps=(TextView)view.findViewById(R.id.stepsDetect);
        mStop=(Button)view.findViewById(R.id.stop_button);
        mStart=(Button) view.findViewById(R.id.start_button);
        mStepsDetect = (TextView) view.findViewById(R.id.stepsDetect);
        mSensorData=new SensorData(this.getActivity());

        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSensorData.register();
            }
        });
        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                mSensorData.pause();
            }
        });
        mMap=(Button)view.findViewById(R.id.map_button);
        mMap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //activityCommander.showmap(mXc.getText().toString(),mYc.getText().toString());
            }
        });


        return view;
    }


    public static void show(String x,String y){
        //activityCommander.showmap(x,y);
        //BottomSectionFragment b = new BottomSectionFragment();
        activityCommander.showmap(x,y);
    }
}
