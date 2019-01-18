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



// ========================= WORLD MAP ========================= //

public class WorldMap implements GestureListener, InputProcessor {

  private static final String TAG = "WorldMap";



// ========================= DATA ========================= //

  public TiledMap tiled_map;

  public OrthographicCamera camera;

  public OrthogonalTiledMapRenderer otm_renderer;

  public Array<Entity> entities;

  private ShapeRenderer shape_renderer;

  private SpriteBatch entities_batch;

  private InputMultiplexer input_multiplexer;

  private boolean world_running = false;

  private GraphMap graph;

  private GestureDetector gesture_detector;




// ========================= CONSTRUCTION ========================= //

  WorldMap(
      SpriteBatch entities_batch, Array<Entity> entities,
      InputMultiplexer input_multiplexer) {

    int s_width = (int)CoreGame.game_window_width;
    int s_height = (int)CoreGame.game_window_height;

    this.entities_batch = entities_batch;

    this.entities = entities;

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
      this.entities.add(lvl_creats.get(i));

    gesture_detector = new GestureDetector(this);

    // Snaps every owned entity into the multiplexer
    setInputMultiplexer(input_multiplexer);

  }




// ========================= LOGIC ========================= //

  public void update(float dt) {
    if (world_running)
      for (Entity ent : entities) ent.update(dt);
  }


  public void render() {

    otm_renderer.setView(camera);
    camera.update();
    otm_renderer.render();

    drawGrid();

    drawEntities();

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


  public void addEntity(Entity ent) {
    this.entities.add(ent);
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


  public void suspend() {
    world_running = false;
    clearInputMultiplexer();
  }


  public void resume() {
    world_running = true;
    reloadInputMultiplexer();
  }




// ========================= GET / SET ========================= //

  public void setInputMultiplexer(InputMultiplexer input_multiplexer) {
    this.input_multiplexer = input_multiplexer;
    for (Entity ent : entities) input_multiplexer.addProcessor(ent);
    input_multiplexer.addProcessor(this); // That's for InputProcessor
    input_multiplexer.addProcessor(gesture_detector);
  }


  public void reloadInputMultiplexer() {
    clearInputMultiplexer();
    for (Entity ent : entities) input_multiplexer.addProcessor(ent);
    input_multiplexer.addProcessor(this); // That's for InputProcessor
    input_multiplexer.addProcessor(gesture_detector);
  }


  public void clearInputMultiplexer() {
    for (Entity ent : entities) input_multiplexer.removeProcessor(ent);
    input_multiplexer.removeProcessor(this); // That's for InputProcessor
    input_multiplexer.removeProcessor(gesture_detector);
  }




// ========================= INPUT ========================= //

  @Override
  public boolean touchDown(float x, float y,
                           int pointer, int button) {
    return false;
  }


  @Override
  public boolean tap(float screenX, float screenY,
                     int count, int button) {
    Log.d(TAG, "Map touched.");

    // Screen touch/click always clear the scene from any support ui elements.
    CoreGame.command_manager.sendCommand(
        new DestroyWorldEntitiesByTypeCommand(Entity.MOVEMARK) );

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
            CoreGame.command_manager.sendCommand(
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

      Creature pep = new Creature("Fucker_" + screenX + "_" + screenY);
      pep.setPosition(location.x, location.y);

      CoreGame.running_state.world.addEntity(pep);
    }
    return true;
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
