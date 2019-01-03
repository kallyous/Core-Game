package com.kallyous.nopeisland;



import com.badlogic.gdx.InputAdapter;





/** ========================= GAME INPUT ========================= **/

// General purpose input processor, mostly for catching uncaught screen input
public class GameInput extends InputAdapter {

  private static final String TAG = "GameInput";




// ========================= LOGIC BEGIN ========================= //

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    Log.d(TAG + " - Screen Touched at (" + screenX + ", " + screenY + ")");
    return false;
  }

// ========================= LOGIC END ========================= //

}
