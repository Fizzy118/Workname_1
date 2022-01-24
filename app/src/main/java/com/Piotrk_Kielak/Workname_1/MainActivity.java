package com.Piotrk_Kielak.Workname_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.se.omapi.Session;
import android.telephony.SmsManager;
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

import com.Piotrk_Kielak.Workname_1.Model.Upadek;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.Realm;
import io.realm.log.LogLevel;
import io.realm.log.RealmLog;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;

/**
 * Main activity, inicjalizuje baze danych oraz dolne menu. posiada funkcję wysyłającą wiadomości o upadku i braku aktywności
 * oraz mierzy czas bez aktywności.
 */
public class MainActivity extends AppCompatActivity {

    //zmiennne uzytkownika
    private Boolean typKonta=null; //?????????????
    private String[] numerUzytkownika;
    private ArrayList arrayList;
    private User user = null;

    //zmienne realm
    public static App myApp;
    String Appid = "applicationdb-vxmdp";

    //zmienne wizualne/nawigacji
    private Toolbar toolbar;
    private BottomNavigationView bottomNav;
    private NavController navController;

    //zmienne wykrywania upadku
    private SensorManager mSensorManager;
    private Sensor mAccelerometr, mGyroscope;
    private double accelerationCurrentValue;
    private double accelerationPreviousValue;
    private double gyroCurrentValue;
    private double gyroPreviousValue;
    Boolean count = false;
    //odlicza 30min od ostatniego ruchu telefonu
    public CountDownTimer cdt = new CountDownTimer(1800000, 1000){
        public void onFinish(){
            sendSms();
            count=false;
        }
        public void onTick(long millisUntilFinished) {
            Log.v("tag", String.valueOf(millisUntilFinished / 1000));
        }
    };


    // Jeśli telefon jest w bezruchu, zaczyna odliczanie 30min do wysłania wiadomości.
    private SensorEventListener sensorAccEventListner=new SensorEventListener(){
        @Override
        public void onSensorChanged(SensorEvent sensorEvent){

            float ax = sensorEvent.values[0];
            float ay = sensorEvent.values[1];
            float az = sensorEvent.values[2];

            accelerationCurrentValue = Math.sqrt((ax*ax+ay*ay+az*az));

            if(accelerationCurrentValue>9 && accelerationCurrentValue<10){
               // Log.v("tag", "peperoni gotowe");
                if(count==false){
               cdt.start();
               count=true;
            }
            }
            if(accelerationCurrentValue<9 || accelerationCurrentValue>10){
                Log.v("tag", "capiczina gotowe");
                count=false;
                cdt.cancel();
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
                gyroCurrentValue = Math.sqrt((gx * gx + gy * gy));
                if (gyroCurrentValue < 3.6) {
                    Log.v("tag", "hawajska gotowa");
                }
                Log.v("tag", String.valueOf(gyroCurrentValue));
                Log.v("tag", "fffff");


        }
        @Override
        public  void onAccuracyChanged(Sensor sensor, int i){
        }
    };




    //Listeners
//    public interface aListener{
//        void onTranslation(float tx, float ty, float tz);
//    }
//    private aListener alistener;
//    public void setListener(aListener l)
//    {
//     alistener=l;
//    }
//    public interface gListener{
//        void onRotation(float rx, float ry);
//    }
//    private gListener glistener;
//    public void setListener(gListener l)
//    {
//        glistener=l;
//    }





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

//      TODO: ustaiwc zmiennna globalna opisujaca typ konta
        user = myApp.currentUser();
        if (user == null){
            Intent intent = new Intent(this, LogActivity.class);
            this.startActivity(intent);
        }
        else {
            Functions functionsManager = myApp.getFunctions(user);
            List<String> myList = Arrays.asList("");
            functionsManager.callFunctionAsync("getTyp", myList, Boolean.class, (App.Callback) result -> {
                if (result.isSuccess()) {
                    //Log.v("TAG()", "typ konta: " + (Boolean) result.get());
                    typKonta = (Boolean) result.get();
                    Log.v("TAG()", "podano typp konta " + typKonta);
                } else {
                    Log.v("TAG()", "blad podczas pobierania typu" + result.getError());
                }
                Log.v("TAG()", "taki typ" + typKonta);
            });


//        if(typKonta==true) {
//            functionsManager.callFunctionAsync("getNumberOpiekuna", myList, ArrayList.class, (App.Callback) result -> {
//                if (result.isSuccess()) {
//                    arrayList = (ArrayList) result.get();
//                } else {
//                    Log.v("TAG()", "blad podczas pobierania numeru" + result.getError());
//                }
//            });
//        }
            typKonta=true;

            //implementacja gornego paska
            toolbar = findViewById(R.id.my_toolbar);
            setSupportActionBar(toolbar);

            //implementacja dolnego menu
            bottomNav = findViewById(R.id.bottomNavigationView);
            navController = Navigation.findNavController(this, R.id.fragmentContainerView2);
            NavigationUI.setupWithNavController(bottomNav, navController);

            //inicjalizacja sensora
            Log.v("TAG", "konto jest" + typKonta);
            if (typKonta == true) {
                mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

                mAccelerometr = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

                mSensorManager.registerListener(sensorAccEventListner, mAccelerometr, SensorManager.SENSOR_DELAY_NORMAL);
                mSensorManager.registerListener(sensorGyroEventListner, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
            }
//        Intent intent = new Intent(this, Upadek.class);
//        startService(intent);
        }
    }

//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Boolean a = intent.getBooleanExtra("wykryto", false);
//            Log.i("tag", String.valueOf(a));
//            SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
//            sharedPreferences.edit().apply();
//        }
//    };

    protected void onResume(){
        super.onResume();
//        mSensorManager.registerListener(sensorAccEventListner,mAccelerometr, SensorManager.SENSOR_DELAY_NORMAL);
   //     mSensorManager.registerListener(sensorGyroEventListner,mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
       // registerReceiver(broadcastReceiver,new IntentFilter((Upadek.UPADEK)));
    }

    protected void onPause(){
        super.onPause();
       // unregisterReceiver(broadcastReceiver);
       // mSensorManager.unregisterListener(sensorAccEventListner);
        //mSensorManager.unregisterListener(sensorGyroEventListner);
    }
    protected void onDestroy(){
        super.onDestroy();
       // stopService(new Intent(this,Upadek.class));
    }

    //Funkcja wysyłająca powiadomienie do opiekuna

        private void sendSms () {
        SmsManager smsManager = SmsManager.getDefault();
            int a = arrayList.size();
            for(int i=0; i<a; i++){
                smsManager.sendTextMessage(numerUzytkownika[i], null, "Wykryto potencjalne zagrożenie." +
                        " Skontaktuj się z użytkownikiem tego numeru", null, null);
                Toast.makeText(this, "Wysłano powiadomienie do opiekuna", Toast.LENGTH_LONG).show();
            }
    }
}