package com.example.flashcardz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetEditActivity extends AppCompatActivity {

    EditText etSetName;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_edit);

        etSetName = findViewById(R.id.etSetName);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit Set Name");

        etSetName.setText(getIntent().getStringExtra("setName"));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();

                i.putExtra("setName", etSetName.getText().toString());
                i.putExtra("position", getIntent().getExtras().getInt("position"));

                setResult(RESULT_OK, i);

                finish();
            }
        });
    }
}