package me.phifty.database.test.filesystem;

import me.phifty.database.Handler;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class FakeDataHandler implements Handler<byte[]> {

  private byte[] data;

  @Override
  public void handle(byte[] value) {
    data = value;
  }

  @Override
  public void exception(Exception exception) {
    System.err.println(exception);
  }

  public byte[] getData() {
    return data;
  }

}
