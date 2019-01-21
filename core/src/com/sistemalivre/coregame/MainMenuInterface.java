package com.sistemalivre.coregame;

// ========================= MAIN MENU GUI ========================= //

// GUI for the main menu or title screen
public class MainMenuInterface extends UserInterface {

  protected static final String TAG = "MainMenuInterface";



// ========================= CONSTRUCTION ========================= //

  MainMenuInterface(float width, float height) {

    super(width, height, 3);


// --------------- UiElement - Start Button ---------------- //

    // New Anonymous UiElement Sub-Class
    UiElement start_btn = new UiElement(
        "start_continue_btn",
        AssetManager.texture("DefaultInterface"),
        346, 154, 108, 44) {

      // Custom debug tag
      private final String TAG = "PlayGameButton";

      // Overrides the touchDown() input event to generate the desired command
      @Override
      public boolean touchUp(int screenX, int screenY,
                             int pointer, int button) {
        if (collidedScreen(screenX, screenY)) {
          Log.i(TAG, "Play game touched, sending RunGameCommand to manager.");
          CommandManager.sendCommand(new RunGameCommand());
          return true;
        }
        return false;
      }
    };


    // Register button on hash table
    Game.entities.put(start_btn.getName(), start_btn);

    // Set the entity/button location on the screen
    start_btn.setPosition(
        (screen_width - start_btn.getWidth()) / 2,
        (screen_height/2) + (start_btn.getHeight() + margin)
        );

    // Tint the sprite (blue)
    start_btn.graphic_comp.sprite.setColor(0.5f, 0.5f, 1.0f, 1f);

    // Adds the button to the elements list/vector
    elements.add(start_btn);


// --------------- UiElement - Exit Game Button ---------------- //

    // New Anonymous UiElement Sub-Class
    UiElement exit_game_btn = new UiElement(
        "exit_game_btn",
        AssetManager.texture("DefaultInterface"),
        346, 154, 108, 44) {

      // Custom debug tag
      private final String TAG = "ExitButton";

      // Overrides the touchDown() input event to generate the desired command
      @Override
      public boolean touchUp(int screenX, int screenY,
                             int pointer, int button) {
        if (collidedScreen(screenX, screenY)) {
          Log.i(TAG, "Exit game touched, sending ExitCommand to manager.");
          CommandManager.sendCommand(new ExitCommand());
          return true;
        }
        return false;
      }

    };


    // Register button on hash table
    Game.entities.put(exit_game_btn.getName(), exit_game_btn);

    // Set the entity/button location on the screen
    exit_game_btn.setPosition(
        (screen_width - start_btn.getWidth()) / 2,
        (screen_height/2) - margin
    );

    // Tint the sprite (red)
    exit_game_btn.graphic_comp.sprite.setColor(1f, 0.5f, 0.5f, 1f);

    // Adds the button to the elements list/vector
    elements.add(exit_game_btn);


// ------------------------- Game Title Test -------------------------- //

    TextElement game_title = new TextElement(
        "game_title_main","Nope Island Game");

    Game.entities.put(game_title.getName(), game_title);

    game_title.setPosition(margin, screen_height - margin - 24);

    elements.add(game_title);

  }

}
