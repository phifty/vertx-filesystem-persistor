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

  public void testFetch() {
    startTest(getMethodName());
  }

  public void testFetchOfMissing() {
    startTest(getMethodName());
  }

  public void testFetchStatistics() {
    startTest(getMethodName());
  }

  public void testRemove() {
    startTest(getMethodName());
  }

  public void testClear() {
    startTest(getMethodName());
  }

}
