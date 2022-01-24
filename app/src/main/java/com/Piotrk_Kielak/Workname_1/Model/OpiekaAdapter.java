package com.Piotrk_Kielak.Workname_1.Model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import com.Piotrk_Kielak.Workname_1.R;
import com.Piotrk_Kielak.Workname_1.ZadActivity;

/**
 * Adapter do wyświetlania podopiecznych w formie listy.
 */
// TODO: poprawić.
public class OpiekaAdapter extends RealmRecyclerViewAdapter<Opieka, OpiekaAdapter.OpiekaViewHolder> {
    public ViewGroup parent;
    private User user;

    public OpiekaAdapter( RealmList data, User user) {
        super((OrderedRealmCollection)data, true);
        this.user = user;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ViewGroup getParent() {
        return parent;
    }

    public void setParent(ViewGroup parent) {
        this.parent = parent;
    }

    @Override
    public OpiekaViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        this.parent=parent;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.zadanie_activity, parent, false);

        return new OpiekaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OpiekaViewHolder holder, int position) {
    Opieka obj = this.getItem(position);
    holder.setData(obj);
    holder.getName().setText((CharSequence)(obj != null ? obj.getImie() : null));

//    holder.itemView.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(OpiekaAdapter.this.getParent().getContext(), ZadActivity.class);
//            intent.putExtra("Partytion", obj.getPartycja());
//            intent.putExtra("Name", obj.getPartycja());
//            OpiekaAdapter.this.getParent().getContext().startActivity(intent);
//        }
//    });
    }



    class OpiekaViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView status;
        private Opieka data;
        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public Opieka getData() {
            return data;
        }

        public void setData(Opieka data) {
            this.data = data;
        }

        public TextView getStatus() {
            return status;
        }

        public void setStatus(TextView status) {
            this.status = status;
        }



       public OpiekaViewHolder(View view){
            super(view);
            this.name = view.findViewById(R.id.name);
            this.status = view.findViewById(R.id.status);
            this.data = null;
        }


    }
}
