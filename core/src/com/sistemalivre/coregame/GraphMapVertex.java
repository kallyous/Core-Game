package com.sistemalivre.coregame;


import java.util.Vector;



// ========================== GraphMapVertex ========================== //

public class GraphMapVertex {

  public static final String TAG = "GraphMapVertex";



  // =========================== DATA =========================== //

  // Reference to a entity, case there is one occupying the square.
  Entity entity_on;

  /** These are the same for the vertex location in both the graph
   and the tiled map matrix. **/
  private int x, y;

  // Edges originating from this vertex
  private Vector<GraphMapEdge> edges;

  // Terrain and Feature (usually a wall or nothing)
  private int terrain, feature;

  // For algorithms
  private boolean visited = false;
  private GraphMapEdge source_edge;




  // ======================= CONSTRUCTION ======================= //

  GraphMapVertex() {
    terrain = 0;
    feature = 0;
    entity_on = null;
    edges = new Vector<GraphMapEdge>();
  }


  GraphMapVertex(int x, int y) {
    this.x = x;
    this.y = y;
    terrain = 0;
    feature = 0;
    entity_on = null;
    edges = new Vector<GraphMapEdge>();
  }


  GraphMapVertex(int x, int y, int terrain, int feature) {
    this.x = x;
    this.y = y;
    this.terrain = terrain;
    this.feature = feature;
    entity_on = null;
    edges = new Vector<GraphMapEdge>();
  }


  void addEdge(GraphMapEdge edge) {
    edges.add(edge);
  }




  // ======================== GET / SET ================== //

  // Algorithm Processing
  void clear() {
    visited = false;
    source_edge = null;
  }


  boolean isVisited() {
    return visited;
  }


  GraphMapEdge getSourceEdge() {
    return source_edge;
  }


  void visit() {
    visited = true;
  }


  void setSourceEdge(GraphMapEdge edge) {
    source_edge = edge;
  }


  // X
  int getX() {
    return x;
  }


  // Y
  int getY() {
    return y;
  }


  // Edges
  Vector<GraphMapEdge> getEdges() {
    return edges;
  }


  // Entity
  Entity getEntityOn() {
    return entity_on;
  }
  void putEntity(Entity entity) {
    entity_on = entity;
  }
  void clearEntity() { entity_on = null; }


  // Terrain
  int getTerrain() {
    return terrain;
  }
  void setTerrain(int terrain_id) {
    this.terrain = terrain;
  }


  // Feature
  int getFeature() {
    return feature;
  }
  void setFeature(int feature_id) {
    this.feature = feature;
  }

}
