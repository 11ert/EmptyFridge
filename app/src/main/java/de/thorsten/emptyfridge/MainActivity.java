package de.thorsten.emptyfridge;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import de.thorsten.emptyfridge.model.ShoppingItem;

public class MainActivity extends Activity {
    ListView list;
    TextView name;

    GoogleCloudMessaging gcm;
    String regId;


    ArrayList<HashMap<String, String>> shoppinglist = new ArrayList<HashMap<String, String>>();

    private static String url = "http://10.0.2.2:8080/shopping/rest/shoppingitems";
    //private static String url = "http://shoppinglist-11ert.rhcloud.com/shopping/rest/shoppingitems";

    //JSON Node Names
    private static final String TAG_VER = "version";
    private static final String TAG_NAME = "name";

    JSONArray shoppingItems = null;
    private ShoppingItem newShoppingItem;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        shoppinglist = new ArrayList<HashMap<String, String>>();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
        final EditText inputText = (EditText) findViewById(R.id.editText);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.d("editText Input =", v.getText().toString());
  //                  Toast.makeText(inputText.getContext(), v.getText(), Toast.LENGTH_SHORT).show();
                    newShoppingItem  = new ShoppingItem();
                    newShoppingItem.setName(v.getText().toString());
                    new WriteShoppingItemAsyncTask().execute(url);
                    inputText.getText().clear();
                }
                return false;
            }
        });


//        Btngetdata = (Button) findViewById(R.id.getdata);
//        Btngetdata.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
        new ReadShoppingItemAsyncTask().execute();
//
//            }
//        });

    }

    private class ReadShoppingItemAsyncTask extends AsyncTask<String, String, JSONArray> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ver = (TextView) findViewById(R.id.vers);
            name = (TextView) findViewById(R.id.name);

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONArray doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            Log.d("url: ", "> " + url);
            JSONArray json = jParser.getJSONFromUrl(url);

            return json;
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            pDialog.dismiss();
            try {

                Log.d("json.length=", "json.length = " + json.length());

                for (int i = 0; i < json.length(); i++) {

                    JSONObject c = json.getJSONObject(i);

                    String ver = c.getString(TAG_VER);
                    String name = c.getString(TAG_NAME);

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_VER, ver);
                    map.put(TAG_NAME, name);

                    shoppinglist.add(map);
                    list = (ListView) findViewById(R.id.list);


//                    ListAdapter adapter = new SimpleAdapter(MainActivity.this, shoppinglist,
//                            R.layout.list_v,
//                            new String[]{TAG_VER, TAG_NAME}, new int[]{
//                            R.id.vers, R.id.name});

                    ListAdapter adapter = new SimpleAdapter(MainActivity.this, shoppinglist,
                            R.layout.list_v,
                            new String[]{TAG_NAME}, new int[]{R.id.name});

                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            Toast.makeText(MainActivity.this, "You Clicked at " + shoppinglist.get(+position).get("name"), Toast.LENGTH_SHORT).show();

                        }
                        //test

                    });

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private class WriteShoppingItemAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            ShoppingItemService shoppingItemService = new ShoppingItemService(newShoppingItem);
            return shoppingItemService.POST(urls[0], newShoppingItem);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
            shoppinglist.clear();
            //list.getAdapter().notify();
            new ReadShoppingItemAsyncTask().execute();


        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}


