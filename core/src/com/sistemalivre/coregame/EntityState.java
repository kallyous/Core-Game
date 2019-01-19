package com.sistemalivre.coregame;



// ========================== EntityState ========================== //

import com.badlogic.gdx.utils.Array;



abstract public class EntityState {

  private static final String TAG = "EntityState";



// ========================== DATA ========================== //

  protected Array<String> enabled_commands;



// ========================== CREATE ========================== //

  EntityState() {
    enabled_commands = new Array<>(4);
    enabled_commands.add("DestroyEntityCommand");
  }




// ========================== LOGIC ========================== //

  abstract String getName();

  abstract void init();

  abstract void leave();

  boolean enterState(Entity target, String state_name) {
    if (target.setState(state_name)) {
      leave();
      target.getState().init();
      return true;
    }
    return false;
  }

  boolean execute(Command command) {

    Log.v(TAG, "Trying to execute " + command.getTAG());

    for (String cmd : enabled_commands) {

      if (cmd.equals(command.getTAG())) {
        command.execute();
        return true;
      }

    }

    return false;
  }

}





// ========================== InactiveState ========================== //

class InactiveState extends EntityState{

  private static final String TAG = "InactiveState";



// ========================== CREATE ========================== //

  InactiveState() {}




// ========================== LOGIC ========================== //

  @Override
  String getName() {
    return TAG;
  }


  @Override
  void init() {
    Log.v(TAG, "Going into Inactive State.");
  }


  @Override
  void leave() {
    Log.v(TAG, "Leaving inactive state.");
  }

}










// ========================== IdleState ========================== //

class IdleState extends EntityState {

  private static final String TAG = "IdleState";



// ========================== CREATE ========================== //

  IdleState() {
    enabled_commands.add("MoveToCommand");
    enabled_commands.add("SelectCommand");
    enabled_commands.add("TracePathCommand");
  }




// ========================== LOGIC ========================== //

  void init() {
    Log.w(TAG, "init() not implemented");
  }

  void leave() {
    Log.w(TAG, "leave() not implemented");
  }

  String getName() {
    return TAG;
  }

}





// ========================== MovingState ========================== //

class MovingState extends EntityState {

  private static final String TAG = "MovingState";



// ========================== CREATE ========================== //

  MovingState() {
    enabled_commands.add("StopMovingCommand");
  }




// ========================== LOGIC ========================== //

  void init() {
    Log.w(TAG, "init() not implemented");
  }

  void leave() {
    Log.w(TAG, "leave() not implemented");
  }

  String getName() {
    return TAG;
  }

}




