package org.vertx.java.busmods.test;

import org.vertx.java.busmods.FilesystemPersistorModule;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.framework.TestClientBase;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class FilesystemPersistorTestClient extends TestClientBase {

  @Override
  public void start() {
    super.start();

    JsonObject configuration = new JsonObject();
    configuration.putString("base_address", "test.filesystem-persistor");
    configuration.putString("path", "/tmp/db");

    container.deployVerticle(FilesystemPersistorModule.class.getName(), configuration, 1, new Handler<String>() {
      @Override
      public void handle(String deploymentId) {
        tu.appReady();
      }
    });
  }

  public void testStore() {
    vertx.eventBus().send("test.filesystem-persistor.store", generateTestStoreMessage(), new Handler<Message<JsonObject>>() {
      @Override
      public void handle(Message<JsonObject> message) {
        try {
          tu.azzert(message.body.getBoolean("done"), "should add the test data");
        } finally {
          clearAllDocuments(new Handler<Boolean>() {
            @Override
            public void handle(Boolean done) {
              tu.testComplete();
            }
          });
        }
      }
    });
  }

  private void clearAllDocuments(final Handler<Boolean> handler) {
    sendAndReceiveDoneMessage("test.filesystem-persistor.clear", null, handler);
  }

  private void sendAndReceiveDoneMessage(final String address, final JsonObject message, final Handler<Boolean> handler) {
    vertx.eventBus().send(address, message, new Handler<Message<JsonObject>>() {
      @Override
      public void handle(Message<JsonObject> message) {
        handler.handle(message.body.getBoolean("done"));
      }
    });
  }

  private JsonObject generateTestStoreMessage() {
    JsonObject message = new JsonObject();

    JsonObject data = new JsonObject();
    data.putString("test", "value");
    message.putObject("12345", data);

    return message;
  }

}
