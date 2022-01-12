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

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragProfil#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragProfil extends Fragment {

    private User user = null;
    private Realm userRealm;
    private EditText numer, nick, haslo;
    private ImageButton buttonNumer, buttonHaslo, buttonNick;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragProfil() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragProfil.
     */
    // TODO: Rename and change types and number of parameters
    public static FragProfil newInstance(String param1, String param2) {
        FragProfil fragment = new FragProfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        this.user = MainActivity.myApp.currentUser();
        if (this.user == null){
            Intent intent = new Intent(getContext(), LogActivity.class);
            this.startActivity(intent);
        }
        else{
        //Log.e("Tag",user.get);
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

        //listner pola zmiany nazwy
        buttonNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nick.getText().toString().trim().length() > 0) {
                    List<String> myList = Arrays.asList(nick.getText().toString());
                    Functions functionsManager =MainActivity.myApp.getFunctions(user);
                    functionsManager.callFunctionAsync("changeNick", myList, String.class, (App.Callback) result -> {
                        if (result.isSuccess()) {
                            Log.v("TAG()", "Nazwa została zmieniona" + (String) result.get());
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

        //listner pola zmiany hasla
        buttonHaslo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (haslo.getText().toString().trim().length() > 0) {

                    MainActivity.myApp.getEmailPassword().callResetPasswordFunction("21372137",haslo.getText().toString());
                }
                else {
                    Toast.makeText(getContext(), "Pole 'hasło' jest puste", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Listner pola zmiany numeru telefonu
        buttonNumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numer.getText().toString().trim().length() > 0){
                    Functions functionsManager =MainActivity.myApp.getFunctions(user);
                    List<String> myList = Arrays.asList(numer.getText().toString());
                    functionsManager.callFunctionAsync("changeNumber", myList, String.class, (App.Callback) result -> {
                        if (result.isSuccess()) {
                            Log.v("TAG()", "Nazwa została zmieniona" + (String) result.get());
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