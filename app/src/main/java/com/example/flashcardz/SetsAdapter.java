package com.example.flashcardz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseFile;

import java.util.List;

public class SetsAdapter extends RecyclerView.Adapter<SetsAdapter.ViewHolder>{
    private Context context;
    private List<Set> sets;

    public SetsAdapter(Context context, List<Set> sets) {
        this.context = context;
        this.sets = sets;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_set, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Set set = sets.get(position);
        holder.bind(set);
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        sets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Set> list) {
        sets.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvSetName;

        public ViewHolder (@NonNull View itemView){
            super(itemView);
            tvSetName = itemView.findViewById(R.id.tvSetName);
        }

        public void bind(Set sets){
            // bind the post data to the view elements
            tvSetName.setText(sets.getSetName());

        }
    }
}
