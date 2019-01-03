package com.kallyous.nopeisland;



import java.util.LinkedList;
import java.util.Vector;





/** ========================= COMMAND CONSUMING COMPONENT ========================= **/

public class CommandManager {

  private static final String TAG = "CommandManager";



// ========================= DATA SETUP BEGIN ========================= //

  // Queue with all commands to broadcast each game cycle
  private LinkedList<Command> pending_commands;

  // List with everyone listening to commands in the game
  private Vector<Entity> listeners;

// ========================= DATA SETUP END ========================= //




// ========================= CONSTRUCTION BEGIN ========================= //

  CommandManager() {
    pending_commands = new LinkedList<Command>();
    listeners = new Vector<Entity>();
  }

// ========================= CONSTRUCTION END ========================= //




// ------------------------- Logic -------------------------- //

  public void flushCommands() {

    Command command;

    while ( !(pending_commands.isEmpty()) ) {

      // Get and remove a command from the queue
      command = pending_commands.poll();

      // If the command is single targeted, we simple execute it
      if (command.target != null) {

        // Debug message about this shit.
        Log.i(TAG + " - Flushing " + command.getTAG() + " for "
            + command.target.getName() );

        if ( command.target.executeCommand(command) )
          Log.i(TAG + " - Comando aceito e executado ");
        else
          Log.i(TAG + " - Comando rejeitado. ");

      }
      // If target is null, the command is for multiple targets and shall be broadcast
      else {

        // We travel all listening entities and try to run int for each and all
        for (Entity entity : listeners) {

          // Send a debug message for each entity which execute the command
          if ( entity.executeCommand(command) ) {
            Log.d(TAG + " - " + entity.getName() + " executou "
                + command.getTAG() );
          }
        }

        // Shout out if command was flushed to several fuckers
        Log.i(TAG + " - Flushed " + command.getTAG() + " by broadcast");
      }

    }

  }

  // Insere commando na fila
  public boolean sendCommand(Command command) {
    Log.i(TAG + " - Enfileirando commando " + command.getTAG()
        + " para " + command.target.getName() );
    return pending_commands.offer(command);
  }

  // Adiciona nova entidade a ser commandada
  public boolean addListenner(Entity entity) {
    if ( !(listeners.contains(entity)) ) {
      Log.i(TAG + ": Adicionando " + entity.getName() + " em " + TAG);
      return listeners.add(entity);
    }
    Log.i(TAG + ": " + entity.getName() + " já presente em "
        + TAG + ", nada a fazer");
    return false;
  }

  public boolean remListenner(Entity entity) {
    Log.i(TAG + ": Removendo " + entity.getName() + " de "
        + TAG + ", se presente");
    return listeners.remove(entity);
  }

// ---------------------------------------------------------- //

}
