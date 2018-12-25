package com.kallyous.nopeisland;



import com.badlogic.gdx.InputAdapter;



public class GameInput extends InputAdapter {

  private static final String TAG = "GameInput";

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    System.out.println(TAG + ": Screen Touched at (" + screenX + ", " + screenY + ")");
    return false;
  }

}
