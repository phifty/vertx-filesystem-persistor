package me.phifty.database.test.filesystem;

import me.phifty.database.Handler;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class FakeHandler<T> implements Handler<T> {

  private T value;

  @Override
  public void handle(T value) {
    this.value = value;
  }

  @Override
  public void exception(Exception exception) {
    System.err.println(exception);
  }

  public void reset() {
    value = null;
  }

  public T getValue() {
    return value;
  }

}
