package de.example.frank.location;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView textview;
    private LocationManager manager;
    private LocationListener listener;
    private String provider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textview = (TextView) findViewById(R.id.textview);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission fehlt");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
           // return;
        }
        // LocationManager-Instanz ermitteln
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Liste mit Namen aller Provider erfragen
        List<String> providers = manager.getAllProviders();

        for (String name : providers) {

            LocationProvider lp = manager.getProvider(name);
            Log.d(TAG,
                    lp.getName() + " --- isProviderEnabled(): "
                            + manager.isProviderEnabled(name));
            Log.d(TAG, "requiresCell(): " + lp.requiresCell());
            Log.d(TAG, "requiresNetwork(): " + lp.requiresNetwork());
            Log.d(TAG, "requiresSatellite(): " + lp.requiresSatellite());
        }
        // Provider mit grober Auflösung
        // und niedrigen Energieverbrauch
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        provider = manager.getBestProvider(criteria, true);
        Log.d(TAG, provider);
        // LocationListener-Objekt erzeugen
        listener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                Log.d(TAG, "onStatusChanged()");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d(TAG, "onProviderEnabled()");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG, "onProviderDisabled()");
            }

            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, "onLocationChanged()");
                if (location != null) {
                    String s = "Breite: " + location.getLatitude()
                            + "\nLänge: " + location.getLongitude();
                    textview.setText(s);
                }
            }

        };
        // Umwandlung von String- in double-Werte
        Location locNuernberg = new Location(LocationManager.GPS_PROVIDER);
        double latitude = Location.convert("49:27");
        locNuernberg.setLatitude(latitude);
        double longitude = Location.convert("11:5");
        locNuernberg.setLongitude(longitude);
        Log.d(TAG, "latitude: " + locNuernberg.getLatitude());
        Log.d(TAG, "longitude: " + locNuernberg.getLongitude());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission fehlt");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2);

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
          //  return;
        }
        Log.d(TAG, "Permission da");
        manager.requestLocationUpdates(provider, 3000, 0.001f,
                listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission fehlt");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    2);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
           // return;
        }
        manager.removeUpdates(listener);
    }
}