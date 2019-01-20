package com.sistemalivre.coregame;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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
  Texture texture;

  JsonValue data;

  // Texture sections after splitting the texture
  private TextureRegion texture_region[];

  // Size of regions/frames and how much of them there is in the texture sheet
  private int sheet_cols, sheet_rows;

  // Offsets for this component
  private float x_offset, y_offset;

  // Index of the current texture region to be rendered at runtime
  private int region_index;




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

    texture_region = new TextureRegion[1];

    texture_region[0] = new TextureRegion(
        this.texture, region_x, region_y,
        width, height);

    region_index = 0;

    sprite = new Sprite(texture_region[region_index]);

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
    texture_region = asset.textureRegions();
    region_index = data.getInt("default");
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

    sprite = new Sprite(texture_region[region_index]);
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
    sprite.setPosition(
        owner.getX() - x_offset,
        owner.getY() - y_offset);
  }




// ========================= GET / SET ========================= //

  // Texture
  public Texture getTexture() { return texture; }


  // Region Index (Frame)
  public int getRegionIndex() {
    return region_index;
  }
  public void setRegionIndex(int i) {
    region_index = i;
    sprite.setRegion(texture_region[i]);
  }


  // Sheet Columns
  public int getSheetCols() {
    return sheet_cols;
  }


  // Sheet Rows
  public int getSheetRows() {
    return sheet_rows;
  }


  // X Offset
  public float getSpriteOffsetX() { return x_offset; }
  public void setSpriteOffsetX(float x) { x_offset = x; }


  public float getSpriteOffsetY() { return y_offset; }
  public void setSpriteOffsetY(float y) { y_offset = y; }

}
