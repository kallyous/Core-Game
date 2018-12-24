package com.kallyous.nopeisland;


import java.util.Vector;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



public class UserInterface {

  // ======================================= CONSTANTS ======================================== //

  // Default UI graphics
  private Texture uiTexture = NopeIslandGame.uiTexture;



  // =================================== INNER CLASS UiElement ================================ //
  class UiElement extends Entity {

    UiElement() {
      super("uiElement", uiTexture, 0);
    }

    UiElement(String name, int region_index) {
      super(name, uiTexture, region_index);
    }

    UiElement(String name, Texture texture) {
      super(name, texture, 0);
    }

    UiElement(String name, Texture texture, int region_index) {
      super(name, texture, region_index);
    }

    UiElement(String name, Texture texture, int sheet_cols, int sheet_rows, int region_index) {
      super(name, texture, sheet_cols, sheet_rows, region_index);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
      if (collidedScreen(screenX, screenY)) {
        System.out.println("Entity " + this.getName() + " touched.");

        int curr_index = getRegionIndex();
        if ( curr_index < getTextureRegionLength() -1 )  setRegionIndex(curr_index + 1);
        else setRegionIndex(0);

        return true;
      }
      return false;
    }

  }



  // ======================================== DATA ============================================ //

  // Screen Dimensions
  private static float screen_width, screen_height;

  // Array holding the UI elements
  public Vector<UiElement> elements;




  // ====================================== CONSTRUCTION ====================================== //

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

  // Loads and sets up external InputMultiplexer
  public void setInputMultiplexer(InputMultiplexer inputMultiplexer) {
    for (UiElement elem : elements) {
      System.out.println("Created " + elem.getName());
      inputMultiplexer.addProcessor(elem);
    }
  }



  // ======================================== LOGIC =========================================== //

  // Update All UiElement's
  public void update(Entity selected_entity) {
    return;
  }

  // Draw All UiElement's
  public void draw(SpriteBatch batch) {
    for (UiElement elem : elements) {
      elem.sprite.draw(batch);
    }
  }

}
