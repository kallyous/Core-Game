package com.kallyous.nopeisland;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.InputMultiplexer;



/** ========================= GAME STARTS ========================= **/

public class NopeIslandGame extends ApplicationAdapter {

  private static final String TAG = "NopeIslandGame";




// ========================= DATA SETUP BEGIN ========================= //

  // Game running flag
  public static boolean game_running = true;

  // Default GUI interface texture
  public static Texture uiTexture;

  // Finite State Machine
  public static GameState game;

  // Window and Viewport dimensions
  public static float game_window_width, game_window_height;

  // Input Multiplexer
  public static InputMultiplexer input_multiplexer;


  // Game States
  static MainMenuGameState main_menu_state;
  static RunningGameState running_state;

  // Commands Manager
  static CommandManager command_manager;


  // Tracks time for animations and other time based events
  float state_time;

  // Batch render for the GUI
  SpriteBatch guiBatch;
  /**
   We use a separate sprite batch's for game world stuff and UI elements because of projection.
   While all stuff are draw relative to the world coordinates, UI elements are draw relative
   to the screen coordinates, for obvious reasons (you don't want drag menu buttons out of the
   screen when panning the world, do you?).
   Also, is worth to remember that UI shall be rendered/draw after everything else, so it is always
   at the top/front of all other things.
   */

  // Input Processor
  GameInput game_input_adapter;

// ========================= DATA SETUP END ========================= //





// ========================= STARTUP BEGIN ========================= //
  @Override
  public void create() {

    // Starts timer
    state_time = 0f;

    // Gets viewport sizes
    game_window_width = Gdx.graphics.getWidth();
    game_window_height = Gdx.graphics.getHeight();

    // Initializes InputProcessor
    game_input_adapter = new GameInput();

    // Initializes InputMultiplexer
    input_multiplexer = new InputMultiplexer();

    // Prepares the Command Manager
    command_manager = new CommandManager();

    // Loads GUI graphics
    uiTexture = new Texture(Gdx.files.internal("graphic/interface.png"));

    // SpriteBach exclusivo para GUI, pois será o únco a não utilizar projeção no mapa
    guiBatch = new SpriteBatch();
    /** Sprite batches takes sprites and prepares them to send to the GPU/CPU for rendering the
     final image. Very important stuff. Do your worship, human. */

    // Sets the multiplexer as the active shit
    //Gdx.input.setInputProcessor(input_multiplexer);

    // Prepara estado do menu principal do jogo, connectando-o com controladores globais
    main_menu_state = new MainMenuGameState(command_manager);
    main_menu_state.setScreenbatch(guiBatch);

    // Prepara estado de execução do jogo, conectando-o com controladores globais
    running_state = new RunningGameState(command_manager);
    running_state.setScreenbatch(guiBatch);

    // Põe jogo em seu estado inicial.
    main_menu_state.enter(main_menu_state);

  }

// ========================= STARTUP END ========================= //





// ========================= MAIN LOOP BEGIN ========================= //


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
    command_manager.flushCommands();

    // Render the result
    game.render();

    // Triggers game shutdown if it's flag has been raised
    if (!game_running) gameShutdown();

    // ------------------------- LOGIC / RENDER END ------------------------- //




  }

// ========================= MAIN LOOP END ========================= //





// -------------------------  glActions ------------------------- //

  private void glActions() {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
  }

// -------------------------------------------------------------- //




// ------------------------- GameShutdown ------------------------- //

  private void gameShutdown() {
    System.out.println(TAG + ": Game no longer running. Shutting down. ");
    pause();
    dispose();
    System.exit(0);
  }

// -------------------------------------------------------------- //


}




