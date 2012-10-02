package me.phifty.database.filesystem;

import java.io.File;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class PathBuilder {

  private String basePath;
  private int[] pathSegmentLengths;

  public PathBuilder(String basePath) {
    this(basePath, new int[] { 1, 1, 1 });
  }

  public PathBuilder(String basePath, int[] pathSegmentLengths) {
    this.basePath = basePath;
    this.pathSegmentLengths = pathSegmentLengths;
  }

  public String filename(String id) throws IdTooShortException {
    return path(id) + File.separator + id;
  }

  public String path(String id) throws IdTooShortException {
    String result = basePath;
    String[] pathSegments = pathSegments(id);

    for (String pathSegment : pathSegments) {
      result += File.separator + pathSegment;
    }

    return result;
  }

  private String[] pathSegments(String id) throws IdTooShortException {
    if (id.length() < totalPathSegmentLength()) {
      throw new IdTooShortException("id " + id + " is too short for path segmentation");
    }

    String[] result = new String[pathSegmentLengths.length];

    int position = 0;
    for (int index = 0; index < pathSegmentLengths.length; index++) {
      int pathSegmentLength = pathSegmentLengths[index];
      result[index] = id.substring(position, position + pathSegmentLength);
      position += pathSegmentLength;
    }

    return result;
  }

  private int totalPathSegmentLength() {
    int result = 0;
    for (int pathSegmentLength : pathSegmentLengths) {
      result += pathSegmentLength;
    }
    return result;
  }

}
