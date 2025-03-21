package com.vaishnavi.photoalbumapp.utils;

import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageCaptionGenerator {
    private static final String API_KEY = "AB1CWRmdM3dDV5sw94N7g9nk1BPzTwhmcR1scoQGCtnrTvTYcmOYJQQJ99BCACYeBjFXJ3w3AAAFACOGu2Ax"; // Replace with actual API Key
    private static final String ENDPOINT = "https://imagecaptiongen.cognitiveservices.azure.com/"; // Example: "https://your-region.cognitiveservices.azure.com/"

    public interface CaptionCallback {
        void onCaptionGenerated(String caption);
    }

    public static void generateCaption(String imageUrl, CaptionCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    String apiUrl = ENDPOINT + "/vision/v3.1/analyze?visualFeatures=Description";

                    URL url = new URL(apiUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Ocp-Apim-Subscription-Key", API_KEY);
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);

                    String requestBody = "{ \"url\": \"" + imageUrl + "\" }";
                    OutputStream os = conn.getOutputStream();
                    os.write(requestBody.getBytes("UTF-8"));
                    os.close();

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Parse JSON response
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray captions = jsonResponse.getJSONObject("description").getJSONArray("captions");

                    if (captions.length() > 0) {
                        return captions.getJSONObject(0).getString("text");
                    }

                } catch (Exception e) {
                    Log.e("CaptionGenerator", "Error fetching image caption", e);
                }
                return "No caption available.";
            }

            @Override
            protected void onPostExecute(String caption) {
                callback.onCaptionGenerated(caption);
            }
        }.execute();
    }
}
