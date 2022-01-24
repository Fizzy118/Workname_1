package com.Piotrk_Kielak.Workname_1;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import io.realm.mongodb.App;
import io.realm.mongodb.User;


/**
 * Fragment layoutu zawierający ustawienia oraz przycisk do wylogowywania się.
 */
public class FragUstawienia extends Fragment {
    private io.realm.mongodb.User user;
    private Button button;

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

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_frag_ustawienia,container,false);
        button= v.findViewById(R.id.buttonwyloguj);

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
}