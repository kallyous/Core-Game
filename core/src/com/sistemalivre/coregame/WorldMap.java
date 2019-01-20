package com.sistemalivre.coregame;


import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;

import java.util.Vector;

// ========================= WORLD MAP ========================= //

public class WorldMap implements GestureListener, InputProcessor {

  private static final String TAG = "WorldMap";



// ========================= DATA ========================= //

  TiledMap tiled_map;

  OrthographicCamera camera;

  OrthogonalTiledMapRenderer otm_renderer;

  private Array<Entity> entities;

  private Vector<SupportUIElement> support_gui;

  private ShapeRenderer shape_renderer;

  private SpriteBatch entities_batch;

  private InputMultiplexer input_multiplexer;

  private boolean world_running = false;

  private GraphMap graph;

  private GestureDetector gesture_detector;




// ========================= CONSTRUCTION ========================= //

  WorldMap(InputMultiplexer input_multiplexer) {

    gesture_detector = new GestureDetector(this);

    setInputMultiplexer(input_multiplexer);

    int s_width = (int)CoreGame.game_window_width;
    int s_height = (int)CoreGame.game_window_height;

    entities_batch = new SpriteBatch();

    entities = new Array<>();

    support_gui = new Vector<>();

    tiled_map = new TmxMapLoader().load(
        "maps/debug_island/overworld_island.tmx");

    otm_renderer = new OrthogonalTiledMapRenderer(tiled_map);

    camera = new OrthographicCamera();
    camera.setToOrtho(false, s_width, s_height);

    shape_renderer = new ShapeRenderer();

    Entity.setCamera(camera);

    // Set initial camera position, relative to the tiled map
    camera.position.set(23*32, 40*32, 0);

    // Prepares graph map
    graph = new GraphMap(100, 100);

    // Loads all creatures from the map's creatures layer
    Array<Creature> lvl_creats =
        CreatureLoader.LoadCreaturesFromTiledMap(tiled_map);

    // Insert all loaded creatures into the game running world
    for (int i = 0; i < lvl_creats.size; i++)
      addEntity(lvl_creats.get(i));

  }




// ========================= LOGIC ========================= //

  void update(float dt) {
    if (world_running) {
      for (Entity ent : entities)
        ent.update(dt);
      for (SupportUIElement elem : support_gui)
        elem.update(dt);
    }
  }


  void render() {

    otm_renderer.setView(camera);
    camera.update();
    otm_renderer.render();

    drawGrid();

    drawSupportGUI();

    drawEntities();

  }


  void suspend() {
    world_running = false;
    clearInputMultiplexer();
  }


  void resume() {
    world_running = true;
    reloadInputMultiplexer();
  }


  void addEntity(Entity ent) {
    entities.add(ent);
    reloadInputMultiplexer();
  }


  void remEntity(Entity ent) {
    input_multiplexer.removeProcessor(ent);
    entities.removeValue(ent, true);
  }


  private void drawEntities() {

    // Start the batch thing
    entities_batch.begin();

    /** Camera Projection:
     This projects the objects position into the camera point of view.
     It means that things will be rendered with their sprites positions
     projected into the game world matrix or coordinate system, instead of
     relative to the screen coordinates. **/
    entities_batch.setProjectionMatrix(camera.combined);

    // Lets go and check all entities for drawing
    for (Entity ent : entities) {

      // Check, for each entity, if it is of a drawable type.
      switch (ent.type()) {
        case Entity.CREATURE:
          ((Creature)ent).graphic_comp.draw(entities_batch);
          break;
        case Entity.PLANT:
          Log.w(TAG, "No plants implemetned");
          break;
        case Entity.STRUCTURE:
          Log.w(TAG, "No structures implemented");
          break;
        case Entity.PICKUP:
          Log.w(TAG, "No pickups implemented");
          break;
        case Entity.MOVEMARK:
          ((SupportUIElement)ent).graphic_comp.draw(entities_batch);
          break;
      }

    }

    // Finishes the batch thing and send the package to the GPU for rendering
    entities_batch.end();


    // Debug collision boxes
    shape_renderer.setProjectionMatrix(camera.combined);
    shape_renderer.begin(ShapeRenderer.ShapeType.Line);
    // Lets go and check all entities for drawing their bounding boxes
    for (Entity ent : entities) {
      // Check, for each entity, if it is of a drawable type.
      switch (ent.type()) {
        case Entity.CREATURE:
          ((Creature)ent).graphic_comp.drawCollBox(shape_renderer);
          break;
        case Entity.PLANT:
          Log.w(TAG, "No plants implemented");
          break;
        case Entity.STRUCTURE:
          Log.w(TAG, "No structures implemented");
          break;
        case Entity.PICKUP:
          Log.w(TAG, "No pickups implemented");
          break;
      }
    }
    shape_renderer.end();

  }


  private void drawGrid() {
    shape_renderer.begin(ShapeRenderer.ShapeType.Line);
    shape_renderer.setColor(Color.GRAY);

    for (int i = 0; i < 101; i++) {
      shape_renderer.line(0, i*Global.tile_size,
          100*Global.tile_size, i*Global.tile_size);
      for (int j = 0; j < 101; j++) {
        shape_renderer.line(j*Global.tile_size, 0,
            j*Global.tile_size, 100*Global.tile_size);
      }
    }

    shape_renderer.end();
  }


