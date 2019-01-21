package com.sistemalivre.coregame;


import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Queue;

import java.util.Vector;



// ========================= COMMAND ========================= //

abstract public class Command {

  private static final String TAG = "Command";



// ========================= DATA ========================= //

  public static final int TARGET_WIDE = -1;
  public static final int TARGET_NONE = 0;
  public static final int TARGET_SINGLE = 1;
  public static final int TARGET_ENTITY_TYPE = 2;

  // Informs the targeting method.
  int command_target_type = TARGET_NONE;

  // For single target commands. Points to the target.
  Entity target = null;

  // For entity type targets. Informs the type of entity targeted.
  int entity_type = -1;

  // Array of arguments for complex shit
  String args[];




// ========================= CONSTRUCTION ========================= //

  // This kind of command are usually simple internal system stuff
  Command() {
    this.command_target_type = TARGET_NONE;
  }


  // Internal but rather complex stuff that needs extra arguments
  Command(String args[]) {
    this.command_target_type = TARGET_NONE;
    this.args = args;
  }

  // Mostly used for entities issuing commands to themselves
  Command(Entity target, String args[]) {
    this.command_target_type = TARGET_SINGLE;
    if (target == null)
      Log.w(TAG, "Null target received.");
    else
      this.target = target;
      this.args = args;
  }


  // Issued for specific entities with known name
  Command(String target_name, String args[]) {
    this.command_target_type = TARGET_SINGLE;
    this.target = Game.entities.get(target_name);
    if (this.target == null)
      Log.w(TAG, "Falha ao localizar " + target_name + " na hash table.");
    else
      this.args = args;
  }


  // Issued for commands to be run against all of a kind
  Command(int target_or_entity_type, String args[]) {
    /** Delivering multi-targeted commands:
      All entity types are non-negative numbers.
      If the first argument of a command is a number, it will first be
      evaluated for if it matches TARGET_WIDE(-1). If it does, the command is
      wide targeted, and shall be broadcast to everyone.
      Now if the value is not -1 (TARGET_WIDE), it must match one entity type.
      Therefore, if the number doesn't matches TARGET_WIDE, then we simply
      use it as the entity type to boradcast the command to. **/
    if (target_or_entity_type == TARGET_WIDE)
      this.command_target_type = TARGET_WIDE;
    else {
      this.command_target_type = TARGET_ENTITY_TYPE;
      this.entity_type = target_or_entity_type;
    }

    this.args = args;
  }





// ========================= ABSTRACT ========================= //

  // Return the command TAG
  abstract public String getTAG();

  // The core of all commands
  abstract public boolean execute();




// ========================== GET / SET ========================== //

  int type() {
    return command_target_type;
  }

}





// ========================= OpenMainMenuCommand ========================= //

// Pause state and bring in the title screen with the main menu
class OpenMainMenuCommand extends Command {

  private static final String TAG = "OpenMainMenuCommand";



// ========================== LOGIC ========================== //

  @Override
  public String getTAG() {
    return TAG;
  }


  @Override
  public boolean execute() {
    Log.i(TAG, "Pausando jogo e abrindo menu principal. ");
    Game.state.switchTo(Game.main_menu_state);
    return true;
  }

}





// ========================= RunGameCommand ========================= //

// Start/Resume the state
class RunGameCommand extends Command {

  private static final String TAG = "RunGameCommand";



// ========================== LOGIC ========================== //

  @Override
  public String getTAG() {
    return TAG;
  }


  @Override
  public boolean execute() {
    Log.i(TAG, "Starting/Resuming state. ");
    Game.state.switchTo(Game.running_state);
    return true;
  }

}





// ========================= ExitCommand ========================= //

// Exit/Close the state
class ExitCommand extends Command {

  private static final String TAG = "ExitCommand";



// ========================== LOGIC ========================== //

