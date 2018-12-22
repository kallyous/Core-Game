package com.kallyous.nopeisland;

import java.util.Vector;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;



public class NopeIslandGame extends ApplicationAdapter implements InputProcessor, GestureDetector.GestureListener {
    // ========================================== DATA ========================================== //

    // Tracks time for animations and other time based events.
    float state_time;

    // Default GUI interface texture.
    public static Texture uiTexture;

    // Graphical User Interface
    private UserInterface gui;

    // A tiled map. We are using a 32x32 pixels grid and tile system.
    private TiledMap tiledMap;
    /** The 32 pixel size still hard coded. This must change. We shall load this information from
     * a config file and use through all the game. */

    // Graph Map
    private GraphMap graph_map;

    // Movements processor.
    private MovementProcessor movementProcessor;
    /** This class will be used to process movement actions. We use it in a separate class from creatures
     for future malleability. It also keeps Creature class cleaner.
     Also we use a lot of different algorithms to determinate where the creature can move, and there
     will be even more, like path finding algorithms and AI integration for example. */

    // Camera
    OrthographicCamera camera;
    /** This is the perspective from what the game will be draw.
     One important thing to remember, is that the screen coordinates, world coordinates and map
     coordinates are all different.
     Every action captured from the InputProcessor(LibGDX input listener class) gets the viewport,
     here called screen or, since we only use a single camera, the camera view's coordinates, not
     the world or map coords. Putting it simple: every interaction with the screen, gives the input
     processor the coordinates of the device's SCREEN or(for desktops) WINDOW coordinates.
     This is perfect, plain and simple for UI elements. For all the rest, it requires the following:
     1. Get screen coordinates with the InputProcessor
     2. Use camera.unproject(screenX,screenY,0) to get the world coordinates;
     2. If the coordinates needed are the tiled map ones, now we just calculate it:
     int x = ((int)unprojectedX)/16; for getting tiled map X coordinate
     world coordinates are float, tiled map coordinates are int */

    // Renderer method for the tiled map
    OrthogonalTiledMapRendererWithSprites mapRender;
    /** The default tiled map render from LibGDX is weird.
     So we use this one, from:
     http://www.gamefromscratch.com/post/2014/05/01/LibGDX-Tutorial-11-Tiled-Maps-Part-2-Adding-a-character-sprite.aspx
     May consider changing this on future */

    // For input management
    InputMultiplexer inputMultiplexer;
    /** Wait, what!? o_O
     Yeah, a input multiplexer is what it sounds: a class for holding all input listeners and manage
     them. It may sound weird, but simplify the chore of having a ton of input processors in a single
     class and/or scene. Just like we have. */

    // Window and viewport
    float game_window_width, game_window_height;
    /* Just like it sounds, we will store the game window size here, for use later */

    // Entities that take actions
    private Vector<Entity> entities_active;

    // Entities that doesn't take actions
    private Vector<Entity> entities_passive;

    // Creature assets
    Creature player, bug;
    SpriteBatch batch;
    SpriteBatch guiBatch;
    /** We use a separate sprite batch's for game world stuff and UI elements because of projection.
     While all stuff are draw relative to the world coordinates, UI elements are draw relative
     to the screen coordinates, for obvious reasons (you don't want drag menu buttons out of the
     screen when panning the world, want you?).
     Also, is worth to remember that UI shall be rendered/draw after everything else, so it is always
     at the top/front of all other things. */


    // ==================================== CREATE / STARTUP =====================================//

