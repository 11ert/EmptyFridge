package de.thorsten.emptyfridge;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

public class MainActivity extends Activity {
    ListView list;
    TextView ver;
    TextView name;
    TextView api;
    Button Btngetdata;
    ArrayList<HashMap<String, String>> shoppinglist = new ArrayList<HashMap<String, String>>();

    //URL to get JSON Array
    //private static String url = "http://api.learn2crack.com/android/jsonos/";
    private static String url = "http://10.0.2.2:8080/shopping/rest/shoppingitems";
    //JSON Node Names
    private static final String TAG_OS = "android";
    private static final String TAG_VER = "ver";
//    private static final String TAG_API = "api";
    private static final String TAG_NAME = "name";

    JSONArray android = null;
    JSONArray names = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        shoppinglist = new ArrayList<HashMap<String, String>>();

        Btngetdata = (Button)findViewById(R.id.getdata);
        Btngetdata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new JSONParse().execute();

            }
        });

    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ver = (TextView)findViewById(R.id.vers);
            name = (TextView)findViewById(R.id.name);
            //api = (TextView)findViewById(R.id.api);
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {

            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            Log.d("url: ", "> " + url);
            JSONObject json = jParser.getJSONFromUrl(url);
            if (json == null)
                Log.d("json"," is null");
            else
                Log.d("Response: ", "> " + json.toString());

            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
//                android = json.getJSONArray(TAG_OS);
//                for(int i = 0; i < android.length(); i++){
                names = json.getJSONArray(TAG_NAME);
                Log.d("json.length=", "json.length = " + json.length());

                for(int i = 0; i < names.length(); i++){

                    JSONObject c = names.getJSONObject(i);

                    // Storing  JSON item in a Variable
                    String ver = c.getString(TAG_VER);
                    String name = c.getString(TAG_NAME);
                    //String api = c.getString(TAG_API);

                    // Adding value HashMap key => value

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put(TAG_VER, ver);
                    map.put(TAG_NAME, name);
                    //map.put(TAG_API, api);

                    shoppinglist.add(map);
                    list=(ListView)findViewById(R.id.list);

                    ListAdapter adapter = new SimpleAdapter(MainActivity.this, shoppinglist,
                            R.layout.list_v,
                            //new String[] { TAG_VER,TAG_NAME, TAG_API }, new int[] {
                            //R.id.vers,R.id.name, R.id.api});
                            new String[] { TAG_VER, TAG_NAME }, new int[] {
                            R.id.vers, R.id.name});

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

}