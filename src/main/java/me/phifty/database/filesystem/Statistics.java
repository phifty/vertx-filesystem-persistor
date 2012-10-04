package me.phifty.database.filesystem;

import java.util.Date;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class Statistics {

  public Statistics() {
    this.accessTime = new Date();
    this.updateTime = new Date();
    this.creationTime = new Date();
  }

  public Statistics(Date accessTime, Date updateTime, Date creationTime) {
    this.accessTime = accessTime;
    this.updateTime = updateTime;
    this.creationTime = creationTime;
  }

  public Date accessTime;
  public Date updateTime;
  public Date creationTime;

}
