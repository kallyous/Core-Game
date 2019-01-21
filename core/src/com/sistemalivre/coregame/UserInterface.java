package com.sistemalivre.coregame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;



// ========================= GUI SUPERCLASS ========================= //

// Superclass on top of what all GUI shall be built
public class UserInterface {

  protected static final String TAG = "UserInterface ";



// ========================= DATA ========================= //

  // Screen Dimensions
  protected static float screen_width, screen_height;

  // UiElement's margin (in pixels)
  protected static int margin;

  // Array holding the UI elements
  public Array<UiElement> elements;

  // Interface name
  String name;




// ========================= CONSTRUCTION ========================= //

  // Main Constructor
  UserInterface(float width, float height, int initial_elements_count) {

    elements = new Array<>(false, initial_elements_count);

    // Screen Dimensions Initialization
    screen_width = width;
    screen_height = height;

    // Define the margin between elements
    margin = 16;

  }




// ========================= LOGIC ========================= //

  // Update all elements based on state sate
  public void update(float dt) {
    for (UiElement elem : elements) elem.update(dt);
  }


  // Draw All UiElement's
  public void draw(SpriteBatch batch) {
    for (UiElement elem : elements) elem.draw(batch);
  }


  // Sets all UiElements' input connections
  public void setInputMultiplexer(InputMultiplexer multiplexer) {
    Log.i(TAG, "Setting InputMultiplexer to "
        + multiplexer.toString() );
    for (UiElement elem : elements)
      multiplexer.addProcessor(elem);
  }


  // Remove elements from InputMultiplexer
  public void unsetInputMultiplexer(InputMultiplexer multiplexer) {
    for (UiElement elem : elements) multiplexer.removeProcessor(elem);
  }

}





// ========================= GUI ELEMENT ========================= //

// Superclass on top of what all GUI's elements shall be built
class UiElement extends Entity {

  private static final String TAG = "UiElement ";



// ========================= DATA ========================= //

  GraphicComponent graphic_comp;




// ========================= CONSTRUCTION ========================= //

  // Default constructor spans a (?) button
  UiElement(String name) {
    super(name);
    graphic_comp = new GraphicComponent(this);
  }


  // This one spans a button using the default GUI texture
  UiElement(String name, int region_index) {
    super(name);
    graphic_comp = new GraphicComponent(this);
    graphic_comp.setStandIndex(region_index);
  }


  /** Cosntructor that optionally does not initializes a graphic
    component, mostly for texts. **/
  UiElement(String name, boolean has_graphic_comp) {
    super(name);
    if (has_graphic_comp) graphic_comp = new GraphicComponent(this);
    else graphic_comp = null;
  }


  // Creates a GUI element of arbitrary texture and dimensions
  UiElement(String name, Texture texture, int region_x, int region_y,
            int width, int height) {

    super(name);

    graphic_comp = new GraphicComponent(
        this, texture,
        region_x, region_y, width, height
    );

  }




// ========================= LOGIC ========================= //

  // Updates graphic component
  @Override
  public void updateExtra(float dt) {
    graphic_comp.update(dt);
  }


  // Draw whatever is int graphic component
  public void draw(SpriteBatch batch) {
    graphic_comp.draw(batch);
  }


  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (collidedScreen(screenX, screenY)) {
      Log.v(TAG, this.getName() + " touched.");
      return true;
    }
    return false;
  }


  @Override
  public void dispose() {
    graphic_comp.destroy();
    graphic_comp = null;
  }

}






// ========================= TextElement ========================= //

class TextElement extends UiElement {

  private static final String TAG = "TextElement";



// ========================= DATA ========================= //

  public static final BitmapFont default_font = new BitmapFont(
      Gdx.files.internal("graphic/def-bitmap-font.fnt") );

  public Label label;

  protected Label.LabelStyle label_style;




// ========================= CONSTRUCTION ========================= //

  TextElement(String name, String content) {

    // Uses the version without a graphic component of UiElement
    super(name, false);

    label_style = new Label.LabelStyle();

    label_style.font = default_font;

    label_style.fontColor = Color.WHITE;

    label = new Label(content, label_style);

    this.setWidth( (int)label.getPrefWidth() );

    this.setHeight( (int)label.getPrefHeight() );

    Game.entities.put(name, this);

  }




// ========================= LOGIC ========================= //

  @Override
  public void updateExtra(float dt) {}


  @Override
  public void draw(SpriteBatch batch) {
    label.draw(batch, 1);
  }


  @Override
  public void setPosition(float x, float y) {
    this.label.setPosition(x, y);
    super.setPosition(x, y);
  }

}
