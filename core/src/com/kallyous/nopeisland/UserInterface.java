package com.kallyous.nopeisland;


import java.util.Vector;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;




/** ========================= GRAPHICAL USER INTERFACE ========================= **/

public class UserInterface {

  // Debug tag
  private static final String TAG = "UserInterface ";




// ========================= DATA BEGIN ========================= //

  // Default UI graphics
  protected Texture uiTexture = NopeIslandGame.uiTexture;

  // Screen Dimensions
  protected static float screen_width, screen_height;

  // UiElement's margin (in pixels)
  protected static int margin;

  // Array holding the UI elements
  public Vector<UiElement> elements;

// ========================= DATA END ========================= //




// ========================= CONSTRUCTION BEGIN ========================= //

  // Main Constructor
  UserInterface(float width, float height) {

    // Screen Dimensions Initialization
    screen_width = width;
    screen_height = height;

    // Define the margin
    margin = 16;

    // Elements Array Initialization
    elements = new Vector<UiElement>();

  }

// ========================= CONSTRUCTION END ========================= //




// ========================= LOGIC BEGIN ========================= //

  // Update all elements based on game sate
  public void update(float dt) {
    for (UiElement elem : elements) elem.update(dt);
  }

  // Draw All UiElement's
  public void draw(SpriteBatch batch) {
    for (UiElement elem : elements) elem.draw(batch);
  }

  // Sets all UiElements' input connections
  public void setInputMultiplexer(InputMultiplexer multiplexer) {
    for (UiElement elem : elements) multiplexer.addProcessor(elem);
  }

  public void setCommandManager(CommandManager manager) {
    for (UiElement elem : elements) manager.addListenner(elem);
  }

// ========================= LOGIC END ========================= //

}





/** ========================= GUI ELEMENT ========================= **/

class UiElement extends Entity {

  private static final String TAG = "UiElement ";


// ========================= DATA BEGIN ========================= //

  GraphicComponent graphic_comp;

// ========================= DATA END ========================= //




// ========================= CONSTRUCTION BEGIN ========================= //

  UiElement() {
    super("uiElement");
    setupComponents(0);
  }

  UiElement(String name, int region_index) {
    super(name);
    setupComponents(region_index);
  }

  UiElement(String name, Texture texture, int region_x, int region_y,
            int width, int height) {
    super(name);

    graphic_comp = new GraphicComponent(
        this, texture,
        region_x, region_y, width, height
    );

    command_comp = new CommandComponent(this);

  }

  private void setupComponents(int region_index) {
    graphic_comp = new GraphicComponent(this, region_index);

    command_comp = new CommandComponent(this)
    {
      @Override
      public boolean execute(Command command) {
        if (isItForMe(command)) {
          System.out.println(TAG + ": Comando exclusivo de " + owner.getName());
          System.out.println(TAG + ": " + owner.getName() + " executando "
              + command.getTAG() );
          return true;
        }
        return false;
      }
    };

  }

// ========================= CONSTRUCTION END ========================= //




// ========================= LOGIC BEGIN ========================= //

  @Override
  public void update(float dt) {
    graphic_comp.update(dt);
  }

  public void draw(SpriteBatch batch) {
    graphic_comp.draw(batch);
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if (collidedScreen(screenX, screenY)) {
      NopeIslandGame.command_manager.sendCommand( new SelectCommand(this) );
    }
    return false;
  }

// ========================= LOGIC END ========================= //

}
