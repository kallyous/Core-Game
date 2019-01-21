package com.sistemalivre.coregame;


import com.badlogic.gdx.utils.Array;



// ========================= RUNNING GAME GUI ========================= //

// GUI for the main menu or title screen
public class RunningGameInterface extends UserInterface {

  protected static final String TAG = "RunningGameInterface";



// ========================= CONSTRUCTION ========================= //

  RunningGameInterface(float width, float height) {

    super(width, height, 3);



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
          CommandManager.sendCommand(new OpenMainMenuCommand());
          return true;
        }
        return false;
      }
    });

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
          Log.d(TAG, "Pass Turn Button touched.");
          Log.w(TAG, "Turns not implemented.");
          return true;
        }
        return false;
      }

    };

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

    EntityState state = new EntityState(sel_txt) {

      private static final String TAG = "DynamicTextState";

      @Override
      String getName() {
        return TAG;
      }

      @Override
      void init() {}

      @Override
      void leave() {}
    };

    state.enabled_commands.add("SetTextContentCommand");

    sel_txt.states.add(state);

    // Register the element in the hash map
    CoreGame.entities.put(sel_txt.getName(), sel_txt);

    // Scale down the font
    sel_txt.label.setFontScale(.6f);

    // Positioning
    sel_txt.setPosition(128, screen_height - 48);

    // Add to the GUI elements array
    elements.add(sel_txt);

  }

}
