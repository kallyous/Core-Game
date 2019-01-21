package com.sistemalivre.coregame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

// ========================= ENTITY SUPERCLASS ========================= //

abstract public class Entity implements InputProcessor {

  private static final String TAG = "Entity";



// ========================== DATA ========================== //

  // Game world camera
  protected static OrthographicCamera world_camera;

// --------------- Entity Types ---------------- //

  // For undefined and/or invisible entities
  public static final int GENERIC = 0;

  // Graphical User Interface Elements
  public static final int GUI = 1;

  // Players, opponents...
  public static final int CREATURE = 2;

  // Trees, bushes...
  public static final int PLANT = 3;

  // Walls, chests, rocks on the floor...
  public static final int STRUCTURE = 4;

  // Things creatures can pass over and will trigger something when so
  public static final int PICKUP = 5;

  // Path markers for movement preview
  public static final int MOVEMARK = 6;



// ------------------------- Facing Directions -------------------------- //

  // Entity facing directions
  public static final int FACING_LEFT = 0;
  public static final int FACING_TOP = 1;
  public static final int FACING_RIGHT = 2;
  public static final int FACING_BOT = 3;



// ------------------------- Variables -------------------------- //

  /** ID tracker
    While using unique internal names is pretty convenient, string
    operations are slow. For later optimizations, we will work out a
    way to map the entities ID's to their objects and use integer
    operations on the hash tables when doing lookups. **/
  private static long last_used_id = 0;

  // Entity ID
  private long id;

  // Selected Entity tracker
  public static Entity selected_entity;

  // Type of entity. GENERIC by default, must be overridden by every subclass
  private int entity_type = GENERIC;

  // Entity Effective Location
  private float x_location, y_location;

  // Entity dimensions
  private int width, height;

  // Entity name
  private String name;

  // Entity display name
  private String display_name;

  // Player Controllable (default is false)
  private boolean player_controllable = false;

  // Facing direction (default bottom)
  private int facing_direction = FACING_BOT;

  /** Commands:
    There are two unifying features to all entities - They all have a location
    in the state world and they all at least CAN receive commands, to perform
    or be targets of actions. Therefore, all entities have (X,Y) coordinates
    and a Finite State Machine setup. Each state has a set of commands it can
    perform. **/
  Array<EntityState> states;

  // Holds the current state
  EntityState current_state;




// ========================= CONSTRUCTION ========================= //

  // General constructor
  Entity(String name) {

    // While not at use yet, we will keep record of id's for later use.
    id = ++last_used_id;

    /** Internal names must be unique.
      If the name is duplicated, the entity creation won't actuall fail.
      The new entity just won't be registered to the hash table and will
      call it's owndestruction method right after it's creation. **/
    this.name = name;

    states = new Array<>(4);
    EntityState s = new InactiveState(this);
    states.add(s);
    current_state = s;

    // By default we use a 32x32 pixels size.
    setDefaultDimensions();

    // The so important duplicated name checkup
    if (Game.entities.containsKey(name)) {

      // Debug warnings
      Log.d(TAG, "Entidade com nome " + name + " já existe. É o objeto "
          + Game.entities.get(name).toString() );
      Log.d(TAG, "Duplicatas não são permitidas," +
          " emitindo comando de autodestruição");

      // Issue self destruct command
      CommandManager.sendCommand(
          new DestroyEntityCommand(this) );

    }

    /** If name is free, register self into the hash table and proceed
      to the initial creation. **/
    else {

      // Hash table registration
      Game.entities.put(name, this);

      // Debug notification
      Log.i(TAG, "Adicionado " + name + " à Hashtable de entidades.");

    }

  }

  // Default Collision Box (32x32)
  public void setDefaultDimensions() {
    width = 32;
    height = 32;
  }




// ========================= ABSTRACTION BEGIN ========================= //

  abstract void updateExtra(float dt);

  /** Dispose of any resources and/or unplug from any trackers
    for entity destruction. **/
  abstract public void dispose();




// ========================= LOGIC BEGIN ========================= //

  void update(float dt) {
    current_state.update(dt);
    updateExtra(dt);
  }

  // Pipes commands to current_state
  boolean executeCommand(Command command) {
    return current_state.execute(command);
  }


  // Collision Detection (from camera/world)
  boolean worldTouched(Vector3 point) {

    world_camera.unproject(point);

    if ( (point.x > x_location && point.x < x_location + width) &&
        (point.y > y_location && point.y < y_location + height) ) {
      Log.d(TAG, "worldTouched() detected world collision on " + name);
      return true;
    }

    return false;
  }


