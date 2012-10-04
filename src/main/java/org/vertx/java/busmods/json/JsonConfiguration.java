package org.vertx.java.busmods.json;

import org.vertx.java.busmods.Configuration;
import org.vertx.java.busmods.DefaultConfiguration;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class JsonConfiguration implements Configuration {

  private JsonObject object;
  private Configuration defaultConfiguration;

  public JsonConfiguration(JsonObject object) {
    this.object = object;
    this.defaultConfiguration = new DefaultConfiguration();
  }

  @Override
  public String getBaseAddress() {
    return object.getString("base_address", defaultConfiguration.getBaseAddress());
  }

  @Override
  public String getPath() {
    return object.getString("path", defaultConfiguration.getPath());
  }

  @Override
  public int[] getPathSegmentLengths() {
    JsonArray pathSegmentLengths = object.getArray("path_segment_lengths");

    if (pathSegmentLengths == null) {
      return defaultConfiguration.getPathSegmentLengths();
    } else {
      int[] result = new int[pathSegmentLengths.size()];
      int index = 0;
      for (Object pathSegmentLength : pathSegmentLengths) {
        result[index++] = (Integer)pathSegmentLength;
      }
      return result;
    }
  }

}
