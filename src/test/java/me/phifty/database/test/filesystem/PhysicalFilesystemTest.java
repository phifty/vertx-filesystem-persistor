package me.phifty.database.test.filesystem;

import me.phifty.database.filesystem.Filesystem;
import me.phifty.database.filesystem.PhysicalFilesystem;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class PhysicalFilesystemTest {

  private FakeHandler<Boolean> booleanHandler = new FakeHandler<Boolean>();
  private FakeHandler<String[]> listHandler = new FakeHandler<String[]>();
  private FakeHandler<byte[]> dataHandler = new FakeHandler<byte[]>();

  private Filesystem filesystem;

  @Before
  public void setUp() {
    filesystem = new PhysicalFilesystem();
  }

  @After
  public void tearDown() throws IOException {
    clearTestData();
    booleanHandler.reset();
    listHandler.reset();
    dataHandler.reset();
  }

  @Test
  public void testExists() throws IOException {
    writeTestData();

    filesystem.exists("/tmp/test/1/2/3/12345", booleanHandler);

    Assert.assertEquals(true, (boolean) booleanHandler.getValue());
  }

  @Test
  public void testListFiles() throws IOException {
    writeTestData();

    filesystem.listFiles("/tmp/test", listHandler);

    Assert.assertArrayEquals(new String[] { "/tmp/test/1/2/3/12345" }, listHandler.getValue());
  }

  @Test
  public void testMakePath() {
    filesystem.makePath("/tmp/test/1", booleanHandler);

    Assert.assertEquals(true, new File("/tmp/test/1").exists());
  }

  @Test
  public void testDeletePath() throws IOException {
    writeTestData();

    filesystem.deletePath("/tmp/test", booleanHandler);

    Assert.assertEquals(false, new File("/tmp/test").exists());
  }

  @Test
  public void testWriteFile() throws IOException {
    new File("/tmp/test/1/2/3").mkdirs();

    filesystem.writeFile("/tmp/test/1/2/3/12345", "test".getBytes(), booleanHandler);

    Assert.assertEquals("test", readTestData());
  }

  @Test
  public void testReadFile() throws IOException {
    writeTestData();

    filesystem.readFile("/tmp/test/1/2/3/12345", dataHandler);

    Assert.assertArrayEquals("test".getBytes(), dataHandler.getValue());
  }

  private void writeTestData() throws IOException {
    writeTestData("test");
  }

  private void writeTestData(String content) throws IOException {
    if ((new File("/tmp/test/1/2/3")).mkdirs()) {
      FileWriter fileWriter = new FileWriter("/tmp/test/1/2/3/12345");
      fileWriter.write(content);
      fileWriter.close();
    }
  }

  private String readTestData() throws IOException {
    char[] buffer = new char[4];
    FileReader fileReader = new FileReader("/tmp/test/1/2/3/12345");
    fileReader.read(buffer);
    fileReader.close();
    return new String(buffer);
  }

  private void clearTestData() throws IOException {
    filesystem.deletePath("/tmp/test", booleanHandler);
  }

}
