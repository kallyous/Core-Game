package com.kallyous.nopeisland;



import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



/** ========================= GAME STATES - FSM ========================= **/

abstract public class GameState {

  private static final String TAG = "GameState";

  protected SpriteBatch screen_batch;
  protected GameState previous_state;
  protected boolean initialized;


  GameState() {
    initialized = false;
  }

  abstract public void update(float dt);
  abstract protected void drawAll();

  public void enter() {
    this.previous_state = NopeIslandGame.game;
    NopeIslandGame.game = this;
  }

  public void render() {
    screen_batch.begin();
    drawAll();
    screen_batch.end();
  }

  public void exit() {
    initialized = false;
    NopeIslandGame.game = previous_state;
  }

  public void setScreenbatch(SpriteBatch batch) {
    screen_batch = batch;
  }

}





/** ========================= RUNNING STATE ========================= **/

class RunningGameState extends GameState {

  private static final String TAG = "RunningGameState";


  @Override
  public void update(float dt) {
    // Updates all connected and/or contained elements
  }

  @Override
  protected void drawAll() {
    // Draw all elements using whatever SpriteBatch's available.
  }

}





/** ========================= MAIN MENU STATE ========================= **/

class MainMenuGameState extends GameState {

  private static final String TAG = "MainMenuGameState";


  public UserInterface gui;



  MainMenuGameState(InputMultiplexer input_multiplexer, CommandManager cmd_manager) {

    gui = new UserInterface(
        NopeIslandGame.game_window_width,
        NopeIslandGame.game_window_height);

    gui.setInputMultiplexer(input_multiplexer);

    gui.setCommandManager(cmd_manager);

  }


  @Override
  public void update(float dt) {
    gui.update(dt);
  }


  @Override
  protected void drawAll() {
    gui.draw(screen_batch);
  }

}
