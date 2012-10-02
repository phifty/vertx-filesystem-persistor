package me.phifty.database.test.filesystem;

import me.phifty.database.DoneHandler;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class FakeDoneHandler extends DoneHandler {

  private boolean done;

  @Override
  public void done() {
    done = true;
  }

  public void reset() {
    done = false;
  }

  public boolean isDone() {
    return done;
  }

}
