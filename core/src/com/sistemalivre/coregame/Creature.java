package com.sistemalivre.coregame;


import com.badlogic.gdx.math.Vector3;



// ========================= Creature ========================= //

public class Creature extends Entity {

  private static final String TAG = "Creature";



// ========================= DATA ========================= //

  // Possui recursos gráficos
  public GraphicComponent graphic_comp;

  // Possui atributos e medidores
  public BodyComponent body_comp;




// ========================= CONSTRUCTION ========================= //

  Creature(String name, String spritesheet_name) {
    super(name);
    graphic_comp = new GraphicComponent(this, spritesheet_name);
    body_comp = new BodyComponent(this);
    states.add(new IdleState(this));
    states.add(new MovingState(this));
    setState("IdleState");
    setType(CREATURE);
  }




// ========================= LOGIC ========================= //

  @Override
  public void updateExtra(float dt) {
    graphic_comp.update(dt);
  }


  @Override
  public void dispose() {}


  @Override
  public boolean touchUp(int screenX, int screenY,
                         int pointer, int button) {

    /** Converts touched location into Vector3,
      for OrthographicCamera consuming it. **/
    Vector3 touched_spot = new Vector3(screenX, screenY, 0);

    // Tests world collision for the touched point
    if ( worldTouched(touched_spot) ) {
      Log.d(TAG, "We got a collision with the touch.");
      CommandManager.sendCommand( new SelectCommand(this) );
      return true;
    }

    return false;
  }


  @Override
  public String info() {
    String info = ("Info on " + this.getDisplayName() + "\n"
        + "\tlocation " + this.getX() + " " + this.getY() + "\n"
        + "\tHP " + body_comp.getHealthPtsCurr()  + "/"
        + body_comp.getHealthPtsMax() + "\n"
        + "\tAP " + body_comp.getActionPtsCurr()
        + "/" + body_comp.getActionPtsMax() );
    return info;
  }

}
