package com.elice.showpet.common.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class Base64Codec {
  public static String encode(MultipartFile file) throws IOException {
    InputStream in = file.getInputStream();
    byte[] bytes = in.readAllBytes();
    return Base64.getEncoder().encodeToString(bytes);
  }

  public static byte[] decode(String base64) {
    return Base64.getDecoder().decode(base64);
  }
}
