package com.kallyous.nopeisland;



public class MainMenuInterface extends UserInterface {

  MainMenuInterface(float width, float height) {
    super(width, height);


    // Start/Continue Game button
    UiElement start_btn = new UiElement(
        "start_continue_btn", NopeIslandGame.uiTexture,
        346, 154, 108, 44);

    start_btn.setPosition(
        (screen_width - start_btn.getWidth()) / 2,
        (screen_height/2) + (start_btn.getHeight() + margin)
        );

    start_btn.graphic_comp.sprite.setColor(0.5f, 0.5f, 1.0f, 1f);


    // Exit Game button
    UiElement exit_game_btn = new UiElement(
        "exit_game_btn", NopeIslandGame.uiTexture,
        346, 154, 108, 44);

    exit_game_btn.setPosition(
        (screen_width - start_btn.getWidth()) / 2,
        (screen_height/2) - margin
    );

    exit_game_btn.graphic_comp.sprite.setColor(1f, 0.5f, 0.5f, 1f);


    elements.add(start_btn);
    elements.add(exit_game_btn);
  }



}
