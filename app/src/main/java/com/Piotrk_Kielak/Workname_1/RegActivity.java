package com.Piotrk_Kielak.Workname_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;

public class RegActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    EditText editTextPhonereg, textpseudonimreg, hasłoreg;
    Button buttonreg1, buttonreg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        radioGroup=findViewById(R.id.radioGroup);
        editTextPhonereg=findViewById(R.id.nrReg);
        textpseudonimreg=findViewById(R.id.nickReg);
        hasłoreg=findViewById(R.id.hasłolog);

        buttonreg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            RegActivity.this.reg();
            }
        });
        buttonreg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegActivity.this, LogActivity.class);
                startActivity(intent);
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
    private void reg(){
        buttonreg1.setEnabled(false);
        buttonreg2.setEnabled(false);

        String has=this.hasłoreg.getText().toString();
        String num=this.editTextPhonereg.getText().toString();

        MainActivity.myApp.getEmailPassword().registerUserAsync(num, has, new App.Callback<Void>() {
            @Override
            public void onResult(App.Result<Void> result) {
                buttonreg1.setEnabled(true);
                buttonreg2.setEnabled(true);
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