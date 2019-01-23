package com.sistemalivre.coregame;



// ========================== Structure ========================== //

import com.badlogic.gdx.math.Vector3;



public class Structure extends Entity {

  private static final String TAG = "Structure";



// ========================== DATA ========================== //

  GraphicComponent graphic_comp;

  BodyComponent body_comp;




// ========================== CONSTRUCTION ========================== //

  Structure(String name, String asset_name, int texture_region) {
    super(name);
    graphic_comp = new GraphicComponent(this, asset_name);
    graphic_comp.setStandIndex(texture_region);
    body_comp = new BodyComponent(this);
  }


  @Override
  void updateExtra(float dt) {

  }


  @Override
  public void dispose() {
    try {
      Game.world.remEntity(this);
      graphic_comp.destroy();
      body_comp.destroy();
    }
    catch (NullPointerException e) {
      Log.v(TAG, e.getMessage());
    }
    graphic_comp = null;
    body_comp = null;
  }

// ========================== LOGIC ========================== //

  @Override
  public boolean touchUp(int screenX, int screenY,
                         int pointer, int button) {

    /** Converts touched location into Vector3,
     for OrthographicCamera consuming it. **/
    Vector3 touched_spot = new Vector3(screenX, screenY, 0);

    // Tests world collision for the touched point
    if ( worldTouched(touched_spot) ) {
      Log.v(TAG, "Structure " + this.getName() + " touched.");
      return true;
    }

    return false;
  }




// ========================== SET / GET ========================== //




}




