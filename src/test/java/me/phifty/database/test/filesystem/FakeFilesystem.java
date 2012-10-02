package me.phifty.database.test.filesystem;

import me.phifty.database.DoneHandler;
import me.phifty.database.filesystem.Filesystem;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class FakeFilesystem extends Filesystem {

  protected boolean exists;
  protected String[] createdPath;
  protected String writtenFileName;
  protected byte[] writtenFileData;

  @Override
  public boolean exists(String path) {
    return exists;
  }

  @Override
  public void makePath(String[] path, DoneHandler handler) {
    createdPath = path;
    handler.done();
  }

  @Override
  public void writeFile(String name, byte[] data, DoneHandler handler) {
    writtenFileName = name;
    writtenFileData = data;
    handler.done();
  }

}
