package com.kallyous.nopeisland;


import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;



/** ========================= WORLD MAP ========================= **/

public class WorldMap {

  private static final String TAG = "WorldMap";




// ========================= DATA BEGIN ========================= //

  public TiledMap tiled_map;

  public OrthographicCamera camera;

  public OrthogonalTiledMapRenderer otm_renderer;

  private ShapeRenderer shape_rederer;

  private SpriteBatch entities_batch;

  private Array<Entity> entities;

  private InputMultiplexer input_multiplexer;

  private boolean world_running = false;

  private GraphMap graph;

// ========================= DATA END ========================= //




// ========================= CREATION BEGIN ========================= //

  WorldMap(SpriteBatch entities_batch, Array<Entity> entities, InputMultiplexer input_multiplexer) {

    int s_width = (int)NopeIslandGame.game_window_width;
    int s_height = (int)NopeIslandGame.game_window_height;

    this.entities_batch = entities_batch;

    this.entities = entities;

    tiled_map = new TmxMapLoader().load("maps/debug_island/overworld_island.tmx");
    otm_renderer = new OrthogonalTiledMapRenderer(tiled_map);
    camera = new OrthographicCamera();
    camera.setToOrtho(false, s_width, s_height);

    shape_rederer = new ShapeRenderer();

    Entity.setCamera(camera);

    // Set initial camera position, relative to the tiled map
    camera.position.set(23*32, 40*32, 0);

    // Prepares graph map
    graph = new GraphMap(100, 100);

    // Loads all creatures from the map's creatures layer
    Array<Creature> lvl_creats = CreatureLoader.LoadCreaturesFromTiledMap(tiled_map);

    // Insert all loaded creatures into the game running world
    for (int i = 0; i < lvl_creats.size; i++)
      this.entities.add(lvl_creats.get(i));

    // Snaps every owned entity into the multiplexer
    setInputMultiplexer(input_multiplexer);

  }

// ========================= CREATION END ========================= //




// ========================= LOGIC BEGIN ========================= //

  // Draw collision boxes for entities

  // Draw grid



  public void update(float dt) {
    if (world_running)
      for (Entity ent : entities) ent.update(dt);
  }



  public void render() {

    otm_renderer.setView(camera);
    camera.update();
    otm_renderer.render();

    // Implementing debug for collision boxes
    shape_rederer.setProjectionMatrix(camera.combined);
    shape_rederer.begin(ShapeRenderer.ShapeType.Line);
    shape_rederer.setColor(Color.BLUE);
    shape_rederer.rect(23*32, 40*32, 64, 64);
    shape_rederer.end();

    drawEntities();

  }



  private void drawEntities() {

    // Start the batch thing
    entities_batch.begin();

    /* Camera Projection: This projects the objects position into the camera
     * point of view. It means that things will be rendered with their sprites positions
     * projected into the game world matrix or coordinate system, instead of
     * relative to the screen coordinates. */
    entities_batch.setProjectionMatrix(camera.combined);

    // Lets go and check all entities for drawing
    for (Entity ent : entities) {

      // Check, for each entity, if it is of a drawable type.
      switch (ent.type()) {
        case Entity.CREATURE:
          ((Creature)ent).graphic_comp.draw(entities_batch);
          break;
        case Entity.PLANT:
          Log.w(TAG + " - No plants implemetned");
          break;
        case Entity.STRUCTURE:
          Log.w(TAG + " - No structures implemented");
          break;
        case Entity.PICKUP:
          Log.w(TAG + " - No pickups implemented");
          break;
      }

    }

    // Finishes the batch thing and send the package to the GPU for rendering
    entities_batch.end();

    // Debug collision boxes
    shape_rederer.setProjectionMatrix(camera.combined);
    shape_rederer.begin(ShapeRenderer.ShapeType.Line);
    shape_rederer.setColor(Color.BLUE);
    // Lets go and check all entities for drawing
    for (Entity ent : entities) {
      // Check, for each entity, if it is of a drawable type.
      switch (ent.type()) {
        case Entity.CREATURE:
          ((Creature)ent).graphic_comp.drawCollBox(shape_rederer);
          break;
        case Entity.PLANT:
          Log.w(TAG + " - No plants implemetned");
          break;
        case Entity.STRUCTURE:
          Log.w(TAG + " - No structures implemented");
          break;
        case Entity.PICKUP:
          Log.w(TAG + " - No pickups implemented");
          break;
      }
    }
    shape_rederer.end();

  }



  public void suspend() {
    world_running = false;
    clearInputMultiplexer();
  }

  public void resume() {
    world_running = true;
    reloadInputMultiplexer();
  }

// ========================= LOGIC END ========================= //




// ========================= SETTERS / GETTERS BEGIN ========================= //

  public void setInputMultiplexer(InputMultiplexer input_multiplexer) {
    this.input_multiplexer = input_multiplexer;
    for (Entity ent : entities) input_multiplexer.addProcessor(ent);
  }

  public void reloadInputMultiplexer() {
    clearInputMultiplexer();
    for (Entity ent : entities) input_multiplexer.addProcessor(ent);
  }

  public void clearInputMultiplexer() {
    for (Entity ent : entities) input_multiplexer.removeProcessor(ent);
  }

// ========================= SETTERS / GETTERS END ========================= //


}