  private void drawSupportGUI() {

    if (support_gui.size() > 0) {

      entities_batch.begin();
      entities_batch.setProjectionMatrix(camera.combined);

      for (SupportUIElement elem : support_gui)
        elem.graphic_comp.draw(entities_batch);

      entities_batch.end();
    }

  }


  void addSupportElem(SupportUIElement elem) {
    support_gui.add(elem);
    reloadInputMultiplexer();
  }


  void remSupportElem(SupportUIElement elem) {
    input_multiplexer.removeProcessor(elem);
    support_gui.remove(elem);
  }




// ========================= GET / SET ========================= //

  void setInputMultiplexer(InputMultiplexer input_multiplexer) {
    this.input_multiplexer = input_multiplexer;
    input_multiplexer.addProcessor(this); // That's for InputProcessor
    input_multiplexer.addProcessor(gesture_detector);
  }


  void reloadInputMultiplexer() {
    clearInputMultiplexer();
    for (Entity ent : entities)
      input_multiplexer.addProcessor(ent);
    for (SupportUIElement elem : support_gui)
      input_multiplexer.addProcessor(elem);
    input_multiplexer.addProcessor(this); // That's for InputProcessor
    input_multiplexer.addProcessor(gesture_detector);
  }


  void clearInputMultiplexer() {
    for (Entity ent : entities)
      input_multiplexer.removeProcessor(ent);
    for (SupportUIElement elem : support_gui)
      input_multiplexer.removeProcessor(elem);
    input_multiplexer.removeProcessor(this); // That's for InputProcessor
    input_multiplexer.removeProcessor(gesture_detector);
  }


  Array<Entity> entities() {
    return entities;
  }

  Vector<SupportUIElement> getSupportGUI() {
    return support_gui;
  }



// ========================= INPUT ========================= //

  @Override
  public boolean tap(float screenX, float screenY,
                     int count, int button) {
    Log.d(TAG, "Map touched.");

    CommandManager.sendCommand(new DestroyWorldSupportGUICommand());

    if (Entity.selected_entity != null) {

      // Prepares a vector with the coordinates of the touch on the screen.
      Vector3 target = new Vector3(screenX, screenY, 0);

      /** Using the world camera, unprojects this coordinates from the screen
       into the world coordinates. **/
      camera.unproject(target);

      // Enforces grid alignment
      int rest = (int)target.x % Global.tile_size;
      target.x = (int)target.x - rest;
      rest = (int)target.y % Global.tile_size;
      target.y = (int)target.y - rest;

      switch (Entity.selected_entity.type()) {
        case Entity.CREATURE:
          if (Entity.selected_entity.isControllable()) {

            // Locate entrance vertex
            GraphMapVertex entrance = graph.getVertexAt(
                (int)Entity.selected_entity.getX()/Global.tile_size,
                (int)Entity.selected_entity.getY()/Global.tile_size
            );

            // Locate exit vertex
            GraphMapVertex exit = graph.getVertexAt(
                (int)target.x/Global.tile_size,
                (int)target.y/Global.tile_size
            );

            // Send the command
            CommandManager.sendCommand(
                new TracePathCommand(Entity.selected_entity, entrance, exit));

          }
          else {
            Log.d(TAG, Entity.selected_entity.getName()
                + " is not controllable.");
          }
          break;
        default:
          Log.d(TAG, "No action for the selected entity.");
          break;
      }
    }
    else if (button == 1){ // Right button. Places fucker aligned to the grid.
      Vector3 location = new Vector3(screenX, screenY, 0);
      camera.unproject(location);

      // Forces grid alignment
      int rest = (int)location.x % Global.tile_size;
      location.x = (int)location.x - rest;
      rest = (int)location.y % Global.tile_size;
      location.y = (int)location.y - rest;

      String args[] = {
          "Rodrigo Macarone", "fighter", "true",
          String.valueOf(location.x),
          String.valueOf(location.y)
      };

      CommandManager.sendCommand( new LoadAndPlaceCreatureCommand(args) );

    }
    return true;
  }


  @Override
  public boolean touchDown(float x, float y,
                           int pointer, int button) {
    return false;
  }


  @Override
  public boolean longPress(float x, float y) {
    return false;
  }


  @Override
  public boolean fling(float velocityX, float velocityY,
                       int button) {
    return false;
  }


  @Override
  public boolean pan(float x, float y,
                     float deltaX, float deltaY) {
    float effective_drag_x = deltaX*camera.zoom;
    float effective_drag_y = deltaY*camera.zoom;
    Vector3 new_position = new Vector3(
        camera.position.x - effective_drag_x,
        camera.position.y + effective_drag_y,
        0
    );
    camera.position.set(new_position);
    return true;
  }


  @Override
  public boolean panStop(float x, float y,
                         int pointer, int button) {
    return false;
  }


  @Override
  public boolean zoom(float initialDistance, float distance) {
    return false;
  }


  @Override
  public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
                       Vector2 pointer1, Vector2 pointer2) {
    return false;
  }


  @Override
  public void pinchStop() {

  }


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
//    int z = MathUtils.clamp(amount, 1, 4);
    float zoom = camera.zoom + amount;
    camera.zoom = (float)MathUtils.clamp(zoom, 1.0, 10.0);
    return true;
  }

}
