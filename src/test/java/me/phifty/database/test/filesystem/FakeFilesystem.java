package me.phifty.database.test.filesystem;

import me.phifty.database.Handler;
import me.phifty.database.filesystem.Filesystem;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class FakeFilesystem implements Filesystem {

  protected boolean exists;
  protected String createdPath;
  protected String deletedPath;
  protected String writtenFileName;
  protected byte[] writtenFileData;

  @Override
  public void exists(String path, Handler<Boolean> handler) {
    handler.handle(exists);
  }

  @Override
  public void makePath(String path, Handler<Boolean> handler) {
    createdPath = path;
    handler.handle(true);
  }

  @Override
  public void deletePath(String path, Handler<Boolean> handler) {
    deletedPath = path;
    handler.handle(true);
  }

  @Override
  public void writeFile(String name, byte[] data, Handler<Boolean> handler) {
    writtenFileName = name;
    writtenFileData = data;
    handler.handle(true);
  }

  @Override
  public void readFile(String name, Handler<byte[]> handler) {
    if (name.equals(writtenFileName)) {
      handler.handle(writtenFileData);
    }
  }

}
