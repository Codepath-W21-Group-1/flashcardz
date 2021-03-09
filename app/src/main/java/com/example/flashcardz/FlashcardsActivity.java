package com.example.flashcardz;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FlashcardsActivity extends AppCompatActivity {
    public static final String TAG = "FlashcardsActivity.java";

    RecyclerView rvFlashcards;
    FlashcardsAdapter adapter;
    List<Flashcard> allFlashcards;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flashcards);

        rvFlashcards = findViewById(R.id.rvFlashcards);

        allFlashcards = new ArrayList<>();
        adapter = new FlashcardsAdapter(this, allFlashcards);

        rvFlashcards.setAdapter(adapter);

        queryFlashcards();
    }

    protected void queryFlashcards() {
        ParseQuery<Flashcard> query = ParseQuery.getQuery(Flashcard.class);
        query.include(Flashcard.KEY_SET);
//        query.whereEqualTo(Flashcard.KEY_SET, ParseUser.get());
//        query.setLimit(20);
//        query.addAscendingOrder(Set.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Flashcard>() {
            @Override
            public void done(List<Flashcard> flashcards, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issure with getting Flashcards",e);
                    return;
                }

//                for (Set set: sets){
//                    Log.i(TAG, "Set Name: " + set.getSetName());
//                }
                allFlashcards.addAll(flashcards);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
