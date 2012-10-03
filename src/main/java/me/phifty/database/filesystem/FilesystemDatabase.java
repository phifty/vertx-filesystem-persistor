package me.phifty.database.filesystem;

import me.phifty.database.Database;
import me.phifty.database.DatabaseException;
import me.phifty.database.Handler;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class FilesystemDatabase extends Database {

  private Filesystem filesystem;
  private PathBuilder pathBuilder;

  public FilesystemDatabase(String path) {
    this(path, new PhysicalFilesystem());
  }

  public FilesystemDatabase(String path, Filesystem filesystem) {
    super(path);
    this.filesystem = filesystem;
    this.pathBuilder = new PathBuilder(path);
  }

  @Override
  public void store(String id, final byte[] data, final Handler<Boolean> handler) throws DatabaseException {
    final String path = pathBuilder.path(id);
    final String filename = pathBuilder.filename(id);

    filesystem.exists(path, new Handler<Boolean>() {
      @Override
      public void handle(Boolean value) {
        if (value) {
          filesystem.writeFile(filename, data, handler);
        } else {
          filesystem.makePath(path, new Handler<Boolean>() {
            @Override
            public void handle(Boolean value) {
              filesystem.writeFile(filename, data, handler);
            }

            @Override
            public void exception(Exception exception) {
              handler.exception(exception);
            }
          });
        }
      }

      @Override
      public void exception(Exception exception) {
        handler.exception(exception);
      }
    });
  }

  @Override
  public void fetch(String id, final Handler<byte[]> handler) throws DatabaseException {
    final String filename = pathBuilder.filename(id);

    filesystem.exists(filename, new Handler<Boolean>() {
      @Override
      public void handle(Boolean value) {
        if (value) {
          filesystem.readFile(filename, handler);
        } else {
          handler.handle(null);
        }
      }

      @Override
      public void exception(Exception exception) {
        handler.exception(exception);
      }
    });
  }

}