  @Override
  public boolean execute() {
    Log.v(TAG, "Issuing shutdow flag. Game is about to exit.");
    Game.game_running = false;
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
    super(entity, null);
  }




// ========================== LOGIC ========================== //

  @Override
  public boolean execute() {

    if (this.target == Entity.selected_entity) {
      Log.v(TAG, "Unselecting " + this.target.getName() );
      Entity.selected_entity = null;
    }
    else {
      Log.v(TAG, "Selecting " + this.target.getName());
      Entity.selected_entity = this.target;
      Log.i( TAG, "Info on selected entity:\n" + this.target.info() );
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
    super(entity, null);
    this.content = content;
  }


  SetTextContentCommand(String entity_name, String content) {
    super(entity_name, null);
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
    super(target, null);
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
    super(entity, null);
    this.entrance = entrance;
    this.exit = exit;
  }




// ========================== LOGIC ========================== //

  @Override
  public boolean execute() {
    Log.d(TAG, "Tracing path for " + target.getName());

    Queue<GraphMapVertex> path = Pathfinder.findPath(entrance, exit);

    GraphMapVertex g;

    for (int i = 0; i < path.size; i++) {

      g = path.get(i);

      /* This is for the exit vertex, the destination.
      It is the location where the player actually taped the world.
      Instead of creating the usual path marker for the player
      visualizing the movement path, the last marker not only has
      different visual but holds special behavior.
      When clicked, it confirms the movement intent and sends the
      MoveToCommand to the CommandManager, containing both the entity
      selected at the moment of path finding and the path found.
      Effectively this means for the player that taping again on the
      same spot will perform the movement of the selected entity.
       */
      if (i == path.size - 1) {

        SupportUIElement element = new SupportUIElement(
            "mov_mark_" + Entity.getLastUsedID(),
            13) {

          @Override // For detecting the confirmation tap
          public boolean touchUp(int screenX, int screenY,
                                 int pointer, int button) {

            Vector3 touched_spot = new Vector3(screenX, screenY, 0);

            if ( worldTouched(touched_spot) ) {
              Log.v(TAG, "We got a collision with the touch.");

              CommandManager.sendCommand( // Triggers/confirms movement
                  new MoveToCommand(target, path) );

              // Clears the screen/world from path markers
              CommandManager.sendCommand(
                  new DestroyWorldSupportGUICommand()
              );

              return true;
            }

            return false;
          }

        };

        element.setDisplayName("Path Destination");

        element.setPosition(
            g.getX()*Global.tile_size,
            g.getY()*Global.tile_size
        );

        Game.world.addSupportElem(element);

      }

      /* Visual Path marker
      For the player to see the path it's selected entity will take
      to reach the destination, we create and place one of those on
      every tile it will walk through.
       */
      else {
        SupportUIElement element = new SupportUIElement(
            "mov_mark_" + Entity.getLastUsedID(),
            12);
        element.setPosition(
            g.getX()*Global.tile_size,
            g.getY()*Global.tile_size
        );
        Game.world.addSupportElem(element);
      }

    }

    return true;
  }




// ========================== GET / SET ========================== //

  @Override
  public String getTAG() {
    return TAG;
  }




// ========================== REFERENCE ========================== //
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

    // Travel back from exit until entrance and return the path
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
// ========================== REFERENCE ========================== //

}





// ======================== DestroyWorldSupportGUICommand ======================== //

class DestroyWorldSupportGUICommand extends Command {

  private static final String TAG = "DestroyWorldSupportGUICommand";



// ========================= CONSTRUCTION ========================= //

  DestroyWorldSupportGUICommand() {}




// ========================= LOGIC ========================= //

