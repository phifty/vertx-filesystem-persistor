package me.phifty.database.filesystem;

import me.phifty.database.Handler;

import java.io.*;
import java.util.ArrayList;

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
  public void properties(String path, Handler<Properties> handler) {
    System.out.println("properties");
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
      FileInputStream fileInputStream = new FileInputStream(name);
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(fileInputStream.available());

      byte[] buffer = new byte[ 1024 ];
      int offset = 0;
      int bytesRead;
      while ((bytesRead = fileInputStream.read(buffer)) != -1) {
        byteArrayOutputStream.write(buffer, offset, bytesRead);
        offset += bytesRead;
      }

      fileInputStream.close();
      byteArrayOutputStream.close();

      handler.handle(byteArrayOutputStream.toByteArray());
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
