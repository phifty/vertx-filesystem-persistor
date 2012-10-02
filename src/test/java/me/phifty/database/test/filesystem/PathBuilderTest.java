package me.phifty.database.test.filesystem;

import me.phifty.database.filesystem.IdTooShortException;
import me.phifty.database.filesystem.PathBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class PathBuilderTest {

  private PathBuilder pathBuilder;

  @Before
  public void setUp() {
    pathBuilder = new PathBuilder("/tmp/db");
  }

  @Test
  public void testPathForId() throws IdTooShortException {
    Assert.assertEquals("/tmp/db/1/2/3", pathBuilder.path("12345"));
  }

  @Test(expected = IdTooShortException.class)
  public void testPathForTooShortId() throws IdTooShortException {
    pathBuilder.path("12");
  }

  @Test
  public void testFilenameForId() throws IdTooShortException {
    Assert.assertEquals("/tmp/db/1/2/3/12345", pathBuilder.filename("12345"));
  }

}
