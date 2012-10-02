package me.phifty.database;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public abstract class DoneHandler implements Handler<Boolean> {

  @Override
  public void handler(Boolean value) {
    if (value) {
      done();
    }
  }

  @Override
  public void exception(Exception exception) {
    System.err.println(exception);
  }

  public abstract void done();

}
