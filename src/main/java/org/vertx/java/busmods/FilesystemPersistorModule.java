package org.vertx.java.busmods;

import me.phifty.database.Database;
import me.phifty.database.DatabaseException;
import me.phifty.database.filesystem.FilesystemDatabase;
import org.vertx.java.busmods.json.JsonConfiguration;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
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
    database = new FilesystemDatabase(configuration.getPath(), new VertxFilesystem(vertx));
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
              message.reply(doneMessage());
            }

            @Override
            public void exception(Exception exception) {
              exception.printStackTrace();
              message.reply(failMessage());
            }
          });
        } catch (DatabaseException exception) {
          exception.printStackTrace();
          message.reply(failMessage());
        }
      }
    });
  }

  private void registerClearHandler() {
    vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".clear", new Handler<Message<JsonObject>>() {
      @Override
      public void handle(Message<JsonObject> message) {
        try {
          message.reply(doneMessage());
        } catch (Exception exception) {
          exception.printStackTrace();
          message.reply(failMessage());
        }
      }
    });
  }

  private JsonObject doneMessage() {
    JsonObject message = new JsonObject();
    message.putBoolean("done", true);
    return message;
  }

  private JsonObject failMessage() {
    JsonObject message = new JsonObject();
    message.putBoolean("done", false);
    return message;
  }

}
