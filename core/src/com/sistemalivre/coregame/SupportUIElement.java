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
  public void update(float dt) {
    graphic_comp.update(dt);
  }


  @Override
  public void dispose() {
    graphic_comp = null;
  }
}
