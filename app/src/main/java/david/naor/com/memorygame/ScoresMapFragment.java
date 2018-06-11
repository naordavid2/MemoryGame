package david.naor.com.memorygame;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import david.naor.com.memorygame.Data.Score;

public class ScoresMapFragment extends Fragment implements OnMapReadyCallback{
    private ArrayList<Score> data;
    private GameSelectActivity myActivity;
    private GoogleMap mMap;
    private View myView;
    private MapView mapView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myView =   inflater.inflate(R.layout.scores_map, container,false);
        myActivity = (GameSelectActivity) getActivity();
        data = myActivity.getScores();
        return myView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = myView.findViewById(R.id.scores_map_view);
        if(mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        for (int i = 0; i < data.size(); i++){
            Score score = data.get(i);
            LatLng marker = new LatLng(score.getLocationX(), score.getLocationY());
            mMap.addMarker(new MarkerOptions().position(marker).title(score.getName() + " - Score: " + score.getScore()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));
        }
    }
}
