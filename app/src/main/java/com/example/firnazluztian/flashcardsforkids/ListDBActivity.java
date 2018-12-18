package com.example.firnazluztian.flashcardsforkids;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListDBActivity extends AppCompatActivity {

    private static final String TAG = "ListDBActivity";
    DatabaseHelper mDatabaseHelper;
    private ListView mListView;
    private Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_db);
        setTitle("High Scores");

        mListView = (ListView)findViewById(R.id.dbView);
        bt = (Button)findViewById(R.id.resetScore);
        mDatabaseHelper = new DatabaseHelper(this);
        
        populateListView();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper.resetTable();
                populateListView();
            }
        });
    }

    private void populateListView() {
        Cursor data = mDatabaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()) {
            listData.add(data.getString(1));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,listData);
        mListView.setAdapter(adapter);
    }
}
