package seniorproject.caretakers.caretakersapp.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import seniorproject.caretakers.caretakersapp.R;
import seniorproject.caretakers.caretakersapp.data.handlers.AccountHandler;
import seniorproject.caretakers.caretakersapp.data.handlers.EventHandler;
import seniorproject.caretakers.caretakersapp.tempdata.model.Caretaker;
import seniorproject.caretakers.caretakersapp.tempdata.model.Event;
import seniorproject.caretakers.caretakersapp.tempdata.model.Patient;
import seniorproject.caretakers.caretakersapp.ui.actvities.AddEventActivity;
import seniorproject.caretakers.caretakersapp.ui.actvities.LoginActivity;
import seniorproject.caretakers.caretakersapp.ui.actvities.ViewEventActivity;
import seniorproject.caretakers.caretakersapp.ui.views.AddFloatingActionButton;

public class LocationFragment extends Fragment {
    Button mLocationButton;
    TextView text;
    double longitude;
    double latitude;

    MapView mapView;
    GoogleMap map;

    View.OnClickListener mLocationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.test_button:
                    text.setVisibility(View.VISIBLE);
                    text.setText(latitude + ", " + longitude);

                    /*Context context = view.getContext();
                    CharSequence text = "Hello toast!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();*/
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, viewGroup, false);

        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);

        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        try{
            MapsInitializer.initialize(this.getActivity());
        }catch (GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }

        Location testLocation = new Location("");
        testLocation.setLatitude(39.7392138);
        testLocation.setLongitude(-104.9879518);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(testLocation.getLatitude(),testLocation.getLongitude()), 10);
        map.animateCamera(cameraUpdate);

        /*mLocationButton = (Button) rootView.findViewById(R.id.test_button);
        text = (TextView) rootView.findViewById(R.id.test_location);
        ((Button)rootView.findViewById(R.id.test_button)).setOnClickListener(mLocationClickListener);

        Location testLocation = new Location("");
        testLocation.setLatitude(42.293535);
        testLocation.setLongitude(-87.609434);*/


        /*LocationManager locationManager = (LocationManager) rootView.getContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = testLocation.getLongitude();
        latitude = testLocation.getLatitude();*/

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
