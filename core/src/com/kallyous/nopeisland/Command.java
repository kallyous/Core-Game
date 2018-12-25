package com.kallyous.nopeisland;



abstract public class Command {

  private static final String TAG = "Command";

  // The superclass only has a target and uses its class name as keyword
  public Entity target;

  // Return the command TAG
  abstract public String getTAG();

  // Must have a target for the command
  Command(Entity target) {
    this.target = target;
  }

  // The core of all commands
  abstract public boolean action();

}



// Start/Resume the game
class RunGameCommand extends Command {

  private static final String TAG = "RunGameCommand";


  RunGameCommand(Entity target) {
    super(target);
  }


  @Override
  public String getTAG() {
    return TAG;
  }

  @Override
  public boolean action() {
    System.out.println(TAG + ": Starting/Resuming game. ");
    NopeIslandGame.game.enter(NopeIslandGame.running_state);
    return true;
  }
}





// Exit/Close the game
class ExitCommand extends Command {

  private static final String TAG = "ExitCommand";


  ExitCommand(Entity target) {
    super(target);
  }

  @Override
  public boolean action() {
    System.out.println(TAG + ": Issuing shutdow flag. Game is about to exit.");
    NopeIslandGame.game_running = false;
    return true;
  }

  @Override
  public String getTAG() {
    return TAG;
  }

}





// A subclass for testing
class SelectCommand extends Command {

  private static final String TAG = "SelectCommand";

  SelectCommand(Entity entity) {
    super(entity);
  }

  @Override
  public boolean action() {
    System.out.println(TAG + ": " + this.target.getName()
        + " received a select command.");
    return true;
  }

  @Override
  public String getTAG() {
    return TAG;
  }

}