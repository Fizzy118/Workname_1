package com.Piotrk_Kielak.Workname_1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.Piotrk_Kielak.Workname_1.Model.Opieka;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
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
    private Realm realm_main;

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
    private double gyroCurrentValue;
    Boolean count_cdt = false;

    //zmienne mapy
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0;
    private double longitude = 0;

    //odlicza 30min od ostatniego ruchu telefonu
    public CountDownTimer cdt = new CountDownTimer(1800000, 1000){
        public void onFinish(){
            sendSms();
            count_cdt =false;
        }
        public void onTick(long millisUntilFinished) {
            Log.v("tag", String.valueOf(millisUntilFinished / 1000));
        }
    };
    // TODO: upewnić się że timer dobrze ustawiony (0,3sec)
    //odlicza czas po spełnienu warunku akcelerometru
    public CountDownTimer fall_timer = new CountDownTimer(3000, 1000){
        public void onFinish(){
            Log.v("fall timer", String.valueOf(gyroCurrentValue));
            if(gyroCurrentValue>3.5){
                alert_timer.start();
                //wiadomość o chęci wysłania powiadomienia
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this,"Fall notification");
                notificationBuilder.setContentTitle("Wykryto zagrożenie!");
                notificationBuilder.setContentText("Aby nie wysyłać wiadomości, zareaguj.");
                notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                managerCompat.notify(111,notificationBuilder.build());
                //alert
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setTitle("Wykryto zagrożenie!");
                alertDialogBuilder.setMessage("Jeżeli nie zareagujesz, wyśle wiadomość do Twojego opiekuna.");
                alertDialogBuilder.setPositiveButton("Fałszywy alarm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("alert", "fałszywy alarm");
                        managerCompat.cancel(111);
                        alert_timer.cancel();
                    }
                });
                    alertDialogBuilder.show();
            };
        }
        public void onTick(long millisUntilFinished) {
        }
    };

    //odlicza minutę od upadku
    public CountDownTimer alert_timer = new CountDownTimer(60000, 1000){
        public void onFinish(){
            Log.v("alert timer", "Wiadomość wysłana");
            sendSms();
        }
        public void onTick(long millisUntilFinished) {
            Log.v("alert_timer", String.valueOf(millisUntilFinished / 1000));
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

            if(accelerationCurrentValue<4.5){
                Log.v("tag", "acc < 4.5");
                fall_timer.start();
            }

            if(accelerationCurrentValue<9 || accelerationCurrentValue>10){
                Log.v("tag", "wykryto ruch, reset timera");
                count_cdt =false;
                cdt.cancel();
                cdt.start();
                count_cdt =true;
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
                float gz = sensorEvent.values[2];
                gyroCurrentValue = Math.sqrt((gx * gx + gy * gy));
        }
        @Override
        public  void onAccuracyChanged(Sensor sensor, int i){
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //inicjalizacja realm app
        Realm.init(this);
        myApp = new App(new AppConfiguration.Builder(Appid).build());
        String realmName = "My Project";
        //RealmConfiguration config = new RealmConfiguration.Builder().allowQueriesOnUiThread();

        //Realm.setDefaultConfiguration(Realm.getDefaultConfiguration());
        //Realm backgroundThreadRealm = Realm.getInstance(config);


//        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
//                .initialData(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        realm.createObject(User.class);
//                    }})
//                .build();
//        Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
//        Realm.setDefaultConfiguration(realmConfig);

        //kanał powiadomien
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Fall notification", "App notifications", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService((NotificationManager.class));
            manager.createNotificationChannel(channel);
        }

        user = myApp.currentUser();
        if (user == null){
            Intent intent = new Intent(this, LogActivity.class);
            this.startActivity(intent);
        }
        else {
            getLocalization();
            Functions functionsManager = myApp.getFunctions(user);
            List<String> myList = Arrays.asList("");
            functionsManager.callFunctionAsync("getTyp", myList, Boolean.class, (App.Callback) result -> {
                if (result.isSuccess()) {
                    //Log.v("TAG()", "typ konta: " + (Boolean) result.get());
                    typKonta = (Boolean) result.get();
                    Log.v("MainActivity", "podano typp konta " + typKonta);
                } else {
                    Log.v("MainActivity", "blad podczas pobierania typu" + result.getError());
                }
            });



//        if(typKonta==false) {
//            functionsManager.callFunctionAsync("getNumberOpiekuna", myList, ArrayList.class, (App.Callback) result -> {
//                if (result.isSuccess()) {
//                    arrayList = (ArrayList) result.get();
//                } else {
//                    Log.v("TAG()", "blad podczas pobierania numeru" + result.getError());
//                }
//            });
//        }


//          TODO: ustaiwc zmiennna globalna opisujaca typ konta
            typKonta=false;

            //implementacja gornego paska
            toolbar = findViewById(R.id.my_toolbar);
            setSupportActionBar(toolbar);

            //implementacja dolnego menu
            bottomNav = findViewById(R.id.bottomNavigationView);
            navController = Navigation.findNavController(this, R.id.fragmentContainerView2);
            NavigationUI.setupWithNavController(bottomNav, navController);

            //inicjalizacja sensora
            Log.v("TAG", "konto jest" + typKonta);

            //generowanie sensorów jeżeli konto jest podopiecznego
            if (typKonta == false) {
                mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

                mAccelerometr = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                if(mGyroscope == null){
                    Log.v("TAG", "Gryro null");
                }
                mSensorManager.registerListener(sensorAccEventListner, mAccelerometr, SensorManager.SENSOR_DELAY_NORMAL);
                mSensorManager.registerListener(sensorGyroEventListner, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);

                if(mAccelerometr == null){
                    Log.v("TAG", "Acc null");
                }
            }
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
    //Funcja pobiera lokalizacje i wysyła ją do bazy danych
    private void getLocalization(){
        Log.v("getLocalization", "start");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.v("getLocalization", "udzielony dostęp");

        }else{
            Log.v("getLocalization", "nie udzielony dostęp");
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

        //TODO dokonczyc to
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        Log.v("getLocalization", "location null");
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latitude=location.getLatitude();
                            longitude=location.getLongitude();
                            Log.v("getLocalization", "latlong"+ latitude + longitude);

                            // Logic to handle location object
                        }
                    }
                });
        Log.v("getLocalization", "half");
        Functions functionsManager = MainActivity.myApp.getFunctions(user);
        List<Double> myList = Arrays.asList(latitude,longitude);
        functionsManager.callFunctionAsync("changeLocalization", myList, Document .class, (App.Callback) result -> {
            if (result.isSuccess()) {
                Log.v("getLocalization", "pobrano lokalizacje " + (Document) result.get());
            } else {
                Log.v("getLocalization", "błąd: nie pobrano lokalizacji " + result.get());
            }

        });
    }
}