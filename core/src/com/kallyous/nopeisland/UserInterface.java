package com.kallyous.nopeisland;


import java.util.Vector;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;




/** ========================= GRAPHICAL USER INTERFACE ========================= **/

public class UserInterface {



// ========================= DATA BEGIN ========================= //

  // Default UI graphics
  private Texture uiTexture = NopeIslandGame.uiTexture;

  // Screen Dimensions
  private static float screen_width, screen_height;

  // Array holding the UI elements
  public Vector<UiElement> elements;

// ========================= DATA END ========================= //




// ========================= CONSTRUCTION BEGIN ========================= //

  // Main Constructor
  UserInterface(float width, float height) {

    // Screen Dimensions Initialization
    screen_width = width;
    screen_height = height;

    // Elements Array Initialization
    elements = new Vector<UiElement>();

    // Actions Buttom
    elements.add( new UiElement("btn_actions", 1) );
    elements.get(0).setPosition(10, 10);

    // Options Buttom
    elements.add( new UiElement("btn_options", 8) );
    elements.get(1).setPosition(10, screen_height - 42);

    // Inventory Buttom
    elements.add( new UiElement("btn_inventory", 5) );
    elements.get(2).setPosition(screen_width - 42, 10);

    // Pass Turn Buttom
    elements.add( new UiElement("btn_pass", 4) );
    elements.get(3).setPosition(screen_width - 42, screen_height - 42);
  }

// ========================= CONSTRUCTION END ========================= //


// ========================= RENDER / UPDATE BEGIN ========================= //

  // Update all elements based on game sate
  public void update(float dt) {
    for (UiElement elem : elements) elem.update(dt);
  }

  // Draw All UiElement's
  public void draw(SpriteBatch batch) {
    for (UiElement elem : elements) elem.draw(batch);
  }

// ========================= RENDER / UPDATE END ========================= //

}





/** ========================= GUI ELEMENT ========================= **/

class UiElement extends Entity {



// ========================= DATA BEGIN ========================= //

  GraphicComponent graphic_comp;

// ========================= DATA END ========================= //




// ========================= CONSTRUCTION BEGIN ========================= //

  UiElement() {
    super("uiElement");
    graphic_comp = new GraphicComponent(this);
  }

  UiElement(String name, int region_index) {
    super(name);
    graphic_comp = new GraphicComponent(this, region_index);
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

// ========================= LOGIC END ========================= //

}
