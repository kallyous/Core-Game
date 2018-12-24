package com.kallyous.nopeisland;


import java.util.NoSuchElementException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Queue;



public class Creature extends Entity {

  // ========================================== CONSTANTS ===================================== //

  // Implements new Entity type
  public static final int CREATURE_ENTITY = 1;

  // Creatue Stance Toward the Player
  public static final int STANCE_ALLY = 0;
  public static final int STANCE_FRIENDLY = 1;
  public static final int STANCE_NEUTRAL = 2;
  public static final int STANCE_HOSTILE = 3;
  public static final int STANCE_HUNTING = 4;

  // Creature Alignment
  public static final int ALIGNMENT_GOOD = 0;
  public static final int ALIGNMENT_NEUTRAL = 1;
  public static final int ALIGNMENT_EVIL = 2;



  // ========================================= CREATURE DATA ================================== //

  // Creature Info
  private int creature_level;
  private String creature_type;
  private String creature_race;
  private String creature_class;

  // Attributes
  private float strength;
  private float agility;
  private float vitality;
  private float intelligence;
  private float willpower;
  private float charisma;

  // Soft Status (Rises and lowers all the time)
  private float health_max;
  private float health_current;
  private float stamina_max;
  private float stamina_current;
  private float mana_max;
  private float mana_current;
  private float food_max;
  private float food_current;
  private float water_max;
  private float water_current;
  private float rest_max;
  private float rest_current;
  private int actions_max;
  private int actions_curent;
  private float morale_max;
  private float morale_normal;
  private float morale_current;

  // Hard Status (Changes pretty less often)
  private float atk_melee; // Melee hit-rate
  private float atk_ranged; // Ranged hit-rate
  private float magic_pressure; // Kind of magic hit-rate. Kind of.
  private float dodge; // Chance to avoid being hit by attacks
  private float defense; // Mitigate damage from direct physical sources (slash, impact...)
  private float resistence; // Mitigate damage from non-physical sources (fire, acid, cold...)
  private float resilience; // Avoid special and magical effects (charm, confusion, sleep...)

  // Additional Status
  private float natural_armor;
  private float speed; /* Default is 1. If reaches 2 the creature can act twice in a turn, if it
   * gets lower than 1 the creature loses a turn between actions and so on.*/

  // Creature Alignment (Good, Neutral or Evil)
  private int alignment = ALIGNMENT_NEUTRAL;

  // Creature Stance Towards the Player
  private int creature_stance = STANCE_NEUTRAL;
  /* Ally, Friendly, Neutral, Hostile or Hunting.
   * Yeah, hunting. Because fuck you that's why! You are the prey now. */

  // Movement Types
  private boolean move_land, move_water, move_air, move_walls;

  // Camera in use by main class for projection, unprojection and collision detection.
  private Camera cam;

  // Metadata about the frames.
  private JsonValue frames_meta;

  // Last known facing direction. To known when to recreate the animation for a changed direction.
  private int last_facing_direction;



  // ======================================= CREATION ========================================= //

  // Constructor
  Creature(String name, Texture texture, int sheet_cols, int sheet_rows, int region_index) {
    super(name, texture, sheet_cols, sheet_rows, region_index);
    last_facing_direction = this.getDirection();
  }



  // ========================================= LOGIC ========================================== //

  // Update
  public void update(float state_time) {
    if (this.isMoving())
      this.move();
    if (animate)
      sprite.setRegion( animation.getKeyFrame(state_time, true) );
    else
      sprite.setRegion( getRegionAt(getRegionIndex()) );
  }

  // Moves graphical asset or something...
  private void advanceX(float ammount) {
    this.setX( this.getX() + ammount );
  }
  private void advanceY(float ammount) {
    this.setY( this.getY() + ammount );
  }

  // Movement Triggers
  @Override
  public void startMoving() {
    // Start the animation.
    this.animateMovement();
    this.setMoving(true);
  }
  @Override
  public void stopMoving() {
    this.animateIdle();
    this.setMoving(false);
  }



  // ======================================== ACTIONS ========================================= //

  // Default Action
  @Override
  public void defaultAction(Entity target_entity) {
    if (this.isControllable()) {
      switch (target_entity.getEntityType()) {
        case Entity.GENERIC_ENTITY:
          interact(target_entity);
          break;
        case Creature.CREATURE_ENTITY:
          int stance = ((Creature)target_entity).getStance();
          if (stance == STANCE_HOSTILE || stance == STANCE_HUNTING)
            defaultAttack( (Creature)target_entity );
          else
            interact(target_entity);
          break;
        default:
          System.out.println("This isn't a valid entity to interact. Doing nothing.");
          break;
      }
    }
    else {
      System.out.println("Selected creature is not controllable, so no action taken.");
    }
  }

