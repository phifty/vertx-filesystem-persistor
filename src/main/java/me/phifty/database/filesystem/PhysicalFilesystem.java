package me.phifty.database.filesystem;

import me.phifty.database.Handler;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class PhysicalFilesystem implements Filesystem {

  @Override
  public void exists(String path, Handler<Boolean> handler) {
    try {
      handler.handle(new File(path).exists());
    } catch (Exception exception) {
      handler.exception(exception);
    }
  }

  @Override
  public void listFiles(String path, Handler<String[]> handler) {
    try {
      handler.handle(listFilesRecursive(new File(path)).toArray(new String[0]));
    } catch (Exception exception) {
      handler.exception(exception);
    }
  }

  @Override
  public void statistics(String path, Handler<Statistics> handler) {
    try {
      Date accessTime = fileTimeAttribute(path, "lastAccessTime");
      Date updateTime = fileTimeAttribute(path, "lastModifiedTime");
      Date creationTime = fileTimeAttribute(path, "creationTime");
      handler.handle(new Statistics(accessTime, updateTime, creationTime));
    } catch (Exception exception) {
      handler.exception(exception);
    }
  }

  @Override
  public void makePath(String path, Handler<Boolean> handler) {
    try {
      handler.handle(new File(path).mkdirs());
    } catch (Exception exception) {
      handler.exception(exception);
    }
  }

  @Override
  public void deletePath(String path, Handler<Boolean> handler) {
    try {
      deleteRecursive(new File(path));
    } catch (Exception exception) {
      handler.exception(exception);
    }
  }

  @Override
  public void writeFile(String name, byte[] data, Handler<Boolean> handler) {
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(name);
      fileOutputStream.write(data);
      fileOutputStream.flush();
      fileOutputStream.close();
    } catch (Exception exception) {
      handler.exception(exception);
    }
  }

  @Override
  public void readFile(String name, Handler<byte[]> handler) {
    try {
      File file = new File(name);
      FileInputStream fileInputStream = new FileInputStream(file);
      byte[] buffer = new byte[ (int)file.length() ];
      fileInputStream.read(buffer);
      fileInputStream.close();
      handler.handle(buffer);
    } catch (Exception exception) {
      handler.exception(exception);
    }
  }

  private ArrayList<String> listFilesRecursive(File file) {
    return listFilesRecursive(file, new ArrayList<String>());
  }

  private ArrayList<String> listFilesRecursive(File file, ArrayList<String> result) {
    if (file.isFile()) {
      result.add(file.getAbsolutePath());
    } else if (file.isDirectory()) {
      for (File nestedFile : file.listFiles()) {
        listFilesRecursive(nestedFile, result);
      }
    }
    return result;
  }

  private Date fileTimeAttribute(String path, String attributeName) throws IOException {
    return new Date(((FileTime)Files.getAttribute(Paths.get(path), "lastAccessTime")).toMillis());
  }

  private void deleteRecursive(File file) throws FileNotFoundException {
    if (file.isDirectory()) {
      for (File nestedFile : file.listFiles()) {
        deleteRecursive(nestedFile);
      }
    }
    if (!file.delete()) {
      throw new FileNotFoundException("Failed to delete file: " + file);
    }
  }

}
