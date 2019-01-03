package com.kallyous.nopeisland;



public class Log {

  public static final int INFO = 0;
  public static final int DEBUG = 1;
  public static final int WARNING = 2;
  public static final int ERROR = 3;
  public static final int NONE = 1000;

  public static int LOG_LEVEL;

  /*  Debug Levels
      d() Debug level shows weird testing stuff. Usually, after testing ends, most of
          d() messages go into i().
      i() Info is for informational not really necessary stuff.
      w() Warnings are things that may be broken or not. They demand attention.
      e() Error level for wrong things. Usually, the game breaks or no longer
          works properly after one of those.
      Who prints out more shit?
          i() > d() > w() > e()
  */

  public static void i(String msg) {
    if (LOG_LEVEL <= INFO) System.out.println("INFO: " + msg);
  }


  public static void d(String msg) {
    if (LOG_LEVEL <= DEBUG) System.out.println("DEBUG: " + msg);
  }


  public static void w(String msg) {
    if (LOG_LEVEL <= WARNING) System.out.println("WARNING: " + msg);
  }


  public static void e(String msg) {
    if (LOG_LEVEL <= ERROR) System.out.println("ERROR: " + msg);
  }
}
