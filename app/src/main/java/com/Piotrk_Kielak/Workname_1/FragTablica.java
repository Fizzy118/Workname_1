package com.Piotrk_Kielak.Workname_1;

import android.content.Intent;
import android.os.Bundle;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.Piotrk_Kielak.Workname_1.Model.OpiekaAdapter;
import java.util.Collections;
import io.realm.Realm;
import io.realm.mongodb.functions.Functions;
import io.realm.mongodb.sync.SyncConfiguration;

/**
 * Fragment layoutu odpowiadający za wyświetlanie najważniejszych informacji. W zależności od typu użytkownika
 * fragment wyświetla podopiecznych w formie listy lub zadania które podopieczny ma do zrobienia.
 * Po kliknięciu na podopiecznego wyświetli się lista zadań oraz możliwość dodania kolejnego zadania. Po kliknięciu na
 * przycisk mapy wyświetli się fragment mapy z podaną lokalizacją podopiecznego.
 */
// TODO: funkcja wyświetlająca dane z bazy w formie listy wymaga dokończenia. Dla działającej zostaje zintegrowanie tasków oraz mapy.
public class FragTablica extends Fragment {
    private io.realm.mongodb.User user;
    private Realm userRealm=null;
    private RecyclerView recyclerView;
    private OpiekaAdapter adapter;
    private Boolean typ;

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
            Log.e("pep", "blad 3");
            StringBuilder b= new StringBuilder().append("user=");
        SyncConfiguration config = new SyncConfiguration.Builder(user, String.valueOf(b)).build();

//            String realmName = "My Project";
//            RealmConfiguration config = new RealmConfiguration.Builder().name(realmName).build();
//            Realm backgroundThreadRealm = Realm.getInstance(config);
//
        // blad
        Realm.getInstanceAsync((RealmConfiguration) config, new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {
                FragTablica.this.userRealm = realm;
                FragTablica.this.setUpRecyclerView(FragTablica.this.getOpieka(realm));//????
            }
        });
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
        this.recyclerView.setAdapter((RecyclerView.Adapter) null);
    }

    private RealmList getOpieka(Realm realm){
        RealmResults syncedUsers = realm.where(com.Piotrk_Kielak.Workname_1.Model.User.class).sort("id").findAll();
        com.Piotrk_Kielak.Workname_1.Model.User syncedUser = (com.Piotrk_Kielak.Workname_1.Model.User) syncedUsers.get(0);

        if(syncedUser!=null) {
            return syncedUser.getPolaczenie();
        }
        else{
            Log.e("pep", "blad 5");
            return syncedUser.getPolaczenie();
        }
    }

    private void setUpRecyclerView(RealmList opiekaList){
    this.adapter=new OpiekaAdapter(opiekaList,this.adapter.getUser());
    this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    this.recyclerView.setAdapter(this.adapter);
    this.recyclerView.setHasFixedSize(true);
    this.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),1));
    }
}