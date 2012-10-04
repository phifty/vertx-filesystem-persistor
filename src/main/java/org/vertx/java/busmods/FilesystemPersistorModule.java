package org.vertx.java.busmods;

import me.phifty.database.Database;
import me.phifty.database.filesystem.FilesystemDatabase;
import org.vertx.java.busmods.json.JsonConfiguration;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;

public class FilesystemPersistorModule extends Verticle {

  private Configuration configuration;
  private Database database;

  @Override
  public void start() throws Exception {
    initializeConfiguration();
    initializeDatabase();
    registerStoreHandler();
    registerFetchHandler();
    registerRemoveHandler();
    registerClearHandler();
  }

  @Override
  public void stop() throws Exception {
    super.stop();
  }

  private void initializeConfiguration() {
    configuration = new JsonConfiguration(container.getConfig());
  }

  private void initializeDatabase() {
    database = new FilesystemDatabase(configuration.getPath(), new VertxFilesystem(vertx), configuration.getPathSegmentLengths());
  }

  private void registerStoreHandler() {
    final Database database = this.database;
    vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".store", new Handler<Message<JsonObject>>() {
      @Override
      public void handle(final Message<JsonObject> message) {
        String id = message.body.getString("id");
        String content = message.body.getString("content");

        try {
          database.store(id, content.getBytes(), new me.phifty.database.Handler<Boolean>() {
            @Override
            public void handle(Boolean value) {
              message.reply(doneMessage(true));
            }

            @Override
            public void exception(Exception exception) {
              message.reply(failMessage(exception));
            }
          });
        } catch (Exception exception) {
          message.reply(failMessage(exception));
        }
      }
    });
  }

  private void registerFetchHandler() {
    final Database database = this.database;
    vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".fetch", new Handler<Message<JsonObject>>() {
      @Override
      public void handle(final Message<JsonObject> message) {
        String id = message.body.getString("id");

        try {
          database.fetch(id, new me.phifty.database.Handler<byte[]>() {
            @Override
            public void handle(byte[] value) {
              message.reply(contentMessage(value == null ? null : new String(value)));
            }

            @Override
            public void exception(Exception exception) {
              message.reply(failMessage(exception));
            }
          });
        } catch (Exception exception) {
          message.reply(failMessage(exception));
        }
      }
    });
  }

  private void registerRemoveHandler() {
    vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".remove", new Handler<Message<JsonObject>>() {
      @Override
      public void handle(final Message<JsonObject> message) {
        String id = message.body.getString("id");

        try {
          database.remove(id, new me.phifty.database.Handler<Boolean>() {
            @Override
            public void handle(Boolean value) {
              message.reply(doneMessage(true));
            }

            @Override
            public void exception(Exception exception) {
              message.reply(failMessage(exception));
            }
          });
        } catch (Exception exception) {
          message.reply(failMessage(exception));
        }
      }
    });
  }

  private void registerClearHandler() {
    vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".clear", new Handler<Message<JsonObject>>() {
      @Override
      public void handle(final Message<JsonObject> message) {
        try {
          database.clear(new me.phifty.database.Handler<Boolean>() {
            @Override
            public void handle(Boolean value) {
              message.reply(doneMessage(true));
            }

            @Override
            public void exception(Exception exception) {
              message.reply(failMessage(exception));
            }
          });
        } catch (Exception exception) {
          message.reply(failMessage(exception));
        }
      }
    });
  }

  private JsonObject idsMessage(String[] ids) {
    JsonObject message = new JsonObject();
    JsonArray idsMessage = new JsonArray();
    for (String id : ids) {
      idsMessage.addString(id);
    }
    message.putArray("ids", idsMessage);
    return message;
  }

  private JsonObject doneMessage(Boolean done) {
    JsonObject message = new JsonObject();
    message.putBoolean("done", done);
    return message;
  }

  private JsonObject contentMessage(String content) {
    JsonObject message = new JsonObject();
    if (content != null) {
      message.putString("content", content);
    }
    return message;
  }

  private JsonObject failMessage(Exception exception) {
    JsonObject message = new JsonObject();
    message.putString("exception", exception.toString());
    return message;
  }

}
