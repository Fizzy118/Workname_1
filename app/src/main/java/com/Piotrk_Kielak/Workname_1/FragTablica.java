package com.Piotrk_Kielak.Workname_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.mongodb.User;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Piotrk_Kielak.Workname_1.Model.Opieka;
import com.Piotrk_Kielak.Workname_1.Model.OpiekaAdapter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.mongodb.sync.SyncConfiguration;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragTablica#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragTablica extends Fragment {

    private User user;
    private Realm userRealm=null;
    private RecyclerView recyclerView;
    private OpiekaAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragTablica() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragTablica.
     */
    // TODO: Rename and change types and number of parameters
    public static FragTablica newInstance(String param1, String param2) {
        FragTablica fragment = new FragTablica();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_frag_tablica,container,false);
        recyclerView= v.findViewById(R.id.button23);
        return v;
    }

    @Override
    public void onStart(){
        super.onStart();
        this.user = MainActivity.myApp.currentUser();
        //jesli nikt nie jest zalogowany, zacznij od logowania
        if (this.user == null){
            Intent intent = new Intent(getContext(), LogActivity.class);
            this.startActivity(intent);
        }
        else{
            Log.e("pep", "blad 3");
            StringBuilder b= new StringBuilder().append("user=");
            //blad
        SyncConfiguration config = new SyncConfiguration.Builder(this.user, b.append(this.user.getId()).toString()).build();

        Realm.getInstanceAsync(config, new Realm.Callback() {
            @Override
            public void onSuccess(Realm realm) {
                FragTablica.this.userRealm = realm;
                Log.e("pep", "blad 4");
                FragTablica.this.setUpRecyclerView(FragTablica.this.getOpieka(realm));
                Log.e("pep", "blad 5");
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