  @Override
  public boolean execute() {

    // Prepares a holder for entities reference
    Vector<SupportUIElement> to_destroy = new Vector<>();

    // Collects all entities of given type into holder
    for (SupportUIElement elem : Game.world.getSupportGUI()) {
      to_destroy.add(elem);
    }

    // Destroy them all. Entities must make sure they are removed from World.
    for (Entity entity : to_destroy)
      entity.destroy();

    // Makes sure there is no reference for the objects being destroyed
    to_destroy = null;

    // Hints Java Garbage Collector to do it's job
    System.gc();

    return true;

  }




// ========================= GET / SET ========================= //

  @Override
  public String getTAG() {
    return TAG;
  }

}





// ========================== LoadCreatureCommand ========================== //

class LoadCreatureCommand extends Command {

  private static final String TAG = "LoadCreatureCommand";



// ========================== CONSTRUCTION ========================== //

  LoadCreatureCommand(String args[]) {
    // This command is target-less.
    super(args);
  }







// ========================== LOGIC ========================== //

  @Override
  public boolean execute() {
    for (int i=0; i < args.length; i++)
      Log.v(TAG,"Arg" + i + ": " + args[i]);
    return true;
  }




// ========================== GET / SET ========================== //

  @Override
  public String getTAG() {
    return TAG;
  }

}





// ========================== PlaceCreatureCommand ========================== //

class PlaceCreatureCommand extends Command {

  private static final String TAG = "PlaceCreatureCommand";



// ========================== CONSTRUCTION ========================== //

  PlaceCreatureCommand(String args[]) {
    // This command is target-less.
    super(args);
  }




// ========================== LOGIC ========================== //

  @Override
  public boolean execute() {
    for (int i=0; i < args.length; i++)
      Log.v(TAG,"Arg" + i + ": " + args[i]);
    return true;
  }




// ========================== GET / SET ========================== //

  @Override
  public String getTAG() {
    return TAG;
  }

}





// ========================== LoadAndPlaceCreatureCommand ========================== //

class LoadAndPlaceCreatureCommand extends Command {

  private static final String TAG = "LoadAndPlaceCreatureCommand";



// ========================== CONSTRUCTION ========================== //

  LoadAndPlaceCreatureCommand(String args[]) {
    // This command is target-less.
    super(args);
  }




// ========================== LOGIC ========================== //

  @Override
  public boolean execute() {
    for (int i=0; i < args.length; i++)
      Log.v(TAG,"Arg" + i + ": " + args[i]);

    Creature creature = CreatureLoader.loadBaseCreature(args[1]);
    creature.setDisplayName(args[0]);
    creature.setX(Float.parseFloat(args[3]));
    creature.setY(Float.parseFloat(args[4]));
    Game.world.addEntity(creature);

    return true;
  }




// ========================== GET / SET ========================== //

  @Override
  public String getTAG() {
    return TAG;
  }

}





// ========================== MoveToCommand ========================== //

class MoveToCommand extends Command {

  private static final String TAG = "MoveToCommand";



// ========================== DATA ========================== //

  Queue<GraphMapVertex> path;




// ========================== CREATE ========================== //

  MoveToCommand(Entity target, Queue<GraphMapVertex> path) {
    super(target, null);
    this.path = path;
  }

  MoveToCommand(String name, Queue<GraphMapVertex> path) {
    super(name, null);
    this.path = path;
  }




// ========================== LOGIC ========================== //

  @Override
  public boolean execute() {
    target.setState( new MovingState(target, path) );
    return true;
  }


  @Override
  public String getTAG() {
    return TAG;
  }

}





// ========================== StopMovingCommand ========================== //

class StopMovingCommand extends Command {

  private static final String TAG = "StopMovingCommand";



// ========================== CREATE ========================== //

  StopMovingCommand(Entity target) {
    super(target, null);
  }

  StopMovingCommand(String name) {
    super(name, null);
  }




// ========================== LOGIC ========================== //

  @Override
  public boolean execute() {
    Log.w(TAG, "Not implemented yet.");
    target.setState("IdleState");
    return true;
  }


  @Override
  public String getTAG() {
    return TAG;
  }

}




