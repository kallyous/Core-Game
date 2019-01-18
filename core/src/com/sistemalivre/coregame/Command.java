package com.sistemalivre.coregame;


import com.badlogic.gdx.utils.Queue;

import java.util.Vector;



// ========================= COMMAND ========================= //

abstract public class Command {

  private static final String TAG = "Command";



// ========================= DATA ========================= //

  // The superclass only has a target and uses its class name as keyword
  public Entity target;
  public int entity_type = -1;




// ========================= CONSTRUCTION ========================= //

  // Mostly used for entities issuing commands to themselves
  Command(Entity target) {
    this.target = target;
  }


  // Issued for specific entities of known name
  Command(String target_name) {
    this.target = CoreGame.entities.get(target_name);
    if (this.target == null) {
      Log.w(TAG, "Falha ao localizar " + target_name + " na hash table.");
    }
  }


  // Issued for commands to be run against all of a kind
  Command(int entity_type) {
    this.target = null;
    this.entity_type = entity_type;
  }




// ========================= ABSTRACT ========================= //

  // Return the command TAG
  abstract public String getTAG();

  // The core of all commands
  abstract public boolean execute();

}





// ========================= OpenMainMenuCommand ========================= //

// Pause game and bring in the title screen with the main menu
class OpenMainMenuCommand extends Command {

  private static final String TAG = "OpenMainMenuCommand";



// ========================= CONSTRUCTION ========================= //

  OpenMainMenuCommand(Entity target) {
    super(target);
  }




// ========================== LOGIC ========================== //

  @Override
  public String getTAG() {
    return TAG;
  }


  @Override
  public boolean execute() {
    Log.i(TAG, "Pausando jogo e abrindo menu principal. ");
    CoreGame.game.switchTo(CoreGame.main_menu_state);
    return true;
  }

}





// ========================= RunGameCommand ========================= //

// Start/Resume the game
class RunGameCommand extends Command {

  private static final String TAG = "RunGameCommand";



// ========================= CONSTRUCTION  ========================= //

  RunGameCommand(Entity target) {
    super(target);
  }




// ========================== LOGIC ========================== //

  @Override
  public String getTAG() {
    return TAG;
  }


  @Override
  public boolean execute() {
    Log.i(TAG, "Starting/Resuming game. ");
    CoreGame.game.switchTo(CoreGame.running_state);
    return true;
  }

}





// ========================= ExitCommand ========================= //

// Exit/Close the game
class ExitCommand extends Command {

  private static final String TAG = "ExitCommand";



// ========================= CONSTRUCTION ========================= //

  ExitCommand(Entity target) {
    super(target);
  }




// ========================== LOGIC ========================== //

  @Override
  public boolean execute() {
    Log.i(TAG, "Issuing shutdow flag. Game is about to exit.");
    CoreGame.game_running = false;
    return true;
  }


  @Override
  public String getTAG() {
    return TAG;
  }

}





// ========================= SelectCommand ========================= //

class SelectCommand extends Command {

  private static final String TAG = "SelectCommand";



// ========================= CONSTRUCTION ========================= //

  SelectCommand(Entity entity) {
    super(entity);
  }




// ========================== LOGIC ========================== //

  @Override
  public boolean execute() {

    if (this.target == Entity.selected_entity) {
      Log.i(TAG, "Unselecting " + this.target.getName() );
      Entity.selected_entity = null;
    }
    else {
      Log.i(TAG, "Selecting " + this.target.getName());
      Entity.selected_entity = this.target;
      Log.d( TAG, "Info on selected entity:\n" + this.target.info() );
    }

    return true;
  }


  @Override
  public String getTAG() {
    return TAG;
  }

}





// ======================= SetTextContentCommand ======================= //

class SetTextContentCommand extends Command {

  private static final String TAG = "SetTextContentCommand";



// ========================= DATA ========================= //

  public String content;




// ========================= CONSTRUCTION ========================= //

  SetTextContentCommand(Entity entity, String content) {
    super(entity);
    this.content = content;
  }


  SetTextContentCommand(String entity_name, String content) {
    super(entity_name);
    this.content = content;

  }




// ========================= LOGIC ========================= //

  @Override
  public String getTAG() {
    return TAG;
  }


  @Override
  public boolean execute() {
    // TODO: 26/12/18 Adicionar try{}catch(){} adequado
    try {
      TextElement target_te = (TextElement)this.target;
      target_te.label.setText(content);
      return true;
    } catch (Exception e) {
      Log.e(TAG, "Comando gerou um erro durante a execução.");
      e.printStackTrace();
      return false;
    }
  }

}





