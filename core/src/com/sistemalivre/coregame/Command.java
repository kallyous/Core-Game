package com.sistemalivre.coregame;


import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Queue;

import java.util.Vector;



// ========================= COMMAND ========================= //



/**
 Defines a command to be issued to one or more entities.
 <p>
 This is the superclass to define all commands.<br>
 As an abstract class, it must be not instantiated and every command shall
 extend this class.
 <p>
 The {@link CommandManager} enqueues and delivers all commands generated along
 the game process.
 @see CommandManager
 @author Lucas Carvalho Flores
 */
abstract public class Command {

  private static final String TAG = "Command";



// ========================= DATA ========================= //

  public static final int TARGET_WIDE = -1;
  public static final int TARGET_NONE = 0;
  public static final int TARGET_SINGLE = 1;
  public static final int TARGET_ENTITY_TYPE = 2;

  /**
   Tells the targeting method.
    */
  int command_target_type = TARGET_NONE;

  /**
   For single target commands. Points to the target.
   */
  Entity target = null;

  /**
   When multi-targeting, tells targeting method.<br>
   Default is TARGET_WIDE.
   */
  int entity_type = -1;

  /**
   Array of arguments for complex commands.
   */
  String args[];




// ========================= CONSTRUCTION ========================= //


  /**
   Construct a command intent for internal system use.
   <p>
   These type of commands are consumed by the CommandManager internal entity,
   named 'GameCommander'.
   <p>
   Examples of such commands are 'ExitGameCommand', 'RunGameCommand' and
   'OpenMainMenuCommand'.<br>
   They are also referred as 'targetless commands'.
   */
  Command() {
    this.command_target_type = TARGET_NONE;
  }


  /**
   Construct a complex targetless command.
   <p>
   Some commands are rather complex and needs a lot of data. This is their
   constructor.<br>
   The array of arguments may have specific entities names, values, and many
   more.<p>
   These commands are executed by the 'GameCommander'.
   @param args   Array of strings containing the extra arguments for the
                 command execution.
   */
  Command(String args[]) {
    this.command_target_type = TARGET_NONE;
    this.args = args;
  }


  /**
   Construct a command with a specific target entity linked.
   <p>
   This is very suitable for a entity issuing a command for itself.
   May contain extra complex arguments.
   @param target   Entity who shall execute the command.
   @param args     Extra arguments for complex commands.
   */
  Command(Entity target, String args[]) {
    this.command_target_type = TARGET_SINGLE;
    if (target == null)
      Log.w(TAG, "Null target received.");
    else
      this.target = target;
      this.args = args;
  }


  /**
   Construct a command for specific entity, using the entity's name.
   <p>
   This constructor searches in {@code Game.entities} for the target
   entity, then links it to the command.
   <p>
   Apart from that it behaves just like
   {@code Command(Entity target, String args[])}
   @param target_name Internal name of the target entity, inside Game.entities
   @param args        Extra arguments for complex commands.
   */
  Command(String target_name, String args[]) {
    this.command_target_type = TARGET_SINGLE;
    this.target = Game.entities.get(target_name);
    if (this.target == null)
      Log.w(TAG, "Falha ao localizar " + target_name + " na hash table.");
    else
      this.args = args;
  }


  /**
   Construct a command targeted to many entities.
   <p>
   The command can be for all entities of a matching type or a 'wide
   target' command.
   <p>
   'Wide Target' commands are broadcast to all available entities within
   Game.entities and left for each entity to decide if the issued command is
   valid for it.
   <p>
   'Entity Type' commands are similarly broadcast, but to a narrower set of
   entities that match the specified type.
   <p>
   <b>Delivering multi-targeted commands</b><br>
   If the first argument of a command is a number, it will first be
   evaluated for if it matches TARGET_WIDE. If it does, the command is
   wide-targeted, and shall be broadcast to everything inside
   {@code Game.entities} .<br>
   Now if the value is not TARGET_WIDE, it MUST match one entity type.
   Therefore, if the number doesn't matches TARGET_WIDE then it is used as
   the entity type identifier to broadcast the command to.
   @param target_or_entity_type If the value is TARGET_WIDE, the command
                                will be broadcast to everything inside
                                {@code Game.entities} .<br>
                                Otherwise the value shall match a entity type
                                code, and the command will be broadcast to the
                                set of entities matching this type identifier.
   @param args                  Extra arguments for complex commands.
   */
  Command(int target_or_entity_type, String args[]) {

    if (target_or_entity_type == TARGET_WIDE)
      this.command_target_type = TARGET_WIDE;
    else {
      this.command_target_type = TARGET_ENTITY_TYPE;
      this.entity_type = target_or_entity_type;
    }

    this.args = args;
  }





// ========================= ABSTRACT ========================= //


  /**
   Return the command TAG
   @return String
   */
  abstract public String getTAG();


