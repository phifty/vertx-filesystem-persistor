package me.phifty.database;

import me.phifty.database.filesystem.Statistics;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public interface Database {

  public void ids(Handler<String[]> handler);

  public void store(String id, byte[] data, Handler<Boolean> handler) throws DatabaseException;

  public void fetch(String id, Handler<byte[]> handler) throws DatabaseException;

  public void fetchStatistics(String id, Handler<Statistics> handler) throws DatabaseException;

  public void remove(String id, Handler<Boolean> handler) throws DatabaseException;

  public void clear(Handler<Boolean> handler);

}
