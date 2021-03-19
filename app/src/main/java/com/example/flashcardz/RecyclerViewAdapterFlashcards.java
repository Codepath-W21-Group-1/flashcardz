package com.example.flashcardz;

//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapterFlashcards extends RecyclerView.Adapter<RecyclerViewAdapterFlashcards.MyViewHolder> {

    private ArrayList<Flashcard> allFlashcards;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView frontText;
        private TextView backText;
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            frontText = itemView.findViewById(R.id.tvFrontText);
            backText = itemView.findViewById(R.id.tvBackText);
        }
    }

    public RecyclerViewAdapterFlashcards(ArrayList<Flashcard> allFlashcards) {
        this.allFlashcards = allFlashcards;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_flashcard, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.frontText.setText(allFlashcards.get(position).toString());
        holder.backText.setText(allFlashcards.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return allFlashcards.size();
    }


    public void removeItem(int position) {
        allFlashcards.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Flashcard flashcard, int position) {
        allFlashcards.add(position, flashcard);
        notifyItemInserted(position);
    }

    public ArrayList<Flashcard> getData() {
        return allFlashcards;
    }
}