  // Collision Detection from Screen
  boolean collidedScreen(int screenX, int screenY) {
    int h = Gdx.graphics.getHeight();
    if ((screenX > x_location && screenX < x_location + width) &&
        ((screenY > h - y_location - height) && (screenY < h - y_location))){
      Log.d(TAG, "collidedScreen() detected screen collision on " + name);
      return true;
    }
    return false;
  }


  /** Destruction:
    After calling the dispose() method, which shall dispose of all allocated
    assets and/or unplug the entity of any records/trackers, the method
    destroy() will finally unplug the entity from the hash table. **/
  void destroy() {
    Log.i(TAG, "Destroying " + this.toString() + "(" + name + ")");
    if (selected_entity == this) selected_entity = null;
    this.dispose();
    for (EntityState state : states) {
      state.owner = null;
      state = null;
    }
    states = null;
    Game.entities.remove(this.name);
  }

  boolean setState(String state_name) {
    for (EntityState available_state : states) {
      if (available_state.getName().equals(state_name)) {
        enterState(available_state);
        return true;
      }
    }
    return false;
  }

  boolean setState(EntityState new_state) {
    for (EntityState state : states) {
      if (state.getName().equals(new_state.getName())) {
        state = new_state;
        enterState(state);
        return true;
      }
    }
    return false;
  }

  protected void enterState(EntityState state) {
    current_state.leave();
    current_state = state;
    current_state.init();
  }




// ========================= GET / SET ========================= //

  public static long getLastUsedID() {
    return last_used_id;
  }

  public static void setCamera(OrthographicCamera new_cam) {
    world_camera = new_cam;
  }

  EntityState getState() {
    return current_state;
  }

  // Facing Direction
  public int getFaceDirection() {
    return facing_direction;
  }
  public void setFaceDirection(int val) {
    facing_direction = val;
  }


  // ID
  public long getId() {
    return id;
  }


  // Get some general data as string
  public String info() {
    return getDisplayName();
  }


  // Entity Type
  public int type() {
    return entity_type;
  }
  public void setType(int entity_type) {
    this.entity_type = entity_type;
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

  void alignToGrid() {
    alignX();
    alignY();
  }

  void alignX() {

    int full_x = (int)getX();
    int excess = full_x % Global.tile_size;
    int flat_x = full_x - excess;

    if (excess >= Global.tile_size/2)
      setX(flat_x + Global.tile_size);
    else
      setX(flat_x);

  }

  void alignY() {

    int full_y = (int)getY();
    int excess = full_y % Global.tile_size;
    int flat_y = full_y - excess;

    if (excess >= Global.tile_size/2)
      setY(flat_y + Global.tile_size);
    else
      setY(flat_y);

  }

  public int getTileX() {

    // Get integer version of X
    int world_x = (int)getX();

    // Gets how much it exceeds the current tile X
    int ex = world_x % Global.tile_size;

    // Removes excess, leaving us with a multiple of tile_size,
    // Then divides by tile_size, getting the current tile location.
    int x = (world_x - ex) / Global.tile_size;

    return x;
  }

  public void setTileX(int x) {
    setX(x*Global.tile_size);
  }

  public void setTileX(float x) {
    int world_x = (int)x;
    int ex = world_x % Global.tile_size;
    setTileX( (world_x - ex)/Global.tile_size );
  }


  public int getTileY() {

    // Get integer version of X
    int world_y = (int)getY();

    // Gets how much it exceeds the current tile X
    int ex = world_y % Global.tile_size;

    // Removes excess, leaving us with a multiple of tile_size,
    // Then divides by tile_size, getting the current tile location.
    int y = (world_y - ex) / Global.tile_size;

    return y;
  }

  public void setTileY(int y) {
    setY(y*Global.tile_size);
  }

  public void setTileY(float y) {
    int world_y = (int)y;
    int ex = world_y % Global.tile_size;
    setTileY( (world_y - ex)/Global.tile_size );
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




// ========================= INPUT ========================= //

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
  public boolean touchDown(int screenX, int screenY,
                           int pointer, int button) {
    return false;
  }


  @Override
  public boolean touchUp(int screenX, int screenY,
                         int pointer, int button) {
    if (collidedScreen(screenX, screenY)) {
      Log.d(TAG, this.getName() + " touched.");
      return true;
    }
    return false;
  }


  @Override
  public boolean touchDragged(int screenX, int screenY,
                              int pointer) {
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


