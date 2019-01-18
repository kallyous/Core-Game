package com.sistemalivre.coregame;



// ========================== Log ========================== //

public class Log {


// ========================== DATA ========================== //

  public static final int VERBOSE = 0;
  public static final int INFO = 1;
  public static final int DEBUG = 2;
  public static final int WARNING = 3;
  public static final int ERROR = 4;
  public static final int NONE = 1000;

  public static int LOG_LEVEL = NONE;

  /**  Debug Levels

    d() Debug level shows weird testing stuff. Usually, after testing ends,
        most of d() messages go into i() or v().
    i() Info is for not really necessary informational stuff.
    v() A more rich(cluttered) info on running stuff.
    w() Warnings are things that may be broken or not. They demand attention.
    e() Error level for wrong things. Usually, the game breaks or no longer
        works properly after one of those.

    Who prints out more shit?
        v() > i() > d() > w() > e()

  **/




// ========================== LOGIC ========================== //

  public static void v(String TAG, String msg) {
    if (LOG_LEVEL <= VERBOSE)
      System.out.println("VERB: " + TAG + " - " + msg);
  }


  public static void i(String TAG, String msg) {
    if (LOG_LEVEL <= INFO)
      System.out.println("INFO: " + TAG + " - " + msg);
  }


  public static void d(String TAG, String msg) {
    if (LOG_LEVEL <= DEBUG)
      System.out.println("DEBUG: " + TAG + " - " + msg);
  }


  public static void w(String TAG, String msg) {
    if (LOG_LEVEL <= WARNING)
      System.out.println("WARNING: " + TAG + " - " + msg);
  }


  public static void e(String TAG, String msg) {
    if (LOG_LEVEL <= ERROR)
      System.out.println("ERROR: " + TAG + " - " + msg);
  }

}
