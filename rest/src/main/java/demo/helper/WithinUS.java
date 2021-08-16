package demo.helper;

import org.json.JSONArray;
import org.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WithinUS {

  private static final Logger log = LoggerFactory.getLogger(WithinUS.class);

  public static boolean isWithinUS(double lat, double lon, String key) throws IOException {
    URL urlForGetRequest = new URL("http://www.mapquestapi.com/geocoding/v1/reverse?key="+key+"&location="+lat+","+lon+"&includeRoadMetadata=true&includeNearestIntersection=true");
    String readLine = null;
    HttpURLConnection conn = (HttpURLConnection) urlForGetRequest.openConnection();
    conn.setRequestMethod("GET");
    int responseCode = conn.getResponseCode();

    if (responseCode == HttpURLConnection.HTTP_OK) {
      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuffer response = new StringBuffer();
      while ((readLine = in .readLine()) != null) {
        response.append(readLine);
      } in.close();
      return parseResponseForUS(response.toString());
    }
    else {
      log.info("GET NOT WORKED");
    }
    return false;
  }

  private static boolean parseResponseForUS(String jsonResponse){
    JSONObject obj = new JSONObject(jsonResponse);
    JSONArray resultsArray = obj.getJSONArray("results");
    String countryCode = null;

    if (resultsArray!=null && resultsArray.length()>0) {
      JSONArray locations = resultsArray.getJSONObject(0).getJSONArray("locations");   //getJSONArray("locations");
      if (locations.length() > 0) {
        countryCode = (String) locations.getJSONObject(0).get("adminArea1");
      }
      if ("US".equals(countryCode)) {
        log.info("countryCode: " + countryCode);
        return true;
      }
    }
    log.info("countryCode: " + countryCode + " NOT within the US!");
    return false;
  }


}
