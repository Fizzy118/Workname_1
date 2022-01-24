package com.Piotrk_Kielak.Workname_1;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

/**
 * Widok logowania umożliwiający zalogowanie się do istniejącego konta lub przejscie do widoku rejestracji.
 */
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

        // Funkcja logowania.
        zaloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogActivity.this.login();
            }
        });

        // Przejście do widoku rejestracji.
        zarejestruj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed(){
        // Zablokowanie możliwości przejścia wstecz.
        this.moveTaskToBack(true);
    }

    private final void onLoginSuccess(){
        // po udanym zalogowaniu przejść do fragmentu tablica
        this.finish();
        Toast.makeText(this.getBaseContext(), "Zalogowano!", Toast.LENGTH_LONG).show();
        // TODO: zmienic na frag_tablica
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    private final void onLoginFailed(String errormsg) {
       // Log.e(TAG(), errormsg);
        Toast.makeText(this.getBaseContext(), errormsg, Toast.LENGTH_LONG).show();
    }


    // Funkcja umożliwiająca logowanie
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