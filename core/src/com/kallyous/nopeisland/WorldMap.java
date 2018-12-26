package com.kallyous.nopeisland;


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

  private Array<Entity> entities_to_draw;

// ========================= DATA END ========================= //




// ========================= CREATION BEGIN ========================= //

  WorldMap(SpriteBatch entities_batch, Array<Entity> entities_to_draw) {

    this.entities_batch = entities_batch;
    this.entities_to_draw = entities_to_draw;

    tiled_map = new TmxMapLoader().load("levels/the_bug_island/overworld.tmx");
    camera = new OrthographicCamera();
    otm_renderer = new OrthogonalTiledMapRenderer(tiled_map);

    int s_width = (int)NopeIslandGame.game_window_width;
    int s_height = (int)NopeIslandGame.game_window_height;

    camera.setToOrtho(false, s_width, s_height);

    // Set initial camera position, relative to the tiled map
    camera.position.set(50*32, 47*32, 0);
    
  }

// ========================= CREATION END ========================= //




// ========================= LOGIC BEGIN ========================= //

  public void update(float dt) {}



  public void render() {

    otm_renderer.setView(camera);
    camera.update();
    otm_renderer.render();

    drawEntities();

  }



  private void drawEntities() {

    entities_batch.begin();

    entities_batch.setProjectionMatrix(camera.combined);

    for (Entity ent : entities_to_draw) {
      // TODO: 26/12/18 Draw fuckers
    }

    entities_batch.end();

  }

// ========================= LOGIC END ========================= //

}