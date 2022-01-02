package com.Piotrk_Kielak.Workname_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.realm.mongodb.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragDodaj#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragDodaj extends Fragment {

    private User user;
    private Button button;
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
//    @Override
//    public void onStart(){
//        super.onStart();
//
//        this.user = MainActivity.myApp.currentUser();
//
//        if (this.user == null){
//            Intent intent = new Intent(getContext(), LogActivity.class);
//            this.startActivity(intent);
//
//        }
//        else{
//
//        }
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_frag_dodaj,container,false);
        button= v.findViewById(R.id.button23);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Zaproszenia.class);
                startActivity(intent);
            }
        });
        return v;
    }
}