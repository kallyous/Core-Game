package com.sistemalivre.coregame;



// ========================= MovementComponent ========================= //

class MovementComponent {

  private static final String TAG = "MovementComponent";



// ========================= DATA ========================= //

  public Entity entity;

  public GraphMap graph;

  public GraphMapVertex curr_vertex;




// ========================= COSNTRUCTION ========================= //

  MovementComponent(Entity entity) {
    this.entity = entity;
  }




// ========================= LOGIC ========================= //

  public void moveToTileAt(float x, float y) {
    int tile_x = (int)(x/Global.tile_size);
    int tile_y = (int)(y/Global.tile_size);
    Pathfinder.findPath();
  }

}

