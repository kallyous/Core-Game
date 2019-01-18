package com.sistemalivre.coregame;


import java.util.Vector;



// ========================== GraphMap ========================== //

public class GraphMap {

  private static final String TAG = "GraphMap";



  // ===================== DATA ======================= //

  private GraphMapVertex[][] vertexes;
  private Vector<GraphMapEdge> edges;
  private int width, height;




  // ===================== CONSTRUCTION =================== //

  GraphMap(int width, int height) {
    this.width = width;
    this.height = height;
    this.build();
  }

  private void build() {
    int x, y;
    GraphMapEdge edge;

    vertexes = new GraphMapVertex[height][];
    edges = new Vector<GraphMapEdge>();

    for (y = 0; y < height; y++) {
      vertexes[y] = new GraphMapVertex[width];
      for (x = 0; x < width; x++) {
        // Adds new vertex
        // TODO: 03/01/19 Potencial Error ?
        vertexes[y][x] = new GraphMapVertex(x,y);
        /** If X > 0, we create a edge connecting this vertex with the
          one at it's left and one coming from that too. **/
        if (x > 0) {
          edge = new GraphMapEdge(vertexes[y][x], vertexes[y][x - 1]);
          edges.add(edge);
          vertexes[y][x].addEdge(edge);
          edge = new GraphMapEdge(vertexes[y][x-1], vertexes[y][x]);
          vertexes[y][x-1].addEdge(edge);
          edges.add(edge);
        }
        /** If Y > 0, we create a edge connecting this vertex with the
          one above it and one coming from that too. **/
        if (y > 0) {
          edge = new GraphMapEdge(vertexes[y][x], vertexes[y-1][x]);
          vertexes[y][x].addEdge(edge);
          edges.add(edge);
          edge = new GraphMapEdge(vertexes[y-1][x], vertexes[y][x]);
          vertexes[y-1][x].addEdge(edge);
          edges.add(edge);
        }
      }
    }
  }

  public void test() {
    int i = 0;
    for (GraphMapEdge e : edges) {
      Log.d(TAG, "edge" + i + ":");
      Log.d(TAG, "\tsource: x" + e.getSource().getX()
          + "y" + e.getSource().getY());
      Log.d(TAG, "\ttarget: x" + e.getTarget().getX()
          + "y" + e.getTarget().getY());
      i++;
    }
  }



  // ===================== GET / SET ==================== //

  public GraphMapVertex getVertexAt(int x, int y) {
    return vertexes[y][x];
  }

}
