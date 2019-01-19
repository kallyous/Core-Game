package com.sistemalivre.coregame;


import com.badlogic.gdx.utils.Array;

import java.util.Vector;



// ==================== CommandComponent ==================== //

public class CommandComponent {

  private static final String TAG = "CommandComponent";



// ========================= DATA ========================= //

  // Entity owning the current instance of CommandComponent
  public Entity owner;

  // Vector holding references to all commands the entity can follow
  protected Vector<String> commands;

  // Array with the states the current entity can be on
  private Array<EntityState> states;

  private EntityState current_state;




// ========================= CONSTRUCTION ========================= //

  // Must have a owner
  CommandComponent(Entity owner) {
    this.owner = owner;
    commands = new Vector<String>();
    states = new Array<>(4);
    InactiveState s = new InactiveState();
    states.add(s);
    setState(s.getName());
  }




// ========================== LOGIC ========================== //

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
    return current_state.execute(command);
  }


  EntityState getState() {
    return current_state;
  }


  boolean setState(String state_name) {
    for (EntityState state : states) {
      if (state.getName().equals(state_name)) {
        current_state = state;
        return true;
      }
    }
    return false;
  }


  void addState(EntityState state) {
    states.add(state);
  }

}
