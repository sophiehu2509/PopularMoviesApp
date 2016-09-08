package com.example.yehu.practice1;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import jp.wasabeef.recyclerview.animators.ScaleInAnimator;
//import android.widget.Toolbar;

/**
 * Created by yehu on 7/25/16.
 */
public class ActivityRecyclerItemAnimations extends ActionBarActivity {
    private EditText mInput;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private AdapterRecyclerItemAnimation mAdapter;

    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_item_animation);
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mInput = (EditText)findViewById(R.id.text_input);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerAnimatedItems);
        mAdapter = new AdapterRecyclerItemAnimation(this);

        //ScaleInAnimator animator = new ScaleInAnimator();
        DefaultItemAnimator animator = new DefaultItemAnimator();

        animator.setAddDuration(2000);
        animator.setRemoveDuration(2000);

        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void addItem(View view){
        L.t(this, "Click me!");
        if (mInput.getText() != null){
            String text = mInput.getText().toString();
            if (text != null && text.trim().length() > 0){
                mAdapter.addItem(mInput.getText().toString());
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_activity_recycler_item_animations, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id ==  R.id.action_settings){
            return true;
        }
        if (android.R.id.home == id){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
