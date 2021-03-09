package com.example.flashcardz;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FlashcardsAdapter extends RecyclerView.Adapter<FlashcardsAdapter.ViewHolder>{
    public static final String TAG = "FlashcardsAdapter.java";

    private Context context;
    private List<Flashcard> flashcards;

    public FlashcardsAdapter(Context context, List<Flashcard> flashcards) {
        this.context = context;
        this.flashcards = flashcards;
        Log.i(TAG, "flashcard adapter");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_flashcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flashcard flashcard = flashcards.get(position);
        holder.bind(flashcard);
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        flashcards.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Flashcard> list) {
        flashcards.addAll(list);
        notifyDataSetChanged();

    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTerm;
        private TextView tvDescription;

        public ViewHolder (@NonNull View itemView){
            super(itemView);
            tvTerm = itemView.findViewById(R.id.tvFrontText);
            tvDescription = itemView.findViewById(R.id.tvBackText);
        }

        public void bind(Flashcard flashcard){

            // bind the flashcard data to the view elements

            tvTerm.setText(flashcard.getFrontText());
            tvDescription.setText(flashcard.getBackText());
            Log.i(TAG, "after bind");
        }
    }
}
