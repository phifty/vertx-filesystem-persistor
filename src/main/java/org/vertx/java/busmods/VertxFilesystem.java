package org.vertx.java.busmods;

import me.phifty.database.Handler;
import me.phifty.database.filesystem.Filesystem;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class VertxFilesystem implements Filesystem {

  private Vertx vertx;

  public VertxFilesystem(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  public void exists(String path, final Handler<Boolean> handler) {
    vertx.fileSystem().exists(path, new AsyncResultHandler<Boolean>() {
      @Override
      public void handle(AsyncResult<Boolean> event) {
        if (event.exception == null) {
          handler.handle(event.result);
        } else {
          handler.exception(event.exception);
        }
      }
    });
  }

  @Override
  public void makePath(String path, final Handler<Boolean> handler) {
    vertx.fileSystem().mkdir(path, true, new AsyncResultHandler<Void>() {
      @Override
      public void handle(AsyncResult<Void> event) {
        if (event.exception == null) {
          handler.handle(true);
        } else {
          handler.exception(event.exception);
        }
      }
    });
  }

  @Override
  public void writeFile(String name, byte[] data, final Handler<Boolean> handler) {
    vertx.fileSystem().writeFile(name, new Buffer(data), new AsyncResultHandler<Void>() {
      @Override
      public void handle(AsyncResult<Void> event) {
        if (event.exception == null) {
          handler.handle(true);
        } else {
          handler.exception(event.exception);
        }
      }
    });
  }

  @Override
  public void readFile(String name, Handler<byte[]> handler) {

  }

}
