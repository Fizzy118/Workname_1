package com.Piotrk_Kielak.Workname_1.Model;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.Piotrk_Kielak.Workname_1.MainActivity;
import com.Piotrk_Kielak.Workname_1.MapsActivity;
import com.Piotrk_Kielak.Workname_1.R;
import com.Piotrk_Kielak.Workname_1.ZadActivity;

import org.bson.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.functions.Functions;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Document> list;

    double latitude;
    double longitude;

    public RecyclerViewAdapter(Context context, List<Document> list) {
        Log.i("TAG", "Created RealmRecyclerViewAdapter for "
                + list.size() + " items.");
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_base, parent, false);
        return new RecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("onbindviewholder", "called");
        holder.textName.setText(list.get(position).get("nick",String.class));

        int p = holder.getAdapterPosition();
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser(list.get(p).get("name",String.class),context);
            }
        });

        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap(list.get(p).get("name",String.class));
            }
        });

        holder.tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ZadActivity.class);
                intent.putExtra("message_key_number", list.get(p).get("name",String.class));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName;
        public ImageButton delete, map, tasks;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.person_name);
            delete = itemView.findViewById(R.id.delete_button);
            tasks = itemView.findViewById(R.id.task_button);
            map = itemView.findViewById(R.id.map_button);
        }
    }


    public void deleteUser(String name, Context context){
        Functions functionsManager = MainActivity.myApp.getFunctions(MainActivity.myApp.currentUser());
        functionsManager.callFunctionAsync("deletePerson", Collections.singletonList(name), Document.class, (App.Callback) result -> {
            if (result.isSuccess()) {
                Log.v("deleteUser", "Usunięto " + (Document) result.get());
                Toast.makeText(context, "Usunięto", Toast.LENGTH_LONG).show();
            } else {
                Log.v("deleteUser", "Nie udało się usunąć " + result.get());
                Toast.makeText(context, "Wystąpił błąd: " + result.getError(), Toast.LENGTH_LONG).show();
            }

        });
    }

    public void showMap(String name){

        Collections.singletonList(name);
        Functions functionsManager = MainActivity.myApp.getFunctions(MainActivity.myApp.currentUser());
        functionsManager.callFunctionAsync("getLocalization",Collections.singletonList(name), ArrayList.class, (App.Callback) result -> {
            if (result.isSuccess()) {
                Log.v("showMap", "pobrano lokalizacje z realm " + (ArrayList) result.get());
                ArrayList loc = (ArrayList) result.get();
                latitude = (double) loc.get(0);
                longitude = (double) loc.get(1);

                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("message_key_lat", latitude);
                intent.putExtra("message_key_long", longitude);
                context.startActivity(intent);
            } else {
                Log.v("showMap", "nie pobrano lokalizacje z realm  " + result.get());
            }
        });
    }
}