  // Default Interaction
  @Override
  public void defaultInteraction(Entity triggering_entity) {
    System.out.println(getName() + ": " + triggering_entity.getName()
        + " triggered my interaction.");
  }

  // Context Menu
  @Override
  public void openContextMenu(Entity source_entity) {
    System.out.println("Opening " + getName() + " Contextual Menu");
  }

  // Selected Creature (this) Interacts with Touched Entity (target)
  public void interact(Entity target_entity){
    if (this.getActionPoints() > 0) {
      System.out.println(getName() + ": I will interact with " + target_entity.getName());
      target_entity.defaultInteraction(this);
      this.setActionPoints( this.getActionPoints() - 1 );
    }
    else
      System.out.println(this.getName() + " can't interact now: No AP left.");
  }

  // Select Creature Uses it's Default Attack on Touched Creature
  public void defaultAttack(Creature target_creature){
    if ( this.getActionPoints() > 0 ) {
      System.out.println(getName() + ": I'm attacking " + target_creature.getName());
      attackMain(target_creature);
      this.setActionPoints( this.getActionPoints() - 1 );
    }
    else
      System.out.println(this.getName() + " can't attack: No AP left.");
  }

  // Main attack
  public void attackMain(Creature target_creature) {
    // Triggers attack animation
    animateMainAttack();
    // Primitive damage calculation. Never misses.
    target_creature.setHealth(target_creature.getHealth() - this.strength);
    System.out.println(this.getDisplayName() + " hit " + target_creature.getDisplayName() +
        " for " + this.strength + " damage.");
    if (target_creature.getHealth() <= 0.0)
      System.out.println(target_creature.getDisplayName() + " is dead.");
  }

  // Secondary attack
  public void attackSec() {
    // TODO
  }

  // Movement. Called on update() if `this.isMoving() == true`.
  @Override
  public void move() {
    // Prepare some data.
    float speed = 4.0f;
    boolean moved = false;
    boolean turned = false;
    GraphMapVertex temp_v;
    int dest_tx, dest_ty;
    float dest_x, dest_y;

    // Do some stuff.
    try {
      // Peek into next edge of the path.
      temp_v = this.getActivePath().first();
      dest_tx = temp_v.getX();
      dest_ty = temp_v.getY();
      dest_x = (float)(dest_tx*32);
      dest_y = (float)(dest_ty*32);
    }
    catch (NullPointerException e) {
      System.out.println("NOTICE: active_movement is null.");
      this.stopMoving();
      return;
    }
    catch (NoSuchElementException e) {
      System.out.println("NOTICE: active_movement is empty.");
      // Stops moving and puts creature in idle frame facing the last moved position.
      this.stopMoving();
      this.setActivePath(null);
      return;
    }

    switch (this.getDirection()) {
      case FACING_TOP:
        System.out.println("Facing TOP");
        break;
      case FACING_RIGHT:
        System.out.println("Facing RIGHT");
        break;
      case FACING_BOT:
        System.out.println("Facing BOT");
        break;
      case FACING_LEFT:
        System.out.println("Facing LEFT");
        break;
      default:
        System.out.println("Facing undefined.");
        break;
    }

    // Evaluates, based where is going, where is the creature is facing to and moves it.
    if (dest_ty > this.getTileY()) {
      this.setDirection(FACING_TOP);
      this.advanceY(speed);
    }
    else if (dest_ty < this.getTileY()) {
      this.setDirection(FACING_BOT);
      this.advanceY(-speed);
    }
    else if (dest_tx > this.getTileX()) {
      this.setDirection(FACING_RIGHT);
      this.advanceX(speed);
    }
    else if (dest_tx < this.getTileX()) {
      this.setDirection(FACING_LEFT);
      this.advanceX(-speed);
    }
    else {
      System.out.println("Creature arrived next edge.");
      this.getActivePath().removeFirst();
      return;
    }

    // If animations aren't playing or facing direction changed, (re)build animation.
    if (!this.isMoving() || last_facing_direction != this.getDirection()) {
      System.out.println("(re)Build moving animation.");
      last_facing_direction = this.getDirection();
      this.startMoving();
    }

    // Here we calculate if the creature arrived at the next edge.
    switch (this.getDirection()) {
      case FACING_TOP:
        if (this.getY() >= dest_y) {
          this.setTileY(dest_ty);
          if (this.getActivePath().size == 0)
            this.setTileX(dest_tx);
          return;
        }
        break;
      case FACING_BOT:
        if (this.getY() <= dest_y) {
          this.setTileY(dest_ty);
          if (this.getActivePath().size == 0)
            this.setTileX(dest_tx);
          return;
        }
        break;
      case FACING_RIGHT:
        if (this.getX() >= dest_x) {
          this.setTileX(dest_tx);
          if (this.getActivePath().size == 0)
            this.setTileY(dest_ty);
          return;
        }
        break;
      case FACING_LEFT:
        if (this.getX() <= dest_x) {
          this.setX(dest_x);
          if (this.getActivePath().size == 0)
            this.setTileX(dest_tx);
          return;
        }
        break;
      default:
        System.out.println("WARNNING: Something is weird at te move() method."
            + " We shouldn't arrive here when creature reaches the next edge.");
        break;
    }

  }



