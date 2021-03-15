package com.example.flashcardz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FlashcardEditActivity extends AppCompatActivity {

    EditText etFrontText;
    EditText etBackText;
    Button btnSave;

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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();

                i.putExtra("frontText", etFrontText.getText().toString());
                i.putExtra("backText", etBackText.getText().toString());
                i.putExtra("position", getIntent().getExtras().getInt("position"));

                setResult(RESULT_OK, i);

                finish();
            }
        });
    }
}