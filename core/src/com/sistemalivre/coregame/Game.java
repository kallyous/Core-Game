package com.sistemalivre.coregame;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Hashtable;



// ========================= CORE GAME STARTS ========================= //

public class Game extends ApplicationAdapter {

  private static final String TAG = "NopeIslandGame";



// ============================== DATA SETUP ============================== //

  /** This flag is used within the main loop to keep then game running and
    stop it when exit command has been issued. */
  public static boolean game_running = true;

  /** This is a pseudo-service locator for the game static assets. For any class
    to have access to images, sounds, fonts and etc, it just needs a reference
    to this object. */
  public static AssetManager asset_manager;

  public static World world;

  /** Finite State Machine - This one holds the current game state.
    The game is actually split into states. Each state holds almost everything
    related to it's doings. Things that are shared among game states are either
    held by the superclass GameState or the Game itself. */
  public static GameState game;

  // Available Game States
  static MainMenuGameState main_menu_state;
  static RunningGameState running_state;

  // Window and Viewport dimensions
  public static float game_window_width, game_window_height;

  /** Input Multiplexer for managing all different input source types.
    All input handling objects shall register to this multiplexer.
    No exceptions. Also, each game state swap must perform two things: The
    leaving game state must remove itself and childrens from the multiplexer,
    and the entering state must register itself and childrens in this
    multiplexer. */
  public static InputMultiplexer input_multiplexer;

  /** Keeps all game entities organized and accessible. To any class to have
    access to every entity, it only needs a reference to this hashtable.
    Entities register themselves in this hashtable automatically,
    when they are created. */
  public static ObjectMap<String, Entity> entities;

  // Tracks time for animations and other time based events
  float state_time;

  /** Batch render for the GUI
   We use a separate sprite batch's for game world stuff and UI elements,
   because of projection.
   While all stuff are draw relative to the world coordinates, UI elements
   are draw relative to the screen coordinates, for obvious reasons (you don't
   want to drag menu buttons out of the screen when panning the world, do you?).
   Also, is worth to remember that UI shall be rendered/draw after everything
   else, so it is always at the top/front of all other things. */
  SpriteBatch guiBatch;





// ============================ STARTUP BEGIN ============================ //
  @Override
  public void create() {

    Log.LOG_LEVEL = Log.VERBOSE;

    // Starts timer
    state_time = 0f;

    // Gets viewport sizes
    game_window_width = Gdx.graphics.getWidth();
    game_window_height = Gdx.graphics.getHeight();

    asset_manager = new AssetManager();

    input_multiplexer = new InputMultiplexer();

    entities = new ObjectMap<>();

    /** Command Manager - One to rule them all.
     No input goes straight into the controlled entity. All input generate
     commands and sends those commands to te Command Manager.
     The command manager flushes, and boradcasts all commands every
     cycle, at the beginning.
     When initialized, CommandManager declares one entity, for running no-target
     commands. Because of this, CommandManager MUST be initialized AFTER the
     entities hashtable is initialized. **/
    CommandManager.setup();

    // Sets active input multiplexer
    Gdx.input.setInputProcessor(input_multiplexer);

    /** Sprite Batch exclusive for GUI, since it doesn't uses camera projection.
     Sprite batches takes sprites and prepares them to send to the GPU/CPU
     for rendering the final image.
     Very important stuff. Do your worship, human. */
    guiBatch = new SpriteBatch();

    // Creates the game world
    world = new World(input_multiplexer);

    // Prepares the running game state, by making several connections.
    running_state = new RunningGameState(input_multiplexer, world);
    running_state.setScreenbatch(guiBatch);
    running_state.clear(); // Stupid hack to clean the input multiplexer.
    // TODO: Set a more elegant solution.

    // Prepares the main menu game state, by making several connections.
    main_menu_state = new MainMenuGameState(input_multiplexer);
    main_menu_state.setScreenbatch(guiBatch);
    /** Since the game actually starts at the main menu, we don't have to
     clean the input multiplexer. */

    // Sets game to the initial game state, Main Menu
    main_menu_state.switchTo(main_menu_state);

  }





// =============================== MAIN LOOP =============================== //

  @Override
  public void render() {

    // Double-Buffering internal procedures from LibGDX
    glActions();



  // ------------------------- LOGIC / RENDER START ------------------------- //

    // Increment elapsed time
    state_time += Gdx.graphics.getDeltaTime();

    // Update the game state
    game.update(state_time);

    // Execute all commands until command queue is empty
    CommandManager.flushCommands();

    // Render the result
    game.render();

    // Triggers game shutdown if it's flag has been raised
    if (!game_running) gameShutdown();

  }





// ------------------------------  glActions ------------------------------ //

  private void glActions() {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
  }



// ----------------------------- GameShutdown ----------------------------- //

  private void gameShutdown() {
    Log.i(TAG, "Game no longer running. Shutting down. ");
    pause();
    dispose();
    System.exit(0);
  }

}




