package com.hashtagplus.model.util;

import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLUtils {

  public static List<String> getImages(String text) {
    List<String> images = new ArrayList<>();
    Pattern p = Pattern.compile("<img[^>]*src=[\\\"']([^\\\"^']*)");
    Matcher m = p.matcher(text);
    while (m.find()) {
      String src = m.group();
      int startIndex = src.indexOf("src=") + 5;
      String srcTag = src.substring(startIndex, src.length());
      images.add(srcTag);
    }
    return images;
  }


  public static List<String> extractURLS(String text) {
    List<String> urls = new ArrayList<>();
    StringTokenizer tokenizer = new StringTokenizer(text, " ");

    while(tokenizer.hasMoreTokens()) {
      String next = tokenizer.nextToken();
      if(next.startsWith("http") ||
              next.startsWith("https")) {
        urls.add(next);
      }
    }
    return urls;
  }

  public static Boolean testImage(String urlString) {
    HttpURLConnection urlConnection;
    try {
      urlConnection = (HttpURLConnection) new URL(urlString).openConnection();
      urlConnection.setInstanceFollowRedirects(true);
      HttpURLConnection.setFollowRedirects(true);

      int status = urlConnection.getResponseCode();
      if (status >= 300 && status <= 307) {
        urlString = urlConnection.getHeaderField("Location");
        urlConnection = (HttpURLConnection) new URL(urlString).openConnection();
        System.out.println("Redirect to URL : " + urlString);
      }
      String contentType = urlConnection.getHeaderField("Content-Type");
      if (contentType.startsWith("image/")) {
        return true;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }



  public static boolean exists(String URLName){
    try {
      HttpURLConnection.setFollowRedirects(false);
      // note : you may also need
      //        HttpURLConnection.setInstanceFollowRedirects(false)
      HttpURLConnection con =
              (HttpURLConnection) new URL(URLName).openConnection();
      con.setRequestMethod("HEAD");
      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
    }
    catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

}
