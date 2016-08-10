package com.example.alex.activityfinder;

import android.app.DatePickerDialog;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alex.activityfinder.model.ActivityData;
import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class createActivity_Activity extends AppCompatActivity {

    private ImageView imageView;
    private final static int DEFAULT_SIZE = 384;
    private static int REQUEST_IMAGE_CAPTURE = 101;
    private Uri mCurrentPhotoUri;

    //define a firebase object
    private static final String FIREBASE_URL = "https://project-6933251902387239369.firebaseio.com/";
    //define the firebase reference
    private Firebase firebaseRef;

    private ActivityData activityData;

    //the object for the date
    private EditText dateText;

    //the object for the time
    private EditText timeText;

    //the object for activity name
    private EditText activityNameText;

    //the object for the description
    private EditText descriptionText;

    //define the valud to store the date and time
    private int Year, Month, Day, Hour, Minute;

    //store the status of hiding location or not
    private boolean isHideLocation;

    //use this string to store the current user email
    private String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_activity_);

        //new the data that will be used when storing in firebase
        activityData = new ActivityData();

        //display the backbutton in actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //set android context to firebase
        Firebase.setAndroidContext(this);


        Intent intent =getIntent();
        currentUser = intent.getStringExtra("USER_EMAIL");

        activityData.setActivityOwner(currentUser);

        //get editText from the view
        dateText = (EditText)findViewById(R.id.activity_date);
        timeText = (EditText)findViewById(R.id.activity_time);
        descriptionText = (EditText)findViewById(R.id.activity_description);
        activityNameText = (EditText)findViewById(R.id.activity_name);
        isHideLocation = true;
        Year = 0;
        Month = 0;
        Day = 0;
        Hour = 0;
        Minute = 0;

        subscribeImageClickEvent();
        subscribeImageLongClickEvent();
        subscribeSwitchCheckChangeEvent();
        subscribeDateEditTextOnClickEvent();
        subscribeTimeEditTextOnClickEvent();
    }


    //define the subscribeDateEditTextOnClickEvent
    private void subscribeDateEditTextOnClickEvent(){
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                Year = c.get(Calendar.YEAR);
                Month = c.get(Calendar.MONTH);
                Day = c.get(Calendar.DAY_OF_MONTH);

                //Create the date picker dialog
                DatePickerDialog dialog = new DatePickerDialog(createActivity_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateText.setText(year + "-" + (monthOfYear + 1 > 9 ? monthOfYear + 1 : "0" + (monthOfYear + 1)) + "-" + dayOfMonth);

                        Year = year;
                        Month = monthOfYear;
                        Day = dayOfMonth;
                        activityData.setDate( new Date(year - 1900, monthOfYear, dayOfMonth, Hour, Minute));

                    }
                }, Year, Month, Day);

                dialog.show();
            }
        });
    }

    //define the subscribeTimeEditTextOnClickEvent function so that when user finish picking a time,
    //the dateString will be set on the textEdit
    private void subscribeTimeEditTextOnClickEvent(){
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                Hour = c.get(Calendar.HOUR_OF_DAY);
                Minute = c.get(Calendar.MINUTE);

                // Create Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(createActivity_Activity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                timeText.setText((hourOfDay > 9 ? hourOfDay : "0" + hourOfDay )+ ":" + (minute > 9 ? minute : "0" + minute));

                                Hour = hourOfDay;
                                Minute = minute;

                                activityData.setDate( new Date(Year - 1900, Month, Day, Hour, Minute));

                                SimpleDateFormat format = new SimpleDateFormat("MMMM d, yyyy 'at' h:mm a");
                                System.out.println(format.format(activityData.getDate()));


                            }
                        }, Hour, Minute, false);
                timePickerDialog.show();
            }
        });
    }

    private void subscribeSwitchCheckChangeEvent()
    {
        Switch HideSwitch = (Switch) findViewById(R.id.hideLocationSwitch);
        HideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isHideLocation = false;
                    //get the coordinates as well as the location name
                    Location location = getLastKnowLocation();

                    double lat =  location.getLatitude();
                    double lon = location.getLongitude();

                    //set the latitude value
                    activityData.setLatitude((float)(Math.round(lat * 100000d) / 100000d));
                    //set the longitude
                    activityData.setLongitude((float)(Math.round(lon * 100000d) / 100000d));

                    final String[] locationNameArray = getLocationName(activityData.getLatitude(),activityData.getLongitude());


                    //CharSequence colors[] = new CharSequence[] {"red", "green", "blue", "black"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(createActivity_Activity.this);
                    builder.setTitle("Selection your location");
                    builder.setItems(locationNameArray, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String location = locationNameArray[which];

                            int  location1Pos = location.indexOf("-");

                            String location1 = location.substring(0, location1Pos);
                            String location2 = location.substring(location1Pos + 1);

                            activityData.setLocation_line1(location1);
                            activityData.setLocation_line2(location2);
                        }
                    });
                    builder.show();
                }
                else {
                    isHideLocation =true;
                    activityData.setLocation_line1("");
                    activityData.setLocation_line2("");
                }
            }
        });

    }

    //get the possible location name list by input the latitude and longtitude
    private String[] getLocationName(double latitude, double longitude){
        String name = "";

        String[] locationNameArray = null;

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);

            int nSize = listAddresses.size();

            if(null!=listAddresses && nSize>0){

                locationNameArray = new String[nSize];
                String location;
                for(int i=0; i<nSize; i++){

                    location = listAddresses.get(i).getAddressLine(0) + "-" + listAddresses.get(i).getAddressLine(1);

                    locationNameArray[i] = location;
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return locationNameArray;
    }

    private void subscribeImageLongClickEvent(){
        imageView =(ImageView)findViewById(R.id.activity_photo);
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                takePhoto();

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(createActivity_Activity.this);
                builder.setMessage("Do you wanna take another photo?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                return  true;
            };
        });

    }

    private void subscribeImageClickEvent() {
        imageView = (ImageView) findViewById(R.id.activity_photo);
        imageView.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             //if the image is the default image, need to switch to photo taking intent
                                             if (v.getWidth() == DEFAULT_SIZE) {
                                                 takePhoto();
                                             }

                                         }
                                     }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //when user clicks the send button in the action bar
        if (item.getItemId() == R.id.send_activityData) {
            //validate the data
            if(dataValidate()){

                firebaseRef = new Firebase(FIREBASE_URL);

                Firebase ActivityRef =  firebaseRef.child("Activities");
                Firebase newActivityRef = ActivityRef.push();

                newActivityRef.setValue(activityData);
            }
        }
        else if(item.getItemId() == android.R.id.home){
            Intent upIntent = NavUtils.getParentActivityIntent(this);

            startActivity(upIntent);
            finish();
            return true;
        }



        return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                previewCapturedImage();

                activityData.setImageString(convertImageToString());

                //storeImageToFirebase();
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

    private void previewCapturedImage() {

        BitmapFactory.Options options = new BitmapFactory.Options();

        // images
        options.inSampleSize = 4;
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoUri.getPath(), options);

        imageView.setImageBitmap(bitmap);

        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) imageView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        // existing height is ok as is, no need to edit it
        params.height = 600;


        imageView.setLayoutParams(params);

        imageView.requestLayout();

    }


    private String convertImageToString() {

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 6; // shrink it down otherwise we will use stupid amounts of memory

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoUri.getPath(), options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] bytes = baos.toByteArray();

        String base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

        // we finally have our base64 string version of the image, save it.
        //firebaseRef = new Firebase(FIREBASE_URL);
        /*old method to save image
        firebaseRef.child("pics").setValue(base64Image);
        */

       // Firebase picRef = firebaseRef.child("pics");
        //Firebase newPicRef = picRef.push();

        //newPicRef.setValue(base64Image);

        //System.out.println("Stored image with length: " + bytes.length);

        //Firebase testRef = firebaseRef.child("test").child("moreTest");

        //testRef.setValue("THis is a test");

        return base64Image;
    }


    private boolean dataValidate() {
        if(!checkImage()){
            return  false;
        }
        if(!checkActivityName()) {
            return  false;
        }
        if(!checkActivityDate()){
            return  false;
        }
        if(!checkActivityTime()){
            return  false;
        }
        if(!checkActivityDescription()){
            return  false;
        }
        if(!checkLocation()){
            return false;
        }
        return true;
    }

    private boolean checkImage(){
        if (activityData.getImageString() == "" ||  activityData.getImageString() == null){
            return  false;
        }

        return  true;
    }

    private boolean checkActivityName(){
        activityData.setActivityName(activityNameText.getText().toString());
        if(activityData.getActivityName() == "" ||  activityData.getActivityName() == null){
            return  false;
        }
        return  true;
    }

    private boolean checkActivityDate(){
        if(dateText.getText().toString() == "" ){
            return  false;
        }
        return  true;
    }

    private boolean checkActivityTime(){
        if(timeText.getText().toString() == ""){
            return  false;
        }

        return  true;
    }

    private boolean checkActivityDescription(){
        activityData.setDescription(descriptionText.getText().toString());
        if(activityData.getDescription() == "" || activityData.getDescription() == null){
            return  false;
        }

        return true;
    }

    private boolean checkLocation(){
        if(isHideLocation == true){
            return  true;
        }
        else if(activityData.getLocation_line1() != "" && activityData.getLocation_line2() != ""){
            return true;
        }

        return  false;
    }

    private Location getLastKnowLocation() {
        LocationManager myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = myLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return bestLocation;
            }
            Location l = myLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
// Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

}

