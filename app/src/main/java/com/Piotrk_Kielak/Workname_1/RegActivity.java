package com.Piotrk_Kielak.Workname_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.os.Handler;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;

public class RegActivity extends AppCompatActivity {
     RadioGroup radioGroup;
    RadioButton radio1, radio2;
    EditText editTextPhonereg, textpseudonimreg, hasłoreg;
    Button zarejestruj, logowanie;
    private User user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        radio1=findViewById(R.id.radio_one);
        radio2=findViewById(R.id.radio_two);
        radioGroup=findViewById(R.id.radioGroup);
        editTextPhonereg=findViewById(R.id.nrReg);
        textpseudonimreg=findViewById(R.id.nickReg);
        hasłoreg=findViewById(R.id.hasloReg);
        logowanie=findViewById(R.id.buttonreg2);
        zarejestruj=findViewById(R.id.buttonreg1);
        logowanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegActivity.this, LogActivity.class);
                startActivity(intent);
            }
        });

        zarejestruj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegActivity.this.reg();
            }
        });
    }
    public void onBackPressed(){
        // Disable going back to the MainActivity
        this.moveTaskToBack(true);
    }
    private final void onRegFailed(String errormsg) {
        // Log.e(TAG(), errormsg);
        Toast.makeText(this.getBaseContext(), errormsg, Toast.LENGTH_LONG).show();
    }

    //funkcja sprawdza czy radiobutton jest zaznaczony
    public boolean checked(RadioButton r1, RadioButton r2){
        if(r1.isChecked() || r2.isChecked()) {
            return false;
        }
        else
        {
            return true;
        }
    }
    private final void onLoginSuccess(){
        // successful login ends this activity, bringing the user back to the project activity
        RegActivity.this.addDocument();
        this.finish();
        Toast.makeText(this.getBaseContext(), "Zalogowano", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getBaseContext(), MainActivity.class); //zmienic na frag_tablice
        startActivity(intent);
    }

    private final void onLoginFailed(String errormsg) {
        // Log.e(TAG(), errormsg);
        Toast.makeText(this.getBaseContext(), errormsg, Toast.LENGTH_LONG).show();
    }

    private void addDocument(){
        user = MainActivity.myApp.currentUser();
        Functions functionsManager =MainActivity.myApp.getFunctions(user);
        List<String> myList = Arrays.asList(textpseudonimreg.getText().toString());
        if(radio2.isChecked()){
            functionsManager.callFunctionAsync("NowyDokument", myList,Document.class, (App.Callback) result -> {
                if(result.isSuccess()){
                    Log.v("TAG()", "utworzono"+ (Document)result.get());

                }
                else{
                    Log.v("TAG()", "nie utworzono"+ result.getError());
                }
            });
        }
        else {
            functionsManager.callFunctionAsync("NowyDokument2", myList,Document.class, (App.Callback) result -> {
                if(result.isSuccess()){
                    Log.v("TAG()", "utworzono"+ (Document)result.get());

                }
                else{
                    Log.v("TAG()", "nie utworzono"+ result.getError());
                }
            });
        }
    }

    //funkcja rejestrujaca uzytkownika
    private void reg(){

        if(editTextPhonereg.getText().toString().isEmpty() || hasłoreg.getText().toString().isEmpty() || textpseudonimreg.getText().toString().isEmpty() || checked(radio1, radio2)){

            onRegFailed("Zły numer lub hasło");
            return;
        }
        logowanie.setEnabled(false);
        zarejestruj.setEnabled(false);

        String has=this.hasłoreg.getText().toString();
        String num=this.editTextPhonereg.getText().toString();

        MainActivity.myApp.getEmailPassword().registerUserAsync(num, has, new App.Callback<Void>() {
            @Override
            public void onResult(App.Result<Void> result) {
                logowanie.setEnabled(true);
                zarejestruj.setEnabled(true);
                if (!result.isSuccess()) {
                    RegActivity.this.onRegFailed(result.getError().getErrorMessage());
                    Log.e("Reg activity", "Nie udało się zarejestrować");
                    Log.v("TAG()", "nie utworzono"+ result.getError());
                } else {
                    Log.i("Reg activity", "Zarejestrowano");
                    Intent intent = new Intent(getBaseContext(), LogActivity.class);
                    startActivity(intent);
                    Toast.makeText(getBaseContext(),"Zarejestrowano!",Toast.LENGTH_LONG).show();
                    Credentials creds = Credentials.emailPassword(num, has);
                    MainActivity.myApp.loginAsync(creds, new App.Callback<User>() {
                        @Override
                        public void onResult(App.Result<User> result) {
                            if (!result.isSuccess()) {
                                RegActivity.this.onLoginFailed(result.getError().getErrorMessage());
                            } else {
                                RegActivity.this.onLoginSuccess();
                            }
                        }
                    });

                }
            }
        });
    }
}