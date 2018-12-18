package com.example.firnazluztian.flashcardsforkids;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setTitle("welcome " + getName());
    }

    private String getName() {
        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        return name;
    }

    public void onClickStart(View view) {
        Intent intent = new Intent(this, CardActivity.class);
        intent.putExtra("name",getName());
        startActivity(intent);
    }

    public void onClickScore(View view) {
        Intent intent = new Intent(this, ListDBActivity.class);
        startActivity(intent);
    }
}
