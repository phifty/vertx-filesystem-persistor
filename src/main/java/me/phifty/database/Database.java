package me.phifty.database;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public interface Database {

  public abstract void store(String id, byte[] data, Handler<Boolean> handler) throws DatabaseException;

  public abstract void fetch(String id, Handler<byte[]> handler) throws DatabaseException;

  public abstract void clear(Handler<Boolean> handler);

}
