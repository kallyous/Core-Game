package com.sistemalivre.coregame;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.JsonValue;

// ========================= GRAPHIC COMPONENT ========================= //

public class GraphicComponent {

  private static final String TAG = "GraphicComponent";



// ========================= DATA ========================= //

  // Sprite holding all crazy transformation stuff
  Sprite sprite;

  // Entity owning this instance of GraphicComponent
  Entity owner;

  // Texture to be split and used
  private Texture texture;

  private JsonValue data;

  // Texture sections after splitting the texture
  private TextureRegion texture_regions[];

  // Size of regions/frames and how much of them there is in the texture sheet
  private int sheet_cols, sheet_rows;

  // Offsets for this component
  private float x_offset, y_offset;

  // Index of the frame to fallback when animation stops or ends.
  private int stand_index;

  private Animation<TextureRegion> animation;

  private float frame_interval = 0.04f;

  private boolean animate;



// ========================= CONSTRUCTION ========================= //

  // Default constructor assumes default GUI texture.
  GraphicComponent(Entity entity) {
    owner = entity;
    GraphicAsset asset =(GraphicAsset)AssetManager.asset("DefaultInterface");
    setupAssetGraphics(asset);
  }


  // Most common use, we specify the name of an asset for the component to use
  GraphicComponent(Entity entity, String asset_name) {
    owner = entity;
    GraphicAsset asset = (GraphicAsset)AssetManager.asset(asset_name);
    setupAssetGraphics(asset);
  }


  // Makes a graphic component from an arbitrary region of a given texture
  GraphicComponent(
      Entity entity, Texture texture,
      int region_x, int region_y, int width, int height) {

    owner = entity;

    this.texture = texture;

    texture_regions = new TextureRegion[1];

    texture_regions[0] = new TextureRegion(
        this.texture, region_x, region_y,
        width, height);

    stand_index = 0;

    sprite = new Sprite(texture_regions[stand_index]);

    Log.d(TAG, entity.getName() +
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
    sheet_cols = data.getInt("cols");
    sheet_rows = data.getInt("rows");

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

  public void draw(SpriteBatch batch) {
    sprite.draw(batch);
  }


  public void drawCollBox(ShapeRenderer shape_renderer) {

    shape_renderer.setColor(Color.GREEN);
    shape_renderer.rect(owner.getX(), owner.getY(), owner.getWidth(), owner.getHeight());

    shape_renderer.setColor(Color.LIGHT_GRAY);
    shape_renderer.rect(sprite.getX(), sprite.getY(),
        sprite.getWidth(), sprite.getHeight());

  }


  public void update(float dt) {

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


  public void setAnimation(String name) {

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
