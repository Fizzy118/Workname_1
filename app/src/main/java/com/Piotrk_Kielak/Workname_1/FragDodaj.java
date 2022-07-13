package com.Piotrk_Kielak.Workname_1;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import org.bson.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;


/**
 * Fragment layoutu odpowiedzialny za dodawanie użytkowników. Znajduje się w nim pole tekstowe umożliwiające wprowadzenie
 * numeru telefonu osoby którą chcemy dodać oraz przycisk dodaj.
 */
public class FragDodaj extends Fragment {

    private User user = null;
    private Button button;
    private ImageButton button2;
    private EditText editText;
    private Boolean typ;
    ArrayList arrayList;
    private ArrayList<com.Piotrk_Kielak.Workname_1.Model.User> pep;
    private Realm realm;

    public FragDodaj() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart(){
        super.onStart();
        user = MainActivity.myApp.currentUser();
        //jesli nikt nie jest zalogowany, zacznij od logowania
        if (user == null){
            Intent intent = new Intent(getContext(), LogActivity.class);
            this.startActivity(intent);
        }
        else{
            //funkcja sprawdza jakiego jakiego typu to konto
            Functions functionsManager = MainActivity.myApp.getFunctions(user);
            List<String> myList = Arrays.asList(editText.getText().toString());
            functionsManager.callFunctionAsync("getTyp", myList, Boolean.class, (App.Callback) result -> {
                if (result.isSuccess()) {
                    Log.v("TAG(funkcja getTyp)", "typ: " + (Boolean) result.get());
                    typ=(Boolean) result.get();
                } else {
                    Log.v("TAG(funkcja getTyp)", "Błąd " + result.getError());
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout
        View v = inflater.inflate(R.layout.fragment_frag_dodaj,container,false);
        button= v.findViewById(R.id.button23);
        editText=v.findViewById(R.id.textinputedittext);
        ///////////////////////////////////////////////////////////////////////////////test
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Functions functionsManager = MainActivity.myApp.getFunctions(user);
                List<String> myList = Arrays.asList("000111");
               functionsManager.callFunctionAsync("getLatitude", myList, Double.class, (App.Callback) result -> {
            if (result.isSuccess()) {
                Log.v("TAG()", "dodano kogos " + (Double) result.get());
                Toast.makeText(getContext(), "Dodano użytkownika", Toast.LENGTH_LONG).show();
            } else {
                Log.v("TAG(1)", "niedodano kogos " + result.get());
                Toast.makeText(getContext(), "Wystąpił błąd: " + result.getError(), Toast.LENGTH_LONG).show();
            }

        });
            }
        });
        ///////////////////////////////////////////////////////////////////////////////

        //funkcja dodająca uzytkownika
        button2 = v.findViewById(R.id.imageButton);
        button2.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Functions functionsManager = MainActivity.myApp.getFunctions(user);
                List<String> myList = Arrays.asList(editText.getText().toString());
                //sprawdza czy nie wprowadzono pustego stringa
                if (editText.getText().toString().trim().length() > 0) {
                    //jezeli uzytkownikiem wywolujacym funkcje jest podopieczny
                    if (!typ) {
                        Log.v("TAG()", "dodaje kogos ");
                        functionsManager.callFunctionAsync("addPerson", myList, Document.class, (App.Callback) result -> {
                            if (result.isSuccess()) {
                                Log.v("TAG()", "dodano kogos " + (Document) result.get());
                                Toast.makeText(getContext(), "Dodano użytkownika", Toast.LENGTH_LONG).show();
                            } else {
                                Log.v("TAG()", "niedodano kogos " + result.getError());
                                Toast.makeText(getContext(), "Wystąpił błąd: " + result.getError(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    //jezeli uzytkownikiem wywolujacym funkcje jest opiekun
                    else {
                        Log.v("TAG()", "dodaje ciebie");
                        functionsManager.callFunctionAsync("addYourself", myList, Document.class, (App.Callback) result -> {
                            if (result.isSuccess()) {
                                Log.v("TAG()", "dodano ciebie" + (Document) result.get());
                                Toast.makeText(getContext(), "Dodano użytkownika", Toast.LENGTH_LONG).show();
                            } else {
                                Log.v("TAG()", "niedodano ciebie" + result.getError());
                                Toast.makeText(getContext(), "Wystąpił błąd: " + result.getError(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }else {
                    Toast.makeText(getContext(), "Pole 'Numer telefonu' jest puste", Toast.LENGTH_LONG).show();
                }
            }
        }));
        return v;
    }
}