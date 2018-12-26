package com.kallyous.nopeisland;



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

    // Pass Turn Buttom SetTextContentCommand
    elements.add( new UiElement("btn_pass", 4) {

      // Custom debug tag
      private static final String TAG = "PassTurnButton";

      // Overrides the touchDown() input event to generate the desired command
      @Override
      public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (collidedScreen(screenX, screenY)) {
          System.out.println(TAG + ": Pass Turn Button touched, but no action implemented.");
          //NopeIslandGame.command_manager.sendCommand(new SetTextContentCommand());
          return true;
        }
        return false;
      }

    } );

    elements.get(1).setPosition(screen_width - 42, screen_height - 42);



// ------------------------- TextElement - Name of Entity -------------------------- //

    TextElement sel_txt = new TextElement("Nada selecionado");
    sel_txt.command_comp.enableCommand("SetTextContentCommand");
    sel_txt.label.setFontScale(.6f);
    sel_txt.setPosition(128, screen_height - 48);
    elements.add(sel_txt);

  }

  // ========================= CONSTRUCTION END ========================= //

}
