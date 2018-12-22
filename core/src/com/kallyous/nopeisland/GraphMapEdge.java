package com.kallyous.nopeisland;


public class GraphMapEdge {

  // ========================================== DATA ========================================== //

  private GraphMapVertex source_vertex;
  private GraphMapVertex target_vertex;
  private int weight;



  // ====================================== CREATION ========================================== //

  GraphMapEdge(GraphMapVertex source, GraphMapVertex target) {
    this.source_vertex = source;
    this.target_vertex = target;
    this.weight = 0;
  }

  GraphMapEdge(GraphMapVertex source, GraphMapVertex target, int weight) {
    this.source_vertex = source;
    this.target_vertex = target;
    this.weight = weight;
  }



  // ==================================== GETTERS / SETTERS =================================== //

  // Source Vertex
  public GraphMapVertex getSource() {
    return source_vertex;
  }
  public void setSource(GraphMapVertex source_vertex) {
    this.source_vertex = source_vertex;
  }

  // Target Vertex
  public GraphMapVertex getTarget() {
    return target_vertex;
  }
  public void setTarget(GraphMapVertex target_vertex) {
    this.target_vertex = target_vertex;
  }

  // Weight
  public int getWeight() {
    return weight;
  }
  public void setWeight(int weight) {
    this.weight = weight;
  }
}
