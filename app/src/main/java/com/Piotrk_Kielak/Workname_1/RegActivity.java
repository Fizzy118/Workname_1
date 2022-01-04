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

import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;

public class RegActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radio1, radio2;
    EditText editTextPhonereg, textpseudonimreg, hasłoreg;
    Button zarejestruj, logowanie;

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
    public boolean checked(RadioButton r1, RadioButton r2){
        if(r1.isChecked() || r2.isChecked()) {
            return false;
        }
        else
        {
            return true;
        }
    }

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
                } else {
                    Log.i("Reg activity", "Zarejestrowano");
                }
            }
        });
    }
}