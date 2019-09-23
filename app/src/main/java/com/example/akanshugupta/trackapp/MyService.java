package com.example.akanshugupta.trackapp;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;


public class MyService extends Service {
    public MyService() {

    }
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    public static int[] RSSI;
    public static int i;
    private static final String TAG = "com.example.akanshugupta.trackapp";
    //private LinkedList<Device> deviceList;

    //private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Runnable r = new Runnable() {
            @Override
            public void run() {

                //getActionBar().setTitle(R.string.title_devices);
               /* if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
                    return;
                }
                final BluetoothManager bluetoothManager =
                        (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                mBluetoothAdapter = bluetoothManager.getAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
                    return;
                }*/

                mHandler = new Handler();
                scanLeDevice(true);

                /*if (!mBluetoothAdapter.isEnabled()) {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                }*/

                // Initializes list view adapter.
                mLeDeviceListAdapter = new LeDeviceListAdapter();

                //setListAdapter(mLeDeviceListAdapter);
                scanLeDevice(true);
                double[][] positions = new double[][]{{5.0, -6.0}, {13.0, -15.0}, {21.0, -3.0}, {12.4, -21.2}};
                double[] distances = new double[]{0, 0, 0, 0};
                int i = 0;
                if (mLeDeviceListAdapter.RSSI.size()==4) {
                    for (i = 0; i < 4; i++) {
                        distances[i] = -5 * mLeDeviceListAdapter.RSSI.get(i) - 36;
                    }
                }

                NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());


                LeastSquaresOptimizer.Optimum optimum = solver.solve();

                // the answer
                double[] centroid = optimum.getPoint().toArray();
                Log.i(TAG," "+centroid[0]+" "+centroid[1]);


            }
        };
        Thread t = new Thread(r);
        t.start();
        return START_STICKY;
    }
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    //invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        //invalidateOptionsMenu();
    }

    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;
        public ArrayList<Integer> RSSI;

        public LeDeviceListAdapter() {
            super();
            RSSI = new ArrayList<Integer>();
            mLeDevices = new ArrayList<BluetoothDevice>();
          //  mInflator = DeviceScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device, int rssi) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
                RSSI.add(rssi);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            /*ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = view.findViewById(R.id.device_address);
                viewHolder.deviceName = view.findViewById(R.id.device_name);
                viewHolder.RSSIValue = view.findViewById(R.id.RSSIValue);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            int r = this.RSSI.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());
            viewHolder.RSSIValue.setText(r+"");
*/
            return view;
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {*/
                            mLeDeviceListAdapter.addDevice(device, rssi);
                            mLeDeviceListAdapter.notifyDataSetChanged();
                      //  }
                    //});
                }
            };
    /*static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView RSSIValue;
    }*/


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}