// ========================= DestroyEntityCommand ========================= //

class DestroyEntityCommand extends Command {

  private static final String TAG = "DestroyEntityCommand";



// ========================= CONSTRUCTION ========================= //

  DestroyEntityCommand(Entity target) {
    super(target);
  }




// ========================= LOGIC ========================= //

  @Override
  public String getTAG() {
    return TAG;
  }


  @Override
  public boolean execute() {
    Log.i(TAG, "Destroying " + target.getName());
    Log.i(TAG, "Object is  " + target.toString());
    target.destroy();
    return true;
  }

}





// ========================= TracePathCommand ========================= //

class TracePathCommand extends Command {

  private static final String TAG = "TracePathCommand";



// ========================= DATA ========================= //

  public GraphMapVertex entrance;

  public GraphMapVertex exit;




// ========================= CONSTRUCTION ========================= //

  TracePathCommand(Entity entity,
                   GraphMapVertex entrance,
                   GraphMapVertex exit) {
    super(entity);
    this.entrance = entrance;
    this.exit = exit;
  }




// ========================== LOGIC ========================== //

  @Override
  public boolean execute() {
    Log.d(TAG, "Tracing path for " + target.getName());

    Queue<GraphMapVertex> path = breadthFirstSearch(entrance, exit);

    // TODO: delete all previous movement markers by broadcasting a destroy

    GraphMapVertex g;

    for (int i = 0; i < path.size;) {

      g = path.removeFirst();

      SupportUIElement element = new SupportUIElement(
          "mov_mark_" + path.size, 26);

      element.setPosition(
          g.getX()*Global.tile_size,
          g.getY()*Global.tile_size
      );

      GameState.world.addEntity(element);

    }

    return true;
  }


  private Queue<GraphMapVertex> breadthFirstSearch(
      GraphMapVertex entrance, GraphMapVertex exit) {

    Queue<GraphMapVertex> frontier = new Queue<>();
    Vector<GraphMapVertex> visited = new Vector<>();

    Queue<GraphMapVertex> path;

    GraphMapVertex current;

    frontier.addLast(entrance);

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
      }
      // If curr_vertex is not exit:
      // For each vertex connect by edges of curr_vertex, add it to frontier if thei're not in parsed.
      // Add curr_vertex into parsed
      // Else, return the reverse path
    }
    // The final path is in reverse order, so we reverse it so we are left with the desired path for the entity to move through
    path = reversePath(exit);
    // The vertexes have to be cleared from data.
    for (GraphMapVertex vertex : frontier)
      vertex.clear();
    for (GraphMapVertex vertex : visited)
      vertex.clear();
    // Return the path
    return path;
  }


  private Queue<GraphMapVertex> reversePath(GraphMapVertex exit) {

    Queue<GraphMapVertex> path = new Queue<GraphMapVertex>();

    GraphMapVertex current;

    path.addLast(exit);

    // Crashing with NullPointerException here when attempting to move a single square.
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




// ========================== GET / SET ========================== //

  @Override
  public String getTAG() {
    return TAG;
  }

}





// ======================== DestroyAllOfTypeCommand ======================== //

class DestroyWorldEntitiesByTypeCommand extends Command {

  private static final String TAG = "DestroyWorldEntitiesByTypeCommand";



// ========================= CREATION BEGIN ========================= //

  DestroyWorldEntitiesByTypeCommand(int entity_type) {
    super(entity_type);
  }




// ========================= LOGIC BEGIN ========================= //

  @Override
  public boolean execute() {
    int amount = 0;

    // How many of those there is within the world ?
    for (int a=0; a < GameState.world.entities.size; a++)
      if (GameState.world.entities.get(a).type() == this.entity_type) amount++;

    // If none, then whatever.
    if (amount == 0) return false;

    // For this many times, we will cycle through everything.
    for (int b=0; b < amount; b++) {
      // Each time, we remove and destroy a entity matching the type
      for (int i=0; i < GameState.world.entities.size; i++) {
        Entity ent = GameState.world.entities.get(i);
        if (ent.type() == this.entity_type) {
          GameState.world.entities.removeIndex(i);
          ent.destroy();
          break;
        }
      }
    }

    System.gc();

    return true;
  }




// ========================= GET / SET ========================= //

  @Override
  public String getTAG() {
    return TAG;
  }

}