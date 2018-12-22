package com.kallyous.nopeisland;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;

import java.util.Vector;

public class UserInterface {

    // ======================================= CONSTANTS ======================================== //

    // Default UI graphics
    private Texture uiTexture = NopeIslandGame.uiTexture;



    // =================================== INNER CLASS UiElement ================================ //
    class UiElement extends Entity {

        UiElement() {
            super("uiElement", uiTexture, 0);
        }

        UiElement(String name, int region_index) {
            super(name, uiTexture, region_index);
        }

        UiElement(String name, Texture texture) {
            super(name, texture, 0);
        }

        UiElement(String name, Texture texture, int region_index) {
            super(name, texture, region_index);
        }

        UiElement(String name, Texture texture, int sheet_cols, int sheet_rows, int region_index) {
            super(name, texture, sheet_cols, sheet_rows, region_index);
        }

    }



    // ======================================== DATA ============================================ //

    // Screen Dimensions
    private static float screen_width, screen_height;

    // Array holding the UI elements
    public Vector<UiElement> elements;

    // Debug Text
    private BitmapFont default_font = new BitmapFont();
    private String select_entity_str;
    private BitmapFont debug_font  = new BitmapFont();
    private String debug_text;
    private BitmapFont cursor_font  = new BitmapFont();
    private String debug_cursor;


    // ====================================== CONSTRUCTION ====================================== //

    // Main Constructor
    UserInterface(float width, float height) {

        // Screen Dimensions Initialization
        screen_width = width;
        screen_height = height;

        debug_cursor = "X:--  Y:--";

        // Elements Array Initialization
        elements = new Vector<UiElement>();

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

    // Loads and sets up external InputMultiplexer
    public void setInputMultiplexer(InputMultiplexer inputMultiplexer) {
        for (UiElement elem : elements)
            inputMultiplexer.addProcessor(new GestureDetector(elem));
            //inputMultiplexer.addProcessor(elem);
    }



    // ======================================== LOGIC =========================================== //

    // Update All UiElement's
    public void update(Entity selected_entity) {
        // ´selected_entity == null´ when nothing is selected.
        try {
            select_entity_str = selected_entity.getInfo();
            if (selected_entity.getEntityType() == Creature.CREATURE_ENTITY) {
                debug_text = "HP: " + Float.toString(
                        ((Creature)selected_entity).getHealth()) + "\n";
                debug_text += "AP: " + Float.toString(
                        ((Creature)selected_entity).getActionPoints() ) + "\n";
            }
            else {
                debug_text = selected_entity.getDisplayName() + " is inanimate";
            }
            debug_text += "fX: " + Float.toString(selected_entity.getX()) + " fY: "
                    + Float.toString(selected_entity.getY()) + "\n";
        }
        catch (NullPointerException np) {
            select_entity_str = "Nothing selected";
            debug_text = "";
        }
        // Update UiElemten's
        for (UiElement elem : elements) {
            // TODO: Shitty workaround for the lack of delta time here. Lets pass the state time
            //       from the main class to here when updating the GUI.
            elem.update(0f);
        }
    }

    // Draw All UiElement's
    public void draw(SpriteBatch batch) {
        for (UiElement elem : elements) {
            elem.sprite.draw(batch);
        }
        drawDebugText(batch);
    }

    // Debug Text
    private void setDebugText(String text) {
        debug_text = text;
    }
    public void setDebugCursorText(String text) {
        debug_cursor = text;
    }
    private void drawDebugText(SpriteBatch batch) {
        default_font.setColor(1.0f,1.0f,1.0f,1.0f); // Tint for the font
        default_font.draw(batch, select_entity_str, 80, screen_height - 10); // Where and what to draw
        debug_font.setColor(1.0f,1.0f,1.0f,1.0f); // Tint for the other font
        debug_font.draw(batch, debug_text,80, screen_height - 30); // Where and what again
        cursor_font.setColor(1.0f,1.0f,1.0f,1.0f);
        cursor_font.draw(batch, debug_cursor, screen_width - 110, screen_height - 10);
    }
}
