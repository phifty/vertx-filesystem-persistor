package me.phifty.database.filesystem;

import me.phifty.database.DoneHandler;

import java.io.File;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public abstract class Filesystem {

  public abstract boolean exists(String path);

  public void makePath(String path, DoneHandler handler) {
    makePath(path.split(File.separator), handler);
  }

  public abstract void makePath(String[] path, DoneHandler handler);

  public abstract void writeFile(String name, byte[] data, DoneHandler handler);

}
