package com.kallyous.nopeisland;



import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
      System.out.println(TAG + ": We got a collision with the touch.");
      NopeIslandGame.command_manager.sendCommand( new SelectCommand(this) );
      return true;
    }

    return false;
  }

// ========================= LOGIC END ========================= //

}