package com.Piotrk_Kielak.Workname_1;

import android.content.Intent;
import android.os.Bundle;

import io.realm.mongodb.App;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.Piotrk_Kielak.Workname_1.Model.RecyclerViewAdapter;
import com.Piotrk_Kielak.Workname_1.Model.RecyclerViewAdapterTasks;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import io.realm.Realm;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;

import org.bson.Document;

/**
 * Fragment layoutu odpowiadający za wyświetlanie najważniejszych informacji. W zależności od typu użytkownika
 * fragment wyświetla podopiecznych w formie listy lub zadania które podopieczny ma do zrobienia.
 * Po kliknięciu na podopiecznego wyświetli się lista zadań oraz możliwość dodania kolejnego zadania. Po kliknięciu na
 * przycisk mapy wyświetli się fragment mapy z podaną lokalizacją podopiecznego.
 */
// TODO: funkcja wyświetlająca dane z bazy w formie listy wymaga dokończenia. Dla działającej zostaje zintegrowanie tasków oraz mapy.
public class FragTablica extends Fragment {
    private User user=null;
    private Realm userRealm=Realm.getDefaultInstance();
    private RecyclerView recyclerView;
    private Boolean typ;
    private  ArrayList<Document> user_list;


    public FragTablica() {
        // Required empty public constructor
    }

    // Funkcja zwracająca typ użytkownika
    public Boolean getType(){
        this.user = MainActivity.myApp.currentUser();
        Functions functionsManager = MainActivity.myApp.getFunctions(user);
        functionsManager.callFunctionAsync("getTyp", Collections.singletonList(""), Boolean.class, (App.Callback) result -> {
            if (result.isSuccess()) {
                Log.v("FragTablica", "gettyp: " + (Boolean) result.get());
                typ=(Boolean) result.get();
                        if(typ==false) {
            setUpRecyclerView();
        }else{
            setUpRecyclerView_task();
        }
            } else {
                Log.v("FragTablica", "getType Błąd " + result.getError());
            }
        });
        return  typ;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Realm.deleteRealm(Realm.getDefaultConfiguration());
        Log.v("onCreate", "get on create");
        typ = getType();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.fragment_frag_tablica, container, false);
        recyclerView= v.findViewById(R.id.project_list);
        Log.v("onCreateView", "get on create ");
        return v;
    }


    @Override
    public void onStart(){
        super.onStart();
        Log.v("onStart", "get on create ");
        this.user = MainActivity.myApp.currentUser();
        //jesli nikt nie jest zalogowany, zacznij od logowania
        if (this.user == null){
            Intent intent = new Intent(getContext(), LogActivity.class);
            this.startActivity(intent);
        }else{
            getType();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(this.userRealm != null){
            userRealm.close();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(this.userRealm != null){
            userRealm.close();
        }
        recyclerView.setAdapter(null);
    }


    private void setUpRecyclerView(){
        Log.v("recview", "start ");
        user = MainActivity.myApp.currentUser();
        Functions functionsManager = MainActivity.myApp.getFunctions(user);
        List<Document> myList = new ArrayList<>();
        functionsManager.callFunctionAsync("getList", myList, ArrayList.class, (App.Callback) result -> {
            if (result.isSuccess()) {
                Log.v("setUpRecyclerView", "pobrano liste: " + (ArrayList<Document>) result.get());
                user_list = (ArrayList<Document>) result.get();
            } else {
                Log.v("setUpRecyclerView", "błąd: " + result.get());
            }

            if(user_list==null) {
                Toast.makeText(getContext(), "Nikogo nie dodano.", Toast.LENGTH_LONG).show();
            }
            else{
                    recyclerView.setLayoutManager(
                            new LinearLayoutManager(getActivity().getApplicationContext()));
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), user_list);
                    recyclerView.setAdapter(adapter);
                };
            }
    );
    }

    private void setUpRecyclerView_task(){
        Log.v("recview_task", "start ");
        user = MainActivity.myApp.currentUser();
        Functions functionsManager = MainActivity.myApp.getFunctions(user);
        List<Document> myList = new ArrayList<>();
        functionsManager.callFunctionAsync("getTasks", myList, ArrayList.class, (App.Callback) result -> {
                    if (result.isSuccess()) {
                        Log.v("setUpRecyclerViewTask", "pobrano liste: " + (ArrayList<Document>) result.get());
                        user_list = (ArrayList<Document>) result.get();
                    } else {
                        Log.v("setUpRecyclerViewTask", "błąd: " + result.get());
                    }

                    if(user_list==null) {
                        Toast.makeText(getContext(), "Brak przypomnień.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        recyclerView.setLayoutManager(
                                new LinearLayoutManager(getActivity().getApplicationContext()));
                        RecyclerViewAdapterTasks adapter = new RecyclerViewAdapterTasks(getActivity(), user_list);
                        recyclerView.setAdapter(adapter);
                    };
                }
        );
    }
}