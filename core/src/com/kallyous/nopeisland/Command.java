package com.kallyous.nopeisland;



abstract public class Command {

  // The superclass only has a target and uses its class name as keyword
  public Entity target;
  protected String keyword = this.toString();

  // Must have a target for the command
  Command(Entity target) {
    this.target = target;
  }

  // We need the keyowrd for debugging
  public String info() {
    return keyword;
  }

}




// A subclass for testing
class SelectCommand extends Command {

  SelectCommand(Entity entity) {
    super(entity);
    keyword = this.toString();
  }

}