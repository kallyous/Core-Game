package com.sistemalivre.coregame;


import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Queue;



// ===================== CommandManager ===================== //

/**
 Singleton oriented class that orders the commands pipeline.
 <p>
 The CommandManager receives command requests, enqueues them, them fluses all
 pending commands every cycle. This allows control actions to arrive form many
 sources, like direct input, A.I., network, etc.
 <p>
 Commands can be issued to any entity, but the entity itself will validate the
 command before execute it.
 @see Command
 @author Lucas Carvalho Flores
 */
public class CommandManager {

  private static final String TAG = "CommandManager";



// ========================= DATA ========================= //

  /**
   Queue with all commands to broadcast/direct each cycle.
   */
  private static Queue<Command> pending_commands;

  /**
   Entity for running targetless commands.
   */
  private static Entity commander;




// ========================= CONSTRUCTION ========================= //

  public static void setup() {
    // Prepares the holder for received commands
    pending_commands = new Queue<>();

    // Cosntructos a entity to run target-less commands
    commander = new Entity("GameCommander") {
      @Override
      boolean executeCommand(Command command) {
        command.execute();
        return true;
      }
      @Override
      public void updateExtra(float dt) {}
      @Override
      public void dispose() {}
    };
  }




// ========================== LOGIC ========================== //

  public static void flushCommands() {

    Command command;

    while (pending_commands.size > 0) {

      // Get and remove a command from the queue
      command = pending_commands.removeFirst();

      // If it has no target, the commander execute it
      if (command.type() == Command.TARGET_NONE) {
        commander.executeCommand(command);
      }
      // If it is single target, the target execute it
      else if (command.type() == Command.TARGET_SINGLE) {
        command.target.executeCommand(command);
      }
      /** If it is directed to a specific type of entities, it has it's own
        logic, those entities are within the world and the commander
        runs it. **/
      else if(command.type() == Command.TARGET_ENTITY_TYPE) {
        commander.executeCommand(command);
      }
      /** If it isn't none of the previous type, it is a wide target.
        We make an enumeration of all current entities within the state, and
        send the command to all of them. Slow but effective. **/
      else {
        // Builds enumeration with all entities
        ObjectMap.Entries<String, Entity> entities = Game.entities.iterator();
        // Cycles through them all and let them know about the command
        while(entities.hasNext())
          entities.next().value.executeCommand(command);
      }

    }

  }


  // Insere commando na fila
  public static boolean sendCommand(Command command) {
    try {
      Log.i(TAG, "Enfileirando commando " + command.getTAG()
          + " para " + command.target.getName());
    }
    catch (NullPointerException excpetion) {
      Log.i(TAG, "Enfileirando comando " + command.getTAG());
    }

    pending_commands.addLast(command);

    if (pending_commands.last() == command)
      return true;
    else
      return false;
  }

}
