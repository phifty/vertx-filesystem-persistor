package org.vertx.java.busmods.test;

import org.vertx.java.framework.TestBase;

public class FilesystemPersistorModuleTest extends TestBase {

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    startApp(FilesystemPersistorTestClient.class.getName());
  }

  public void testStore() {
    startTest(getMethodName());
  }

}
