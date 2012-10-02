package me.phifty.database.filesystem;

import me.phifty.database.DatabaseException;

/**
 * @author phifty <b.phifty@gmail.com>
 */
public class IdTooShortException extends DatabaseException {

  public IdTooShortException(String message) {
    super(message);
  }

}