    @Override
    public void create(){
        // Starts timer
        state_time = 0f;

        // Gets viewport sizes
        game_window_width = Gdx.graphics.getWidth();
        game_window_height = Gdx.graphics.getHeight();

        uiTexture = new Texture(Gdx.files.internal("graphic/interface.png"));

        // Initializes GUI
        gui = new UserInterface(game_window_width, game_window_height);

        // Initialize Entity Vectors
        entities_active = new Vector<Entity>();
        entities_passive = new Vector<Entity>();

        // Initialize Sprite Batches
        batch = new SpriteBatch();
        guiBatch = new SpriteBatch();
        /** Sprite batches takes sprites and prepares them to send to the GPU/CPU for rendering the
         final image. Very important stuff. Do your worship, human. */

        // Tiled Map Initialization
        tiledMap = new TmxMapLoader().load("levels/the_bug_island/overworld.tmx");

        // Graph Map Initialization
        graph_map = new GraphMap(100,100);

        // Map Render Initialization
        mapRender = new OrthogonalTiledMapRendererWithSprites(tiledMap);

        // Initialize Camera and place it above the player
        camera = new OrthographicCamera();
        camera.setToOrtho(false,game_window_width,game_window_height);
        camera.position.set(50*32, 47*32, 0); // Initial island location
        camera.update();
        /** This is camera's morning coffee. It will glitch without having it at
         * start of every day. I mean, cycle. And it may need additional ones
         * through the day, depending on the circumstances. */

        // Initialize Movement Processor
        movementProcessor = new MovementProcessor();

        // Bring bugs to life!
        try {
            player = CreatureBuilder.buildFromCatalog("humanFighter", 1);
            player.linkGraphMap(graph_map);
            player.setTilePosition(50,47);
            player.setControllable(true);
            player.setCamera(camera);
            entities_active.add(player);
            bug = CreatureBuilder.buildFromCatalog("forestBeetle", 1);
            bug.linkGraphMap(graph_map);
            bug.setTilePosition(51,47);
            bug.setCamera(camera);
            bug.setControllable(true);
            bug.setStance(Creature.STANCE_HOSTILE);
            entities_active.add(bug);
        }
        catch (NoSuchFieldException e) {
            e.printStackTrace();
        }



        // Input Multiplexer Initialization and Filling
        inputMultiplexer = new InputMultiplexer();
        // GUI elements
        gui.setInputMultiplexer(inputMultiplexer);
        // Entity Vectors
        for (Entity ent : entities_active)
            inputMultiplexer.addProcessor(new GestureDetector(ent));
        //inputMultiplexer.addProcessor(ent);
        for (Entity ent : entities_passive)
            inputMultiplexer.addProcessor(new GestureDetector(ent));
        //inputMultiplexer.addProcessor(ent);
        // Main Class (Unprojects on the map)
        inputMultiplexer.addProcessor(this);
        // Map Itself
        inputMultiplexer.addProcessor(new GestureDetector(this));
        // Set this Input Multiplexer as the active one.
        Gdx.input.setInputProcessor(inputMultiplexer);
    }



    // ======================================= MAIN LOOP ======================================== //
    @Override
    public void render () {
        // Some openGL stuff.
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Increment elapsed time
        state_time += Gdx.graphics.getDeltaTime();




        // ################################# GAME LOGIC START ################################### //

        // Entities updates
        for (Entity ent : entities_active)
            ent.update(state_time);
        for (Entity ent : entities_passive)
            ent.update(state_time);

        // GUI elements
        gui.update(Entity.selected_entity);

        // Start New Turn (as needed)
        if ( everyoneActed() )
            nextTurn();

        // ################################### GAME LOGIC END ################################### //




        // #################################### RENDER START #################################### //
        // Update camera status and properties. Very important stuff.
        camera.update();

        // Tilemap render
        mapRender.setView(camera);
        mapRender.render();

        // We call batch.begin() before giving any thing for drawing
        batch.begin();
        batch.setProjectionMatrix(camera.combined); /* Don't ask, just do it. */

        // Movement Processor markers
        movementProcessor.draw(batch);

        // Draw Entities
        for (Entity entity : entities_active)
            entity.draw(batch);
        for (Entity entity : entities_passive)
            entity.draw(batch);

        // When we have everything set on the right precedence order, we call batch.end() for it to
        // send all stuff to GPU/CPU renderer
        batch.end();

        // Graphical User Interface Rendering
        guiBatch.begin();
        gui.draw(guiBatch);
        guiBatch.end();
        // ##################################### RENDER END ##################################### //

    }



    // ======================================== LOGIC =========================================== //

