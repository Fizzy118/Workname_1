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

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.SyncConfiguration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragUstawienia#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragUstawienia extends Fragment {
    private io.realm.mongodb.User user;
    private Button button;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragUstawienia() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragUstawienia.
     */
    // TODO: Rename and change types and number of parameters
    public static FragUstawienia newInstance(String param1, String param2) {
        FragUstawienia fragment = new FragUstawienia();
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