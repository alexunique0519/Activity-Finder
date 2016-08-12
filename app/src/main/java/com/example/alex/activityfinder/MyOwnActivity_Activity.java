package com.example.alex.activityfinder;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.example.alex.activityfinder.adapter.MyRecyclerViewAdapter;
import com.example.alex.activityfinder.model.ActivityData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyOwnActivity_Activity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickCallback{

    private List<ActivityData> myActivityList;
    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter adapter;
    private ProgressBar progressBar;
    private Firebase firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_own_activity_);

        //display the backbutton in actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myActivityList = new ArrayList<ActivityData>();

        //set android context to firebase
        Firebase.setAndroidContext(this);

        LoadFirebaseStoredActivityData();

        //get the recyclerView instance
        mRecyclerView = (RecyclerView) findViewById(R.id.activity_list);
        //set layoutManage to recView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //instantiate an recyclerViewAdapter object
        adapter = new MyRecyclerViewAdapter(this, myActivityList);
        //attach the adapter to the recyclerView

        mRecyclerView.setAdapter(adapter);
        //setItemClickCall functions for this adapter
        adapter.setItemClickCallback(this);



    }

    private void LoadFirebaseStoredActivityData(){
        firebaseRef = new Firebase("https://project-6933251902387239369.firebaseio.com/Activities");
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //get all the activity data from firebase
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    ActivityData data = child.getValue(ActivityData.class);

                    String base64Image = data.getImageString();
                    if(base64Image == null){
                        return;
                    }
                    //generate the image using the imageString
                    byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
                    data.setImage(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                    myActivityList.add(data);
                }
                /*if(adapter != null){
                    adapter.setListData(myActivityList);
                    adapter.notifyDataSetChanged();
                }*/
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent upIntent = NavUtils.getParentActivityIntent(this);

            startActivity(upIntent);
            finish();
        }

        return true;
    }

    @Override
    public void onItemClick(int pos) {

    }
}
