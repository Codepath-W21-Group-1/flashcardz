package com.example.flashcardz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SET = 15;
    public static final int CODE_EDIT_SET = 25;
    Button btnSet;
    public static final String TAG = "MainActivity.java";
    RecyclerView rvSets;
    RecyclerViewAdapterSets mAdapter;
    ArrayList<Set> allSets;
    SetsAdapter adapter;
    FloatingActionButton fabCreateSet;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        coordinatorLayout = findViewById(R.id.coordinatorLayoutS);


        btnSet = findViewById(R.id.btnSet);
        fabCreateSet = findViewById(R.id.fabCreateSet);

        rvSets = findViewById(R.id.rvSets);

        fabCreateSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAddSetActivity();
            }
        });

        SetsAdapter.OnClickListener onClickListener = new SetsAdapter.OnClickListener() {
            @Override
            public void onSetClicked(String objectId, String setName) {
                Log.d("MainActivity", "Object Id: " + objectId);
                // create new activity
                Intent i = new Intent(MainActivity.this, FlashcardsActivity.class);
                //pass data to be edited

                i.putExtra("objectId", objectId);
                i.putExtra("setName", setName);
//                i.putExtra(KEY_ITEM_TEXT, items.get(position));
//                i.putExtra(KEY_ITEM_POSITION, position);
                //display activity
//                startActivityForResult(i, EDIT_TEXT_CODE);
                startActivity(i);

            }


        };

        SetsAdapter.OnLongClickListener onLongClickListener = new SetsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position, String objectId, String setName) {
                Intent i = new Intent(MainActivity.this, SetEditActivity.class);
                //pass data to be edited
                i.putExtra("objectId", objectId);
                i.putExtra("setName", setName);
                i.putExtra("position", position);

                startActivityForResult(i,CODE_EDIT_SET);

            }
        };

        allSets = new ArrayList<>();
        adapter = new SetsAdapter(this, allSets, onClickListener, onLongClickListener);

        mAdapter = new RecyclerViewAdapterSets(allSets);


        rvSets.setLayoutManager(new LinearLayoutManager(this));
        rvSets.setAdapter(adapter);

        querySets();
        enableSwipeToDeleteAndUndo();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final Set set = mAdapter.getData().get(position);

                removeSet(position);
                mAdapter.removeItem(position);
                adapter.notifyDataSetChanged();

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mAdapter.restoreItem(set, position);
                        restoreSet(set);
                        rvSets.scrollToPosition(position);
                        adapter.notifyDataSetChanged();
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rvSets);
    }



    private void removeSet(int position) {
        String objectId = mAdapter.getData().get(position).getObjectId();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Set");
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

    private void restoreSet(Set tempSet) {
        Set set = new Set();
        set.setSetName(tempSet.getSetName());
        set.setUser(tempSet.getUser());
        set.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "ERROR while saving", e);
                    Toast.makeText(MainActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful!!");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_SET && resultCode == RESULT_OK){
            String setName = data.getStringExtra("setName");
            Set set = new Set();
            set.setSetName(setName);
            set.setUser(ParseUser.getCurrentUser());
            allSets.add(set);
            adapter.notifyDataSetChanged();
        }

        if (requestCode == CODE_EDIT_SET && resultCode == RESULT_OK){
            String setName = data.getStringExtra("setName");

            int position = data.getIntExtra("position", 1);
            Log.i(TAG, "IN MAIN, Position: " + position);
            allSets.get(position).setSetName(setName);

            adapter.notifyItemChanged(position);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.profile){
            // Compose icon has been selected
            // navigate to compose activity
            Intent intent = new Intent(this, ProfileActivity.class);
//            startActivityForResult(intent, REQUEST_CODE);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void querySets() {
        ParseQuery<Set> query = ParseQuery.getQuery(Set.class);
        query.include(Set.KEY_USER);
        query.whereEqualTo(Set.KEY_USER, ParseUser.getCurrentUser());
//        query.setLimit(20);
//        query.addAscendingOrder(Set.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Set>() {
            @Override
            public void done(List<Set> sets, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issure with getting Sets",e);
                    return;
                }

//                for (Set set: sets){
//                    Log.i(TAG, "Set Name: " + set.getSetName());
//                }
                allSets.addAll(sets);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void goAddSetActivity() {
        Intent i = new Intent(this, AddSetActivity.class);
        startActivityForResult(i, REQUEST_CODE_SET);
    }


}