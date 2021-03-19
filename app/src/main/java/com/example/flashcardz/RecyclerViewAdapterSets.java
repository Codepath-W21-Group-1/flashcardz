package com.example.flashcardz;

//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterSets extends RecyclerView.Adapter<RecyclerViewAdapterSets.MyViewHolder> {

    private ArrayList<Set> allSets;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private Button setName;
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            setName = itemView.findViewById(R.id.btnSet);
        }
    }

    public RecyclerViewAdapterSets(ArrayList<Set> allSets) {
        this.allSets = allSets;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.setName.setText(allSets.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return allSets.size();
    }


    public void removeItem(int position) {
        allSets.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Set set, int position) {
        allSets.add(position, set);
        notifyItemInserted(position);
    }

    public ArrayList<Set> getData() {
        return allSets;
    }
}
