package com.example.flashcardz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FlashcardEditActivity extends AppCompatActivity {

    EditText etFrontText;
    EditText etBackText;
    Button btnSave;
    String objectId;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_edit);

        etFrontText = findViewById(R.id.etFrontText);
        etBackText = findViewById(R.id.etBackText);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit Flashcard");

        etFrontText.setText(getIntent().getStringExtra("frontText"));
        etBackText.setText(getIntent().getStringExtra("backText"));
        objectId = getIntent().getStringExtra("objectId");
        position = getIntent().getExtras().getInt("position");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String frontText = etFrontText.getText().toString();
                String backText = etBackText.getText().toString();

                if (frontText.isEmpty() || backText.isEmpty()) {
                    Toast.makeText(FlashcardEditActivity.this, "Text can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveFlashcard(frontText, backText, objectId);
            }
        });
    }

    private void saveFlashcard(String frontText, String backText, String objectId) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Flashcard");

        query.getInBackground(objectId, (object, e) -> {
            if (e == null) {
                // Update the fields we want to
                object.put("frontText", frontText);
                object.put("backText", backText);

                // All other fields will remain the same
                object.saveInBackground();

            } else {
                // something went wrong
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Intent i = new Intent();
        i.putExtra("frontText", frontText);
        i.putExtra("backText", backText);
        i.putExtra("position", position);
        setResult(RESULT_OK, i);
        finish();
    }
}