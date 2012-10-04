package me.phifty.database.test.filesystem;

import me.phifty.database.Handler;
import me.phifty.database.filesystem.Filesystem;
import me.phifty.database.filesystem.Statistics;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class FakeFilesystem implements Filesystem {

  protected boolean exists;
  protected HashMap<String, String[]> listedFiles = new HashMap<String, String[]>();
  protected Statistics statistics;
  protected ArrayList<String> createdPaths = new ArrayList<String>();
  protected ArrayList<String> deletedPaths = new ArrayList<String>();
  protected String writtenFileName;
  protected byte[] writtenFileData;

  @Override
  public void exists(String path, Handler<Boolean> handler) {
    handler.handle(exists);
  }

  @Override
  public void listFiles(String path, Handler<String[]> handler) {
    if (listedFiles.containsKey(path)) {
      handler.handle(listedFiles.get(path));
    } else {
      handler.handle(new String[0]);
    }
  }

  @Override
  public void statistics(String path, Handler<Statistics> handler) {
    handler.handle(statistics);
  }

  @Override
  public void makePath(String path, Handler<Boolean> handler) {
    createdPaths.add(path);
    handler.handle(true);
  }

  @Override
  public void deletePath(String path, Handler<Boolean> handler) {
    deletedPaths.add(path);
    handler.handle(true);
  }

  @Override
  public void writeFile(String name, byte[] data, Handler<Boolean> handler) {
    writtenFileName = name;
    writtenFileData = data;
    handler.handle(true);
  }

  @Override
  public void readFile(String name, Handler<byte[]> handler) {
    if (name.equals(writtenFileName)) {
      handler.handle(writtenFileData);
    }
  }

  public void reset() {
    exists = false;
    listedFiles.clear();
    statistics = null;
    createdPaths.clear();
    deletedPaths.clear();
    writtenFileName = null;
    writtenFileData = null;
  }

}
