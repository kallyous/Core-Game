package com.kallyous.nopeisland;




/** ========================= COMMAND CONSUMING COMPONENT ========================= **/

public class CommandComponent {

  // Entity owning the current instance of CommandComponent
  public Entity owner;

  // Must have a owner
  CommandComponent(Entity owner) {
    this.owner = owner;
  }

  // By default we just return true if shit matched
  public boolean execute(Command command) {
    if (command.target == this.owner) return true;
    return false;
  }

}
