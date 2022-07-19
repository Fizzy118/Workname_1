package com.Piotrk_Kielak.Workname_1.Model;

import android.content.Context;
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
import com.Piotrk_Kielak.Workname_1.R;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.List;

import io.realm.mongodb.App;
import io.realm.mongodb.functions.Functions;

public class RecyclerViewAdapterTasks extends RecyclerView.Adapter<RecyclerViewAdapterTasks.ViewHolder>{
    private Context context;
    private List<Document> list;


    public RecyclerViewAdapterTasks(Context context, List<Document> list) {
        Log.i("RecyclerViewAdapterTasks", "Created RealmRecyclerViewAdapterTask for "
                + list.size() + " tasks.");
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterTasks.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new RecyclerViewAdapterTasks.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterTasks.ViewHolder holder, int position) {
        Log.d("onbindviewholder", "called");
        holder.textName.setText(list.get(position).get("description",String.class));
        holder.textMaster.setText(list.get(position).get("master",String.class));
        int p = holder.getAdapterPosition();
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(list.get(p).get("_id", ObjectId.class),context);
            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textMaster;
        public ImageButton delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMaster = itemView.findViewById(R.id.textView_TaskMaster);
            textName = itemView.findViewById(R.id.textView_oneTask);
            delete = itemView.findViewById(R.id.delete_button_task);
        }
    }


    public void deleteTask(ObjectId id, Context context){
        Functions functionsManager = MainActivity.myApp.getFunctions(MainActivity.myApp.currentUser());
        functionsManager.callFunctionAsync("deleteTask", Collections.singletonList(id), Document.class, (App.Callback) result -> {
            if (result.isSuccess()) {
                Log.v("deleteUser", "Usunięto " + (Document) result.get());
                Toast.makeText(context, "Usunięto", Toast.LENGTH_LONG).show();
            } else {
                Log.v("deleteUser", "Nie udało się usunąć " + result.get());
                Toast.makeText(context, "Wystąpił błąd: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
