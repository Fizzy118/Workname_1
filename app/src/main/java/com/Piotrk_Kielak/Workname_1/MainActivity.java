package com.Piotrk_Kielak.Workname_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.Realm;
import io.realm.log.LogLevel;
import io.realm.log.RealmLog;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;

public class MainActivity extends AppCompatActivity {
    public static App myApp;
    String Appid = "applicationdb-vxmdp";
    private Toolbar toolbar;
    private BottomNavigationView bottomNav;
    private NavController navController;

    //App myApp;

    //String TAG = MainActivity.class.getSimpleName();
    //  Intrinsics.checkNotNullExpressionValue(TAG(), "T::class.java.simpleName");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Realm.init(this);
        myApp = new App(new AppConfiguration.Builder(Appid).build());
        //defaultSyncErrorHandler(Session, error ->Log.e(TAG(), "Sync error: ${error.errorMessage}")).
        // Enable more logging in debug mode
        if (BuildConfig.DEBUG) {
            RealmLog.setLevel(LogLevel.ALL);
        }
       // Log.v(TAG(), "Initialized the Realm App configuration for: ${taskApp.configuration.appId}");



        toolbar=findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        bottomNav = findViewById(R.id.bottomNavigationView);
        navController = Navigation.findNavController(this,  R.id.fragmentContainerView2);
        NavigationUI.setupWithNavController(bottomNav, navController);

    }


}