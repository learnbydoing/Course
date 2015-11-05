package edu.depaul.csc472.restaurant;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Johnny on 11/5/15.
 */
public class JsonHelper extends AsyncTask<String, Void, JSONArray> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //your code
    }
    @Override
    protected JSONArray doInBackground(String... params) {
        JSONArray object = HttpHelper.GetList(params[0]);
        return object;
    }

    @Override
    protected void onPostExecute(JSONArray result) {
        super.onPostExecute(result);
        //get the values from result here
    }

}

