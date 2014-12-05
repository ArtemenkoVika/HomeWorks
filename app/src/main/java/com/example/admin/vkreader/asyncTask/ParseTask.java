package com.example.admin.vkreader.asyncTask;

import android.os.AsyncTask;

import com.example.admin.vkreader.entity.JsonClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseTask extends AsyncTask<Void, Void, String[]> {
    private String resultJson;
    private String stringUrl;
    public String[] title;
    private ArrayList arr = new ArrayList();

    public ParseTask(String stringUrl) {
        this.stringUrl = stringUrl;
    }

    public ArrayList getArr() {
        return arr;
    }

    @Override
    protected String[] doInBackground(Void... params) {
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            resultJson = buffer.toString();
            JSONObject jsonObject;
            jsonObject = new JSONObject(resultJson);
            jsonObject = jsonObject.getJSONObject("response");
            JSONArray jArray = jsonObject.getJSONArray("wall");
            title = new String[jArray.length()];
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_message = jArray.getJSONObject(i);
                if (json_message != null) {
                    String text = json_message.getString("text");
                    Pattern pat = Pattern.compile("<.+?>");
                    Matcher mat = pat.matcher(text);
                    mat.find();
                    int k = mat.start();
                    String match = mat.replaceAll("\n");
                    String substring = match.substring(0, k);
                    title[i] = substring;
                    JSONObject im = json_message.getJSONObject("attachment");
                    im = im.getJSONObject("photo");
                    String urls = im.getString("src_big");
                    String data = json_message.optString("date").toString();
                    JsonClass gs = new JsonClass(match, data, urls);
                    gs.getMap().put("textContent", gs.getTextContent());
                    gs.getMap().put("textDate", gs.getTextDate());
                    gs.getMap().put("imageContent", gs.getImageContent());
                    arr.add(gs.getMap());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return title;
    }

    @Override
    protected void onPostExecute(String[] strJson) {
        super.onPostExecute(strJson);
    }
}
