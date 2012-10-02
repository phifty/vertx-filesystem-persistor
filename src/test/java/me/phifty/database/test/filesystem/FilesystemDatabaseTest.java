package me.phifty.database.test.filesystem;

import me.phifty.database.Database;
import me.phifty.database.DatabaseException;
import me.phifty.database.filesystem.FilesystemDatabase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class FilesystemDatabaseTest {

  private FakeDoneHandler doneHandler = new FakeDoneHandler();
  private FakeFilesystem filesystem;
  private Database database;

  @Before
  public void setUp() throws Exception {
    filesystem = new FakeFilesystem();
    filesystem.exists = true;

    database = new FilesystemDatabase("/tmp/test", filesystem);
  }

  @After
  public void tearDown() {
    doneHandler.reset();
  }

  @Test
  public void testBasicStore() throws InterruptedException, DatabaseException {
    database.store("12345", testData(), doneHandler);
    Assert.assertEquals("/tmp/test/1/2/3/12345", filesystem.writtenFileName);
    Assert.assertArrayEquals(testData(), filesystem.writtenFileData);
  }

  private byte[] testData() {
    return "test".getBytes();
  }

}
