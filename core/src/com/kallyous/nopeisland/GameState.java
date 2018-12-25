package com.kallyous.nopeisland;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



/** ======================== GAME STATES SUPERCLASS - FSM ======================== **/

abstract public class GameState {

  private static final String TAG = "GameState";


  // For single back actions support
  public static GameState previous_state;


  protected InputMultiplexer input_multiplexer;
  protected SpriteBatch screen_batch;
  protected boolean initialized;
  public UserInterface gui;


  // Default constructor only needs to create a new input multiplexer
  GameState() {
    initialized = false;
    input_multiplexer = new InputMultiplexer();
  }


  // The update is obligatory
  abstract public void update(float dt);


  // Each state may have many other things to draw beyond the GUI
  protected void drawAll() {}


  public void enter(GameState new_state) {
    // Call the clear for releasing resources
    clear();
    // Switches the game state on the main loop
    previous_state = NopeIslandGame.game;
    NopeIslandGame.game = new_state;
    // Initializes stuff, like the InputMultiplexer
    init();
  }


  protected void init() {
    // Each game state handles input it's own way.
    Gdx.input.setInputProcessor(input_multiplexer);
  }


  public void render() {

    // Draw everything that goes below the GUI
    drawAll();

    // Then draw the GUI on the top
    screen_batch.begin();
    gui.draw(screen_batch);
    screen_batch.end();

  }


  public void setScreenbatch(SpriteBatch batch) {
    screen_batch = batch;
  }

  // Deactivate any necessary shit, release any unnecessary crap
  public void clear() {}

}





/** ========================= RUNNING STATE ========================= **/

class RunningGameState extends GameState {

  private static final String TAG = "RunningGameState";


  RunningGameState(CommandManager cmd_manager) {
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



  MainMenuGameState(CommandManager cmd_manager) {

    gui = new MainMenuInterface(
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