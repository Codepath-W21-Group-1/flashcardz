package com.example.flashcardz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class FlashcardsActivity extends AppCompatActivity {
    public static final String TAG = "FlashcardsActivity.java";
    private static final int REQUEST_CODE_FLASHCARD = 20;
    private static final int CODE_EDIT_FLASHCARD = 30;

    RecyclerView rvFlashcards;
    FlashcardsAdapter adapter;
    ArrayList<Flashcard> allFlashcards;
    String objectId;
    String setName;
    FloatingActionButton fabCreateFlashcard;
    CoordinatorLayout coordinatorLayout;
    RecyclerViewAdapterFlashcards mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_flashcards);
        coordinatorLayout = findViewById(R.id.coordinatorLayoutF);

        rvFlashcards = findViewById(R.id.rvFlashcards);
        fabCreateFlashcard = findViewById(R.id.fabCreateFlashcard);



        FlashcardsAdapter.OnLongClickListener onLongClickListener = new FlashcardsAdapter.OnLongClickListener() {
            @Override
            public void onFlashcardLongClicked(int position, String frontText, String backText, String objectId) {
                Intent i = new Intent(FlashcardsActivity.this, FlashcardEditActivity.class);
                i.putExtra("frontText", frontText);
                i.putExtra("backText", backText);
                i.putExtra("position", position);
                i.putExtra("objectId", objectId);

                startActivityForResult(i, CODE_EDIT_FLASHCARD);
            }
        };

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                objectId = null;
                setName = null;
            } else {
                objectId = extras.getString("objectId");
                setName = extras.getString("setName");
            }
        } else {
            objectId = (String) savedInstanceState.getSerializable("objectId");
            setName = (String) savedInstanceState.getSerializable("setName");
        }

        fabCreateFlashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAddFlashcardActivity();
            }
        });


        allFlashcards = new ArrayList<>();
        adapter = new FlashcardsAdapter(this, allFlashcards, onLongClickListener);

        mAdapter = new RecyclerViewAdapterFlashcards(allFlashcards);

        rvFlashcards.setLayoutManager(new LinearLayoutManager(this));

        rvFlashcards.setAdapter(adapter);


        queryFlashcards();
        enableSwipeToDeleteAndUndo();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final Flashcard flashcard = mAdapter.getData().get(position);

                removeFlashcard(position);
                mAdapter.removeItem(position);
                adapter.notifyDataSetChanged();

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mAdapter.restoreItem(flashcard, position);
                        restoreFlashcard(flashcard);
                        rvFlashcards.scrollToPosition(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rvFlashcards);
    }



    private void removeFlashcard(int position) {
        String objectId = mAdapter.getData().get(position).getObjectId();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Flashcard");
        query.getInBackground(objectId, (object, e) -> {
            if (e == null) {
                // Deletes the fetched ParseObject from the database
                object.deleteInBackground(e2 -> {
                    if(e2==null){
                        Toast.makeText(this, "Delete Successful", Toast.LENGTH_SHORT).show();
                    }else{
                        //Something went wrong while deleting the Object
                        Toast.makeText(this, "Error: "+e2.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                //Something went wrong while retrieving the Object
                Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void restoreFlashcard(Flashcard tempFlashcard) {
        Flashcard flashcard = new Flashcard();
        flashcard.setFrontText(tempFlashcard.getFrontText());
        flashcard.setBackText(tempFlashcard.getBackText());
        flashcard.setSetObjectId(tempFlashcard.getSetObjectId());
        flashcard.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "ERROR while saving", e);
                    Toast.makeText(FlashcardsActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful!!");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i(TAG, "ASDF");
        if (requestCode == REQUEST_CODE_FLASHCARD && resultCode == RESULT_OK){
//            Set set = Parcels.unwrap(data.getParcelableExtra("set"));
            String frontText = data.getStringExtra("frontText");
            String backText = data.getStringExtra("backText");
            String objectId = data.getStringExtra("objectId");
            Flashcard flashcard = new Flashcard();
            flashcard.setFrontText(frontText);
            flashcard.setBackText(backText);
            flashcard.setObjectId(objectId);
            allFlashcards.add(flashcard);
            adapter.notifyDataSetChanged();
        }
        if (requestCode == CODE_EDIT_FLASHCARD && resultCode == RESULT_OK) {
            String frontText = data.getStringExtra("frontText");
            String backText = data.getStringExtra("backText");
            int position = data.getExtras().getInt("position");

            allFlashcards.get(position).setFrontText(frontText);
            allFlashcards.get(position).setBackText(backText);

            adapter.notifyItemChanged(position);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected void queryFlashcards() {
        ParseQuery<Flashcard> query = ParseQuery.getQuery(Flashcard.class);
        query.include(Flashcard.KEY_SET);
        query.whereEqualTo(Flashcard.KEY_SET, objectId);
//        query.setLimit(20);
//        query.addAscendingOrder(Set.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Flashcard>() {
            @Override
            public void done(List<Flashcard> flashcards, ParseException e) {

                if (e != null){
                    Log.e(TAG, "Issue with getting Flashcards",e);
                    return;
                }

//                for (Set set: sets){
//                    Log.i(TAG, "Set Name: " + set.getSetName());
//                }
                allFlashcards.addAll(flashcards);
                adapter.notifyDataSetChanged();
                Log.i(TAG, "query");
            }
        });
        
    }

    private void goAddFlashcardActivity() {
        Intent i = new Intent(this, AddFlashcardActivity.class);
        i.putExtra("setObjectId", objectId);
        startActivityForResult(i, REQUEST_CODE_FLASHCARD);
    }
}
