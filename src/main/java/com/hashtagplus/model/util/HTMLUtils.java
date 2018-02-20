package com.hashtagplus.model.util;

import java.util.ArrayList;
import java.util.List;
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

}