  /**
   Command's core routine.
   <p>
   The routine of a command is defined inside the {@code execute()} method
   override.<br>
   All commands must implement it.
   @return {@code true} if the command was executed successfully and
           {@code false} otherwise.
   */
  abstract public boolean execute();




// ========================== GET / SET ========================== //


  /**
   Return the type of the command.
   @return int
   */
  int type() {
    return command_target_type;
  }

}





// ========================= OpenMainMenuCommand ========================= //



/**
 Pause the game and bring in the title screen with the main menu.
 <p>
 This command is designed to be called form the running game by a menu button,
 which causes the game to pause and brings the view into the main menu screen
 and game state.
 @see GameState
 @author Lucas Carvalho Flores
 */
class OpenMainMenuCommand extends Command {

  private static final String TAG = "OpenMainMenuCommand";



// ========================== LOGIC ========================== //

  @Override
  public String getTAG() {
    return TAG;
  }


  /**
   Call Game.state.switchTo() for entering into the main menu game state.
   @return always {@code true}
   */
  @Override
  public boolean execute() {
    Log.i(TAG, "Pausando jogo e abrindo menu principal. ");
    Game.state.switchTo(Game.main_menu_state);
    return true;
  }

}





// ========================= RunGameCommand ========================= //



/**
 Sets the game to 'running' state.
 <p>
 Designed to be called from any state except the running state itself.
 <p>
 This is the command to be used to start and resume the game from non-running
 states.
 @see GameState
 @author Lucas Carvalho Flores
 */
class RunGameCommand extends Command {

  private static final String TAG = "RunGameCommand";



// ========================== LOGIC ========================== //

  @Override
  public String getTAG() {
    return TAG;
  }


  /**
   Call {@code Game.state.switchTo()} for changing the game stato to running
   state.
   @return always {@code true}
   */
  @Override
  public boolean execute() {
    Log.i(TAG, "Starting/Resuming state. ");
    Game.state.switchTo(Game.running_state);
    return true;
  }

}





// ========================= ExitCommand ========================= //



/**
 Shuts the game down.
 <p>
 When executed, this command will cause the game to shut down next iteration in
 the main loop.
 @see Game
 @author Lucas Carvalho Flores
 */
class ExitCommand extends Command {

  private static final String TAG = "ExitCommand";



// ========================== LOGIC ========================== //


  /**
   Sets the {@code Game.running} flag to false.
   <p>
   This cause the game to gracefully exit at the next main loop iteration.<br>
   Hopefully.
   @return
   */
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



/**
 Selects the target entity.
 <p>
 Issued to entities, usually o the touched/clicked entity.
 <p>
 It sets the {@link Entity}.{@code selected_entity} to point to the command's
 target entity.
 <p>
 A broad range of commands and actions are generated with the selected entity
 as target, which makes this command fairly important.
 */
class SelectCommand extends Command {

  private static final String TAG = "SelectCommand";



// ========================= CONSTRUCTION ========================= //

  SelectCommand(Entity entity) {
    super(entity, null);
  }




// ========================== LOGIC ========================== //


  /**
   Selects or deselects target entity.
   <p>
   If the target entity is the currently selected entity, then we deselect it.
   <br>
   Otherwise, we select the target entity.
   @return always {@code true}
   */
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

/**
 Sets the text content of a textual entity.
 <p>
 This command is designed to entities that holds text and implements a method
 for setting it's text content.
 TODO: This command is currently improperly setup and does access the label element directly, which is a terrible design choice.
 @see UserInterface
 @author Lucas Carvalho Flores
 */
class SetTextContentCommand extends Command {

  private static final String TAG = "SetTextContentCommand";



// ========================= DATA ========================= //

  public String content;




// ========================= CONSTRUCTION ========================= //


  /**
   Construct a command directly with the target entity and the text content.
   @param entity  Target entity. Must be a textual entity.
   @param content Text content to populate the target entity.
   */
  SetTextContentCommand(Entity entity, String content) {
    super(entity, null);
    this.content = content;
  }


  /**
   Construct a command with the name of a textual entity and the text content.
   @param entity_name Internal name of target entity.
   @param content     Text content to populate the target entity.
   */
  SetTextContentCommand(String entity_name, String content) {
    super(entity_name, null);
    this.content = content;
  }




// ========================= LOGIC ========================= //

  @Override
  public String getTAG() {
    return TAG;
  }


  /**
   TODO: Make textual entities inplement a method of access to their text content.
   @return boolean
   */
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

/**
 Destroys the target entity.
 <p>
 This command actually calls the target's destroy() method and hopes for
 the best.
 @see Entity
 @author Lucas Carvalho Flores
 */
class DestroyEntityCommand extends Command {

  private static final String TAG = "DestroyEntityCommand";



// ========================= CONSTRUCTION ========================= //


