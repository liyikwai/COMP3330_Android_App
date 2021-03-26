package com.example.knowcourseapp.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.knowcourseapp.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtility {

    private NetworkUtility() {}

    public static String getJson(String url) throws ConnectException {

        return getJson(url, "");
    }

    public static String getJson(String url, String query) throws ConnectException {
        HttpURLConnection connection = null;
        String response = "";
        String urlWithQuery = url;
        if (!query.isEmpty()) {
            urlWithQuery += "?search=" + query;
        }
        try {

            URL urlObj = new URL(urlWithQuery);
            connection = (HttpURLConnection) urlObj.openConnection();
            connection.setInstanceFollowRedirects(true);
            System.out.println(connection.getResponseCode());
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder jsonSourceBuilder = new StringBuilder();
            while((line = reader.readLine()) != null) {
                jsonSourceBuilder.append(line);
                jsonSourceBuilder.append(System.lineSeparator());
            }
            response = jsonSourceBuilder.toString();
            reader.close();

        } catch (ConnectException ex) {
            throw ex;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response;
    }

    public static String postJson(String url, String json, Context context) {
        HttpURLConnection connection;
        String response = "";
        try {
            URL urlObj = new URL(url);
            connection = (HttpURLConnection) urlObj.openConnection();
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");

            String sharedPreference = context.getResources().getString(R.string.app_preferences);
            String tokenResource = context.getResources().getString(R.string.token);
            SharedPreferences preferences = context.getSharedPreferences(sharedPreference, Context.MODE_PRIVATE);
            String token = preferences.getString(tokenResource, null);
            if (token != null) {
                String authHeaderValue = "Bearer " + token;
                connection.setRequestProperty("Authorization", authHeaderValue);

            }
            connection.setDoInput(true);
            connection.setDoOutput(true);


            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(json);
            writer.flush();
            writer.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String line;
                while((line = reader.readLine()) != null) {
                    response += line;
                }
            }
            else if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED ) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response += line;
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return response;
    }
}
