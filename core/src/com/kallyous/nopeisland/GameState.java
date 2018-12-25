package com.kallyous.nopeisland;



import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



/** ========================= GAME STATES - FSM ========================= **/

abstract public class GameState {

  private static final String TAG = "GameState";

  protected SpriteBatch screen_batch;
  protected GameState previous_state;
  protected boolean initialized;
  public UserInterface gui;


  GameState() {
    initialized = false;
  }

  abstract public void update(float dt);

  protected void drawAll() {}

  public void enter() {
    this.previous_state = NopeIslandGame.game;
    NopeIslandGame.game = this;
  }

  public void render() {

    // Draw everything that goes below the GUI
    drawAll();

    // Then draw the GUI.
    screen_batch.begin();
    gui.draw(screen_batch);
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


  RunningGameState(InputMultiplexer input_multiplexer, CommandManager cmd_manager) {
    // Creates a new interface
    gui = new RunningGameInterface(
        NopeIslandGame.game_window_width,
        NopeIslandGame.game_window_height);
    // Sets it's input multiplexer
    gui.setInputMultiplexer(input_multiplexer);
    // Sets it's command manager
    gui.setCommandManager(cmd_manager);

  }

  @Override
  public void update(float dt) {
    gui.update(dt);
  }

  @Override
  protected void drawAll() {
    // Draw all elements using whatever SpriteBatch's available.
  }

}





/** ========================= MAIN MENU STATE ========================= **/

class MainMenuGameState extends GameState {

  private static final String TAG = "MainMenuGameState";



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

}
