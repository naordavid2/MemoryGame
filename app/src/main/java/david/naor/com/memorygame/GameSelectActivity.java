package david.naor.com.memorygame;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import david.naor.com.memorygame.Data.Score;

public class GameSelectActivity extends FragmentActivity {

    public static final String USER_NAME = "USER_NAME";
    public static final String USER_AGE = "USER_AGE";
    private static final int EASY_LEVEL = 2;
    private static final int MID_LEVEL = 4;
    private static final int HARD_LEVEL = 5;

    private static final int MAP_MODE = 0;
    private static final int TABLE_MODE = 1;

    private static final int MAX_SCORES_TO_DISPLAY = 10;

    private String name;
    private int age;
    private int gameScore;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private Location currLocation;

    private TextView textView_name;
    private TextView textView_age;
    private int scoreViewMode;
    private Button scoresModeBtn;

    private FragmentManager fragmentManager;
    private ScoresTableFragment scoresTableFragment;
    private ScoresMapFragment scoresMapFragment;

    protected ArrayList<Score> scores;
    private DatabaseReference firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_select_with_scores);
        scores = new ArrayList<>();
        initMembersFromIntent();
        initLocation();
        //initScoreList();
        initComponents();
        initDb();
        getScoreListFromDb();
    }

    private void initComponents() {
        String nameText = String.format(getString(R.string.act_game_select_user_name), name);
        textView_name = (TextView) findViewById(R.id.act_game_select_text_view_name);
        textView_name.setText(nameText);

        String ageText = String.format(getString(R.string.act_game_select_user_age), age);
        textView_age = (TextView) findViewById(R.id.act_game_select_text_view_age);
        textView_age.setText(ageText);

        Button easyBtn = (Button) findViewById(R.id.act_game_select_button_easy_level);
        easyBtn.setText(String.format(getString(R.string.act_game_select_easy_level), EASY_LEVEL, EASY_LEVEL));
        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGameByLevel(EASY_LEVEL);
            }
        });

        Button midBtn = (Button) findViewById(R.id.act_game_select_button_mid_level);
        midBtn.setText(String.format(getString(R.string.act_game_select_mid_level), MID_LEVEL, MID_LEVEL));
        midBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGameByLevel(MID_LEVEL);
            }
        });

        Button hardBtn = (Button) findViewById(R.id.act_game_select_button_hard_level);
        hardBtn.setText(String.format(getString(R.string.act_game_select_hard_level), HARD_LEVEL, HARD_LEVEL));
        hardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGameByLevel(HARD_LEVEL);
            }
        });

        scoresModeBtn = (Button) findViewById(R.id.act_game_select_button_scores_mode);
        scoresModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextMode();
                switchScoresFragment();
            }
        });

        scoreViewMode = TABLE_MODE;
        fragmentManager = getSupportFragmentManager();
        switchScoresFragment();


    }

    private void initMembersFromIntent() {
        Bundle intentData = getIntent().getExtras();
        name = intentData.getString(USER_NAME);
        age = intentData.getInt(USER_AGE);
    }

    private void goToGameByLevel(int level) {
        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra(GameActivity.GAME_LEVEL, level);

        //startActivity(intent);
        startActivityForResult(intent, GameActivity.GAME_ACTIVITY_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(getString(R.string.log_tag), "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GameActivity.GAME_ACTIVITY_RESULT){
            if (resultCode == RESULT_OK) {
                Log.e(getString(R.string.log_tag), "onActivityResult: game finished");
                gameScore = data.getExtras().getInt("score");
                gameScore *= age;
                saveScore();
            }
        }
    }

    private void switchScoresFragment() {
        String txt = "";
        switch (scoreViewMode) {
            case MAP_MODE:
                switchToMapMode();
                txt = getString(R.string.act_game_select_scores_mode_table);
                break;
            case TABLE_MODE:
                switchToTableMode();
                txt = getString(R.string.act_game_select_scores_mode_map);
                break;
        }
        scoresModeBtn.setText(txt);
    }

    private void switchToMapMode() {
        if (scoresMapFragment == null) {
            scoresMapFragment = new ScoresMapFragment();
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.act_game_select_fragment_scores_fragment, scoresMapFragment);
        ft.commit();
    }

    private void switchToTableMode() {
        if (scoresTableFragment == null)
            scoresTableFragment = new ScoresTableFragment();

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.act_game_select_fragment_scores_fragment, scoresTableFragment);
        ft.commit();
    }

    private void setNextMode() {
        scoreViewMode++;
        scoreViewMode %= 2;
    }

    private void initScoreList() {

        for (int i = 0; i < MAX_SCORES_TO_DISPLAY; i++)
            scores.add(new Score("Name" + i, 50 + i, -2 + i, 10 + i));
    }

    void getScoreListFromDb(){
        Log.e(getString(R.string.log_tag), "getScoreListFromDb: start");
        Query q = firebaseDatabase.orderByChild("score").limitToLast(10);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(getString(R.string.log_tag), "getScoreListFromDb: onDataChange");
                scores.clear();
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    Score score = child.getValue(Score.class);
                    scores.add(score);
                }
                Collections.reverse(scores);
                notifyDataChange();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initLocation() {
        Log.e(getString(R.string.log_tag), "initLocation: start");
        createLocationRequest();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationCallback();
        startLocationUpdate();
    }

    private void setCurrLocation(Location location){
        this.currLocation = location;
        String locationTxt = currLocation.toString();
        Log.e(getString(R.string.log_tag), "setCurrLocation: " + locationTxt);
        stopLocationUpdates();
    }

    private void startLocationUpdate(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(getString(R.string.log_tag),"NO PERMISSION LOCATION");
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        Log.e(getString(R.string.log_tag), "stopLocationUpdates: location stopped");
    }

    private void createLocationCallback(){
        mLocationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.e(getString(R.string.log_tag), "onLocationResult: start");
                if (locationResult == null || locationResult.getLocations().isEmpty()){
                    String nullOrEmpty = locationResult == null ? "null" : "empty";
                    Log.e(getString(R.string.log_tag), "onLocationResult: locationResult is " + nullOrEmpty);
                    return;
                }
                Location firstLocation = locationResult.getLocations().get(0);
                setCurrLocation(firstLocation);
            }
        };
    }

    private void createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public ArrayList<Score> getScores() {
        return scores;
    }

    private void initDb(){
        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("testScores");
        firebaseDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String prevKey) {
                String newKey = dataSnapshot.getKey();
                Log.e(getString(R.string.log_tag), "onChildAdded: key = " + newKey);
                getScoreListFromDb();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveScore(){
        double x = currLocation != null ? currLocation.getLatitude() : 0;
        double y = currLocation != null ? currLocation.getLatitude() : 0;
        Score score = new Score(name, x, y, gameScore);
        firebaseDatabase.push().setValue(score);

    }

    private void notifyDataChange(){
        if (scoresTableFragment != null)
            scoresTableFragment.notifyOnDataChange();
    }
}
