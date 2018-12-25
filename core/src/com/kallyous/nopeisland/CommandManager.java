package com.kallyous.nopeisland;



import java.util.LinkedList;
import java.util.Vector;



/** ========================= COMMAND CONSUMING COMPONENT ========================= **/

public class CommandManager {

  private static final String TAG = "CommandManager";




// ========================= DATA BEGIN ========================= //

  // Queue with all commands to broadcast each game cycle
  private LinkedList<Command> pending_commands;

  // List with everyone listening to commands in the game
  private Vector<Entity> listenners;

  // Temporary command holder for various procedures
  private Command command;

// ========================= DATA END ========================= //




// ========================= CONSTRUCTION BEGIN ========================= //

  CommandManager() {
    pending_commands = new LinkedList<Command>();
    listenners = new Vector<Entity>();
  }

// ========================= CONSTRUCTION END ========================= //




// ========================= LOGIC BEGIN ========================= //

  public void flushCommands() {

    while ( !(pending_commands.isEmpty()) ) {

      command = pending_commands.poll();

      System.out.println(TAG + ": Flushing " + command.info() + " for "
          + command.target.getName() );

      for (Entity entity : listenners) {

        if ( entity.executeCommand(command) ) {
          System.out.println(TAG + ": " + entity.getName() + " executou "
              + command.info() );
        }

      }
    }
  }

  // Insere commando na fila
  public boolean sendCommand(Command command) {
    System.out.println(TAG + ": Enfileirando commando " + command.info()
        + " para " + command.target.getName() );
    return pending_commands.offer(command);
  }

  // Adiciona nova entidade a ser commandada
  public boolean addListenner(Entity entity) {
    if ( !(listenners.contains(entity)) ) {
      System.out.println(TAG + ": Adicionando " + entity.getName() + " em " + TAG);
      return listenners.add(entity);
    }
    System.out.println(TAG + ": " + entity.getName() + " já presente em "
        + TAG + ", nada a fazer");
    return false;
  }

  public boolean remListenner(Entity entity) {
    System.out.println(TAG + ": Removendo " + entity.getName() + " de "
        + TAG + ", se presente");
    return listenners.remove(entity);
  }

// ========================= LOGIC END ========================= //
}
