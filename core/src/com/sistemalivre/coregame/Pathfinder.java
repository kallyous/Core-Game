package com.sistemalivre.coregame;



// ========================== Pathfinder ========================== //

import com.badlogic.gdx.utils.Queue;

import java.util.Vector;



public class Pathfinder {

  private static final String TAG = "Pathfinder";



// ========================== LOGIC ========================== //


  /** Wraps path finding algorithms, calls them and reverse the final
   path for convenience, then return it.
   @param entrance
   @param exit
   @return
   */
  public static Queue<GraphMapVertex> findPath(
      GraphMapVertex entrance, GraphMapVertex exit ) {

    Queue<GraphMapVertex> path =
        Pathfinder.breadthFirstSearch(entrance, exit);

    path = Pathfinder.reversePath(path);

    return path;
  }


  /** Breadth First Search path finding algorithm.
   @param entrance
   @param exit
   @return found path to destination
   */
  public static Queue<GraphMapVertex> breadthFirstSearch(
      GraphMapVertex entrance, GraphMapVertex exit) {

    Queue<GraphMapVertex> frontier = new Queue<>();
    Vector<GraphMapVertex> visited = new Vector<>();

    Queue<GraphMapVertex> path;

    GraphMapVertex current;

    frontier.addLast(entrance);

    boolean first = true;

    while(frontier.size > 0) {
      // Pop a vertex from frontier into the current vertex
      current = frontier.removeFirst();

      // Early exit
      if (current == exit) {
        current.visit();
        visited.add(current);
        break;
      }

      // Only process if not visited yet
      if (!current.isVisited()) {
        current.visit();
        if (current.getEntityOn() == null || first) {
          int x = current.getX();
          int y = current.getY();
          if (Game.world.tileIsPassable(x,y)) {
            Log.v(TAG, "Passable tile found at " + x + " " + y);
            // Enqueue all it's neighbors for later parsing
            for(GraphMapEdge edge : current.getEdges()) {
              // Skip already visited vertexes
              if(!edge.getTarget().isVisited()) {
                // Marks into the parsing neighbor vertex where we came from when we reached it.
                edge.getTarget().setSourceEdge(edge);
                // Enqueue at the tail
                frontier.addLast(edge.getTarget());
              }
            }
            // Throws current parsed vertex into the visited group
            visited.add(current);
            first = false;
          }
        }
      }

    }

    // Travel back from exit until entrance and return the path
    path = traceBack(exit);

    Game.world.clearGraph();

    // The vertexes have to be cleared from data.
    /*for (GraphMapVertex vertex : frontier)
      vertex.clear();
    for (GraphMapVertex vertex : visited)
      vertex.clear();*/

    // Return the path
    return path;
  }


  /** A path obtained from an algorithm is usually in reverse order, from
   exit to entrance. This method receives a path and reverses it.
   **/
  public static Queue<GraphMapVertex> reversePath(
      Queue<GraphMapVertex> path) {

    Queue<GraphMapVertex> reversed = new Queue<>();

    while (path.size > 0)
      reversed.addLast(path.removeLast());

    return reversed;
  }


  /** After a path has been found from another method, and the path is
   available on the graph, take the exit vervex and travel back to the
   entrance, storing all traveled vertexes in a queue then return it.
   **/
  public static Queue<GraphMapVertex> traceBack(GraphMapVertex exit) {

    Queue<GraphMapVertex> path = new Queue<>();

    GraphMapVertex current;

    path.addLast(exit);

    try {
      current = exit.getSourceEdge().getSource();
      while (current != null) {
        path.addLast(current);
        try {
          current = current.getSourceEdge().getSource();
        }
        catch (NullPointerException e) {
          break;
        }
      }
    }

    catch (NullPointerException e) {
      e.printStackTrace();
    }

    return path;
  }


}
