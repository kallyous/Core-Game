package com.sistemalivre.coregame;


import java.util.Vector;



public class GraphMapVertex {

  // ========================================== DATA ========================================== //

  // These are the same for the vertex location in both the graph and the tiled map matrix
  private int x, y;
  // Edges originating from this vertex
  private Vector<GraphMapEdge> edges;
  // Terrain and Feature (usually a wall or nothing)
  private int terrain, feature;
  // Reference to a entity, case there is one occupying the square.
  public Entity entity_on;
  // For algorithms
  private boolean visited = false;
  private GraphMapEdge source_edge;



  // ====================================== CONSTRUCTION ====================================== //

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

  public void addEdge(GraphMapEdge edge) {
    edges.add(edge);
  }



  // ======================================= GETTERS / SETTERS ================================= //

  // Algorithm Processing
  public void clear() {
    visited = false;
    source_edge = null;
  }
  public boolean isVisited() {
    return visited;
  }
  public GraphMapEdge getSourceEdge() {
    return source_edge;
  }
  public void visit() {
    visited = true;
  }
  public void setSourceEdge(GraphMapEdge edge) {
    source_edge = edge;
  }

  // X
  public int getX() {
    return x;
  }
  public void setX(int x) {
    this.x = x;
  }

  // Y
  public int getY() {
    return y;
  }
  public void setY(int y) {
    this.y = y;
  }

  // Edges
  public Vector<GraphMapEdge> getEdges() {
    return edges;
  }

  // Entity
  public Entity getEntityOn() {
    return entity_on;
  }
  public void putEntity(Entity entity) {
    entity_on = entity;
  }
  public void clearEntity() { entity_on = null; }

  // Terrain
  public int getTerrain() {
    return terrain;
  }
  public void setTerrain(int terrain_id) {
    this.terrain = terrain;
  }

  // Feature
  public int getFeature() {
    return feature;
  }
  public void setFeature(int feature_id) {
    this.feature = feature;
  }

}
