package me.phifty.database.test.filesystem;

import me.phifty.database.filesystem.Filesystem;
import me.phifty.database.filesystem.PhysicalFilesystem;
import me.phifty.database.filesystem.Statistics;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class PhysicalFilesystemTest {

  private FakeHandler<Boolean> booleanHandler = new FakeHandler<Boolean>();
  private FakeHandler<String[]> listHandler = new FakeHandler<String[]>();
  private FakeHandler<Statistics> statisticsHandler = new FakeHandler<Statistics>();
  private FakeHandler<byte[]> dataHandler = new FakeHandler<byte[]>();

  private Filesystem filesystem;
  private String testContent = "test";

  @Before
  public void setUp() {
    filesystem = new PhysicalFilesystem();
    for (int i = 0; i < 16; i++) testContent += testContent;
  }

  @After
  public void tearDown() throws IOException {
    clearTestData();
    booleanHandler.reset();
    listHandler.reset();
    statisticsHandler.reset();
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
  public void testStatistics() throws IOException {
    Date now = new Date();
    writeTestData();

    filesystem.statistics("/tmp/test/1/2/3/12345", statisticsHandler);

    Assert.assertEquals(true, statisticsHandler.getValue().accessTime.before(now));
    Assert.assertEquals(true, statisticsHandler.getValue().updateTime.before(now));
    Assert.assertEquals(true, statisticsHandler.getValue().creationTime.before(now));
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

    filesystem.writeFile("/tmp/test/1/2/3/12345", testContent.getBytes(), booleanHandler);

    Assert.assertEquals(testContent, readTestData());
  }

  @Test
  public void testReadFile() throws IOException {
    writeTestData();

    filesystem.readFile("/tmp/test/1/2/3/12345", dataHandler);

    Assert.assertArrayEquals(testContent.getBytes(), dataHandler.getValue());
  }

  private void writeTestData() throws IOException {
    writeTestData(testContent);
  }

  private void writeTestData(String content) throws IOException {
    if ((new File("/tmp/test/1/2/3")).mkdirs()) {
      FileWriter fileWriter = new FileWriter("/tmp/test/1/2/3/12345");
      fileWriter.write(content);
      fileWriter.close();
    }
  }

  private String readTestData() throws IOException {
    char[] buffer = new char[ testContent.length() ];
    FileReader fileReader = new FileReader("/tmp/test/1/2/3/12345");
    fileReader.read(buffer);
    fileReader.close();
    return new String(buffer);
  }

  private void clearTestData() throws IOException {
    filesystem.exists("/tmp/test", booleanHandler);
    if (booleanHandler.getValue()) {
      filesystem.deletePath("/tmp/test", booleanHandler);
    }
  }

}
