package com.hashtagplus.model.util;

import com.hashtagplus.model.MediaUrl;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

  public static List<MediaUrl> extractMediaURLS(List<String> urls) {
    return urls.stream()
            .map(s -> new MediaUrl(s, HTMLUtils.getContentType(s)))
            .filter(mu -> HTMLUtils.isMediaUrl(mu.getContentType()))
            .collect(Collectors.toList());
  }

  public static Boolean isMediaUrl(String contentType) {
    return contentType.startsWith("image/") ||
            contentType.startsWith("audio/") ||
            contentType.startsWith("video/");
  }

  private static String getContentType(String urlString) {
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
      return urlConnection.getHeaderField("Content-Type");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
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


  public static TextComponents processText(String text) {
    List<String> urls = new ArrayList<>();
    List<String> hashtags = new ArrayList<>();
    List<String> mentions = new ArrayList<>();
    StringTokenizer tokenizer = new StringTokenizer(text, " ");

    int numTokens = tokenizer.countTokens();

    while(tokenizer.hasMoreTokens()) {
      String next = tokenizer.nextToken();
      if(next.startsWith("http") ||
              next.startsWith("https")) {
        urls.add(next);
      } else if(next.startsWith("#")) {
        hashtags.add(next.substring(1,next.length()));
      } else if(next.startsWith("@")) {
        mentions.add(next.substring(1,next.length()));
      }
    }
    boolean hasText = numTokens != (urls.size() + hashtags.size() + mentions.size());
    return new TextComponents(urls, hashtags, mentions, Boolean.toString(hasText));
  }

  public static class TextComponents {
    public String hasText = "false";
    public List<String> urls = new ArrayList<>();
    public List<String> hashtags = new ArrayList<>();
    public List<String> mentions = new ArrayList<>();

    TextComponents(final List<String> urls, final List<String> hashtags, final List<String> mentions, String hasText) {
      this.urls = urls;
      this.hashtags = hashtags;
      this.mentions = mentions;
      this.hasText = hasText;
    }
  }

}