package com.kallyous.nopeisland;



import java.util.Vector;



/** ========================= COMMAND CONSUMING COMPONENT ========================= **/

public class CommandComponent {

  private static final String TAG = "CommandComponent";

  // Entity owning the current instance of CommandComponent
  public Entity owner;

  // Vector holding references to all commands the entity can follow
  protected Vector<String> commands;

  // Must have a owner
  CommandComponent(Entity owner) {
    this.owner = owner;
    commands = new Vector<String>();
  }

  protected boolean isItForMe(Command command) {
    if (command.target == this.owner && commands.contains(command.getTAG()) ) {
      return true;
    }
    return false;
  }

  // Add a command to the list of available commands
  public void enableCommand(String command_name) {
    commands.add(command_name);
  }

  // Remove a command to the list of available commands
  public void disableCommand(String command_name) {
    commands.remove(command_name);
  }

  // Check if command is for current entity and try to run int if so
  public boolean execute(Command command) {
    if ( isItForMe(command) ) {
      if ( command.action() ) return true;
    }
    return false;
  }

}
