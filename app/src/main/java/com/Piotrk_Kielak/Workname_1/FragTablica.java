package com.Piotrk_Kielak.Workname_1;

import android.content.Intent;
import android.os.Bundle;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.Piotrk_Kielak.Workname_1.Model.Opieka;
import com.Piotrk_Kielak.Workname_1.Model.OpiekaAdapter;
import com.Piotrk_Kielak.Workname_1.Model.RecyclerViewAdapter;
import com.Piotrk_Kielak.Workname_1.Model.Rodzina;
import com.Piotrk_Kielak.Workname_1.Model.UserAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.User;
import io.realm.mongodb.functions.Functions;
import io.realm.mongodb.sync.SyncConfiguration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.bson.Document;

import io.realm.Realm;

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
    private UserAdapter adapter;
    private Boolean typ;
    private  ArrayList<Document> user_list;


    public FragTablica() {
        // Required empty public constructor
    }

    // Funkcja zwracająca typ użytkownika
    public Boolean getType(){
        Functions functionsManager = MainActivity.myApp.getFunctions(user);
        functionsManager.callFunctionAsync("getTyp", Collections.singletonList(""), Boolean.class, (App.Callback) result -> {
            if (result.isSuccess()) {
                Log.v("TAG()", "typ: " + (Boolean) result.get());
                typ=(Boolean) result.get();
            } else {
                Log.v("TAG()", "Błąd " + result.getError());
            }
        });
        return  typ;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Realm.deleteRealm(Realm.getDefaultConfiguration());

        setUpRecyclerView();
    }
    // TODO: wybór wyświetlonego layoutu
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v;
        typ=false;
        if(typ==false) {
             v = inflater.inflate(R.layout.fragment_frag_tablica, container, false);
        }else{
             v = inflater.inflate(R.layout.fragment_frag_tablica_podopiecznego, container, false);
        }
        recyclerView= v.findViewById(R.id.project_list);
        return v;
    }

    // TODO: do poprawy
    @Override
    public void onStart(){
        super.onStart();
        this.user = MainActivity.myApp.currentUser();
        typ=getType();

        //jesli nikt nie jest zalogowany, zacznij od logowania
        if (this.user == null){
            Intent intent = new Intent(getContext(), LogActivity.class);
            this.startActivity(intent);
        }
        else{
            //funkcja sprawdza jakiego jakiego typu to konto
            Functions functionsManager = MainActivity.myApp.getFunctions(user);
            Log.v("fragtab", "user blad");
            List<String> myList = null;
//            functionsManager.callFunctionAsync("getTyp", myList, Boolean.class, (App.Callback) result -> {
//                if (result.isSuccess()) {
//                    Log.v("fragtab", "typ: " + (Boolean) result.get());
//                    typ=(Boolean) result.get();
//                } else {
//                    Log.v("fragtab", "Błąd " + result.getError());
//                }
//            });
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
        List<com.Piotrk_Kielak.Workname_1.Model.User> myList = new ArrayList<>();
        functionsManager.callFunctionAsync("getList", myList, ArrayList.class, (App.Callback) result -> {
            if (result.isSuccess()) {
                Log.v("setUpRecyclerView", "pobrano liste: " + (ArrayList<com.Piotrk_Kielak.Workname_1.Model.User>) result.get());
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
//        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(new DividerItemDecoration(
//                getActivity().getApplicationContext(),
//                DividerItemDecoration.VERTICAL));
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), user_list);
                    recyclerView.setAdapter(adapter);
                };
            }
    );
    }
}