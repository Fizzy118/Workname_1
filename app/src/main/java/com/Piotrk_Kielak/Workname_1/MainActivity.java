package com.Piotrk_Kielak.Workname_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.util.Log;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.Realm;
import io.realm.log.LogLevel;
import io.realm.log.RealmLog;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class MainActivity extends AppCompatActivity {
    //zmienne realm
    public static App myApp;
    String Appid = "applicationdb-vxmdp";

    //zmienne wizualne/nawigacji
    private Toolbar toolbar;
    private BottomNavigationView bottomNav;
    private NavController navController;

    //zmienne wykrywania upadku
    private SensorManager aSensorManager, gSensorManager;
    private Sensor mAccelerometr, mGyroscope;

    private double accelerationCurrentValue;
    private double accelerationPreviousValue;

    private double gyroCurrentValue;
    private double gyroPreviousValue;
    private SensorEventListener sensorAccEventListner=new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent sensorEvent){
            float ax = sensorEvent.values[0];
            float ay = sensorEvent.values[1];
            float az = sensorEvent.values[2];

            accelerationCurrentValue = Math.sqrt((ax*ax+ay*ay+az*az));

            if(accelerationCurrentValue<4.6){
                Log.v("tag", "peperoni gotowe");
            }
        }
        @Override
        public  void onAccuracyChanged(Sensor sensor, int i){

        }
    };
    private SensorEventListener sensorGyroEventListner=new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent sensorEvent){
            float gx = sensorEvent.values[0];
            float gy = sensorEvent.values[1];
            //float gz = sensorEvent.values[2];
            gyroCurrentValue = Math.sqrt((gx*gx+gy*gy));
            if(gyroCurrentValue>3.6){
                Log.v("tag", "hawajska gotowa");
            }
        }
        @Override
        public  void onAccuracyChanged(Sensor sensor, int i){

        }
    };;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicjalizacja realm app
        Realm.init(this);
        myApp = new App(new AppConfiguration.Builder(Appid).build());
//        String realmName = "My Project";
//        RealmConfiguration config = new RealmConfiguration.Builder().name(realmName).build();
//        Realm backgroundThreadRealm = Realm.getInstance(config);


        //implementacja gornego paska
        toolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //implementacja dolnego menu
        bottomNav = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this,  R.id.fragmentContainerView2);
        NavigationUI.setupWithNavController(bottomNav, navController);

        //inicjalizacja zmiennych
        aSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        gSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometr=aSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope=gSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }

    protected void onResume(){
        super.onResume();
        aSensorManager.registerListener(sensorAccEventListner,mAccelerometr, SensorManager.SENSOR_DELAY_NORMAL);
        gSensorManager.registerListener(sensorGyroEventListner,mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause(){
        super.onPause();
        aSensorManager.unregisterListener(sensorAccEventListner);
        gSensorManager.unregisterListener(sensorGyroEventListner);
    }
}