package com.pub.pubcustomer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pub.pubcustomer.R;
import com.pub.pubcustomer.entity.PubPlaceNotRegistered;
import com.pub.pubcustomer.utils.PubObjectUtil;

import java.util.List;

/**
 * Created by Fernando Santiago on 18/08/2016.
 */
public class PubPlaceNotRegisteredActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private List<PubPlaceNotRegistered> pubPlaceNotRegisteredUnregistered;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_not_registered);
        setTitle(getResources().getString(R.string.where_are_you));

        pubPlaceNotRegisteredUnregistered = getIntent().getParcelableArrayListExtra("pubPlaceNotRegisteredUnregistered");
        updateListView(pubPlaceNotRegisteredUnregistered);

        mListView = (ListView) findViewById(R.id.listView);
        mListView.setLongClickable(true);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {

                final PubPlaceNotRegistered pubPlaceNotRegistered = pubPlaceNotRegisteredUnregistered.get(arg2);

                StringBuilder sb = new StringBuilder();
                sb.append(PubObjectUtil.ifNull(pubPlaceNotRegistered.getName(),"")).append("\n");
                sb.append(PubObjectUtil.ifNull(pubPlaceNotRegistered.getAddress(),"")).append("\n");
                sb.append(PubObjectUtil.ifNull(pubPlaceNotRegistered.getPhoneNumber(),"")).append("\n");
                sb.append(PubObjectUtil.ifNull(pubPlaceNotRegistered.getWebSiteUri(),"")).append("\n");
                Toast toast = Toast.makeText(PubPlaceNotRegisteredActivity.this, sb, Toast.LENGTH_LONG);
                toast.show();

                return true;
            }
        });
    }

    private void updateListView(List<PubPlaceNotRegistered> placeNotRegistered) {
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new PubPlaceNotRegisteredAdapter(this, placeNotRegistered));
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}