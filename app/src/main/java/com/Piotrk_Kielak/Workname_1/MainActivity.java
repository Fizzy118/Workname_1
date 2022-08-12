package com.Piotrk_Kielak.Workname_1;


import static java.util.concurrent.TimeUnit.MINUTES;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.bson.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import io.realm.Realm;
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
    private Boolean typKonta;
    private ArrayList numerUzytkownika;
    private User user = null;


    //zmienne realm
    public static App myApp;
    String Appid = "applicationdb-vxmdp";

    //zmienne wizualne/nawigacji
    private Toolbar toolbar;
    private BottomNavigationView bottomNav;
    private NavController navController;
    private ImageButton buttonSOS;

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
    private ScheduledExecutorService scheduler;


    //odlicza czas po spełnienu warunku akcelerometru
    public CountDownTimer fall_timer = new CountDownTimer(3000, 1000) {
        public void onFinish() {
            Log.v("fall timer", String.valueOf(gyroCurrentValue));
            if (gyroCurrentValue > 3.5) {
                alert_timer.start();
                //wiadomość o chęci wysłania powiadomienia
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MainActivity.this, "Fall notification");
                notificationBuilder.setContentTitle("Wykryto zagrożenie!");
                notificationBuilder.setContentText("Aby nie wysyłać wiadomości, zareaguj.");
                notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
                notificationBuilder.setAutoCancel(true);
                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                managerCompat.notify(111, notificationBuilder.build());
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
            }
            ;
        }

        public void onTick(long millisUntilFinished) {
        }
    };

    //odlicza minutę od upadku
    public CountDownTimer alert_timer = new CountDownTimer(60000, 1000) {
        public void onFinish() {
            Log.v("alert timer", "Wiadomość wysłana");
            sendSms();
        }

        public void onTick(long millisUntilFinished) {
            Log.v("alert_timer", String.valueOf(millisUntilFinished / 1000));
        }
    };
    // Jeśli telefon jest w bezruchu, zaczyna odliczanie 30min do wysłania wiadomości.
    private SensorEventListener sensorAccEventListner = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            float ax = sensorEvent.values[0];
            float ay = sensorEvent.values[1];
            float az = sensorEvent.values[2];

            accelerationCurrentValue = Math.sqrt((ax * ax + ay * ay + az * az));

            if (accelerationCurrentValue < 4.5) {
                Log.v("tag", "acc < 4.5");
                fall_timer.start();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private SensorEventListener sensorGyroEventListner = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            float gx = sensorEvent.values[0];
            float gy = sensorEvent.values[1];
            float gz = sensorEvent.values[2];
            gyroCurrentValue = Math.sqrt((gx * gx + gy * gy));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduler = Executors.newScheduledThreadPool(1);

        //inicjalizacja realm app
        Realm.init(this);
        myApp = new App(new AppConfiguration.Builder(Appid).build());
        user = myApp.currentUser();

        Log.v("onCreate", "fusedLocationClient");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //dostęp do sms
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            Log.v("SEND SMS", "przyznane");
        }else{
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},101);
        }
        //dostęp do statusu telefonu potrzeby aby wysłać sms
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            Log.v("READ_PHONE_STATE", "przyznane");
        }else{
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},101);
        }

        //kanał powiadomien
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Fall notification", "App notifications", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService((NotificationManager.class));
            manager.createNotificationChannel(channel);
        }

        //sprawdza czy użytkownik został podany oraz sprawdza typ użytkownika
        if (user == null) {
            Intent intent = new Intent(this, LogActivity.class);
            this.startActivity(intent);
        } else {
            //inicjacja przycisku sos który w razie klikniecia wysyła prośbę o kontakt
            buttonSOS = findViewById(R.id.imageButton_sos);
            buttonSOS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SmsManager smsManager = SmsManager.getDefault();
                    int a = numerUzytkownika.size();
                    for (int i = 0; i < a; i++) {
                        smsManager.sendTextMessage((String) numerUzytkownika.get(i), null, "Potrzebuję pomocy, zadzwoń do mnie!\r\n https://www.google.com/maps/search/?api=1&query="+latitude+"%2C"+longitude, null, null);
                    }
                    Toast.makeText(getBaseContext(), "Wysłano wiadomość SOS", Toast.LENGTH_LONG).show();
                }
            });
            //sprawdzenie typu użytkownika
            getType();

            //implementacja gornego paska
            toolbar = findViewById(R.id.my_toolbar);
            setSupportActionBar(toolbar);

            //implementacja dolnego menu
            bottomNav = findViewById(R.id.bottomNavigationView);
            navController = Navigation.findNavController(this, R.id.fragmentContainerView2);
            NavigationUI.setupWithNavController(bottomNav, navController);
        }
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    //Funkcja wysyłająca powiadomienie do opiekuna
    private void sendSms() {
        SmsManager smsManager = SmsManager.getDefault();
        int a = numerUzytkownika.size();
        for (int i = 0; i < a; i++) {
            smsManager.sendTextMessage((String) numerUzytkownika.get(i), null, "Wykryto potencjalne zagrożenie." +
                    " Skontaktuj się z użytkownikiem tego numeru\r\n https://www.google.com/maps/search/?api=1&query="+latitude+"%2C"+longitude, null, null);
        }
        Toast.makeText(this, "Wysłano powiadomienie do opiekuna", Toast.LENGTH_LONG).show();
    }



    //Funcja pobiera lokalizacje i wysyła ją do bazy danych
    private void getLocalization() {
        Log.v("getLocalization", "start");
        if (checkPermissions()) {
            Log.v("getLocalization", "udzielony dostęp");
            if (isLocationEnabled()) {
                Log.v("isLocationEnabled", "udzielony dostęp");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { }
                fusedLocationClient.getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    Log.v("getLocalization", "location null");
                                } else {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    Log.v("getLocalization", "location" + latitude + longitude);
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

                        });
            } else{
            Log.v("getLocalization", "nie udzielony dostęp");
            Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            }
        } else{
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, 101);
        }
        if(isLocationEnabled()){
            Log.v("isLocationEnabled", "udzielony dostęp");
        }
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    //funkcja wywołująca lokalizacje co określony odstep czasowy określony przez użytkownika
    public void sendLocalization() {
        Integer time;
        time = ReadFromFile("timerFile");
        Runnable getterLocalization = () -> getLocalization();
        ScheduledFuture<?> getterHandle =
                scheduler.scheduleAtFixedRate(getterLocalization, 0, time, MINUTES);
        Log.v("sendLocalization", "wysłane");
    }

    // Funkcja zwracająca typ użytkownika - w przypadku kiedy typ konta == podopieczny, wykonuje funkcję "setForCharge"
    public Boolean getType(){
        this.user = MainActivity.myApp.currentUser();
        Functions functionsManager = MainActivity.myApp.getFunctions(user);
        functionsManager.callFunctionAsync("getTyp", Collections.singletonList(""), Boolean.class, (App.Callback) result -> {
            if (result.isSuccess()) {
                //Log.v("TAG()", "typ konta: " + (Boolean) result.get());
                typKonta = (Boolean) result.get();
                if(typKonta==true){
                    setForCharge();
                    //wyświetlenie przycisku sos dla podopiecznego
                    buttonSOS.setVisibility(View.VISIBLE);
                }
                Log.v("MainActivity", "podano typ konta " + typKonta);

            } else {
                Log.v("MainActivity", "blad podczas pobierania typu" + result.getError());
            }
        });
        return  typKonta;
    }
    //Funkcja odpowiedzialna za generowanie sensorów,  wysyłanie lokalizacji do bazy, pobieranie numerów opiekunów - wywoływana tylko w przypadku podopiecznych
    private void setForCharge(){
            // wysyłanie lokalizacji do bazy danych
            sendLocalization();
            this.user = MainActivity.myApp.currentUser();
            Functions functionsManager = MainActivity.myApp.getFunctions(user);
            //funkcja pobierająca numery i zapisujaca w zmiennej
            functionsManager.callFunctionAsync("getNumber", Collections.singletonList(""), ArrayList.class, (App.Callback) result -> {
                if (result.isSuccess()) {
                    Log.v("getNumber", "pobrano numery " + (ArrayList) result.get());
                    numerUzytkownika = (ArrayList) result.get();
                } else {
                    Log.v("getNumber", "niepobrano numerow " + result.get());
                }
            });
            //generowanie sensorów
            mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            mAccelerometr = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            if (mGyroscope == null) {
                Log.v("MainActivity", "Gryro null");
            }
            mSensorManager.registerListener(sensorAccEventListner, mAccelerometr, SensorManager.SENSOR_DELAY_NORMAL);
            mSensorManager.registerListener(sensorGyroEventListner, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
            if (mAccelerometr == null) {
                Log.v("MainActivity", "Acc null");
            }
    }

    //funkcja do odczytywania z pliku informacji o częstotliwości wysyłania lokalizacji do bazy danych
    public int ReadFromFile(String fileName){
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path, fileName);
        byte[] content = new byte[(int) readFrom.length()];
        try {
            FileInputStream stream = new FileInputStream(readFrom);
            stream.read(content);
            Log.v("ReadFromFile", " odczytano");
            return new BigInteger(content).intValue();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }

    }
}