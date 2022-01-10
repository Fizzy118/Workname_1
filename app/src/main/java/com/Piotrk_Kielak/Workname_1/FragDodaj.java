package com.Piotrk_Kielak.Workname_1;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.bson.Document;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;
import io.realm.mongodb.sync.SyncConfiguration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragDodaj#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragDodaj extends Fragment {

    private Realm userRealm;
    private Realm projectRealm;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private User user = null;
    private Button button;
    private ImageButton button2;
    private EditText editText;
    private String partition;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragDodaj() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragDodaj.
     */
    // TODO: Rename and change types and number of parameters
    public static FragDodaj newInstance(String param1, String param2) {
        FragDodaj fragment = new FragDodaj();
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
        user = MainActivity.myApp.currentUser();
        //jesli nikt nie jest zalogowany, zacznij od logowania
        if (user == null){
            Intent intent = new Intent(getContext(), LogActivity.class);
            this.startActivity(intent);

        }
        else{

            Log.i(user.getId(),"wiad");
//            Realm.getInstanceAsync(SyncConfiguration.Builder(user!!,partition), new Realm.Callback{
//                @Override
//                       public void onSuccess(Realm realm){
//                    this.projectRealm = realm;
//                    setUpRecycledView(realm, user, partition)
//                }
//            })
        }
    }

//    @Override
//    protected void onStop(){
//        super.onStop();
//        user.run{
//            projectRealm.close();
//        }
//}

//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//       // recyclerView.;
//        projectRealm.close();
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_frag_dodaj,container,false);
        button= v.findViewById(R.id.button23);
        editText=v.findViewById(R.id.textinputedittext);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Zaproszenia.class);
                startActivity(intent);

            }
        });
        button2 = v.findViewById(R.id.imageButton);
        button2.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Functions functionsManager =MainActivity.myApp.getFunctions(user);
                List<String> myList = Arrays.asList(editText.getText().toString());
                functionsManager.callFunctionAsync("DodajOsobe", myList,Document.class, (App.Callback) result -> {
                  if(result.isSuccess()){
                        Log.v("TAG()", "dodano"+ (Document)result.get());
                    }
                    else{
                        Log.v("TAG()", "niedodano"+ result.getError());
                      Toast.makeText(getContext(), "Zły numer", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }));
        return v;
    }
}