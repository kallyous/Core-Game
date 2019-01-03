package com.kallyous.nopeisland;


import java.util.Vector;



public class GraphMap {

  // ========================================= DATA =========================================== //

  private GraphMapVertex[][] vertexes;
  private Vector<GraphMapEdge> edges;
  private int width, height;



  // ========================================= CREATION ======================================= //

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
        vertexes[y][x] = new GraphMapVertex(x,y); // TODO: 03/01/19 Potencial Error ?
        /** If X > 0, we create a edge connecting this vertex with the one at it's left
         *  and one coming from that too. */
        if (x > 0) {
          edge = new GraphMapEdge(vertexes[y][x], vertexes[y][x - 1]);
          edges.add(edge);
          vertexes[y][x].addEdge(edge);
          edge = new GraphMapEdge(vertexes[y][x-1], vertexes[y][x]);
          vertexes[y][x-1].addEdge(edge);
          edges.add(edge);
        }
        /** If Y > 0, we create a edge connecting this vertex with the one above it and
         *  one coming from that too. */
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
      Log.d("edge" + i + ":");
      Log.d("\tsource: x" + e.getSource().getX() + "y" + e.getSource().getY());
      Log.d("\ttarget: x" + e.getTarget().getX() + "y" + e.getTarget().getY());
      Log.d("");
      i++;
    }
  }



  // ==================================== GETTERS / SETTERS =================================== //

  public GraphMapVertex getVertexAt(int x, int y) {
    return vertexes[y][x];
  }
}