  // ======================================= Animations ======================================= //

  public void animateMainAttack() {
    // TODO: Substitute this for method to known the attack type first.
    String frame_group_name = "slash";
    // This gives us the name of the frame group to be loaded.
    switch (this.getDirection()) {
      case Entity.FACING_BOT:
        frame_group_name += "_bot";
        break;
      case Entity.FACING_LEFT:
        frame_group_name += "_left";
        break;
      case Entity.FACING_TOP:
        frame_group_name += "_top";
        break;
      case Entity.FACING_RIGHT:
        frame_group_name += "_right";
        break;
      default:
        System.out.println("Unidentified facing direction. Setting to bot.");
        this.setDirection(Entity.FACING_BOT);
        frame_group_name += "_bot";
        break;
    }
    // Prepares the frames to animate
    try {
      JsonValue frames = frames_meta.get(frame_group_name);
      float anim_interval = 0.04f;
      frames_anim = new TextureRegion[frames.size];
      for (int i = 0; i < frames.size; i++) {
        frames_anim[i] = getRegionAt(frames.getInt(i));
      }
      animation = new Animation<TextureRegion>(anim_interval, frames_anim);
      this.animate = true;
    }
    catch (NullPointerException e) {
      System.out.println("WARNING: Main attack animation error, resource not found.");
      e.printStackTrace();
      return;
    }
  }

  public void animateMovement() {
    String frame_group_name = "walk";
    // This gives us the name of the frame group to be loaded.
    switch (this.getDirection()) {
      case Entity.FACING_BOT:
        frame_group_name += "_bot";
        break;
      case Entity.FACING_LEFT:
        frame_group_name += "_left";
        break;
      case Entity.FACING_TOP:
        frame_group_name += "_top";
        break;
      case Entity.FACING_RIGHT:
        frame_group_name += "_right";
        break;
      default:
        System.out.println("Unidentified facing direction. Setting to bot.");
        this.setDirection(Entity.FACING_BOT);
        frame_group_name += "_bot";
        break;
    }
    // Prepares the frames to animate
    try {
      JsonValue frames = frames_meta.get(frame_group_name);
      float anim_interval = 0.04f;
      frames_anim = new TextureRegion[frames.size];
      for (int i = 0; i < frames.size; i++) {
        frames_anim[i] = getRegionAt(frames.getInt(i));
      }
      animation = new Animation<TextureRegion>(anim_interval, frames_anim);
      this.animate = true;
    }
    catch (NullPointerException e) {
      System.out.println("WARNING: Walk animation error, resource not found.");
      e.printStackTrace();
      return;
    }
  }

  public void animateIdle() {
    this.animate = false;
    String frame_name = "idle";
    switch (this.getDirection()) {
      case Entity.FACING_BOT:
        frame_name += "_bot";
        break;
      case Entity.FACING_LEFT:
        frame_name += "_left";
        break;
      case Entity.FACING_TOP:
        frame_name += "_top";
        break;
      case Entity.FACING_RIGHT:
        frame_name += "_right";
        break;
      default:
        System.out.println("Unidentified facing direction. Setting to bot.");
        this.setDirection(Entity.FACING_BOT);
        frame_name += "_bot";
        break;
    }
    this.setRegionIndex(this.frames_meta.get(frame_name).getInt(0));
    animation = null;
  }



