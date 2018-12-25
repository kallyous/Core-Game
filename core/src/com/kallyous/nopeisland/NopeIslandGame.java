package com.kallyous.nopeisland;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;



/** ========================= GAME STARTS ========================= **/

public class NopeIslandGame extends ApplicationAdapter {

  private static final String TAG = "NopeIslandGame";




// ========================= DATA SETUP BEGIN ========================= //

  // Game States
  static MainMenuGameState main_menu_state = new MainMenuGameState();
  static RunningGameState running_state = new RunningGameState();

  // Commands Manager
  static CommandManager command_manager;

  // Default GUI interface texture
  public static Texture uiTexture;

  // Finite State Machine
  GameState game;

  // Window and Viewport
  float game_window_width, game_window_height;
  // Just like it sounds, we will store the game window size here, for later use.

  // Tracks time for animations and other time based events
  float state_time;

  // Graphical User Interface
  UserInterface gui;

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

  // Input Multiplexer
  InputMultiplexer input_multiplexer;

// ========================= DATA SETUP END ========================= //





// ========================= STARTUP BEGIN ========================= //
  @Override
  public void create() {

    // Jogo inicia na tela do menu principal.
    game = main_menu_state;

    // Prepares the Command Manager
    command_manager = new CommandManager();

    // Starts timer
    state_time = 0f;

    // Gets viewport sizes
    game_window_width = Gdx.graphics.getWidth();
    game_window_height = Gdx.graphics.getHeight();

    // Loads GUI graphics
    uiTexture = new Texture(Gdx.files.internal("graphic/interface.png"));

    // Initializes GUI
    gui = new UserInterface(game_window_width, game_window_height);
    gui.setCommandManager(command_manager);

    guiBatch = new SpriteBatch();
    /** Sprite batches takes sprites and prepares them to send to the GPU/CPU for rendering the
     final image. Very important stuff. Do your worship, human. */

    // Initilizes InputProcessor
    game_input_adapter = new GameInput();

    // Initializes InputMultiplexer
    input_multiplexer = new InputMultiplexer();

    // Adds current InputAdapter to the multiplexer stack
    //input_multiplexer.addProcessor(input_adapter);

    // Adds the GUI InputProcessor to the multiplexer.
    gui.setInputMultiplexer(input_multiplexer);

    // Sets the multiplexer as the active shit
    Gdx.input.setInputProcessor(input_multiplexer);

  }

// ========================= STARTUP END ========================= //





// ========================= MAIN LOOP BEGIN ========================= //


  @Override
  public void render() {

    // Some openGL stuff.
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // Increment elapsed time
    state_time += Gdx.graphics.getDeltaTime();

    // ------------------------- GAME LOGIC START ------------------------- //



    // Receive inputs and generate commands
    game.handleInput();

    // Execute all commands until command queue is empty
    command_manager.flushCommands();

    // Update the game state
    game.update(state_time);

    // Update GUI. TODO: This shall be done inside game.update()
    gui.update(state_time);

    // ------------------------- GAME LOGIC START ------------------------- //

    // ------------------------- RENDER START ------------------------- //

    // Graphical User Interface Rendering
    guiBatch.begin();
    gui.draw(guiBatch);
    guiBatch.end();

    // ------------------------- RENDER END ------------------------- //

  }

// ========================= MAIN LOOP END ========================= //

}





// ========================= INPUT BEGIN ========================= //

class GameInput extends InputAdapter {

  private static final String TAG = "GameInput";

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    System.out.println(TAG + ": Screen Touched at (" + screenX + ", " + screenY + ")");
    return false;
  }

}



class InputHandler {

  private static final String TAG = "InputHandler";

private Command select;

  InputHandler() {}

  public void handleInput() {
    /**
      if (toque/clique detectado) {
        Entregue à GUI e pergunte se ela capturou o toque/clique;
        if (GUI capturou o toque) return;
        Entregue ao gerencioador de entidades e pergunte se ele capturou o toque;
        if (EntityManager capturou o toque) return;
        Entregue ao WorldMap. Ele sempre captura os toques.
        return;
     */
  }

}

// ========================= INPUT END ========================= //





// ========================= GAME STATE CLASSES BEGIN ========================= //

abstract class GameState {

  private static final String TAG = "GameState";

  GameState() {}
  abstract void enter();
  abstract void update(float dt);
  abstract void handleInput();
}



class RunningGameState extends GameState {

  private static final String TAG = "RunningGameState";

  void enter(){ }
  void handleInput() { }
  void update(float dt) { }
}



class MainMenuGameState extends GameState {

  private static final String TAG = "MainMenuGameState";

  void enter(){ }
  void handleInput() { }
  void update(float dt) { }
}

// ========================= GAME STATE CLASSES END ========================= //
