package com.kallyous.nopeisland;



public class RunningGameInterface extends UserInterface {

  RunningGameInterface(float width, float height) {
    super(width, height);

    // Actions Buttom
    elements.add( new UiElement("btn_actions", 1) );
    elements.get(0).setPosition(10, 10);

    // Options Buttom
    elements.add( new UiElement("btn_options", 8) );
    elements.get(1).setPosition(10, screen_height - 42);

    // Inventory Buttom
    elements.add( new UiElement("btn_inventory", 5) );
    elements.get(2).setPosition(screen_width - 42, 10);

    // Pass Turn Buttom
    elements.add( new UiElement("btn_pass", 4) );
    elements.get(3).setPosition(screen_width - 42, screen_height - 42);

  }

}
