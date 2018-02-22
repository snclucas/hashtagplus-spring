package com.hashtagplus.model;

public class MediaUrl {
  final String url;
  final String contentType;

  public MediaUrl(final String url, final String contentType) {
    this.url = url;
    this.contentType = contentType;
  }

  public String getUrl() {
    return url;
  }

  public String getContentType() {
    return contentType;
  }
}
