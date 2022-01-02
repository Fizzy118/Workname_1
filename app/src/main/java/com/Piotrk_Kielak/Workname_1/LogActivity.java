package com.Piotrk_Kielak.Workname_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptIntrinsic;
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

        numer=findViewById(R.id.editTextPhonelog);
        haslo=findViewById(R.id.hasłolog);
        zaloguj=findViewById(R.id.buttonlog1);
        zarejestruj=findViewById(R.id.buttonlog2);

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
    }

    private final void onLoginFailed(String errormsg) {
       // Log.e(TAG(), errormsg);
        Toast.makeText(this.getBaseContext(), errormsg, Toast.LENGTH_LONG).show();
    }


    private void login(){
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