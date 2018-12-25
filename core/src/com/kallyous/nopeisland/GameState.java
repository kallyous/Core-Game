package com.kallyous.nopeisland;



import com.badlogic.gdx.InputMultiplexer;



/** ========================= GAME STATES - FSM ========================= **/

abstract public class GameState {

  private static final String TAG = "GameState";

  GameState() {}
  abstract public void enter(GameState previous_state);
  abstract public void update(float dt);
  abstract public void render();
  abstract public void exit();
}





/** ========================= RUNNING STATE ========================= **/

class RunningGameState extends GameState {

  private static final String TAG = "RunningGameState";

  private boolean initialized = false;
  private GameState previous_state;

  @Override
  public void enter(GameState previous_state) {
    this.previous_state = previous_state;
  }


  @Override
  public void update(float dt) {

  }


  @Override
  public void render() {

  }


  @Override
  public void exit() {
    // TODO: 25/12/18 Dispose of any unnecessary resources
    initialized = false;
    NopeIslandGame.game = previous_state;
  }
}





/** ========================= MAIN MENU STATE ========================= **/

class MainMenuGameState extends GameState {

  private static final String TAG = "MainMenuGameState";



  private GameState previous_state;
  private boolean initialized = false;
  public UserInterface gui;



  MainMenuGameState(InputMultiplexer input_multiplexer, CommandManager cmd_manager) {

    gui = new UserInterface(
        NopeIslandGame.game_window_width,
        NopeIslandGame.game_window_height);

    gui.setInputMultiplexer(input_multiplexer);

    gui.setCommandManager(cmd_manager);

  }


  @Override
  public void enter(GameState previous_state) {
    this.previous_state = previous_state;
  }


  @Override
  public void update(float dt) {

  }


  @Override
  public void render() {

  }


  @Override
  public void exit() {
    initialized = false;
    NopeIslandGame.game = previous_state;
  }
}
