package me.phifty.database.test;

import me.phifty.database.Database;
import me.phifty.database.DoneHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class DatabaseTest {

  private Database database;

  @Before
  public void setUp() throws Exception {
    database = new Database("/tmp/test") {
      @Override
      public void store(String id, byte[] data, DoneHandler handler) {

      }
    };
  }

  @Test
  public void testPath() {
    Assert.assertEquals("/tmp/test", database.getPath());
  }

}
