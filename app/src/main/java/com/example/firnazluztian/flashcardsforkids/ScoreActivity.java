package com.example.firnazluztian.flashcardsforkids;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ScoreActivity extends AppCompatActivity {
    private Button addButton, viewButton, delButton;
    private EditText editText;
    DatabaseHelper mDatabaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        addButton = (Button) findViewById(R.id.addButton);
        viewButton = (Button) findViewById(R.id.viewButton);
        delButton = (Button) findViewById(R.id.delButton);
        editText = (EditText) findViewById(R.id.inputText);
        mDatabaseHelper = new DatabaseHelper(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newEntry = editText.getText().toString();
                if (editText.length() != 0) {
                    AddData(newEntry);
                    editText.setText("");
                } else {
                    ToastMessage("Please put something in the text field");
                }
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this, ListDBActivity.class);
                startActivity(intent);

            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper.resetTable();
            }
        });
    }

    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);
        if (insertData) {
            ToastMessage("Data successfully inserted ");
        } else {
            ToastMessage("Something went wrong");
        }
    }

    private void ToastMessage (String message) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
