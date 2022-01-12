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
    private SensorManager mSensorManager;
    private Sensor mAccelerometr;
    private SensorEventListener sensorEventListner=new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent sensorEvent){

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
        String realmName = "My Project";
        RealmConfiguration config = new RealmConfiguration.Builder().name(realmName).build();
        Realm backgroundThreadRealm = Realm.getInstance(config);


        //implementacja gornego paska
        toolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //implementacja dolnego menu
        bottomNav = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this,  R.id.fragmentContainerView2);
        NavigationUI.setupWithNavController(bottomNav, navController);

        //inicjalizacja zmiennych
        mSensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometr=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    protected void onResume(){
        super.onResume();
       // mSensorManager.registerListener(sensorEventListner,SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause(){
        super.onPause();
        mSensorManager.unregisterListener(sensorEventListner);
    }
}