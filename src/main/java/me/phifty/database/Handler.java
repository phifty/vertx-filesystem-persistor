package me.phifty.database;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public interface Handler<T> {

  public void handle(T value);

  public void exception(Exception exception);

}
