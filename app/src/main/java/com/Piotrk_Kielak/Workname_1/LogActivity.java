package com.Piotrk_Kielak.Workname_1;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.App.Callback;
import io.realm.mongodb.App.Result;
import io.realm.mongodb.User;


public class LogActivity extends AppCompatActivity{
    private EditText haslo,numer;
    private Button zaloguj, zarejestruj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        zaloguj=findViewById(R.id.buttonlog1);
        zarejestruj=findViewById(R.id.buttonlog2);

        haslo=findViewById(R.id.hasloll);
        numer=findViewById(R.id.phonelog);

        zaloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogActivity.this.login();
            }
        });

        zarejestruj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });
    }

    //@Override
    public void onBackPressed(){
        // Disable going back to the MainActivity
        this.moveTaskToBack(true);
    }

    private final void onLoginSuccess(){
        // successful login ends this activity, bringing the user back to the project activity
        this.finish();
        Toast.makeText(this.getBaseContext(), "Zalogowano", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getBaseContext(), MainActivity.class); //zmienic na frag_tablice
        startActivity(intent);
    }

    private final void onLoginFailed(String errormsg) {
       // Log.e(TAG(), errormsg);
        Toast.makeText(this.getBaseContext(), errormsg, Toast.LENGTH_LONG).show();
    }



    private void login(){
        if(numer.getText().toString().isEmpty() || haslo.getText().toString().isEmpty()){
            onLoginFailed("Zły numer lub hasło");
            return;
        }
    zaloguj.setEnabled(false);
    zarejestruj.setEnabled(false);

    String nr=this.numer.getText().toString();
    String pas=this.haslo.getText().toString();

        Credentials creds = Credentials.emailPassword(nr, pas);
        MainActivity.myApp.loginAsync(creds, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                zaloguj.setEnabled(true);
                zarejestruj.setEnabled(true);
                if (!result.isSuccess()) {
                    LogActivity.this.onLoginFailed(result.getError().getErrorMessage());
                } else {
                    LogActivity.this.onLoginSuccess();
                }
            }
        });

    }
}