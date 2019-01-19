package com.sistemalivre.coregame;



// ========================== EntityState ========================== //

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

import java.util.NoSuchElementException;



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



// ========================== DATA ========================== //

  Queue<GraphMapVertex> path;

  GraphMapVertex destination;

  int dest_tile_x, dest_tile_y;

  int dest_x, dest_y;

  float speed;

  boolean arrived;

  boolean broken;

// ========================== CREATE ========================== //

  MovingState(Entity entity) {
    super(entity);
    enabled_commands.add("StopMovingCommand");
    speed = 4;
    broken = false;
    destination = null;
  }

  MovingState(Entity entity, Queue<GraphMapVertex> path) {
    super(entity);
    this.path = path;
    this.path.removeFirst();
    enabled_commands.add("StopMovingCommand");
    speed = 4;
    broken = false;
    destination = null;
  }




// ========================== LOGIC ========================== //



  void init() {
    Log.d(TAG, "Entity is now moving. (" + owner.getName() + ")");
  }

  void update(float dt) {

    if (broken) return;

    if (destination == null) {
      try {
        Log.v(TAG, "Path size: " + path.size);
        if (path.size > 0) {
          destination = path.removeFirst();
          faceDestination();
        }
        else {
          Log.v(TAG, "Entity arrived at destination.");
          owner.alignToGrid();
          CommandManager.sendCommand(new StopMovingCommand(owner));
          broken = true;
          return;
        }
      }
      catch (Exception e) {
        e.printStackTrace();
        CommandManager.sendCommand(new StopMovingCommand(owner));
        broken = true;
        return;
      }
    }

    arrived = false;

    if (owner.getFaceDirection() == Entity.FACING_TOP) {
      Log.v(TAG, "Moving up");
      advanceY(speed);
      if (owner.getY() > destination.getY()*Global.tile_size)
        arrived = true;
    }
    else if (owner.getFaceDirection() == Entity.FACING_RIGHT) {
      Log.v(TAG, "Moving right");
      advanceX(speed);
      if (owner.getX() > destination.getX()*Global.tile_size)
        arrived = true;
    }
    else if (owner.getFaceDirection() == Entity.FACING_BOT) {
      Log.v(TAG, "Moving bot");
      advanceY(-speed);
      if (owner.getY() < destination.getY()*Global.tile_size)
        arrived = true;
    }
    else { // FACING_LEFT
      Log.v(TAG, "Moving left");
      advanceX(-speed);
      if (owner.getX() < destination.getX()*Global.tile_size)
        arrived = true;
    }

    if (arrived) {
      Log.v(TAG, "Entity arrived at next tile.");
      try {
        if (path.size > 0) {
          destination = path.removeFirst();
          int prev_facing_direction = owner.getFaceDirection();
          Log.v(TAG, "curr_x = " + owner.getX());
          Log.v(TAG, "curr_y = " + owner.getY());
          owner.alignToGrid();
          Log.v(TAG, "alig_x = " + owner.getX());
          Log.v(TAG, "alig_y = " + owner.getY());
          faceDestination();
        }
        else
          destination = null;
      }
      catch (Exception e) {
        Log.e(TAG, "Shit on destination update.");
        e.printStackTrace();
        CommandManager.sendCommand(new StopMovingCommand(owner));
        broken = true;
        return;
      }
    }

  }

  void faceDestination() {
    try {
      int dx = destination.getX() * Global.tile_size;
      int dy = destination.getY() * Global.tile_size;
      if (dy > owner.getY()) {
        owner.setFaceDirection(Entity.FACING_TOP);
        Log.v(TAG, "Facing evaluated to TOP");
      }
      else if (dy < owner.getY()) {
        owner.setFaceDirection(Entity.FACING_BOT);
      Log.v(TAG, "Facing evaluated to BOT");
    }
      else if (dx > owner.getX()) {
        owner.setFaceDirection(Entity.FACING_RIGHT);
    Log.v(TAG, "Facing evaluated to RIGHT");
  }
      else if (dx < owner.getX()) {
        owner.setFaceDirection(Entity.FACING_LEFT);
        Log.v(TAG, "Facing evaluated to LEFT");
}
      else
        Log.w(TAG, "faceDestination() called when standing on destination.");
    }
    catch (Exception e) {
      Log.e(TAG, "Shit inside faceDestination()");
      e.printStackTrace();
    }
  }

  void leave() {
    Log.w(TAG, "leave() not implemented");
    broken = false;
    destination = null;
    path = null;
  }

  String getName() {
    return TAG;
  }

  void advanceY(float speed) {
    owner.setY( owner.getY() + speed );
  }

  void advanceX(float speed) {
    owner.setX( owner.getX() + speed );
  }

}




