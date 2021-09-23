package com.amazon.dtasdk.utils;

import java.net.URI;
import org.junit.Assert;
import org.junit.Test;

public class HttpUtilsTest {

  @Test
  public void testAppendUri() {
    Assert.assertEquals("/", HttpUtils.appendUri("", ""));
    Assert.assertEquals("basepath/two%2Bwords", HttpUtils.appendUri("basepath", "two+words"));
    Assert.assertEquals("basepath/", HttpUtils.appendUri("basepath", null));
    Assert.assertEquals("basepath/", HttpUtils.appendUri("basepath/", null));
    Assert.assertEquals("basepath/test", HttpUtils.appendUri("basepath/", "test"));
    Assert.assertEquals("basepath/test", HttpUtils.appendUri("basepath/", "/test"));
    Assert.assertEquals("basepath/test", HttpUtils.appendUri("basepath", "/test"));
    Assert.assertEquals(
        "basepath/test//doubleslash", HttpUtils.appendUri("basepath", "/test//doubleslash"));
    Assert.assertEquals(
        "basepath/test/%2Fdoubleslash",
        HttpUtils.appendUri("basepath", "/test//doubleslash", true));
  }

  @Test
  public void testIsUsingNonDefaultPort() {
    Assert.assertFalse(HttpUtils.isUsingNonDefaultPort(URI.create("http://test:80")));
    Assert.assertFalse(HttpUtils.isUsingNonDefaultPort(URI.create("https://test:443")));
    Assert.assertFalse(HttpUtils.isUsingNonDefaultPort(URI.create("scheme://other")));
    Assert.assertFalse(HttpUtils.isUsingNonDefaultPort(URI.create("scheme://other:0")));
    Assert.assertFalse(HttpUtils.isUsingNonDefaultPort(URI.create("scheme://other:-10")));

    Assert.assertTrue(HttpUtils.isUsingNonDefaultPort(URI.create("http://test:9999")));
    Assert.assertTrue(HttpUtils.isUsingNonDefaultPort(URI.create("http://test:443")));
    Assert.assertTrue(HttpUtils.isUsingNonDefaultPort(URI.create("https://test:9999")));
    Assert.assertTrue(HttpUtils.isUsingNonDefaultPort(URI.create("https://test:80")));
  }

  @Test
  public void testUrlEncode() {
    Assert.assertEquals("", HttpUtils.urlEncode(null, false));
    Assert.assertEquals("url%2Bencode", HttpUtils.urlEncode("url+encode", false));
    Assert.assertEquals("url%2B%2B%2Bencode", HttpUtils.urlEncode("url+++encode", false));
    Assert.assertEquals("a%2Atest~string", HttpUtils.urlEncode("a*test~string", false));
    Assert.assertEquals("not%2Fa%2Fpath", HttpUtils.urlEncode("not/a/path", false));
    Assert.assertEquals("is/a/path", HttpUtils.urlEncode("is/a/path", true));
  }
}
