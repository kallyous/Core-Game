package com.sistemalivre.coregame;


import com.badlogic.gdx.utils.Array;



// ========================= RUNNING GAME GUI ========================= //

// GUI for the main menu or title screen
public class RunningGameInterface extends UserInterface {

  private static final String TAG = "RunningGameInterface";



// ========================= CONSTRUCTION ========================= //

  RunningGameInterface(float width, float height) {

    super(width, height);

    elements = new Array<UiElement>(2);



// --------------- UiElement - Main Menu Button ---------------- //

    // Main Menu Button (setup and add)
    elements.add(
        new UiElement("btn_main_menu", 8)  {
      // Custom debug tag
      private static final String TAG = "MainMenuButton";
      // Overrides the touchDown() input event to generate the desired command
      @Override
      public boolean touchUp(int screenX, int screenY,
                             int pointer, int button) {
        if (collidedScreen(screenX, screenY)) {
          Log.d(TAG, "Main Menu Button touched," +
              " sending OpenMainMenuCommand to manager.");
          CoreGame.command_manager.sendCommand(
              new OpenMainMenuCommand());
          return true;
        }
        return false;
      }
    });

    // Enable de desired command for this UiElement to consume.
    elements.get(0).command_comp.enableCommand("OpenMainMenuCommand");

    // Set the entity/button location on the screen
    elements.get(0).setPosition(10, screen_height - 42);



// --------------- UiElement - Pass Turn Button ---------------- //

    // Pass Turn Buttom SetTextContentCommand
    UiElement btn_pass = new UiElement("btn_pass", 4) {

      // Custom debug tag
      private static final String TAG = "PassTurnButton";

      // Overrides the touchDown() input event to generate the desired command
      @Override
      public boolean touchUp(int screenX, int screenY,
                             int pointer, int button) {
        if (collidedScreen(screenX, screenY)) {
          Log.d(TAG, "Pass Turn Button touched, setting selection_name text");
          CoreGame.command_manager.sendCommand( new SelectCommand(this) );
          return true;
        }
        return false;
      }

    };


    // This entity will process it1s own selection
    // TODO: 27/12/18 All selections shall be handled somewhere else
    btn_pass.command_comp.enableCommand("SelectCommand");

    // Register in the hash map
    CoreGame.entities.put(btn_pass.getName(), btn_pass);

    // Positioning
    btn_pass.setPosition(screen_width - 42, screen_height - 42);

    // Add button to GUI elements array
    elements.add(btn_pass);



// --------------- TextElement - Name of Entity ---------------- //

    // Creates a text element to hold the name of the selected entitie
    TextElement sel_txt = new TextElement(
        "selection_name", "Nada selecionado") {
      private static final String TAG = "SelectionTextElement";
      @Override
      public void update(float dt) {
        if (Entity.selected_entity == null)
          this.label.setText("Nada selecionado");
        else
          this.label.setText( Entity.selected_entity.getDisplayName() );
      }
    };

    // Register the element in the hash map
    CoreGame.entities.put(sel_txt.getName(), sel_txt);

    // Enable the desired command
    sel_txt.command_comp.enableCommand("SetTextContentCommand");

    // Scale down the font
    sel_txt.label.setFontScale(.6f);

    // Positioning
    sel_txt.setPosition(128, screen_height - 48);

    // Add to the GUI elements array
    elements.add(sel_txt);

  }

}
