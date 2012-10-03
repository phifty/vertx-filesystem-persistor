package me.phifty.database.filesystem;

import me.phifty.database.Handler;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class PhysicalFilesystem implements Filesystem {

  @Override
  public void exists(String path, Handler<Boolean> handler) {
    System.out.println("exists");
  }

  @Override
  public void empty(String path, Handler<Boolean> handler) {
    System.out.println("empty");
  }

  @Override
  public void makePath(String path, Handler<Boolean> handler) {
    System.out.println("makePath");
  }

  @Override
  public void deletePath(String path, Handler<Boolean> handler) {
    System.out.println("deletePath");
  }

  @Override
  public void writeFile(String name, byte[] data, Handler<Boolean> handler) {
    System.out.println("writeFile");
  }

  @Override
  public void readFile(String name, Handler<byte[]> handler) {
    System.out.println("readFile");
  }

}
