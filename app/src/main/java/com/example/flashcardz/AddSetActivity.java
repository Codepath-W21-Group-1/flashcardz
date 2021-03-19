package com.example.flashcardz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

public class AddSetActivity extends AppCompatActivity {

    public static final String TAG = "AddSetActivity.java";

    EditText etSetName;
    Button btnAddSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_set);

        etSetName = findViewById(R.id.etFrontText);
        btnAddSet = findViewById(R.id.btnAddFlashcard);

        getSupportActionBar().setTitle("Add Set");

        // when user is done editing, click save button
        btnAddSet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String setName = etSetName.getText().toString();
                if (setName.isEmpty()){
                    Toast.makeText(AddSetActivity.this, "Set name can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveSet(setName, currentUser);


            }



//            @Override

//            public void onClick(View v) {
//                // create intent which will contain results
//                Intent intent = new Intent();
//                // pass data (from the editing field)
//                intent.putExtra(MainActivity.KEY_ITEM_TEXT, etSetName.getText().toString());
//                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
//                // set the result of intent
//                setResult(RESULT_OK, intent);
//                // finish activity and close screen to go back
//                finish();
//
//            }
        });




    }

    private void saveSet(String setName, ParseUser currentUser) {
        Set set = new Set();
        set.setSetName(setName);
        set.setUser(currentUser);
        set.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "ERROR while saving", e);
                    Toast.makeText(AddSetActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful!!");
                Intent i = new Intent();
                i.putExtra("setName", setName);
                setResult(RESULT_OK, i);
//                goMainActivity();
                finish();
            }
        });
    }



    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
