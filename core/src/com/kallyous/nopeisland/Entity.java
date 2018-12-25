package com.kallyous.nopeisland;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;




/** ========================= ENTITY SUPERCLASS ========================= **/

abstract public class Entity implements InputProcessor {

  private static final String TAG = "Entity";




// ========================= CONSTANTS BEGIN ========================= //

  // Each new entity type implements a new type of entity as public, static and final.
  public static final int GENERIC_ENTITY = 0;

  // Entity facing directions
  public static final int FACING_LEFT = 0;
  public static final int FACING_TOP = 1;
  public static final int FACING_RIGHT = 2;
  public static final int FACING_BOT = 3;

// ========================= CONSTANTS END ========================= //




// ========================= DATA BEGIN ========================= //

  // ID tracker
  private static long last_used_id = 0;

  // Selected Entity tracker
  public static Entity selected_entity;

  // Entity type string. For internal use, as the system must decide what to show on the GUI
  // for the player about the selected entity based on what it is.
  private int entity_type;

  // Entity Effective Location
  private float x_location, y_location;
  // Entity dimensions
  private int width, height;
  // Entity name
  private String name;
  // Entity display name
  private String display_name;
  // Entity ID
  private long id;
  // Player Controllable (default is false)
  private boolean player_controllable = false;

  // Facing direction (default bottom)
  private int facing_direction = FACING_BOT;

  /* There may be several different types of command interpreters, so we
  reserve a special place here, with the superclass CommandComponent. */
  protected CommandComponent command_comp;

// ========================= DATA END ========================= //




// ========================= CONSTRUCTION BEGIN ========================= //

  // Default Constructor
  Entity() {
    id = ++last_used_id;
    name = "entity" + String.valueOf(id);
    command_comp = new CommandComponent(this);
    setDefaultDimensions();
  }

  // Basic Constructor
  Entity(String name) {
    id = ++last_used_id;
    this.name = name + String.valueOf(id);
    command_comp = new CommandComponent(this);
    setDefaultDimensions();
  }

  // Default Collision Box (32x32)
  public void setDefaultDimensions() {
    width = 32;
    height = 32;
  }

// ========================= CONSTRUCTION END ========================= //




// ========================= LOGIC BEGIN ========================= //

  // Update entity status
  abstract public void update(float dt);

  // Pipes commands to it's command component
  public boolean executeCommand(Command command) {
    return command_comp.execute(command);
  }

  // Collision Detection (from camera/world)
  public boolean collidedWorld(Vector3 point, Camera cam) {
    cam.unproject(point);
    if ( (point.x > x_location && point.x < x_location + width) &&
        (point.y > y_location && point.y < y_location + height) ) {
      System.out.println(TAG + ".collidedWorld() detected world collision on " + name);
      return true;
    }
    else
      return false;
  }

  // Collision Detection from Screen
  public boolean collidedScreen(int screenX, int screenY) {
    int h = Gdx.graphics.getHeight();
    if ( (screenX > x_location && screenX < x_location + width) &&
        ( (screenY > h - y_location - height) && (screenY < h - y_location) ) ) {
      System.out.println(TAG + ": collidedScreen() detected screen collision on " + name);
      return true;
    }
    return false;
  }

// ========================= LOGIC END ========================= //




// ========================= ACTIONS BEGIN ========================= //

  // Default Entity's Action
  public void defaultAction(Entity target_entity){}

  // Default Interaction with this entity
  public void defaultInteraction(Entity source_entity) {}

// ========================= ACTIONS END ========================= //




// ========================= SETTERS/GETTERS BEGIN ========================= //

  // Facing Direction
  public int getFaceDirection() {
    return facing_direction;
  }
  public void setFaceDirection(int val) {
    facing_direction = val;
  }

  // Return info from the entity
  public String getInfo() {
    return display_name;
  }

  // ID
  public long getId() {
    return id;
  }

  // Entity Type
  public int getEntityType() {
    return entity_type;
  }

  // Control Over Entity
  public boolean isControllable() {
    return player_controllable;
  }
  public void setControllable(boolean val) {
    player_controllable = val;
  }

  // X
  public float getX() {
    return x_location;
  }
  public void setX(float new_x) {
    x_location = new_x;
  }

  // Y
  public float getY() {
    return y_location;
  }
  public void setY(float new_y) {
    y_location = new_y;
  }

  // Tile X (Defaults assumes a 32*32 tiles. TODO: Make this value dynamic.
  public int getTileX() {
    return (int)(getX()/32);
  }
  public void setTileX(int x) {
    setX(x*32);
  }

  // Tile Y (Defaults assumes a 32*32 tiles. TODO: Make this value dynamic.
  public int getTileY() {
    return (int)(getY()/32);
  }
  public void setTileY(int y) {
    setY(y*32);
  }

  // Full Positioning
  public void setPosition(float new_x, float new_y){
    setX(new_x);
    setY(new_y);
  }

  // Width
  public int getWidth() {
    return width;
  }
  public void setWidth(int new_width) {
    width = new_width;
  }

  // Height
  public int getHeight() {
    return height;
  }
  public void setHeight(int new_height) {
    height = new_height;
  }

  // Name
  public String getName() {
    return name;
  }
  public void setName(String new_name) {
    name = new_name;
  }

  // Display Name
  public String getDisplayName() {
    return display_name;
  }
  public void setDisplayName(String name) {
    display_name = name;
  }

// ========================= SETTERS/GETTERS END ========================= //




// ========================= INPUT BEGIN ========================= //

  @Override
  public boolean keyDown(int keycode) {
    return false;
  }


  @Override
  public boolean keyUp(int keycode) {
    return false;
  }


  @Override
  public boolean keyTyped(char character) {
    return false;
  }


  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if (collidedScreen(screenX, screenY)) {
      System.out.println(TAG + ": " + this.getName() + " touched.");
      return true;
    }
    return false;
  }


  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    return false;
  }


  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }


  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }


  @Override
  public boolean scrolled(int amount) {
    return false;
  }
}

// ========================= INPUT END ========================= //
