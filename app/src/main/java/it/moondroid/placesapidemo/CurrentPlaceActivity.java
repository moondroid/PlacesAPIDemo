package it.moondroid.placesapidemo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.List;


public class CurrentPlaceActivity extends GoogleApiClientActivity {

    private static final String TAG = "CurrentPlaceActivity";
    private static final String EXTRA_SELECTED_PLACE = "selected_place";

    private Button mBtnGetCurrentPlace;
    private List<String> placesList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_place);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBtnGetCurrentPlace = (Button)findViewById(R.id.btn_get_current_place);
        mBtnGetCurrentPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                        .getCurrentPlace(mGoogleApiClient, null);
                result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                    @Override
                    public void onResult(PlaceLikelihoodBuffer likelyPlaces) {

                        placesList.clear();
                        for (PlaceLikelihood placeLikelihood : likelyPlaces) {

                            placesList.add(String.format("Place '%s' has likelihood: %g",
                                    placeLikelihood.getPlace().getName(),
                                    placeLikelihood.getLikelihood()));

                        }

                        adapter.notifyDataSetChanged();
                        likelyPlaces.release();
                    }
                });
            }
        });

        ListView listView = (ListView)findViewById(R.id.list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                placesList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_SELECTED_PLACE, adapter.getItem(i));
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    public static String getSelectedPlace(Intent i){
        return i.getStringExtra(EXTRA_SELECTED_PLACE);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Connected to Google Play services!
        mBtnGetCurrentPlace.setEnabled(true);

    }

    @Override
    public void onConnectionSuspended(int cause) {
        mBtnGetCurrentPlace.setEnabled(false);

    }
}