  /**
   Create command with a direct target reference.
   @param target Entity object reference
   */
  DestroyEntityCommand(Entity target) {
    super(target, null);
  }


  /**
   Create command with the internal name of the target entity.
   @param target_name Internal name of target entity.
   */
  DestroyEntityCommand(String target_name) {
    super(target_name, null);
  }




// ========================= LOGIC ========================= //

  @Override
  public String getTAG() {
    return TAG;
  }


  /**
   Call the {@code destroy()} method of the terget entity.
   @return always true.
   */
  @Override
  public boolean execute() {
    Log.i(TAG, "Destroying " + target.getName());
    Log.v(TAG, "Object is  " + target.toString());
    target.destroy();
    return true;
  }

}





// ========================= TracePathCommand ========================= //



/**
 Traces path from current selected entity to selected/touched/clicked location.
 <p>
 This command calls pathfinding mechanisms for finding a path to the desired
 location.
 @see Pathfinder
 @author Lucas Carvalho Flores
 */
class TracePathCommand extends Command {

  private static final String TAG = "TracePathCommand";



// ========================= DATA ========================= //

  /**
   The origin vertex. Usually the standing location of the moving entity.
   */
  GraphMapVertex entrance;

  /**
   The desired destination vertex. Usually a taped or clicked tile.
   */
  GraphMapVertex exit;




// ========================= CONSTRUCTION ========================= //


  /**
   Create a command containing the origin and destination vertexes.
   @param entity   The entity to move along the traced path.
   @param entrance The initial vertex. Equivalent to the occupied tile.
   @param exit     The destination vertex. Equivalent to the desired target
                   tile.
   */
  TracePathCommand(Entity entity,
                   GraphMapVertex entrance,
                   GraphMapVertex exit) {
    super(entity, null);
    this.entrance = entrance;
    this.exit = exit;
  }




// ========================== LOGIC ========================== //


  /**
   Call pathfinding algorithm, display the found path and a confirmation element.
   @return always {@code true}
   */
  @Override
  public boolean execute() {
    Log.d(TAG, "Tracing path for " + target.getName());

    Queue<GraphMapVertex> path = Pathfinder.findPath(entrance, exit);

    GraphMapVertex g;

    for (int i = 0; i < path.size; i++) {

      g = path.get(i);

      /* This element is for the exit vertex, the destination.
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

}





// ======================== DestroyWorldSupportGUICommand ======================== //

/**
 Clear the game world view from any support GUI elements.
 @see SupportUIElement
 @author Lucas Carvalho Flores
 */
class DestroyWorldSupportGUICommand extends Command {

  private static final String TAG = "DestroyWorldSupportGUICommand";



// ========================= CONSTRUCTION ========================= //


  /**
   Create a targetless command for destroying all Support GUI elements.
   */
  DestroyWorldSupportGUICommand() {}




// ========================= LOGIC ========================= //


  /**
   Destroy any SupportUIElement available.
   @return always {@code true}
   */
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

/**
 TODO: Implement this shit
 */
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

/**
 TODO: Implement this motherfucker
 */
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

/**
 TODO: Oh, my, God... when did I even?! Õ_Õ
 */
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



/**
 Makes the target to move along a path.
 @see TracePathCommand
 @author Lucas Carvalho Flores
 */
class MoveToCommand extends Command {

  private static final String TAG = "MoveToCommand";



// ========================== DATA ========================== //

  /**
   The path for the target to follow.
   */
  Queue<GraphMapVertex> path;




// ========================== CREATE ========================== //


  /**
   Create the command with a direct reference to the target.
   @param target Target to move along the path.
   @param path   The path to move through.
   */
  MoveToCommand(Entity target, Queue<GraphMapVertex> path) {
    super(target, null);
    this.path = path;
  }


  /**
   Create the command with the name of the target entity.
   @param name The name of the target entity.
   @param path The path to move through.
   */
  MoveToCommand(String name, Queue<GraphMapVertex> path) {
    super(name, null);
    this.path = path;
  }




// ========================== LOGIC ========================== //


  /**
   Create a new moving state holding the desired path and shoves it into the target entity.
   @return always {@code true}
   */
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

/**
 Takes an entity out of the moving state.
 <p>
 Effectively stops a antity from moving.
 @see MoveToCommand
 @author Lucas Carvalho Flores
 */
class StopMovingCommand extends Command {

  private static final String TAG = "StopMovingCommand";



// ========================== CREATE ========================== //


  /**
   Create a commad with a direct reference to the target entity.
   @param target The target entity.
   */
  StopMovingCommand(Entity target) {
    super(target, null);
  }


  /**
   Create a command with the name of the target entity.
   @param name Internal name of desired entity.
   */
  StopMovingCommand(String name) {
    super(name, null);
  }




// ========================== LOGIC ========================== //


  /**
   Switche the state of target entity to 'IdleState'.
   @return always {@code true}
   */
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




