package org.vertx.java.busmods;

import me.phifty.database.Handler;
import me.phifty.database.filesystem.Filesystem;
import me.phifty.database.filesystem.Properties;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.file.FileProps;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

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
  public void listFiles(String path, final Handler<String[]> handler) {
    vertx.fileSystem().readDir(path, new AsyncResultHandler<String[]>() {
      @Override
      public void handle(AsyncResult<String[]> event) {
        if (event.exception == null) {
          if (event.result.length == 0) {
            handler.handle(new String[0]);
          } else {
            final CountDownLatch countDownLatch = new CountDownLatch(event.result.length);
            final ArrayList<String> fileNames = new ArrayList<String>();

            for (final String fileName : event.result) {
              vertx.fileSystem().props(fileName, new AsyncResultHandler<FileProps>() {
                @Override
                public void handle(AsyncResult<FileProps> event) {
                  if (event.exception == null) {
                    countDownLatch.countDown();
                    if (event.result.isRegularFile) {
                      fileNames.add(fileName);
                    }
                    if (countDownLatch.getCount() == 0) {
                      handler.handle(fileNames.toArray(new String[0]));
                    }
                  } else {
                    handler.exception(event.exception);
                  }
                }
              });
            }
          }
        } else {
          handler.exception(event.exception);
        }
      }
    });
  }

  @Override
  public void properties(String path, Handler<Properties> handler) {

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
  public void deletePath(String path, final Handler<Boolean> handler) {
    vertx.fileSystem().delete(path, true, new AsyncResultHandler<Void>() {
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
  public void readFile(String name, final Handler<byte[]> handler) {
    vertx.fileSystem().readFile(name, new AsyncResultHandler<Buffer>() {
      @Override
      public void handle(AsyncResult<Buffer> event) {
        if (event.exception == null) {
          handler.handle(event.result.getBytes());
        } else {
          handler.exception(event.exception);
        }
      }
    });
  }

}
