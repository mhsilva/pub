package com.pub.pubcustomer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.pub.pubcustomer.R;
import com.pub.pubcustomer.entity.PubPlaceNotRegistered;

import java.util.List;

/**
 * Created by Fernando Santiago on 18/08/2016.
 */
public class PubPlaceNotRegisteredActivity extends AppCompatActivity {

    private List<PubPlaceNotRegistered> pubPlaceNotRegisteredUnregistered;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_not_registered);
        setTitle(getResources().getString(R.string.not_affiliated_establishments));

        pubPlaceNotRegisteredUnregistered =  getIntent().getParcelableArrayListExtra("pubPlaceNotRegisteredUnregistered");
        updateListView(pubPlaceNotRegisteredUnregistered);
    }

    private void updateListView(List<PubPlaceNotRegistered> placeNotRegistered) {
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new PubPlaceNotRegisteredAdapter(this, placeNotRegistered));
       // mListView.setOnItemClickListener(this);
    }
}