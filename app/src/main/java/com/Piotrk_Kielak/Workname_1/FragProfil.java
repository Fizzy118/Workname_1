package com.Piotrk_Kielak.Workname_1;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.bson.Document;

import java.util.Arrays;
import java.util.List;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;

/**
 * Fragment layoutu umożliwiający edytowanie swoich danych osobowych przypisanych do konta.
 * Zawiera pola tekstowe dla pseudonimu, hasła i numeru telefonu, oraz odpowiadające im przyciski.
 */
public class FragProfil extends Fragment {

    private User user = null;
    private Realm userRealm;
    private EditText numer, nick, haslo;
    private ImageButton buttonNumer, buttonHaslo, buttonNick;

    public FragProfil() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();
        // sprawdza czy uzytkownik jest zalogowany, jeżeli nie to przekierowuje go do ekranu logowania.
        this.user = MainActivity.myApp.currentUser();
        if (this.user == null){
            Intent intent = new Intent(getContext(), LogActivity.class);
            this.startActivity(intent);
        }
        else{
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_frag_profil,container,false);
        //button= v.findViewById(R.id.button23);
        numer=v.findViewById(R.id.textNumeredit);
        nick=v.findViewById(R.id.textNickEdit);
        haslo=v.findViewById(R.id.textHasloedit);
        buttonNick=v.findViewById(R.id.updateNick);
        buttonNumer=v.findViewById(R.id.updateNurmer);
        buttonHaslo=v.findViewById(R.id.updateHaslo);
        //Functions functionsManager =MainActivity.myApp.getFunctions(user);

        // Funkcja odpowiadajaca za zmiane pseudonium uzytkownika
        buttonNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // sprawdzenie czy pole tekstowe nie jest puste.
                if (nick.getText().toString().trim().length() > 0) {
                    List<String> myList = Arrays.asList(nick.getText().toString());
                    Functions functionsManager =MainActivity.myApp.getFunctions(user);

                    functionsManager.callFunctionAsync("changeNick", myList, Document.class, (App.Callback) result -> {
                        if (result.isSuccess()) {
                            Log.v("TAG()", "Nazwa została zmieniona" + (Document) result.get());
                            Toast.makeText(getContext(), "Nazwa została zmieniona", Toast.LENGTH_LONG).show();
                        } else {
                            Log.v("TAG()", "Nie udało się zminić nazwy" + result.getError());
                            Toast.makeText(getContext(), "Wystąpił błąd ", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Pole 'Pseudonim' jest puste", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Funkcja odpowiadajaca za zmiane hasła uzytkownika.
        // TODO: utworzyć widok umożliwiający wprowadzenie adresu email na który zostanie wysłane resetowanie hasła.
        buttonHaslo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sprawdzenie czy pole tekstowe nie jest puste.
                if (haslo.getText().toString().trim().length() > 0) {
                    MainActivity.myApp.getEmailPassword().callResetPasswordFunction("21372137",haslo.getText().toString());
                }
                else {
                    Toast.makeText(getContext(), "Pole 'hasło' jest puste", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Funkcja odpowiadajaca za zmiane numeru telefonu uzytkownika.
        buttonNumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // sprawdzenie czy pole tekstowe nie jest puste.
                if (numer.getText().toString().trim().length() > 0){
                    Functions functionsManager =MainActivity.myApp.getFunctions(user);
                    List<String> myList = Arrays.asList(numer.getText().toString());

                    functionsManager.callFunctionAsync("changeNumber", myList, Document.class, (App.Callback) result -> {
                        if (result.isSuccess()) {
                            Log.v("TAG()", "Nazwa została zmieniona" + (Document) result.get());
                            Toast.makeText(getContext(), "Nazwa została zmieniona", Toast.LENGTH_LONG).show();
                        } else {
                            Log.v("TAG()", "Nie udało się zminić nazwy" + result.getError());
                            Toast.makeText(getContext(), "Wystąpił błąd ", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Pole 'numer telefonu' jest puste", Toast.LENGTH_LONG).show();
                }
            }
        });
        return v;
    }
}