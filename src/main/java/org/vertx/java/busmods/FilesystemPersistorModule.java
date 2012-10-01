package org.vertx.java.busmods;

import org.vertx.java.busmods.json.JsonConfiguration;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.deploy.Verticle;

public class FilesystemPersistorModule extends Verticle {

  private Configuration configuration;

  @Override
  public void start() throws Exception {
    initializeConfiguration();
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

  private void registerStoreHandler() {
    vertx.eventBus().registerHandler(configuration.getBaseAddress() + ".store", new Handler<Message<JsonObject>>() {
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
