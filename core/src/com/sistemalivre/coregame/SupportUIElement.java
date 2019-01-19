package com.sistemalivre.coregame;


import com.badlogic.gdx.InputProcessor;



// ========================== SupportUIElement ========================== //

public class SupportUIElement extends Entity implements InputProcessor {

  private static final String TAG = "SupportUIElement";



// ========================= DATA BEGIN ========================= //

  public GraphicComponent graphic_comp;




// ========================== CONSTRUCTION ========================== //

  SupportUIElement(String name) {
    super(name);
    graphic_comp = new GraphicComponent(this);
    this.setType(Entity.MOVEMARK);
  }


  SupportUIElement(String name, int region_index) {
    super(name);
    graphic_comp = new GraphicComponent(this, region_index);
    this.setType(Entity.MOVEMARK);
  }




// ========================== LOGIC ========================== //

  @Override
  public void updateExtra(float dt) {
    graphic_comp.update(dt);
  }


  @Override
  public void dispose() {

    /** Remove reference form graphic_comp to this entity, so garbage
      collector do the thing. **/
    graphic_comp.entity = null;

    /** Remove reference to the graphic component.
      This is for a fail-fast, in case of a attempt to render a destroyed
      object. **/
    graphic_comp = null;

    // Tells WorldMap to get this entity from it's bundle
    GameState.world.remEntity(this);

  }

}
