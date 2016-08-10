package com.example.alex.activityfinder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alex.activityfinder.adapter.ActivityViewAdapter;
import com.example.alex.activityfinder.model.ActivityData;
import com.example.alex.activityfinder.swipecard.SwipeFlingAdapterView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import model.User;
import com.example.alex.activityfinder.adapter.ViewHolder;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //define a firebase object
    private static final String FIREBASE_URL = "https://project-6933251902387239369.firebaseio.com/";
    private static final String FIREBASE_URL_PIC = "https://project-6933251902387239369.firebaseio.com/pics";
    //define the constant code of taking picture action


    private static int  REQUEST_IMAGE_CAPTURE = 101;

    //new
    public static ActivityViewAdapter adapter;
    public static ViewHolder viewHolder;

    private List<ActivityData> ActivityList;

    public static void removeBackground() {
        viewHolder.background.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();

    }

    private Firebase firebaseRef;
    private Uri mCurrentPhotoUri;

    private SwipeFlingAdapterView flingContainer;


    private ImageView mThumbnailPreview;
    private ImageView mLoadView;

    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set android context to firebase
        Firebase.setAndroidContext(this);

        Intent intent = getIntent();

        currentUser = intent.getStringExtra("USER_EMAIL");

        ActivityList = new ArrayList<ActivityData>();
        previewFirebaseStoredImage();

        flingContainer = (SwipeFlingAdapterView)findViewById(R.id.frame);
        adapter = new ActivityViewAdapter(this, ActivityList);
        flingContainer.setAdapter(adapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                ActivityList.remove(0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                ActivityList.remove(0);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();

                //firebaseRef = new Firebase(FIREBASE_URL);

                //User u = new User(1, "", "Stark");
                //firebaseRef.push().setValue(u);
                takePhoto();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //old code
        //mThumbnailPreview = (ImageView)findViewById(R.id.imageView1);
        //mLoadView = (ImageView)findViewById(R.id.imageView2);
        // previewFirebaseStoredImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                previewCapturedImage();
                storeImageToFirebase();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }

    }

    private void previewFirebaseStoredImage(){
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
                    ActivityList.add(data);
                }


                /*
                System.out.println("There are " + dataSnapshot.getChildrenCount() + " activities");

                ActivityData data;

                for (DataSnapshot activitySnapshot: dataSnapshot.getChildren()){



                    String key = activitySnapshot.getKey();

                    data = activitySnapshot.child(key).getValue(ActivityData.class);

                    int a = 100;

                   // activitySnapshot.get

                    /*String base64Image = (String)picSnapshot.getValue(String.class);
                    if(base64Image == null){
                        return;
                    }
                    byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);

                    ActivityData data = new ActivityData();
                    data.setImage(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                    data.setDescription("Testing");
                    //bitmapList.add(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                    ActivityList.add(data);*/
               // }


                //ActivityData data = new ActivityData();
                //data.setImage(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                //data.setDescription("Testing");
                //bitmapList.add(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
               // ActivityList.add(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void storeImageToFirebase() {

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 8; // shrink it down otherwise we will use stupid amounts of memory

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoUri.getPath(), options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] bytes = baos.toByteArray();

        String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

        // we finally have our base64 string version of the image, save it.
        firebaseRef = new Firebase(FIREBASE_URL);
        /*old method to save image
        firebaseRef.child("pics").setValue(base64Image);
        */

        Firebase picRef =  firebaseRef.child("pics");
        Firebase newPicRef = picRef.push();

        newPicRef.setValue(base64Image);

        System.out.println("Stored image with length: " + bytes.length);

        Firebase testRef = firebaseRef.child("test").child("moreTest");

        testRef.setValue("THis is a test");

    }


    private void takePhoto() {
        File placeholderFile = ImageFactory.newFile();
        mCurrentPhotoUri = Uri.fromFile(placeholderFile);
        //if (!mPhotoTaker.takePhoto(placeholderFile)) {
          //  displayPhotoError();
        //}
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(placeholderFile));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }


    private void previewCapturedImage() {

        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger

        // images
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoUri.getPath(), options);

        //comment this for the new version
        //mThumbnailPreview.setImageBitmap(bitmap);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent intent = new Intent(MainActivity.this, createActivity_Activity.class);
            intent.putExtra("USER_EMAIL", currentUser);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }  else if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
