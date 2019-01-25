package com.sistemalivre.coregame;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.JsonValue;



// ========================== EntityComponenet ========================== //

/**
 Generic form of a component for entities.
 <p>
 Every component to be attached to an Entity object must subclass
 EntityComponent.<br>
 This enforces proper memory handling.
 @author Lucas Carvalho Flores
 */
abstract public class EntityComponent {

  private static final String TAG = "EntityComponent";



// ========================== DATA ========================== //

  /** The entity object that owns the component. */
  protected Entity owner;




// ========================== CONSTRUCTION ========================== //


  /**
   Create the component, binding it to their owner object.
   @param owner   The Entity object that owns the component.
   */
  EntityComponent(Entity owner) {
    this.owner = owner;
  }




// ========================== ABSTRACT ========================== //


  /**
   Dispose of resources for destruction of component.
   */
  abstract void dispose();


  /**
   Update the component.
   @param dt   Delta time
   */
  abstract void update(float dt);



// ========================== LOGIC ========================== //


  /**
   Return the entity object owner of the component.
   @return Entity
   */
  Entity owner() {
    return owner;
  }


  /**
   Call the subclass dispose() for releasing it's resources, then disconnect
   the component from it's owner, allowing the garbage collector to reclaim
   the memory.
   <p>
   A component's destroy() must be called only when destroying it's owner
   entity and by the owner's destroy()/dispose() methods.<br>
   This ensures a clean memory management.
   */
  void destroy() {
    dispose();
    owner = null;
  }

}





// ========================= BodyComponent ========================= //



/**
 Component holding physical body data.
 <p>
 This component is only for entities that have physical manifestation in the
 game world, that may be killed or destroyed, hit by attacks, etc.
 <p>
 If it needs hit points, it has a body.
 <p>
 For an entity with a body which health points have been reduced to 0 or
 lower, it has been destroyed and the BodyComponent will trigger a destroy
 command for it's owner.
 */
class BodyComponent extends EntityComponent {

  private static final String TAG = "BodyComponent";



// ========================= DATA ========================= //

  /**
   Maximum possible health.
   <p>
   When updating health points, it won't go higher than this value.
   */
  private float max_health;

  /**
   Current health.
   <p>
   Minimum value is 0, maximum value is {@code max_health}<br>
   The entity will be destroyed if dropped to 0.
   */
  private float curr_health;

  /**
   Maximum actions the entity can take per turn.
   <p>
   When updating action points, it won't go higher than this value.
   */
  private int max_action;

  /**
   Amount of actions left for the current turn.
   <p>
   Minimum value is 0, maximum value is {@code max_action}
   */
  private int curr_action;

  /**
   True if the owner entity has acted at least once during the current turn.
   */
  private boolean acted_this_turn = false;

  /**
   True if the owner entity has acted at least once during the previous turn.
   */
  private boolean acted_last_turn = false;




// ========================= CONSTRUCTION ========================= //


  /**
   Create a basic body, with the informed hit and action points.
   @param owner   Entity object owning the body.
   */
  BodyComponent(Entity owner, float health, int actions) {
    super(owner);

    max_health = health;
    curr_health = max_health;

    max_action = actions;
    curr_action = max_action;

  }





// ========================== LOGIC ========================== //


  /**
   Issue a message if hit points reaches 0.
   <p>
   TODO: implement destruction of entity.
   @param dt   Delta time
   */
  @Override
  void update(float dt) {
    if (currHealth() <= 0)
      Log.v(TAG, owner.getName() + " died.");
  }


  @Override
  void dispose() {}




// ========================= GET / SET ========================= //


  /**
   Return maximum health
   @return Float
   */
  float maxHealth() {
    return max_health;
  }


  /**
   Set new maximum health.
   @param new_max_health   New value for maximum health
   */
  void setMaxHealth(float new_max_health) {
    this.max_health = new_max_health;
  }


  /**
   Return current health points.
   @return Float
   */
  float currHealth() {
    return curr_health;
  }


  /**
   Set the current health points.
   <p>
   Will enforce a minimum of 0 and a maximum of max_health.
   @param new_health   New value for health points.
   */
  void setCurrHealth(float new_health) {
    if(new_health  < 0)
      curr_health = 0;
    else if (new_health > max_health)
      curr_health = max_health;
    else
      curr_health = new_health;
  }


  /**
   Return maximum actions per turn.
   @return Int
   */
  int maxActionPts() {
    return max_action;
  }


  /**
   Set new value for maximum actions per turn.
   @param new_max_action   New actions limit
   */
  void setMaxActionPts(int new_max_action) {
    max_action = new_max_action;
  }


  /**
   Return current amount of actions available
   @return   Int
   */
  int currActionPts() {
    return curr_action;
  }


  /**
   Sets new value for remaining amount of actions
   @param new_curr_action   New value for remaining amount of actions
   */
  void setCurrActionPts(int new_curr_action) {
    curr_action = new_curr_action;
  }


  /**
   Returns if the owner entity has acted at least once this turn.
   @return boolean
   */
  boolean hasActedThisTurn() {
    return acted_this_turn;
  }


  /**
   Informs the body component if the entity has acted this turn.
   @param acted_this_turn
   */
  void setActedThisTurn(boolean acted_this_turn) {
    this.acted_this_turn = acted_this_turn;
  }


