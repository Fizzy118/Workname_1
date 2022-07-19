package com.Piotrk_Kielak.Workname_1;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import org.bson.Document;


import java.util.Arrays;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;

public class ZadActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;
    private User user;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = MainActivity.myApp.currentUser();
        //jesli nikt nie jest zalogowany, zacznij od logowania
        if (user == null){
            Log.v("onCreate", "zadActivity user pusty");
            Intent intent = new Intent(ZadActivity.this, LogActivity.class);
            this.startActivity(intent);
        }

        setContentView(R.layout.activity_zad);
        button = findViewById(R.id.button_send_text);
        editText = findViewById(R.id.editTextTextMultiLine_task);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().isEmpty()){
                    Toast.makeText(ZadActivity.this, "Pole jest puste", Toast.LENGTH_LONG).show();
                }else {
                    Log.v("onCreate", "onClick ");
                    sendTask();
                    finish();
                }
            }
        });
    }



    private void sendTask() {
        Intent intent = getIntent();
        Functions functionsManager = MainActivity.myApp.getFunctions(user);
        Log.v("sendTask", "Wysłano zadanie " + intent.getStringExtra("message_key_number"));
        List<String> myList = Arrays.asList(intent.getStringExtra("message_key_number"),editText.getText().toString());
        functionsManager.callFunctionAsync("newTask", myList, Document.class, (App.Callback) result -> {
            if (result.isSuccess()) {
                Log.v("sendTask", "Wysłano zadanie " + (Document) result.get());
                Toast.makeText(ZadActivity.this, "Wysłano przypomnienie.", Toast.LENGTH_LONG).show();
            } else {
                Log.v("sendTask", "nie wysłano zadania " + result.get());
                Toast.makeText(ZadActivity.this, "Wystąpił błąd: " + result.getError(), Toast.LENGTH_LONG).show();
            }

        });
    }
}