    // Checks if all active entities have spent their AP.
    public boolean everyoneActed() {
        for (Entity ent : entities_active) {
            switch (ent.getEntityType()) {
                case Entity.GENERIC_ENTITY:
                    System.out.println("WARING: " + ent.getName() + " doesn't have AP!");
                    break;
                case Creature.CREATURE_ENTITY:
                    if ( ((Creature)ent).getActionPoints() > 0 ) return false;
                    break;
                default:
                    System.out.println("WTF!?\nUnidentified entity!");
                    break;
            }
        }
        System.out.println("All active entities have ran out of AP. Time to roll the next turn.");
        return true;
    }

    // Starts New Turn
    public void nextTurn() {
        // Restores Everybody Action Points
        for (Entity ent : entities_active) {
            switch (ent.getEntityType()) {
                case Entity.GENERIC_ENTITY:
                    System.out.println("WARING: " + ent.getName() + " has no AP.");
                    break;
                case Creature.CREATURE_ENTITY:
                    ((Creature)ent).setActionPoints( ((Creature)ent).getMaxActionPoints() );
                    break;
                default:
                    System.out.println("WHAT THE FUUUCK!?");
                    break;
            }
        }
    }



    // ========================================== INPUT ========================================= //

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    // Moves Entities
    @Override
    public boolean tap(float x, float y, int count, int button) {
        Vector3 touchedPoint = new Vector3(x, y, 0);

        if (count == 2) {
            if (button == 1) {
                camera.unproject(touchedPoint);
                System.out.println("Double right click on the map at "
                        + (int)(touchedPoint.x/32) + " " + (int)(touchedPoint.y/32) );
                // If there is something selected and is controllable, it triggers the auto-move.
                if (Entity.selected_entity != null ) {
                    System.out.println(Entity.selected_entity.getDisplayName() + " is selected.");
                    if(Entity.selected_entity.isControllable()) {
                        System.out.println(Entity.selected_entity.getDisplayName() +
                                " is controllable, triggering movement.");
                        // Get movement path ready without drawing the path.
                        movementProcessor.processMovement(Entity.selected_entity,
                                touchedPoint, true);
                        // Sets the entity to moving, so on it's next update it starts moving.
                        Entity.selected_entity.startMoving();
                    }
                }
                else System.out.println("With nothing selected, there is noting to do.");
                return true;
            }
            if (button == 0) {
                // TODO: on Android, this will have the effect of double right click on desktop.
                System.out.println("Double left click on the map, nothing to do.");
                return true;
            }
        }

        System.out.println( "Map taped with button " + Integer.toString(button) );

        switch (Gdx.app.getType()) {
            case Android:
                if (Entity.selected_entity != null) {
                    movementProcessor.processMovement(Entity.selected_entity,
                            camera.unproject(touchedPoint), true);
                    return true;
                }
                break;
            case Desktop:
                if (Entity.selected_entity != null) {
                    if (button == 1) {
                        movementProcessor.processMovement(Entity.selected_entity,
                                camera.unproject(touchedPoint), true);
                        return true;
                    }
                    else return false;
                }
                break;
            case iOS:

                break;
            case WebGL:

                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    // Drags Map
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        /* This simple thing, which gave me a hell of a headache to figure out, pans the map
           alongside users dragging finger or mouse. */
        camera.position.set(camera.position.x - deltaX, camera.position.y + deltaY, 0);

        /* We return true so after the finger/mouse release, a tap or click won't be processed for
           the current finger/mouse location, which could be UI element, game creature, etc. */
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom (float originalDistance, float currentDistance){
        return false;
    }

    @Override
    public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer,
                          Vector2 firstPointer, Vector2 secondPointer){
        return false;
    }

    @Override
    public void pinchStop() {
        return;
    }

    @Override
    public void dispose(){
        // Disposes are for preventing memory leaks. I haven't implemented this properly yet.
        /* TODO: properly implement resources disposals */
        tiledMap.dispose();
        mapRender.dispose();
        batch.dispose();
        guiBatch.dispose();
    }



    // ======================================= DEBUG INPUT ====================================== //

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    // Sends Mouse position to UI.
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 point = new Vector3(screenX, screenY, 0);
        camera.unproject(point);
        gui.setDebugCursorText( "X:" + (int)(point.x/32) + "  Y:" + (int)(point.y/32) );
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