  // ==================================== INPUT PROCESSOR ===================================== //

  // Entity Selection and Action
  @Override
  public boolean touchDown(int x, int y, int count, int button) {
    Vector3 touchedPoint = new Vector3(x, y, 0);

    // Detected tap was on this creature?
    if(collidedWorld(touchedPoint, this.cam)){
      // Debug info about tap on this creature
      System.out.println(getName() + " touched.");
      System.out.println("Button: " + Integer.toString(button));
      // Interpret user interaction by running platform
      switch (Gdx.app.getType()) {
        // -- Android Code -- //
        case Android:
          // There is already a selected entity?
          if(Entity.selected_entity != null) {
            System.out.println("Current selected entity is "
                + Entity.selected_entity.getName());
            // If the creature selected is the same taped, deselect it.
            if(Entity.selected_entity != this) {
              // The already selected entity is controllable by the player?
              if(Entity.selected_entity.isControllable()) {
                System.out.println(Entity.selected_entity.getName()
                    + " is controllable. Selecting default action.");
                /* In such case, the selected player controllable entity shall
                 * take its fitting default action if applicable. */
                Entity.selected_entity.defaultAction(this);
              }
              else {
                System.out.println("Current selected entity is not"
                    + " controllable, so no actions to be taken. Selecting "
                    + this.getName());
                /* If the already selected entity is not player controlled,
                 * then select this entity. */
                Entity.selected_entity = this;
              }
            }
            else {
              System.out.println("The creature touched is the"
                  + " same selected. Deselecting.");
              // Deselect this creature.
              Entity.selected_entity = null;
            }
          }
          else {
            System.out.println("There is no selected creature.\nSelecting "
                + this.getName());
            // If there is no selected creature, then select this one.
            Entity.selected_entity = this;
          }
          break;
        // -- Desktop Code -- //
        case Desktop:
          // Left button selects and deselects.
          if (button == 0) {
            if (Entity.selected_entity == this)
              Entity.selected_entity = null;
            else
              Entity.selected_entity = this;
          }
          // Right button performs actions with controllable creatures.
          else {
            try {
              if (Entity.selected_entity != this) {
                if (Entity.selected_entity.isControllable())
                  Entity.selected_entity.defaultAction(this);
              }
              else {
                System.out.println("Selected creature won't interact with itself.");
              }
            }
            catch (NullPointerException e) {
              System.out.println("Nothing selected, nothing to do.");
            }
          }
          break;
        // -- iOS Code -- //
        case iOS:
          break;
        // -- Web Browser Code -- //
        case WebGL:
          break;
        // -- Whatever -- //
        default:
          break;
      }
      return true;
    }
    else {
      // If the detected tap was not on this creature, there is nothing to do.
      return false;
    }
  }



  // ======================================= GETTERS/SETTERS ================================== //

  // Get Creature Info
  @Override
  public String getInfo() {
    return getDisplayName() + " (" + getCreatureType() + " - " + getCreatureRace() + ")";
  }

  // Selected by the Player
  public boolean isSelected(){
    return selected_entity.getId() == this.getId();
  }
  public void select() {
    selected_entity = this;
  }
  public void deselect() {
    selected_entity = null;
  }

  // Camera
  public void setCamera(Camera cam) {
    this.cam = cam;
  }
  public Camera getCam() {
    return this.cam;
  }

  // Creature Level
  public int getCreatureLevel() {
    return creature_level;
  }
  public void setCreatureLevel(int lvl) {
    creature_level = lvl;
  }

  // Creature Race
  public String getCreatureRace() {
    return creature_race;
  }
  public void setCreatureRace(String race){
    creature_race = race;
  }

  // Creature Type
  public String getCreatureType() {
    return creature_type;
  }
  public void setCreatureType(String type) {
    creature_type = type;
  }

  // Creature Class
  public String getCreatureClass() {
    return creature_class;
  }
  public void setCreatureClass(String clas) {
    creature_class = clas;
  }

  // Strength Attribute
  public void setStrength(float str) {
    strength = str;
  }
  public float getStrength() {
    return strength;
  }

  // Agility Attribute
  public float getAgility() {
    return agility;
  }
  public void setAgility(float agil) {
    agility = agil;
  }

  // Vitality Attribute
  public float getVitality() {
    return vitality;
  }
  public void setVitality(float vit) {
    vitality = vit;
  }

