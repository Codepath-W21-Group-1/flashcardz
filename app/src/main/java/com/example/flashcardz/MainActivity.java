package com.example.flashcardz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnLogout;
    public static final String TAG = "MainActivity.java";
    private RecyclerView rvSets;
    List<Set> allSets;
    SetsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btnLogout);



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                goLoginActivity();
            }
        });

        rvSets = findViewById(R.id.rvSets);
        allSets = new ArrayList<>();
        adapter = new SetsAdapter(this, allSets);

        rvSets.setLayoutManager(new LinearLayoutManager(this));
        rvSets.setAdapter(adapter);

        querySets();


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


}