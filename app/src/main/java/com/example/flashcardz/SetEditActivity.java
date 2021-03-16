package com.example.flashcardz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SetEditActivity extends AppCompatActivity {

    public static final String TAG = "EditSetActivity";
    EditText etSetName;
    Button btnSave;
    String objectId;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_edit);


        etSetName = findViewById(R.id.etSetName);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit Set Name");

        etSetName.setText(getIntent().getStringExtra("setName"));
        objectId = getIntent().getStringExtra("objectId");
        position = getIntent().getExtras().getInt("position");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String setName = etSetName.getText().toString();
                if (setName.isEmpty()) {
                    Toast.makeText(SetEditActivity.this, "Set name can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveSet(setName, currentUser, objectId, position);

            }
        });
    }

        private void saveSet(String setName, ParseUser currentUser, String objectId, int position) {
            Log.i(TAG, "Position: " + position);
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Set");

            query.getInBackground(objectId, (object, e) -> {
                if (e == null) {
                    // Update the fields we want to
                    object.put("setName", setName);

                    // All other fields will remain the same
                    object.saveInBackground();

                } else {
                    // something went wrong
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            Intent i = new Intent();
            i.putExtra("setName", setName);
            i.putExtra("position", position);
            setResult(RESULT_OK, i);
            finish();

//            Set set = new Set();
//            set.setSetName(setName);
//            set.setUser(currentUser);
//            set.saveInBackground(new SaveCallback() {
//                @Override
//                public void done(ParseException e) {
//                    if (e != null){
//                        Log.e(TAG, "ERROR while saving", e);
//                        Toast.makeText(SetEditActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
//                    }
//                    Log.i(TAG, "Post save was successful!!");
//                    Intent i = new Intent();
//                    i.putExtra("setName", setName);
//                    setResult(RESULT_OK, i);
////                goMainActivity();
//                    finish();
//                }
//            });
        }
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}