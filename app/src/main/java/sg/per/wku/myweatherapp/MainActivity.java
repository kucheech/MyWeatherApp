package sg.per.wku.myweatherapp;


import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.pwittchen.swipe.library.Swipe;
import com.github.pwittchen.swipe.library.SwipeListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.google.android.gms.location.LocationServices.*;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private ProgressBar progressBar;
    private TextView textViewCurrentlyHeader;
    private TextView textViewCurrentlyData;
    private TextView textViewHourlyHeader;
    private TextView textViewHourlyData;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 111;
    private double lat, lon;
    private WebView webViewCurrently;
    private WebView webViewHourly;
    private Swipe swipe;
    private int currentHour = 0;
    ArrayList<DarkSkyData> darkSkyDataHourlyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewCurrentlyHeader = (TextView) findViewById(R.id.textViewCurrentlyHeader);
        textViewCurrentlyData = (TextView) findViewById(R.id.textViewCurrentlyData);
        textViewHourlyHeader = (TextView) findViewById(R.id.textViewHourlyHeader);
        textViewHourlyData = (TextView) findViewById(R.id.textViewHourlyData);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webViewCurrently = (WebView) findViewById(R.id.webViewCurrently);
        webViewHourly = (WebView) findViewById(R.id.webViewHourly);

        webViewCurrently.getSettings().setJavaScriptEnabled(true);
        webViewHourly.getSettings().setJavaScriptEnabled(true);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(API)
                    .build();
        }


        ((TextView) findViewById(R.id.textViewAPILink))
                .setMovementMethod(LinkMovementMethod.getInstance());
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        retrieveLocationInformation();
        getWeatherFromAPI();
    }


    private boolean retrieveLocationInformation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Weather API requires your latitude and longitude information", Toast.LENGTH_SHORT).show();
            }

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            return false;
        }

        mLastLocation = FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lon = mLastLocation.getLongitude();
            return true;
        } else {
            return false;
        }
    }

    private void getWeatherFromAPI() {
        if (retrieveLocationInformation()) {
            String url = "https://api.forecast.io/forecast/" + getString(R.string.apikey) + "/" + lat + "," + lon;
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Gson gson = new Gson();
                                JSONObject jsonObjectCurrently = response.getJSONObject("currently");
                                final DarkSkyData currently = gson.fromJson(jsonObjectCurrently.toString(), DarkSkyData.class);
                                textViewCurrentlyHeader.setText("Currently (" + getTimeString(currently.getTime()) + ") : " + currently.getSummary());
                                textViewCurrentlyData.setText(currently.toString());
                                webViewCurrently.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public void onPageFinished(WebView view, String url) {
                                        super.onPageFinished(view, url);
                                        String jsFunctionCall1 = String.format("javascript:setIcon('%s')", currently.getIcon());

                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                            webViewCurrently.evaluateJavascript(jsFunctionCall1, null);
                                        } else {
                                            webViewCurrently.loadUrl(jsFunctionCall1);
                                        }
                                    }
                                });
                                webViewCurrently.loadUrl("file:///android_asset/wv1.html");

                                JSONObject jsonObjectHourly = response.getJSONObject("hourly");
                                String summary = jsonObjectHourly.getString("summary");
                                String icon = jsonObjectHourly.getString("icon");
                                JSONArray jsonArrayData = jsonObjectHourly.getJSONArray("data");
                                darkSkyDataHourlyData = new ArrayList<DarkSkyData>();
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    darkSkyDataHourlyData.add(gson.fromJson(jsonArrayData.get(i).toString(), DarkSkyData.class));
                                }
                                final DarkSkyData darkSkyDataHour = darkSkyDataHourlyData.get(currentHour);
                                textViewHourlyHeader.setText("Hourly (" + getTimeString2(darkSkyDataHour.getTime()) + ") : " + darkSkyDataHour.getSummary());
                                textViewHourlyData.setText(darkSkyDataHour.toString());
                                webViewHourly.setWebViewClient(new WebViewClient() {
                                    @Override
                                    public void onPageFinished(WebView view, String url) {
                                        super.onPageFinished(view, url);
                                        String jsFunctionCall = String.format("javascript:setIcon('%s')", darkSkyDataHour.getIcon());

                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                            webViewHourly.evaluateJavascript(jsFunctionCall, null);
                                        } else {
                                            webViewHourly.loadUrl(jsFunctionCall);
                                        }
                                    }
                                });
                                webViewHourly.loadUrl("file:///android_asset/wv2.html");

                                swipe = new Swipe();
                                swipe.addListener(new SwipeListener() {
                                    @Override
                                    public void onSwipingLeft(MotionEvent event) {

                                    }

                                    @Override
                                    public void onSwipedLeft(MotionEvent event) {
                                        //next
                                        int len = darkSkyDataHourlyData.size();
                                        currentHour = (currentHour + 1) % len;
                                        updateHourlyDataDisplay();
                                    }

                                    @Override
                                    public void onSwipingRight(MotionEvent event) {

                                    }

                                    @Override
                                    public void onSwipedRight(MotionEvent event) {
                                        //previous
                                        int len = darkSkyDataHourlyData.size();
                                        currentHour = (currentHour - 1 + len) % len;
                                        updateHourlyDataDisplay();
                                    }

                                    @Override
                                    public void onSwipingUp(MotionEvent event) {

                                    }

                                    @Override
                                    public void onSwipedUp(MotionEvent event) {

                                    }

                                    @Override
                                    public void onSwipingDown(MotionEvent event) {

                                    }

                                    @Override
                                    public void onSwipedDown(MotionEvent event) {

                                    }
                                });

                                progressBar.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO Auto-generated method stub
                            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });

            progressBar.setVisibility(View.VISIBLE);
            MyVolleySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        } else {
            Toast.makeText(this, "Could not retrieve Location information", Toast.LENGTH_LONG).show();
        }


    }

    private void updateHourlyDataDisplay() {
        DarkSkyData darkSkyDataHour = darkSkyDataHourlyData.get(currentHour);
        textViewHourlyHeader.setText("Hourly (" + getTimeString2(darkSkyDataHour.getTime()) + ") : " + darkSkyDataHour.getSummary());
        textViewHourlyData.setText(darkSkyDataHour.toString());

        String jsFunctionCall = String.format("javascript:setIcon('%s')", darkSkyDataHour.getIcon());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webViewHourly.evaluateJavascript(jsFunctionCall, null);
        } else {
            webViewHourly.loadUrl(jsFunctionCall);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    retrieveLocationInformation();
                    getWeatherFromAPI();
                } else {
                    Toast.makeText(this, "Please provide location permission in order for the app to be of service", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override public boolean dispatchTouchEvent(MotionEvent event) {
        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    private String getTimeString(long v) {
        Date date = new Date(v*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        return formatter.format(date);
    }


    private String getTimeString2(long v) {
        Date date = new Date(v*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh a");
        return formatter.format(date);
    }
}

