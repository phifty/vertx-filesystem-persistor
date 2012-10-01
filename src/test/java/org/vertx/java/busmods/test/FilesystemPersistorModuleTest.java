package org.vertx.java.busmods.test;

import org.junit.Test;
import org.vertx.java.framework.TestBase;

public class FilesystemPersistorModuleTest extends TestBase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    startApp(FilesystemPersistorTestClient.class.getName());
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  @Test
  public void testStore() {
    startTest(getMethodName());
  }

}
