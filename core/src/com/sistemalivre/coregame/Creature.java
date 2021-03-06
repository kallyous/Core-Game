package com.sistemalivre.coregame;



import com.badlogic.gdx.math.Vector3;



/** ========================= Creature ========================= **/

public class Creature extends Entity {

  private static final String TAG = "Creature";




// ========================= DATA BEGIN ========================= //

  // Possui recursos gráficos
  public GraphicComponent graphic_comp;

  // Possui atributos e medidores
  public BodyComponent body_comp;

  // Possui funcionalidades de combate
  public CombatComponent combat_comp;

// ========================= DATA END ========================= //




// ========================= CREATION BEGIN ========================= //

  Creature(String name) {
    super(name);
    graphic_comp = new GraphicComponent(this);
    body_comp = new BodyComponent(this);
    combat_comp = new CombatComponent(this);
    this.command_comp.enableCommand("SelectCommand");
    this.setType(CREATURE);
  }

  Creature(String name, String spritesheet_name,
           int sheet_cols, int sheet_rows, int region_index) {
    super(name);
    graphic_comp = new GraphicComponent(this, spritesheet_name,
        sheet_cols, sheet_rows, region_index);
    body_comp = new BodyComponent(this);
    combat_comp = new CombatComponent(this);
    this.command_comp.enableCommand("SelectCommand");
    this.setType(CREATURE);
  }

// ========================= CREATION END ========================= //




// ========================= LOGIC BEGIN ========================= //

  @Override
  public void update(float dt) {
    graphic_comp.update(dt);
  }


  @Override
  public void dispose() {}

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {

    // Converts touched location into Vector3, for OrthographicCamera consuming it
    Vector3 touched_spot = new Vector3(screenX, screenY, 0);

    // Tests world collision for the touched point
    if ( worldTouched(touched_spot) ) {
      Log.i(TAG + " - We got a collision with the touch.");
      CoreGame.command_manager.sendCommand( new SelectCommand(this) );
      return true;
    }

    return false;
  }

  @Override
  public String info() {
    String info = ("Info on " + this.getDisplayName() + "\n"
        + "\tlocation " + this.getX() + " " + this.getY() + "\n"
        + "\tHP " + body_comp.getHealthPtsCurr()  + "/" + body_comp.getHealthPtsMax() + "\n"
        + "\tAP " + body_comp.getActionPtsCurr() + "/" + body_comp.getActionPtsMax() );
    return info;
  }

// ========================= LOGIC END ========================= //

}
