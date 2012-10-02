package me.phifty.database;

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

  public abstract void store(String id, byte[] data, Handler<Boolean> handler) throws DatabaseException;

  public abstract void fetch(String id, Handler<byte[]> handler) throws DatabaseException;

}
