package com.sistemalivre.coregame;


import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;



// ===================== GAME STATES SUPERCLASS - FSM ===================== //

abstract public class GameState {

  private static final String TAG = "GameState";



// ========================= DATA SETUP ========================= //

  // Holds reference to the current game world map or level
  static WorldMap world;

  // For single back actions support
  protected static GameState previous_state;

  // Multiplexer to be used within the current state
  protected InputMultiplexer input_multiplexer;

  // SpriteBatch for rendering current state GUI
  protected SpriteBatch screen_batch;

  // Current state GUI
  protected UserInterface gui;

  // Flags if current state has been initialized, for saving overhead resources
  protected boolean initialized;




// ============================= CONSTRUCTION ============================= //

  // Default constructor only needs to create a new input multiplexer
  GameState() {
    initialized = false;
  }




// ============================== ABSTRACTION ============================== //

  // Deve ajustar as paradas
  abstract public void init();

  // The update is obligatory
  abstract public void update(float dt);

  // Libera os recursos para próximo estado
  abstract public void clear();




// ================================ LOGIC ================================ //

  // Each state may have many other things to draw beyond the GUI
  protected void drawAll() {}


  public void switchTo(GameState new_state) {

    // Call the clear for releasing resources
    clear();

    // Switches the game state on the main loop
    previous_state = this;

    // Sets the new state
    CoreGame.game = new_state;

    // Initializes the new game state
    CoreGame.game.init();

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

}





// ============================ RUNNING STATE ============================ //

class RunningGameState extends GameState {

  private static final String TAG = "RunningGameState";



// ============================= CONSTRUCTION ============================= //

  RunningGameState(CommandManager cmd_manager,
                   InputMultiplexer input_multiplexer) {

    // Creates a new interface
    gui = new RunningGameInterface(
        CoreGame.game_window_width,
        CoreGame.game_window_height);

    // Creates new input multiplexer
    this.input_multiplexer = input_multiplexer;

    // Initializes the world map.
    world = new WorldMap( new SpriteBatch(),
        new Array<Entity>(), input_multiplexer );

  }




// ================================ LOGIC ================================ //

  @Override
  public void init() {

    Log.d(TAG, "init() ");

    gui.setInputMultiplexer(input_multiplexer);

    world.resume();

  }


  @Override
  public void update(float dt) {
    world.update(dt);
    gui.update(dt);
  }


  @Override
  protected void drawAll() {
    world.render();
  }


  @Override
  public void clear() {
    gui.unsetInputMultiplexer(input_multiplexer);
    world.suspend();
  }

}





// =========================== MAIN MENU STATE =========================== //

class MainMenuGameState extends GameState {

  private static final String TAG = "MainMenuGameState";



// ============================== DATA SETUP ============================== //

  private InputMultiplexer input_multiplexer;




// ========================= CONSTRUCTION BEGIN ========================= //

  MainMenuGameState(CommandManager command_manager,
                    InputMultiplexer input_multiplexer) {

    gui = new MainMenuInterface(
        CoreGame.game_window_width,
        CoreGame.game_window_height);

    // Cretes new input multiplexer
    this.input_multiplexer = input_multiplexer;

  }




// ========================= LOGIC BEGIN ========================= //

  @Override
  public void init() {
    // Each game state handles input it's own way.
    Log.d(TAG, "init() ");
    gui.setInputMultiplexer(input_multiplexer);
  }


  @Override
  public void update(float dt) {
    gui.update(dt);
  }


  @Override
  public void clear() {
    gui.unsetInputMultiplexer(input_multiplexer);
  }

}

