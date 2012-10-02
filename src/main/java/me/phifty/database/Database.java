package me.phifty.database;

import java.util.Map;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public abstract class Database {

  private String path;

  public Database(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  public abstract void store(String id, byte[] data, DoneHandler handler) throws DatabaseException;

}
