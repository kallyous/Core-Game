package com.sistemalivre.coregame;



// ========================== EntityState ========================== //

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;



abstract public class EntityState {

  private static final String TAG = "EntityState";



// ========================== DATA ========================== //

  // All available commands for current State
  protected Array<String> enabled_commands;

  // Entity owning the current instance
  public Entity owner;



// ========================== CREATE ========================== //

  EntityState(Entity owner) {
    this.owner = owner;
    enabled_commands = new Array<>(4);
    enabled_commands.add("DestroyEntityCommand");
  }




// ========================== LOGIC ========================== //

  abstract String getName();

  abstract void init();

  abstract void leave();

  void update(float dt) {}

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

  InactiveState(Entity entity) {
    super(entity);
  }




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

  IdleState(Entity entity) {
    super(entity);
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

  Queue<GraphMapVertex> path;

// ========================== CREATE ========================== //

  MovingState(Entity entity) {
    super(entity);
    enabled_commands.add("StopMovingCommand");
  }

  MovingState(Entity entity, Queue<GraphMapVertex> path) {
    super(entity);
    enabled_commands.add("StopMovingCommand");
  }




// ========================== LOGIC ========================== //



  void init() {
    Log.d(TAG, "Entity is now moving. (" + owner.getName() + ")");
  }

  void update(float dt) {
    System.out.print("o ");
  }

  void leave() {
    Log.w(TAG, "leave() not implemented");
  }

  String getName() {
    return TAG;
  }

}




