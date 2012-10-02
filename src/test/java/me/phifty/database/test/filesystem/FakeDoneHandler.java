package me.phifty.database.test.filesystem;

import me.phifty.database.Handler;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class FakeDoneHandler implements Handler<Boolean> {

  private boolean done;

  @Override
  public void handle(Boolean value) {
    done = true;
  }

  @Override
  public void exception(Exception exception) {
    System.err.println(exception);
  }

  public void reset() {
    done = false;
  }

  public boolean isDone() {
    return done;
  }

}
