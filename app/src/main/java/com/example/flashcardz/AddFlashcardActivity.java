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

public class AddFlashcardActivity extends AppCompatActivity {

    public static final String TAG = "AddFlashActivity.java";

    EditText etFrontText;
    EditText etBackText;
    Button btnAddFlashcard;
    String objectId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flashcard);

        etFrontText = findViewById(R.id.etFrontText);
        etBackText = findViewById(R.id.etBackText);
        btnAddFlashcard = findViewById(R.id.btnAddFlashcard);

        getSupportActionBar().setTitle("Add Flashcard");

        btnAddFlashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String frontText = etFrontText.getText().toString();
                String backText = etBackText.getText().toString();
                if (frontText.isEmpty() || backText.isEmpty()) {
                    Toast.makeText(AddFlashcardActivity.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (savedInstanceState == null) {
                    Bundle extras = getIntent().getExtras();
                    if(extras == null) {
                        objectId = null;
                    } else {
                        objectId = extras.getString("setObjectId");
                    }
                } else {
                    objectId = (String) savedInstanceState.getSerializable("setObjectId");
                }

                //ParseUser currentUser = ParseUser.getCurrentUser();
                saveFlashcard(frontText, backText, objectId);
            }
        });
    }

    private void saveFlashcard(String frontText, String backText, String objectId) {
       Flashcard flashcard = new Flashcard();
        flashcard.setFrontText(frontText);
        flashcard.setBackText(backText);
        flashcard.setSetObjectId(objectId);
        flashcard.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "ERROR while saving", e);
                    Toast.makeText(AddFlashcardActivity.this, "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post save was successful!!");
                Intent i = new Intent();
                i.putExtra("frontText", flashcard.getFrontText());
                i.putExtra("backText", flashcard.getBackText());
                i.putExtra("objectId", flashcard.getObjectId());

                setResult(RESULT_OK, i);

//                goFlashcardsActivity();
                finish();
            }
        });
    }

    private void goFlashcardsActivity() {
        Intent i = new Intent(this, FlashcardsActivity.class);
        startActivity(i);
    }

}
