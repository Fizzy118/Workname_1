package com.Piotrk_Kielak.Workname_1.Model;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * klasa odpowiedzialna za wykrywanie upadku. Jeżeli wektor przyspieszenia wyniesie mniej niż 4.5 m/s^2
 * oraz w przeciągu mniej niż 0.3s wektor przyspieszenia kątowego wyniesie więcej niż 3.5 rad/s^2 ruch
 * zostanie uzany za upadek.
 */

// TODO: Dokończyć - zintegrować czas
public class Upadek extends Service{
    public static final String UPADEK = "com.Piotrk_Kielak.Workname_1.Model";
    Intent intentAcc = new Intent(UPADEK);

    //zmienne wykrywania upadku
    private SensorManager aSensorManager, gSensorManager;
    private Sensor mAccelerometr, mGyroscope;
    private double accelerationCurrentValue;
    private double accelerationPreviousValue;
    private double gyroCurrentValue;
    private double gyroPreviousValue;
    SharedPreferences sharedPreferences;
    Boolean a, g;
    @Override
    public void onCreate(){
        super.onCreate();
        sharedPreferences = getSharedPreferences(getPackageName(),MODE_PRIVATE);

         SensorEventListener sensorAccEventListner=new SensorEventListener(){
            @Override
            public void onSensorChanged(SensorEvent sensorEvent){
                float ax = sensorEvent.values[0];
                float ay = sensorEvent.values[1];
                float az = sensorEvent.values[2];

                accelerationCurrentValue = Math.sqrt((ax*ax+ay*ay+az*az));

                if(accelerationCurrentValue<4.5){
                    Log.v("tag", "peperoni gotowe");
                    Boolean a = true;
                }
                intentAcc.putExtra("upadeek", a);
                sendBroadcast(intentAcc);
            }
            @Override
            public  void onAccuracyChanged(Sensor sensor, int i){

            }
        };
         SensorEventListener sensorGyroEventListner=new SensorEventListener(){
            @Override
            public void onSensorChanged(SensorEvent sensorEvent){
                float gx = sensorEvent.values[0];
                float gy = sensorEvent.values[1];
                //float gz = sensorEvent.values[2];
                gyroCurrentValue = Math.sqrt((gx*gx+gy*gy));
                if(gyroCurrentValue>3.5){
                    Log.v("tag", "hawajska gotowa");
                }
              //  intentGyro.putExtra("upadeek", g);
              //  sendBroadcast(intentGyro);
            }
            @Override
            public  void onAccuracyChanged(Sensor sensor, int i){

            }
        };

        //inicjalizacja zmiennych
        aSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        gSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometr=aSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope=gSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        aSensorManager.registerListener(sensorAccEventListner,mAccelerometr, SensorManager.SENSOR_DELAY_NORMAL);
        gSensorManager.registerListener(sensorGyroEventListner,mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }



    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

