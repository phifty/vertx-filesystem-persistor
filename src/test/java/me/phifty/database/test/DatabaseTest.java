package me.phifty.database.test;

import me.phifty.database.Database;
import me.phifty.database.DatabaseException;
import me.phifty.database.Handler;
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
      public void store(String id, byte[] data, Handler<Boolean> handler) { }

      @Override
      public void fetch(String id, Handler<byte[]> handler) throws DatabaseException { }
    };
  }

  @Test
  public void testPath() {
    Assert.assertEquals("/tmp/test", database.getPath());
  }

}
