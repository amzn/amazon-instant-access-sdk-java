package com.amazon.dtasdk.utils;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BinaryUtilsTest {

  @Rule public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testFromHex() {
    Assert.assertArrayEquals(new byte[] {0x7f}, BinaryUtils.fromHex("7f"));
    Assert.assertArrayEquals(new byte[] {0x1a, 0x2b, 0x3c, 0x4d}, BinaryUtils.fromHex("1a2b3c4d"));
    Assert.assertArrayEquals(new byte[] {}, BinaryUtils.fromHex(""));

    thrown.expect(StringIndexOutOfBoundsException.class);
    BinaryUtils.fromHex("0f0");
  }

  @Test
  public void testToBase64() throws Exception {
    Assert.assertEquals("dGVzdCBzdHJpbmc=", BinaryUtils.toBase64("test string".getBytes()));
    Assert.assertEquals("", BinaryUtils.toBase64(new byte[] {}));
    Assert.assertEquals("AQIDBA==", BinaryUtils.toBase64(new byte[] {0x01, 0x02, 0x03, 0x04}));
  }

  @Test
  public void testFromBase64() throws Exception {
    Assert.assertArrayEquals(new byte[] {}, BinaryUtils.fromBase64(""));
    Assert.assertArrayEquals(
        new byte[] {0x01, 0x02, 0x03, 0x04}, BinaryUtils.fromBase64("AQIDBA=="));
  }

  @Test
  public void testToHex() {
    Assert.assertEquals("", BinaryUtils.toHex(new byte[] {}));
    Assert.assertEquals("01", BinaryUtils.toHex(new byte[] {0x01}));
    Assert.assertEquals(
        "00ff102040", BinaryUtils.toHex(new byte[] {0x00, (byte) 0xff, 0x10, 0x20, 0x40}));

    thrown.expect(NullPointerException.class);
    BinaryUtils.toHex(null);
  }
}
