package com.kallyous.nopeisland;




/** ========================= COMMAND ========================= **/

abstract public class Command {

  private static final String TAG = "Command";



// ========================= DATA SETUP BEGIN ========================= //

  // The superclass only has a target and uses its class name as keyword
  public Entity target;

// ========================= DATA SETUP END ========================= //




// ========================= CONSTRUCTION BEGIN ========================= //

  // Mostly used for entities issuing commands to themselves
  Command(Entity target) {
    this.target = target;
  }

  // Issued for specific entities of known name
  Command(String target_name) {
    this.target = NopeIslandGame.entities.get(target_name);
    if (this.target == null) {
      System.out.println(TAG + ": Falha ao localizar " + target_name + " na hash table.");
    }
  }

// ========================= CONSTRUCTION END ========================= //




// ========================= ABSTRACT BEGIN ========================= //

  // Return the command TAG
  abstract public String getTAG();

  // The core of all commands
  abstract public boolean execute();

// ========================= ABSTRACT END ========================= //

}





/** ========================= OPEN MAIN MENU ========================= **/

// Pause game and bring in the title screen with the main menu
class OpenMainMenuCommand extends Command {

  private static final String TAG = "OpenMainMenuCommand";



// ========================= CONSTRUCTOR BEGIN ========================= //

  OpenMainMenuCommand(Entity target) {
    super(target);
  }

// ========================= CONSTRUCTOR END ========================= //




// ------------------------- Logic -------------------------- //

  @Override
  public String getTAG() {
    return TAG;
  }


  @Override
  public boolean execute() {
    System.out.println(TAG + ": Pausando jogo e abrindo menu principal. ");
    NopeIslandGame.game.switchTo(NopeIslandGame.main_menu_state);
    return true;
  }

// ---------------------------------------------------------------- //

}





/** ========================= RUN GAME ========================= **/

// Start/Resume the game
class RunGameCommand extends Command {

  private static final String TAG = "RunGameCommand";



// ========================= COSNTRUCTION BEGIN ========================= //

  RunGameCommand(Entity target) {
    super(target);
  }

// ========================= COSNTRUCTION END ========================= //




// ------------------------- Logic -------------------------- //

  @Override
  public String getTAG() {
    return TAG;
  }

  @Override
  public boolean execute() {
    System.out.println(TAG + ": Starting/Resuming game. ");
    NopeIslandGame.game.switchTo(NopeIslandGame.running_state);
    return true;
  }

// ---------------------------------------------------------------- //

}





/** ========================= EXIT GAME ========================= **/

// Exit/Close the game
class ExitCommand extends Command {

  private static final String TAG = "ExitCommand";



// ========================= CONSTRUCTION BEGIN ========================= //

  ExitCommand(Entity target) {
    super(target);
  }

// ========================= CONSTRUCTION END ========================= //




// ------------------------- Logic -------------------------- //

  @Override
  public boolean execute() {
    System.out.println(TAG + ": Issuing shutdow flag. Game is about to exit.");
    NopeIslandGame.game_running = false;
    return true;
  }

  @Override
  public String getTAG() {
    return TAG;
  }

// ---------------------------------------------------------------- //

}





/** ========================= SELECT ENTITY ========================= **/

// A subclass for testing
class SelectCommand extends Command {

  private static final String TAG = "SelectCommand";



// ========================= CONSTRUCTION BEGIN ========================= //

  SelectCommand(Entity entity) {
    super(entity);
  }

// ========================= CONSTRUCTION END ========================= //




// ------------------------- Logic -------------------------- //

  @Override
  public boolean execute() {
    System.out.println(TAG + ": " + this.target.getName()
        + " received a select command.");
    Entity.selected_entity = this.target;
    return true;
  }

  @Override
  public String getTAG() {
    return TAG;
  }

// ---------------------------------------------------------------- //

}






/** ========================= SET TEXT CONTENT COMMAND ========================= **/

class SetTextContentCommand extends Command {

  private static final String TAG = "SetTextContentCommand";




// ========================= DATA BEGIN ========================= //

  public String content;

// ========================= DATA END ========================= //




// ========================= CREATION BEGIN ========================= //

  SetTextContentCommand(Entity entity, String content) {

    super(entity);

    this.content = content;

  }

  SetTextContentCommand(String entity_name, String content) {

    super(entity_name);

    this.content = content;

  }

// ========================= CREATION END ========================= //




// ========================= LOGIC BEGIN ========================= //

  @Override
  public String getTAG() {
    return TAG;
  }


  @Override
  public boolean execute() {
    // TODO: 26/12/18 Adicionar try{}catch(){} adequado
    TextElement target_te = (TextElement)this.target;
    target_te.label.setText(content);
    return true;
  }

// ========================= LOGIC END ========================= //

}






/** ========================= DESTROY ENTITY COMMAND ========================= **/

class DestroyEntityCommand extends Command {

  private static final String TAG = "DestroyEntityCommand";




// ========================= CREATION BEGIN ========================= //

  DestroyEntityCommand(Entity target) {
    super(target);
  }

// ========================= CREATION END ========================= //




// ========================= LOGIC BEGIN ========================= //

  @Override
  public String getTAG() {
    return TAG;
  }


  @Override
  public boolean execute() {
    System.out.println(TAG + ": Destroying " + target.getName());
    System.out.println(TAG + ": Object is  " + target.toString());
    target.destroy();
    return true;
  }

// ========================= LOGIC END ========================= //

}