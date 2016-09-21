package de.example.frank.location;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.security.Permissions;
import java.util.List;
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION = 2;
    private TextView textview;
    private String provider;
    private Button button;
    // These are required for GPS services.
    private static LocationManager manager;
    private static LocationListener listener;
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = (TextView) findViewById(R.id.textview);
        button = (Button) findViewById(R.id.button);
// if (!isPermissionGranted()) {
// return;
// }
// LocationManager-Instanz ermitteln
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
// // Liste mit Namen aller Provider erfragen
// List<String> providers = manager.getAllProviders();
//
// for (String name : providers) {
//
// LocationProvider lp = manager.getProvider(name);
// Log.d(TAG,
// lp.getName() + " --- isProviderEnabled(): "
// + manager.isProviderEnabled(name));
// Log.d(TAG, "requiresCell(): " + lp.requiresCell());
// Log.d(TAG, "requiresNetwork(): " + lp.requiresNetwork());
// Log.d(TAG, "requiresSatellite(): " + lp.requiresSatellite());
// }
// // Provider mit grober Auflösung
// // und niedrigen Energieverbrauch
// Criteria criteria = new Criteria();
// criteria.setAccuracy(Criteria.ACCURACY_COARSE);
// criteria.setPowerRequirement(Criteria.POWER_LOW);
// provider = manager.getBestProvider(criteria, true);
// Log.d(TAG, provider);
// LocationListener-Objekt erzeugen
        listener = new LocationListener() {
            // When turned on or off.
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                Log.d(TAG, "onStatusChanged()");
            }
            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged()");
                if (location != null) {
                    String s = "Breite: " + location.getLatitude()
                            + "\nLänge: " + location.getLongitude() + "\n\n";
                    textview.append(s);
                }
            }
            @Override
            public void onProviderEnabled(String provider) {
                Log.d(TAG, "onProviderEnabled()");
            }
            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG, "onProviderDisabled()");
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        initRights();
// Umwandlung von String- in double-Werte
// Location locNuernberg = new Location(LocationManager.GPS_PROVIDER);
// double latitude = Location.convert("49:27");
// locNuernberg.setLatitude(latitude);
// double longitude = Location.convert("11:5");
// locNuernberg.setLongitude(longitude);
// Log.d(TAG, "latitude: " + locNuernberg.getLatitude());
// Log.d(TAG, "longitude: " + locNuernberg.getLongitude());
    }
    private void initRights() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onCreate request permissions");
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);// 10 => requestCode (any number)
                return;
            } else {
                Log.d(TAG, "onCreate - permissions already granted.");
                configureButton();
            }
            Log.d(TAG, "onCreate SDK fits.");
        } else {
// Rights come from the manifest file and are approved during installation.
            configureButton();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
// if (isPermissionGranted()) {
// manager.requestLocationUpdates(provider, 3000, 0.001f, listener);
// }
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
// if (isPermissionGranted()) {
// manager.removeUpdates(listener);
// }
    }
    // private boolean isPermissionGranted() {
// boolean ret = false;
// Log.d(TAG, "Permission Wert: ACCESS_FINE_LOCATION = " + Manifest.permission.ACCESS_FINE_LOCATION);
// Log.d(TAG, "Permission Wert: ACCESS_COARSE_LOCATION = " + Manifest.permission.ACCESS_COARSE_LOCATION);
// Log.d(TAG, "Permission Wert: PERMISSION_GRANTED = " + PackageManager.PERMISSION_GRANTED);
//
// if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
// Log.d(TAG, "Permission fehlt");
// ActivityCompat.requestPermissions(this,
// new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
// REQUEST_LOCATION);
// ActivityCompat.requestPermissions(this,
// new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
// 1);
//
// // TODO: Consider calling
// // ActivityCompat#requestPermissions
// // here to request the missing permissions, and then overriding
// // public void onRequestPermissionsResult(int requestCode, String[] permissions,
// // int[] grantResults)
// // to handle the case where the user grants the permission. See the documentation
// // for ActivityCompat#requestPermissions for more details.
// // return;
// ret = true;
// }
// Log.d(TAG, "Permission da");
// return ret;
// }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                Log.d(TAG, "onRequestPermissionsResult = 10");
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "grantResults contains PERMISSION_GRANTED");
                    configureButton();
                }
                return;
        }
// super.onRequestPermissionsResult(requestCode, permissions, grantResults);
// if (requestCode == REQUEST_LOCATION){
// if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
// }
// }
    }
    private void configureButton() {
        Log.d(TAG, "configureButton called");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// provider, minTime (refresh in msec), minDistance (>0 min m meters), locationListener
                manager.requestLocationUpdates("gps", 5000, 0, listener);
                Log.d(TAG, "setOnClickListener manager.requestLocationUpdates");
            }
        });
    }
}
