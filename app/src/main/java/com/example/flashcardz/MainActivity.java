package com.example.flashcardz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btnLogout;
    Button btnSet;
    public static final String TAG = "MainActivity.java";
    RecyclerView rvSets;
    List<Set> allSets;
    SetsAdapter adapter;
    FloatingActionButton fabCreateSet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btnLogout);
        btnSet = findViewById(R.id.btnSet);
        fabCreateSet = findViewById(R.id.fabCreateSet);

        rvSets = findViewById(R.id.rvSets);

//        rvSets.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "rvset on click listener");
//            }
//        });





        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                goLoginActivity();
            }
        });

        fabCreateSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAddSetActivity();
            }
        });

        SetsAdapter.OnClickListener onClickListener = new SetsAdapter.OnClickListener() {

            @Override
            public void onSetClicked(int position) {
                Log.d("MainActivity", "SIngle click at position " + position);
                // create new activity
                Intent i = new Intent(MainActivity.this, FlashcardsActivity.class);
                //pass data to be edited

//                i.putExtra(KEY_ITEM_TEXT, items.get(position));
//                i.putExtra(KEY_ITEM_POSITION, position);
                //display activity
//                startActivityForResult(i, EDIT_TEXT_CODE);
                startActivity(i);



            }
        };

        allSets = new ArrayList<>();
        adapter = new SetsAdapter(this, allSets, onClickListener);

        rvSets.setLayoutManager(new LinearLayoutManager(this));
        rvSets.setAdapter(adapter);

        querySets();

//        btnSet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i(TAG, "SET BUTTON CLICKED");
//            }
//        });





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


    private void goLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    private void goAddSetActivity() {
        Intent i = new Intent(this, AddSetActivity.class);
        startActivity(i);
    }


}