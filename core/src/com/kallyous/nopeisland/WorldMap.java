package com.kallyous.nopeisland;


import com.badlogic.gdx.maps.tiled.TiledMap;



public class WorldMap {
  
  GraphMap graph_map;
  TiledMap tiled_map;
  
  
  public WorldMap(GraphMap gm, TiledMap tm) {
    tiled_map = tm;
    graph_map = gm;
  }
  
  
  public TiledMap getTiledMap() {
    return tiled_map;
  }
  
  public GraphMap getGraph() {
    return graph_map;
  }
  
}
