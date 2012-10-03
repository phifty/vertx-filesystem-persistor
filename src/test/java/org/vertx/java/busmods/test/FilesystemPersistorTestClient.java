package org.vertx.java.busmods.test;

import org.vertx.java.busmods.FilesystemPersistorModule;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
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
    vertx.eventBus().send("test.filesystem-persistor.store", generateTestStoreMessage("12345", "test"), new Handler<Message<JsonObject>>() {
      @Override
      public void handle(Message<JsonObject> message) {
        checkForException(message);
        vertx.fileSystem().readFile("/tmp/db/1/2/3/12345", new AsyncResultHandler<Buffer>() {
          @Override
          public void handle(AsyncResult<Buffer> event) {
            try {
              tu.azzert("test".equals(event.result.toString()), "should create the right file");
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
    });
  }

  public void testFetch() {
    addTestData(new Handler<Boolean>() {
      @Override
      public void handle(Boolean event) {
        vertx.eventBus().send("test.filesystem-persistor.fetch", generateTestFetchMessage("12345"), new Handler<Message<JsonObject>>() {
          @Override
          public void handle(Message<JsonObject> message) {
            try {
              checkForException(message);
              tu.azzert("test".equals(message.body.getString("content")), "should respond the right content");
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
    });
  }

  public void testFetchOfMissing() {
    vertx.eventBus().send("test.filesystem-persistor.fetch", generateTestFetchMessage("missing"), new Handler<Message<JsonObject>>() {
      @Override
      public void handle(Message<JsonObject> message) {
        try {
          checkForException(message);
          tu.azzert(message.body.getString("content") == null, "should respond no content");
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

  private void checkForException(Message<JsonObject> message) {
    if (message.body.getString("exception") != null) {
      tu.exception(new Exception(message.body.getString("exception")), "received exception message");
    }
  }

  private void addTestData(Handler<Boolean> handler) {
    sendAndReceiveDoneMessage("test.filesystem-persistor.store", generateTestStoreMessage("12345", "test"), handler);
  }

  private void clearAllDocuments(Handler<Boolean> handler) {
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

  private JsonObject generateTestStoreMessage(String id, String content) {
    JsonObject message = new JsonObject();
    message.putString("id", id);
    message.putString("content", content);
    return message;
  }

  private JsonObject generateTestFetchMessage(String id) {
    JsonObject message = new JsonObject();
    message.putString("id", id);
    return message;
  }

}
