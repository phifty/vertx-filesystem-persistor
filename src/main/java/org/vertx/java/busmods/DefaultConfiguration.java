package org.vertx.java.busmods;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class DefaultConfiguration implements Configuration {

  @Override
  public String getBaseAddress() {
    return "filesystem-persistor";
  }

  @Override
  public String getPath() {
    return System.getProperty("user.home") + File.pathSeparator + "db";
  }

  @Override
  public int[] getPathSegmentLengths() {
    return new int[] { 1, 1, 1 };
  }

}
