package me.phifty.database.filesystem;

import me.phifty.database.DoneHandler;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class PhysicalFilesystem extends Filesystem {

  @Override
  public boolean exists(String path) {
    return false;
  }

  @Override
  public void makePath(String[] path, DoneHandler handler) {
    System.out.println("makePath");
  }

  @Override
  public void writeFile(String name, byte[] data, DoneHandler handler) {
    System.out.println("writeFile");
  }

}
