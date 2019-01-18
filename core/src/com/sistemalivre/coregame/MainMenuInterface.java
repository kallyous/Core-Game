package com.sistemalivre.coregame;


import com.badlogic.gdx.utils.Array;



// ========================= MAIN MENU GUI ========================= //

// GUI for the main menu or title screen
public class MainMenuInterface extends UserInterface {

  private static final String TAG = "MainMenuInterface";



// ========================= CONSTRUCTION ========================= //

  MainMenuInterface(float width, float height) {

    super(width, height);

    elements = new Array<UiElement>(2);


// --------------- UiElement - Start Button ---------------- //

    // New Anonymous UiElement Sub-Class
    UiElement start_btn = new UiElement(
        "start_continue_btn",
        CoreGame.asset_manager.textures.get("DefaultInterface"),
        346, 154, 108, 44) {

      // Custom debug tag
      private final String TAG = "PlayGameButton";

      // Overrides the touchDown() input event to generate the desired command
      @Override
      public boolean touchUp(int screenX, int screenY,
                             int pointer, int button) {
        if (collidedScreen(screenX, screenY)) {
          Log.i(TAG, "Play game touched, sending RunGameCommand to manager.");
          CoreGame.command_manager.sendCommand(new RunGameCommand(this));
          return true;
        }
        return false;
      }
    };


    // Register button on hash table
    CoreGame.entities.put(start_btn.getName(), start_btn);

    // Enable de desired command for this UiElement to consume.
    start_btn.command_comp.enableCommand( "RunGameCommand" );
    // TODO: 25/12/18 Set the RunGameCommand to be consumed by other entity

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
        CoreGame.asset_manager.textures.get("DefaultInterface"),
        346, 154, 108, 44) {

      // Custom debug tag
      private final String TAG = "ExitButton";

      // Overrides the touchDown() input event to generate the desired command
      @Override
      public boolean touchUp(int screenX, int screenY,
                             int pointer, int button) {
        if (collidedScreen(screenX, screenY)) {
          Log.i(TAG, "Exit game touched, sending ExitCommand to manager.");
          CoreGame.command_manager.sendCommand(new ExitCommand(this));
          return true;
        }
        return false;
      }

    };


    // Register button on hash table
    CoreGame.entities.put(exit_game_btn.getName(), exit_game_btn);

    // Enable de desired command for this UiElement to consume.
    exit_game_btn.command_comp.enableCommand( "ExitCommand" );
    // TODO: 25/12/18 Set the ExitCommand to be consumed by other entity

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

    CoreGame.entities.put(game_title.getName(), game_title);

    game_title.command_comp.enableCommand("SetTextContentCommand");

    game_title.setPosition(margin, screen_height - margin - 24);

    elements.add(game_title);

  }

}
