package com.kallyous.nopeisland;


import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;



/** ========================= WORLD MAP ========================= **/

public class WorldMap {

  private static final String TAG = "WorldMap";




// ========================= DATA BEGIN ========================= //

  public TiledMap tiled_map;

  public OrthographicCamera camera;

  public OrthogonalTiledMapRenderer otm_renderer;

  private SpriteBatch entities_batch;

  private Array<Entity> entities;

  private InputMultiplexer input_multiplexer;

  private boolean world_running = false;

// ========================= DATA END ========================= //




// ========================= CREATION BEGIN ========================= //

  WorldMap(SpriteBatch entities_batch, Array<Entity> entities, InputMultiplexer input_multiplexer) {

    int s_width = (int)NopeIslandGame.game_window_width;
    int s_height = (int)NopeIslandGame.game_window_height;

    this.entities_batch = entities_batch;
    this.entities = entities;

    tiled_map = new TmxMapLoader().load("levels/the_bug_island/overworld.tmx");
    otm_renderer = new OrthogonalTiledMapRenderer(tiled_map);
    camera = new OrthographicCamera();
    camera.setToOrtho(false, s_width, s_height);

    Entity.setCamera(camera);

    // Set initial camera position, relative to the tiled map
    camera.position.set(50*32, 47*32, 0);

    // Set a (?) sprite creature right into the world.
    Creature c = new Creature("Interrogator");
    c.setPosition(50*32, 47*32);
    c.command_comp.enableCommand("SelectCommand");
    this.entities.add(c);

    // Snaps every owned entity into the multiplexer
    setInputMultiplexer(input_multiplexer);

  }

// ========================= CREATION END ========================= //




// ========================= LOGIC BEGIN ========================= //

  public void update(float dt) {
    if (world_running)
      for (Entity ent : entities) ent.update(dt);
  }



  public void render() {

    otm_renderer.setView(camera);
    camera.update();
    otm_renderer.render();

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
          System.out.println("No plants implemetned");
          break;
        case Entity.STRUCTURE:
          System.out.println("No structures implemented");
          break;
        case Entity.PICKUP:
          System.out.println("No pickups implemented");
          break;
      }

    }

    // Finishes the batch thing and send the package to the GPU for rendering
    entities_batch.end();

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