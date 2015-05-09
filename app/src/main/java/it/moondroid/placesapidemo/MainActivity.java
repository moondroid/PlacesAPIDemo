package it.moondroid.placesapidemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.nispok.snackbar.Snackbar;


public class MainActivity extends GoogleApiClientActivity {

    private static final String TAG = "MainActivity";
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int CURRENT_PLACE_REQUEST = 2;
    private static final int PLACE_AUTOCOMPLETE_REQUEST = 3;

    private Button mBtnPlacePicker, mBtnCurrentPlace, mBtnPlaceAutocomplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBtnPlacePicker = (Button)findViewById(R.id.btn_place_picker);
        mBtnPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    Context context = getApplicationContext();
                    startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        mBtnCurrentPlace = (Button)findViewById(R.id.btn_current_place);
        mBtnCurrentPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, CurrentPlaceActivity.class);
                startActivityForResult(i, CURRENT_PLACE_REQUEST);
            }
        });

        mBtnPlaceAutocomplete = (Button)findViewById(R.id.btn_place_autocomplete);
        mBtnPlaceAutocomplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PlaceAutocompleteActivity.class);
                startActivityForResult(i, PLACE_AUTOCOMPLETE_REQUEST);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Snackbar.with(MainActivity.this)
                        .text(toastMsg)
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                        .show(MainActivity.this);
            }
        }
        if (requestCode == CURRENT_PLACE_REQUEST) {
            if (resultCode == RESULT_OK) {

                String toastMsg = CurrentPlaceActivity.getSelectedPlace(data);
                Snackbar.with(MainActivity.this)
                        .text(toastMsg)
                        .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                        .show(MainActivity.this);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Connected to Google Play services!
        mBtnPlacePicker.setEnabled(true);
        mBtnCurrentPlace.setEnabled(true);
        mBtnPlaceAutocomplete.setEnabled(true);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mBtnPlacePicker.setEnabled(false);
        mBtnCurrentPlace.setEnabled(false);
        mBtnPlaceAutocomplete.setEnabled(false);
    }
}
