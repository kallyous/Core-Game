package com.kallyous.nopeisland;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Queue;



// Thing to hold basic things about anything that does something
public class Entity extends Drawable implements GestureDetector.GestureListener {

  // ======================================== CONSTANTS ======================================= //

  // Each new entity type implements a new type of entity as public, static and final.
  public static final int GENERIC_ENTITY = 0;

  // Entity facing directions
  public static final int FACING_LEFT = 0;
  public static final int FACING_TOP = 1;
  public static final int FACING_RIGHT = 2;
  public static final int FACING_BOT = 3;



  // ========================================== DATA ========================================== //

  // ID tracker
  private static long last_used_id = 0;

  // Selected Entity tracker
  public static Entity selected_entity;

  // Entity type string. For internal use, as the system must decide what to show on the GUI
  // for the player about the selected entity based on what it is.
  private int entity_type;

  // Entity Effective Location
  private float x, y;
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

  // Collision Data
  private int colbox_width, colbox_height;

  // Graph Map this entity belongs/links to
  private GraphMap graph_map;

  // Facing direction
  private int facing_direction = FACING_BOT;

  // Path for movement
  private Queue<GraphMapVertex> active_path;

  // Turn won pass while are entities moving.
  private boolean entity_is_moving = false;



  // ======================================= CONSTRUCTION ===================================== //

  // Default Constructor
  Entity() {
    id = ++last_used_id;
    this.name = "entity" + String.valueOf(id);
    setDefaultColbox();
  }

  // Basic Constructor
  Entity(String name) {
    id = ++last_used_id;
    this.name = name + String.valueOf(id);
    setDefaultColbox();
  }

  // Basic Constructor with frame index
  Entity(String name, int region_index) {
    super(region_index);
    id = ++last_used_id;
    this.name = name + String.valueOf(id);
    setDefaultColbox();
  }

  // Custom Graphics Constructor with default dimensions (used on UI)
  Entity(String name, Texture texture, int region_index) {
    super(texture, region_index);
    id = ++last_used_id;
    this.name = name + String.valueOf(id);
    setDefaultColbox();
  }

  // Custom Everything Constructor (used on most things, like creatures and plants)
  Entity(String name, Texture texture, int sheet_cols, int sheet_rows, int region_index) {
    super(texture, sheet_cols, sheet_rows, region_index);
    id = ++last_used_id;
    this.name = name + String.valueOf(id);
    setDefaultColbox();
  }

  // Default Collision Box (32x32)
  public void setDefaultColbox() {
    setCollisionBox(32, 32);
  }



  // ========================================= LOGIC ========================================== //

  // Update entity status
  public void update(float state_time) {

  }

  // Collision Detection (from camera)
  public boolean collided(Vector3 point, Camera cam) {
    cam.unproject(point);
    if ( (point.x > getX() && point.x < getX() + colbox_width) &&
        (point.y > getY() && point.y < getY() + colbox_height) ) {
      return true;
    }
    else
      return false;
  }

  // Sprite Offsets Update
  private void refreshOffsets() {
    sprite.setX( getX() - getOffHoriz() );
    sprite.setY( getY() - getOffVert() );
  }



  // ========================================== ACTIONS ======================================= //

  // Default Entity Action
  public void defaultAction(Entity target_entity){

  }

  // Default Interaction
  public void defaultInteraction(Entity source_entity) {

  }

  // Context Menu
  public void openContextMenu(Entity source_entity) {

  }

  // Movement
  public void move() {

  }

  // ===================================== SETTERS/GETTERS ==================================== //

  // Facing Direction
  public int getDirection() {
    return facing_direction;
  }
  public void setDirection(int val) {
    facing_direction = val;
  }

  // Link to Graph Vertex
  public void linkGraphMap(GraphMap graph_map) {
    this.graph_map = graph_map;
  }
  public void enterVertexAt(int x, int y) {
    graph_map.getVertexAt(x,y).putEntity(this);
  }
  public void leaveVertex() {
    graph_map.getVertexAt(getTileX(), getTileY()).putEntity(null);
  }
  public GraphMap getGraphMap() {
    return graph_map;
  }

  // Collision Box
  public void setCollisionBox(int width, int height) {
    colbox_width = width;
    colbox_height = height;
  }
  public int getColbox_width() {
    return colbox_width;
  }
  public int getColboxHeight() {
    return colbox_height;
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
  public void setEntityType(int entity_type) {
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
    return x;
  }
  public void setX(float new_x) {
    x = new_x;
    refreshOffsets();
  }

  // Y
  public float getY() {
    return y;
  }
  public void setY(float new_y) {
    y = new_y;
    refreshOffsets();
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

  // Full Tile Positioning
  public void setTilePosition(int x, int y) {
    setTileX(x);
    setTileY(y);
    if (graph_map != null) {
      leaveVertex();
      enterVertexAt(x, y);
    }
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

  // Sprite Width and Height
  public int getSpriteWidth() {
    return sprite.getRegionWidth();
  }
  public int getSpriteHeight() {
    return sprite.getRegionHeight();
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

  // Movement Path
  public Queue<GraphMapVertex> getActivePath() {
    return active_path;
  }
  public void setActivePath(Queue<GraphMapVertex> path) {
    active_path = path;
  }

  // Moving
  public boolean isMoving() {
    return entity_is_moving;
  }
  public void setMoving(boolean val) {
    entity_is_moving = val;
  }
  public void startMoving() {

  }
  public void stopMoving() {

  }


  // =========================================== INPUT ======================================== //

  @Override
  public boolean touchDown(float x, float y, int pointer, int button) {
    return false;
  }

  @Override
  public boolean tap(float x, float y, int count, int button) {
    return false;
  }

  @Override
  public boolean longPress(float x, float y) {
    return false;
  }

  @Override
  public boolean fling(float velocityX, float velocityY, int button) {
    return false;
  }

  @Override
  public boolean pan(float x, float y, float deltaX, float deltaY) {
    return false;
  }

  @Override
  public boolean panStop(float x, float y, int pointer, int button) {
    return false;
  }

  @Override
  public boolean zoom(float initialDistance, float distance) {
    return false;
  }

  @Override
  public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
    return false;
  }

  @Override
  public void pinchStop() {

  }
}
