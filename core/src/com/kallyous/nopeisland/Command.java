package com.kallyous.nopeisland;



public abstract class Command {

  Command() {}

  public abstract void execute();

}

class SelectCommand extends Command {

  @Override
  public void execute() {
    System.out.println("Something has been selected.");
  }

}