package com.Piotrk_Kielak.Workname_1.Model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

import com.Piotrk_Kielak.Workname_1.FragTablica;
import com.Piotrk_Kielak.Workname_1.R;

import org.bson.types.ObjectId;

public class UserAdapter extends RealmRecyclerViewAdapter<User, UserAdapter.ViewHolder> {

    private ArrayList<User> mNames;
    private Context mContext;

    private boolean inDeletionMode = false;
    private Set<Integer> countersToDelete = new HashSet<>();

//    public UserAdapter(OrderedRealmCollection<User> data) {
//        super(data,true);
//        //setHasStableIds(true);
//        Log.i("TAG", "Created RealmRecyclerViewAdapter for "
//                + getData().size() + " items.");
//    }
public UserAdapter(RealmResults<User> list, Activity context) {
    super(list,true, true);
    //setHasStableIds(true);
    this.mContext = context;
    Log.i("TAG", "Created RealmRecyclerViewAdapter for "
            + getData().size() + " items.");
}


    void enableDeletionMode(boolean enabled) {
        inDeletionMode = enabled;
        if (!enabled) {
            countersToDelete.clear();
        }
        notifyDataSetChanged();
    }

    Set<Integer> getCountersToDelete() {
        return countersToDelete;
    }
        @Override
    public int getItemCount() {
        return 3;
    }
    @Override
    public long getItemId(int index) {
        return getItem(index).get_id();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_base, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final User obj = getItem(position);
       // Log.i("TAG", "Binding view holder: " + obj.getName());
        //holder.data = obj;
        holder.title.setText(obj.getName());
//        holder.data = obj;
//        final ObjectId UserId = obj.getId();
//        //noinspection ConstantConditions
//        holder.title.setText(obj.getCountString());
//        holder.deletedCheckBox.setChecked(countersToDelete.contains(UserId));
//        if (inDeletionMode) {
//            holder.deletedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        countersToDelete.add(UserId);
//                    } else {
//                        countersToDelete.remove(UserId);
//                    }
//                }
//            });
//        } else {
//            holder.deletedCheckBox.setOnCheckedChangeListener(null);
//        }
//        holder.deletedCheckBox.setVisibility(inDeletionMode ? View.VISIBLE : View.GONE);
    }

//    @Override
//    public long getItemId(int index) {
//        //noinspection ConstantConditions
//        return getItem(index).getId();
//    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        CheckBox deletedCheckBox;
        public User data;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.person_name);
           // deletedCheckBox = view.findViewById(R.id.checkBox);
        }
    }

//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.recycleview_base, parent, false);
//        ViewHolder holder = new ViewHolder(view);
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//             //holder.name.setText((CharSequence) mNames.get(position));
//        holder.name.setText(mNames.get(position).getNick());
//    }
//
//    @Override
//    public int getItemCount() {
//        return mNames.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//       TextView name;
//       RelativeLayout parentLayout;
//
//       public ViewHolder(View itemView){
//           super(itemView);
//           name = itemView.findViewById(R.id.person_name);
//           parentLayout = itemView.findViewById(R.id.parent_layout);
//       }
//   }

}
