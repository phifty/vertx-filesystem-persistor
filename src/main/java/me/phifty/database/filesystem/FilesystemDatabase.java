package me.phifty.database.filesystem;

import me.phifty.database.Database;
import me.phifty.database.DoneHandler;

import java.io.File;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class FilesystemDatabase extends Database {

  private Filesystem filesystem;
  private int[] pathSegmentLengths;

  public FilesystemDatabase(String path) {
    this(path, new PhysicalFilesystem());
  }

  public FilesystemDatabase(String path, Filesystem filesystem) {
    super(path);
    this.filesystem = filesystem;
    this.pathSegmentLengths = new int[] { 1, 1, 1 };
  }

  public void setPathSegmentLengths(int[] value) {
    pathSegmentLengths = value;
  }

  public int[] getPathSegmentLengths() {
    return pathSegmentLengths;
  }

  @Override
  public void store(String id, byte[] data, final DoneHandler handler) throws IdTooShortException {
    filesystem.writeFile(path(id), data, new DoneHandler() {
      @Override
      public void done() {
        handler.done();
      }
    });
  }

  private String path(String id) throws IdTooShortException {
    String result = getPath() + File.separator;
    String[] pathSegments = pathSegments(id);

    for (String pathSegment : pathSegments) {
      result += pathSegment + File.separator;
    }
    result += id;

    return result;
  }

  private String[] pathSegments(String id) throws IdTooShortException {
    if (id.length() < (totalPathSegmentLength() + 1)) {
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
