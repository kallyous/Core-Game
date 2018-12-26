package com.kallyous.nopeisland;



import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;



/** ========================= RUNNING GAME GUI ========================= **/

// GUI for the main menu or title screen
public class RunningGameInterface extends UserInterface {

  private static final String TAG = "RunningGameInterface";



  // ========================= CONSTRUCTION BEGIN ========================= //

  RunningGameInterface(float width, float height) {

    super(width, height);

    elements = new Array<UiElement>(2);



// ------------------------- UiElement - Main Menu Button -------------------------- //

    // Main Menu Button (setup and add)
    elements.add( new UiElement("btn_main_menu", 8)  {

      // Custom debug tag
      private static final String TAG = "MainMenuButton";

      // Overrides the touchDown() input event to generate the desired command
      @Override
      public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (collidedScreen(screenX, screenY)) {
          System.out.println(TAG + ": Main Menu Button touched, sending OpenMainMenuCommand to manager.");
          NopeIslandGame.command_manager.sendCommand(new OpenMainMenuCommand(this));
          return true;
        }
        return false;
      }
    });

    // Enable de desired command for this UiElement to consume.
    elements.get(0).command_comp.enableCommand("OpenMainMenuCommand");

    // Set the entity/button location on the screen
    elements.get(0).setPosition(10, screen_height - 42);



// ------------------------- UiElement - Pass Turn Button -------------------------- //

    // Pass Turn Buttom
    elements.add( new UiElement("btn_pass", 4) );
    elements.get(1).setPosition(screen_width - 42, screen_height - 42);

  }

  // ========================= CONSTRUCTION END ========================= //

}
