package me.phifty.database.test.filesystem;

import me.phifty.database.Database;
import me.phifty.database.DatabaseException;
import me.phifty.database.filesystem.FilesystemDatabase;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class FilesystemDatabaseTest {

  private FakeHandler<String[]> idsHandler = new FakeHandler<String[]>();
  private FakeHandler<Boolean> doneHandler = new FakeHandler<Boolean>();
  private FakeHandler<byte[]> dataHandler = new FakeHandler<byte[]>();
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
    idsHandler.reset();
    doneHandler.reset();
    dataHandler.reset();
    filesystem.reset();
  }

  @Test
  public void testIds() {
    filesystem.listedFiles.put("/tmp/test", new String[] { "/tmp/test/1/2/3/12345" });

    database.ids(idsHandler);

    Assert.assertArrayEquals(new String[] { "12345" }, idsHandler.getValue());
  }

  @Test
  public void testStore() throws DatabaseException {
    database.store("12345", testData(), doneHandler);

    Assert.assertEquals("/tmp/test/1/2/3/12345", filesystem.writtenFileName);
    Assert.assertArrayEquals(testData(), filesystem.writtenFileData);
  }

  @Test
  public void testPathCreationBeforeStore() throws DatabaseException {
    filesystem.exists = false;

    database.store("12345", testData(), doneHandler);

    Assert.assertEquals(true, filesystem.createdPaths.contains("/tmp/test/1/2/3"));
  }

  @Test
  public void testFetch() throws DatabaseException {
    filesystem.writtenFileName = "/tmp/test/1/2/3/12345";
    filesystem.writtenFileData = testData();

    database.fetch("12345", dataHandler);

    Assert.assertArrayEquals(testData(), dataHandler.getValue());
  }

  @Test
  public void testFetchOfMissing() throws DatabaseException {
    filesystem.writtenFileName = "/tmp/test/1/2/3/12345";
    filesystem.writtenFileData = testData();
    filesystem.exists = false;

    database.fetch("12345", dataHandler);

    Assert.assertNull(dataHandler.getValue());
  }

  @Test
  public void testRemove() throws DatabaseException {
    database.remove("12345", doneHandler);

    Assert.assertEquals(true, filesystem.deletedPaths.contains("/tmp/test/1/2/3/12345"));
  }

  @Test
  public void testRemoveOfEmptyDirectories() throws DatabaseException {
    filesystem.listedFiles.put("/tmp/test/1", new String[] { "/tmp/test/1/1" });

    database.remove("12345", doneHandler);

    Assert.assertEquals(true, filesystem.deletedPaths.contains("/tmp/test/1/2/3"));
    Assert.assertEquals(true, filesystem.deletedPaths.contains("/tmp/test/1/2"));
    Assert.assertEquals(false, filesystem.deletedPaths.contains("/tmp/test/1"));
  }

  @Test
  public void testClear() {
    database.clear(doneHandler);
    Assert.assertEquals(true, filesystem.deletedPaths.contains("/tmp/test"));
  }

  private byte[] testData() {
    return "test".getBytes();
  }

}
