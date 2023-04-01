package com.example.mapapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // assign callback into variable
        mMap = googleMap;

        // set normal map style
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));

        // add marker and move the camera
        LatLng vokasiUGM = new LatLng(-7.7742371,110.3691362);
        mMap.addMarker(new MarkerOptions().position(vokasiUGM).title("Marker di Herman Yohanes"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vokasiUGM, 17));

        // call setMapOnClick method
        setMapOnClick(mMap);

        // call setOnPoiClicked method
        setPoiClicked(mMap);
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.map_option,menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.normal_map:
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                    return true;

                case R.id.satellite_map:
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    return true;

                case R.id.terrain_map:
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    return true;

                case R.id.hybrid_map:
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }

        }

        // add marker on long click
        private void setMapOnClick(final GoogleMap map){
            map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(@NonNull LatLng latLng) {
                    String text = String.format(Locale.getDefault(),"Lat: %1$.5f, Long: %2.5f", latLng.latitude, latLng.longitude);
                    map.addMarker(new MarkerOptions().position(latLng).snippet(text).title("Dropped pin."));
                }
            });
        }

        private void setPoiClicked(final GoogleMap map) {
            map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
                @Override
                public void onPoiClick(@NonNull PointOfInterest pointOfInterest) {
                    Marker poiMarker = mMap.addMarker(new MarkerOptions().position(pointOfInterest.latLng).title(pointOfInterest.name));
                    poiMarker.showInfoWindow();
                }
            });
        }

        private void enableMyLocation() {
        // check if permission granted or not, if yes: enable, if no: ask, get the result using onRequestPermissionResult method
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }  else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // request code is from above method, when the method ask user, it's also send request code
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }
}