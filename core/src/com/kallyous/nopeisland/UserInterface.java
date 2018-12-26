package com.kallyous.nopeisland;



import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;



/** ========================= GUI SUPERCLASS ========================= **/

// Superclass on top of what all GUI shall be built
public class UserInterface {

  private static final String TAG = "UserInterface ";




// ========================= DATA SETUP BEGIN ========================= //

  // Screen Dimensions
  protected static float screen_width, screen_height;

  // UiElement's margin (in pixels)
  protected static int margin;

  // Array holding the UI elements
  public Array<UiElement> elements;

// ========================= DATA SETUP END ========================= //




// ========================= CONSTRUCTION BEGIN ========================= //

  // Main Constructor
  UserInterface(float width, float height) {

    // Screen Dimensions Initialization
    screen_width = width;
    screen_height = height;

    // Define the margin
    margin = 16;

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
    System.out.println(TAG + ": Setting InputMultiplexer to "
        + multiplexer.toString() );
    for (UiElement elem : elements) multiplexer.addProcessor(elem);
  }

  // Remove elementos from InputMultiplexer
  public void unsetInputMultiplexer(InputMultiplexer multiplexer) {
    for (UiElement elem : elements) multiplexer.removeProcessor(elem);
  }

  public void setCommandManager(CommandManager manager) {
    for (UiElement elem : elements) manager.addListenner(elem);
  }

// ========================= LOGIC END ========================= //

}





/** ========================= GUI ELEMENT ========================= **/

// Superclass on top of what all GUI's elements shall be built
class UiElement extends Entity {

  private static final String TAG = "UiElement ";




// ========================= DATA SETUP BEGIN ========================= //

  GraphicComponent graphic_comp;

// ========================= DATA SETUP END ========================= //




// ========================= CONSTRUCTION BEGIN ========================= //

  // Default constructor spans a (?) button
  UiElement() {
    super("uiElement");
    graphic_comp = new GraphicComponent(this, 0);
    command_comp = new CommandComponent(this);
  }



  // This one spans a button using the default GUI texture
  UiElement(String name, int region_index) {
    super(name);
    graphic_comp = new GraphicComponent(this, region_index);
    command_comp = new CommandComponent(this);
  }



  // Creates a GUI element of arbitrary texture and dimensions
  UiElement(String name, Texture texture, int region_x, int region_y,
            int width, int height) {

    super(name);

    graphic_comp = new GraphicComponent(
        this, texture,
        region_x, region_y, width, height
    );

    command_comp = new CommandComponent(this);

  }

// ========================= CONSTRUCTION END ========================= //




// ========================= LOGIC BEGIN ========================= //

  // Updates graphic component
  @Override
  public void update(float dt) {
    graphic_comp.update(dt);
  }

  // Draw whatever is int graphic component
  public void draw(SpriteBatch batch) {
    graphic_comp.draw(batch);
  }

  // By default, each button just issues a SelectCommand to itself (potentially useless)
  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if (collidedScreen(screenX, screenY)) {
      System.out.println(TAG + ": Sending a SelectCommand. ");
      NopeIslandGame.command_manager.sendCommand( new SelectCommand(this) );
    }
    return false;
  }

// ========================= LOGIC END ========================= //

}
