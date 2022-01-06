package com.Piotrk_Kielak.Workname_1.Model;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import io.realm.RealmRecyclerViewAdapter;

public class OpiekaAdapter extends RealmRecyclerViewAdapter {
    public ViewGroup parent;
    private User user;

    public ViewGroup getParent() {
        return parent;
    }

    public void setParent(ViewGroup parent) {
        this.parent = parent;
    }

    public OpiekaAdapter onCreateViewHolder(ViewGroup parent, int ViewType){
        return new OpiekaAdapter(itemView);
    }


    //public RecyclerView.ViewHolder
}
