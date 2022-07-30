package com.Piotrk_Kielak.Workname_1;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Collections;

import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;


/**
 * Fragment layoutu zawierający ustawienia oraz przycisk do wylogowywania się.
 */
public class FragUstawienia extends Fragment {
    private io.realm.mongodb.User user;
    private Button button;
    private Boolean typ;
    private TextView textView;
    private NumberPicker numberPicker;

    public FragUstawienia() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void onStart(){
        super.onStart();
        this.user = MainActivity.myApp.currentUser();
        //jesli nikt nie jest zalogowany, zacznij od logowania
        if (this.user == null){
            Intent intent = new Intent(getContext(), LogActivity.class);
            this.startActivity(intent);
        }
        else{
            getType();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inicjacja wyglądu fragmentu ustawienia
        View v = inflater.inflate(R.layout.fragment_frag_ustawienia,container,false);
        button= v.findViewById(R.id.buttonwyloguj);
        textView = v.findViewById(R.id.textView_setTimer_ustawienia);
        numberPicker = v.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(20);
        numberPicker.setValue(ReadFromFile("timerFile"));

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            //zapisywanie informacji do pliku
                File path = getContext().getFilesDir();
                try {
                    FileOutputStream writer = new FileOutputStream(new File(path,"timerFile"));
                    writer.write(newVal);
                    writer.close();
                    Log.v("writer", "zapisano do pliku");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        //Funkcja umożliwiająca wylogowywanie
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragUstawienia.this.user.logOutAsync((io.realm.mongodb.App.Callback) (new App.Callback<User>() {
                   @Override
                   public void onResult(App.Result<User> result) {
                       if (!result.isSuccess()){
                           Log.e("fragust", "nie udało się wylogować");
                       }
                       else{
                           FragUstawienia.this.user = null;
                           Log.v("fragust", "Wylogowano");
                           Intent intent = new Intent(getContext(), LogActivity.class);
                           startActivity(intent);
                           Toast.makeText(getContext(),"Wylogowano",Toast.LENGTH_LONG).show();
                       }
                   }
               }));
            }
        });
        return v;
    }

    public int ReadFromFile(String fileName){
        File path = getContext().getFilesDir();
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
    // Funkcja zwracająca typ użytkownika
    public void getType(){
        this.user = MainActivity.myApp.currentUser();
        Functions functionsManager = MainActivity.myApp.getFunctions(user);
        functionsManager.callFunctionAsync("getTyp", Collections.singletonList(""), Boolean.class, (App.Callback) result -> {
            if (result.isSuccess()) {
                Log.v("FragUstawienia", "gettyp: " + (Boolean) result.get());
                typ=(Boolean) result.get();
                if(typ==false) {
                    textView.setVisibility(View.INVISIBLE);
                }else{
                    textView.setText("Okres co jaki atualizowana będzie informacja o lokalizacji.");
                }
            } else {
                Log.v("FragUstawienia", "getType Błąd " + result.getError());
            }
        });

    }
}