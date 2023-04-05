package com.example.sensorapp;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;


import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;


public class MainActivity extends AppCompatActivity implements LocationListener {
    MapView map = null;
    IMapController mapController = null;
    private LocationManager locationManager;
    private String bestProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance()
                .load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));


        setContentView(R.layout.activity_main);

        initLocation();




        map = (MapView) findViewById(R.id.map);

        mapController = map.getController();
        map.setTileSource(TileSourceFactory.MAPNIK);
        GeoPoint centerPoint = new GeoPoint(35.658531702121714, 139.54329084890188);
        mapController.setCenter(centerPoint);
        map.setMultiTouchControls(true);
        mapController.setZoom(18.0);
        map.setMultiTouchControls(true);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_settings) {
            Log.d("menu", "settings");
            return true;
        } else if (itemId == R.id.action_home) {
            Log.d("menu", "home");
            return true;
        } else if (itemId == R.id.action_search) {
            Log.d("menu", "search");
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart(){
        super.onStart();
        locationStart();
    }
    
    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // パーミッションの許可を取得する
            ActivityCompat.requestPermissions(this,
                    new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, 1000);
        }
    }
    private void initLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // 詳細設定
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setSpeedRequired(false);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        bestProvider = locationManager.getBestProvider(criteria, true);
    }

    private void locationStart(){
        checkPermission();
        locationManager.requestLocationUpdates(bestProvider, 3000, 3, this);
    }

    public void onResume() {
        super.onResume();
        if (map != null) {
            map.onResume();
        }
    }

    public void onPause() {
        super.onPause();
        if (map != null) {
            map.onPause();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // 緯度の表示
        TextView textView1 = findViewById(R.id.textLatitude);
        double lat = location.getLatitude();
        //textView1.setText((int) lat);

        // 経度の表示
        TextView textView2 = findViewById(R.id.textLongitude);
        double lon = location.getLongitude();
        //textView2.setText((int)lon);

        Marker marker = new Marker(map);
        GeoPoint point = new GeoPoint(lat, lon);
        marker.setPosition(point);
        map.getOverlays().add(marker);
        Drawable icon = getDrawable(R.drawable.baseline_local_airport_24);
        marker.setIcon(icon);
    }
}