  /**
   Returns if the owner entity has acted previous turn.
   @return boolean
   */
  boolean hasActedLastTurn() {
    return acted_last_turn;
  }

  void setActedLastTurn(boolean acted_last_turn) {
    this.acted_last_turn = acted_last_turn;
  }

}





// ========================= GraphicComponent ========================= //

class GraphicComponent extends  EntityComponent {

  private static final String TAG = "GraphicComponent";



// ========================= DATA ========================= //

  // Sprite holding all crazy transformation stuff
  Sprite sprite;

  // Texture to be split and used
  private Texture texture;

  private JsonValue data;

  // Texture sections after splitting the texture
  private TextureRegion texture_regions[];

  // Offsets for this component
  private float x_offset, y_offset;

  // Index of the frame to fallback when animation stops or ends.
  private int stand_index;

  private Animation<TextureRegion> animation;

  private float frame_interval = 0.04f;

  private boolean animate;



// ========================= CONSTRUCTION ========================= //

  // Default constructor assumes default GUI texture.
  GraphicComponent(Entity owner) {
    super(owner);
    GraphicAsset asset =(GraphicAsset)AssetManager.asset("DefaultInterface");
    setupAssetGraphics(asset);
  }


  // Most common use, we specify the name of an asset for the component to use
  GraphicComponent(Entity owner, String asset_name) {
    super(owner);
    GraphicAsset asset = (GraphicAsset)AssetManager.asset(asset_name);
    setupAssetGraphics(asset);
  }


  // Makes a graphic component from an arbitrary region of a given texture
  GraphicComponent(
      Entity owner, Texture texture,
      int region_x, int region_y, int width, int height) {

    super(owner);

    this.texture = texture;

    texture_regions = new TextureRegion[1];

    texture_regions[0] = new TextureRegion(
        this.texture, region_x, region_y,
        width, height);

    stand_index = 0;

    sprite = new Sprite(texture_regions[stand_index]);

    Log.d(TAG, owner.getName() +
        " created with custom graphics and a single region.");

    // ------------------------- Entity Size Setup -------------------------- //

    owner.setWidth(width);

    owner.setHeight(height);

    // ---------------------------------------------------------------------- //

  }


  private void setupAssetGraphics(GraphicAsset asset) {
    data = asset.data();
    texture = asset.texture();
    texture_regions = asset.textureRegions();
    stand_index = data.getInt("default");

    try { setSpriteOffsetX(data.getInt("sprite_offset_x")); }
    catch (Exception e) {
      Log.v(TAG, e.getMessage());
      setSpriteOffsetX(0); }

    try { setSpriteOffsetY(data.getInt("sprite_offset_y")); }
    catch (Exception e) {
      Log.v(TAG, e.getMessage());
      setSpriteOffsetY(0); }

    sprite = new Sprite(texture_regions[stand_index]);
  }




// ========================= LOGIC BEGIN ========================= //

  @Override
  void update(float dt) {

    // Updates sprite location to it's respective owner
    sprite.setPosition(
        owner.getX() - x_offset,
        owner.getY() - y_offset);

    // If animating, set the current frame to be the active
    if (animate)
      sprite.setRegion(animation.getKeyFrame(dt, true));
    /** If not animating, set the frame specified for standing.
     This is set by the FMS or something similar that handles transitions
     between behavior. **/
    else
      sprite.setRegion(texture_regions[stand_index]);
  }


  @Override
  void dispose() {
    sprite = null;
    data = null;
    texture_regions = null;
    animation = null;
    texture = null;
  }


  void draw(SpriteBatch batch) {
    sprite.draw(batch);
  }


  void drawCollBox(ShapeRenderer shape_renderer) {

    shape_renderer.setColor(Color.GREEN);
    shape_renderer.rect(owner.getX(), owner.getY(), owner.getWidth(), owner.getHeight());

    shape_renderer.setColor(Color.LIGHT_GRAY);
    shape_renderer.rect(sprite.getX(), sprite.getY(),
        sprite.getWidth(), sprite.getHeight());

  }


  void setAnimation(String name) {

    Log.v(TAG, "Animation setup");

    int[] animation_indexes =
        data.get("animation").get(name).asIntArray();

    TextureRegion[] keyframes =
        new TextureRegion[animation_indexes.length];

    int frame_index;
    for (int i=0; i < animation_indexes.length; i++) {

      frame_index = animation_indexes[i];

      keyframes[i] = texture_regions[frame_index];

    }

    animation = new Animation<>(frame_interval, keyframes);

  }


  void playAnim() {
    Log.v(TAG, "Playing animation");
    animate = true;
  }


  void stopAnim() {
    Log.v(TAG, "Stopping animation");
    animate = false;
  }





// ========================= GET / SET ========================= //

  Texture texture() { return texture; }

  JsonValue data() {
    return data;
  }


  // Region Index (Frame)
  int getStandIndex() {
    return stand_index;
  }
  void setStandIndex(int i) {
    stand_index = i;
    sprite.setRegion(texture_regions[i]);
  }


  // X Offset
  float getSpriteOffsetX() { return x_offset; }
  void setSpriteOffsetX(float x) { x_offset = x; }


  float getSpriteOffsetY() { return y_offset; }
  void setSpriteOffsetY(float y) { y_offset = y; }

}
