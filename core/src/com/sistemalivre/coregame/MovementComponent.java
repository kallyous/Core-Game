package com.sistemalivre.coregame;




/** ========================= MovementComponent ========================= **/

class MovementComponent {

  private static final String TAG = "MovementComponent";




// ========================= DATA BEGIN ========================= //

  public Entity entity;

  public GraphMap graph;

  public GraphMapVertex curr_vertex;

// ========================= DATA END ========================= //




// ========================= CREATION BEGIN ========================= //

  MovementComponent(Entity entity) {
    this.entity = entity;
  }

// ========================= CREATION END ========================= //




// ========================= LOGIC BEGIN ========================= //

  public void moveToTileAt(float x, float y) {
    int tile_x = (int)(x/Global.tile_size);
    int tile_y = (int)(y/Global.tile_size);
    Pathfinder.findPath();
  }

// ========================= LOGIC END ========================= //

}