  // Intelligence Attribute
  public float getIntelligence() {
    return intelligence;
  }
  public void setIntelligence(float intel) {
    intelligence = intel;
  }

  // Willpower Attribute
  public float getWillpower() {
    return willpower;
  }
  public void setWillpower(float wil) {
    willpower = wil;
  }

  // Charisma Attribute
  public float getCharisma() {
    return charisma;
  }
  public void setCharisma(float cha) {
    charisma = cha;
  }

  // Health Status
  public float getMaxHealth() {
    return health_max;
  }
  public void setMaxHealth(float val) {
    health_max = val;
  }
  public float getHealth() {
    return health_current;
  }
  public void setHealth(float val) {
    health_current = val;
  }

  // Stamina Status
  public float getMaxStamina() {
    return stamina_max;
  }
  public void setMaxStamina(float val) {
    stamina_max = val;
  }
  public float getStamina() {
    return stamina_current;
  }
  public void setStamina(float val) {
    stamina_current = val;
  }

  // Mana Status
  public float getMaxMana() {
    return mana_max;
  }
  public void setMaxMana(float val) {
    mana_max = val;
  }
  public float getMana() {
    return mana_current;
  }
  public void setMana(float val) {
    mana_current = val;
  }

  // Water Status
  public float getMaxWater() {
    return water_max;
  }
  public void setMaxWater(float val) {
    water_max = val;
  }
  public float getWater() {
    return water_current;
  }
  public void setWater(float val) {
    water_current = val;
  }

  // Food Status
  public float getMaxFood() {
    return food_max;
  }
  public void setMaxFood(float val) {
    food_max = val;
  }
  public float getFood() {
    return food_current;
  }
  public void setFood(float val) {
    food_current = val;
  }

  // Rest Status
  public float getMaxRest() {
    return rest_max;
  }
  public void setMaxRest(float val) {
    rest_max = val;
  }
  public float getRest() {
    return rest_current;
  }
  public void setRest(float val) {
    rest_current = val;
  }

  // Action Points Status
  public int getMaxActionPoints() {
    return actions_max;
  }
  public void setMaxActionPoints(int val) {
    actions_max = val;
  }
  public int getActionPoints() {
    return actions_curent;
  }
  public void setActionPoints(int val) {
    actions_curent = val;
  }

  // Morale Status
  public float getMaxMorale() {
    return morale_max;
  }
  public void setMaxMorale(float val) {
    morale_max = val;
  }
  public float getMorale() {
    return morale_current;
  }
  public void setMorale(float val) {
    morale_current = val;
  }
  public float getStableMorale(){
    return morale_normal;
  }

  // Moves Air
  public boolean movesAir(){
    return move_air;
  }
  public void setMovesAit(boolean val) {
    move_air = val;
  }

  // Moves Land
  public boolean movesLand() {
    return move_land;
  }
  public void setMovesLand(boolean val) {
    move_land = val;
  }

  // Moves Water
  public boolean movesWater() {
    return move_water;
  }
  public void setMovesWater(boolean val) {
    move_water = val;
  }

  // Moves Through Walls
  public boolean movesWalls(){
    return move_walls;
  }
  public void setMovesWalls(boolean val) {
    move_walls = val;
  }

  // Tile Map Coordinates
  public int getTileX(){
    return ((int)this.getX())/32;
  }
  public int getTileY(){
    return ((int)this.getY())/32;
  }

  // Frames data
  public void setFramesMeta(JsonValue value) {
    frames_meta = value;
  }

  // Spritesheet Columns n Rows (important for CreatureBuilder class)
  public void setSheetColsRows(int cols, int rows) {
    setSheetCols(cols);
    setSheetRows(rows);
  }

  // Stance Toward the Player
  public int getStance() {
    return creature_stance;
  }
  public void setStance(int val) {
    switch(val){
      case STANCE_ALLY:
        creature_stance = STANCE_ALLY;
        break;
      case STANCE_FRIENDLY:
        creature_stance = STANCE_FRIENDLY;
        break;
      case STANCE_NEUTRAL:
        creature_stance = STANCE_NEUTRAL;
        break;
      case STANCE_HOSTILE:
        creature_stance = STANCE_HOSTILE;
        break;
      case STANCE_HUNTING:
        creature_stance = STANCE_HUNTING;
        break;
      default:
        System.out.println("Invalid stance option. Doing nothing.");
        break;
    }
  }

}

























