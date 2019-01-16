package com.sistemalivre.coregame;




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
    this.target = CoreGame.entities.get(target_name);
    if (this.target == null) {
      Log.w(TAG + " - Falha ao localizar " + target_name + " na hash table.");
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
    Log.i(TAG + " - Pausando jogo e abrindo menu principal. ");
    CoreGame.game.switchTo(CoreGame.main_menu_state);
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
    Log.i(TAG + " - Starting/Resuming game. ");
    CoreGame.game.switchTo(CoreGame.running_state);
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
    Log.i(TAG + " - Issuing shutdow flag. Game is about to exit.");
    CoreGame.game_running = false;
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

    if (this.target == Entity.selected_entity) {
      Log.i(TAG + " - Unselecting " + this.target.getName() );
      Entity.selected_entity = null;
    }
    else {
      Log.i(TAG + " - Selecting " + this.target.getName());
      Entity.selected_entity = this.target;
      Log.d( TAG + " - Info on selected entity:\n" + this.target.info() );
    }

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
    try {
      TextElement target_te = (TextElement)this.target;
      target_te.label.setText(content);
      return true;
    } catch (Exception e) {
      Log.e(TAG + " - Comando gerou um erro durante a execução.");
      e.printStackTrace();
      return false;
    }
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
    Log.i(TAG + " - Destroying " + target.getName());
    Log.i(TAG + " - Object is  " + target.toString());
    target.destroy();
    return true;
  }

// ========================= LOGIC END ========================= //

}
