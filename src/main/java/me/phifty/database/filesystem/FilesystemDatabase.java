package me.phifty.database.filesystem;

import me.phifty.database.Database;
import me.phifty.database.DatabaseException;
import me.phifty.database.Handler;
import sun.net.idn.StringPrep;

import java.io.File;
import java.util.ArrayList;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class FilesystemDatabase implements Database {

  private Filesystem filesystem;
  private PathBuilder pathBuilder;

  public FilesystemDatabase(String path) {
    this(path, new PhysicalFilesystem());
  }

  public FilesystemDatabase(String path, Filesystem filesystem) {
    this.filesystem = filesystem;
    this.pathBuilder = new PathBuilder(path);
  }

  @Override
  public void ids(final Handler<String[]> handler) {
    filesystem.listFiles(pathBuilder.getBasePath(), new Handler<String[]>() {
      @Override
      public void handle(String[] fileNames) {
        String[] ids = new String[fileNames.length];
        for (int index = 0; index < fileNames.length; index++) {
          ids[index] = fileNames[index].substring(fileNames[index].lastIndexOf(File.separator) + 1);
        }
        handler.handle(ids);
      }

      @Override
      public void exception(Exception exception) {
        handler.exception(exception);
      }
    });
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

  @Override
  public void remove(String id, final Handler<Boolean> handler) throws DatabaseException {
    final String filename = pathBuilder.filename(id);
    final String path = pathBuilder.path(id);

    filesystem.deletePath(filename, new Handler<Boolean>() {
      @Override
      public void handle(Boolean value) {
        removeEmptyDirectories(path, new Handler<Boolean>() {
          @Override
          public void handle(Boolean value) {
            handler.handle(value);
          }

          @Override
          public void exception(Exception exception) {
            handler.exception(exception);
          }
        });
      }

      @Override
      public void exception(Exception exception) {
        handler.exception(exception);
      }
    });
  }

  @Override
  public void clear(final Handler<Boolean> handler) {
    filesystem.deletePath(pathBuilder.getBasePath(), new Handler<Boolean>() {
      @Override
      public void handle(Boolean value) {
        handler.handle(value);
      }

      @Override
      public void exception(Exception exception) {
        handler.exception(exception);
      }
    });
  }

  private ArrayList<String> readIds(final String path, final Handler<String[]> handler) {
    ArrayList<String> ids = new ArrayList<String>();

    filesystem.listFiles(path, new Handler<String[]>() {
      @Override
      public void handle(String[] names) {
        for (String name : names) {
          filesystem.properties(path + File.separator + name, new Handler<Properties>() {
            @Override
            public void handle(Properties properties) {
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

    return ids;
  }

  private void removeEmptyDirectories(final String path, final Handler<Boolean> handler) {
    filesystem.listFiles(path, new Handler<String[]>() {
      @Override
      public void handle(String[] fileNames) {
        if (fileNames.length == 0) {
          filesystem.deletePath(path, new Handler<Boolean>() {
            @Override
            public void handle(Boolean value) {
              String parentPath = path.substring(0, path.lastIndexOf(File.separator));
              if (parentPath.equals(pathBuilder.getBasePath())) {
                handler.handle(true);
              } else {
                removeEmptyDirectories(parentPath, handler);
              }
            }

            @Override
            public void exception(Exception exception) {
              handler.exception(exception);
            }
          });
        } else {
          handler.handle(true);
        }
      }

      @Override
      public void exception(Exception exception) {
        handler.exception(exception);
      }
    });
  }

}
