package com.sistemalivre.coregame;


import com.badlogic.gdx.InputProcessor;



public class SupportUIElement extends Entity implements InputProcessor {

  private static final String TAG = "SupportUIElement";



  // ========================= DATA BEGIN ========================= //

  public GraphicComponent graphic_comp;

  // ========================= DATA END ========================= //

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


  @Override
  public void update(float dt) {
    graphic_comp.update(dt);
  }



  @Override
  public void dispose() {

  }
}
