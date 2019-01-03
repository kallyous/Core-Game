package com.kallyous.nopeisland;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;




public class Drawable {

// =========================================== DATA ========================================= //

  // Texture
  private Texture texture;
  // Texture Sections
  private TextureRegion texture_region[];
  // Frames used in running animation, if any.
  public TextureRegion frames_anim[];
  // Animation itself holder
  public Animation<TextureRegion> animation;
  // Size of regions/frames and how much of them there is.
  private int sheet_cols, sheet_rows;
  // Region's to be rendered index at texture_region.
  private int region_index;
  // Sprite
  public Sprite sprite;
  // Entity Sprite Offset
  private int offset_horizontal = 0;
  private int offset_vertical = 0;
  // If currently playing animation
  public boolean animate = false;




// ======================================== CONSTRUCTION ==================================== //

  // Default Constructor
  Drawable() {
    texture = NopeIslandGame.asset_manager.textures.get("DefaultInterface");
    sheet_cols = 16;
    sheet_rows = 16;
    region_index = 0;
    setupTextureRegions();
  }

  Drawable(int region_index) {
    texture = NopeIslandGame.asset_manager.textures.get("DefaultInterface");
    sheet_cols = 16;
    sheet_rows = 16;
    this.region_index = region_index;
    setupTextureRegions();
  }

  // Custom Graphics Constructor with defaults dimensions
  Drawable(Texture texture, int region_index) {
    this.texture = texture;
    sheet_cols = 16;
    sheet_rows = 16;
    this.region_index = region_index;
    setupTextureRegions();
  }

  // Custom Everything Constructor
  Drawable(Texture texture, int sheet_cols, int sheet_rows, int region_index) {
    this.texture = texture;
    this.sheet_rows = sheet_rows;
    this.sheet_cols = sheet_cols;
    this.region_index = region_index;
    setupTextureRegions();
  }

  // Texture Regions Setup
  public void setupTextureRegions() {
    // Splits regions based on the data loaded.
    TextureRegion[][] tempReg = TextureRegion.split(texture,
        texture.getWidth()/sheet_cols, texture.getHeight()/sheet_rows);
    // Creates the TextureRegion array with the calculated number or children.
    texture_region = new TextureRegion[sheet_cols*sheet_rows];
    // Assign a region to each array index.
    int index = 0;
    for(int i = 0; i < sheet_rows; i++){
      for(int j = 0; j < sheet_cols; j++){
        texture_region[index++] = tempReg[i][j];
      }
    }
    // Creates the sprites and puts it facing to the informed initial position.
    sprite = new Sprite(texture_region[region_index]);
  }



  // ========================================== GRAPHIC ======================================= //

  public void draw(SpriteBatch batch) {
    sprite.draw(batch);
  }



  // ========================================== LOGIC ========================================= //

  // Collision Detection (flat from screen)
  public boolean collided(int x, int y) {
    if ( x > sprite.getX() && x < sprite.getX() + sprite.getWidth() &&
        y > sprite.getY() && y < sprite.getY() + sprite.getHeight() ) {
      return true;
    }
    else {
      return false;
    }
  }

  // Sets generic offsets for entity rendering
  public void setGenericOffsets() {
    offset_horizontal = sprite.getRegionWidth() / 2;
    offset_vertical = sprite.getRegionHeight() / 2;
  }



  // ==================================== GETTERS / SETTERS =================================== //

  // Position (full)
  public void setSpritePosition(float x, float y) {
    sprite.setPosition(x, y);
  }

  // Texture
  public void setTexture(Texture texture) {
    this.texture = texture;
  }

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
  public void setSheetCols(int cols) {
    sheet_cols = cols;
  }

  // Sheet Rows
  public int getSheetRows() {
    return sheet_rows;
  }
  public void setSheetRows(int rows) {
    sheet_rows = rows;
  }

  // Horizontal Offset
  public int getOffHoriz() {
    return offset_horizontal;
  }
  public void setOffHoriz(int val) {
    offset_horizontal = val;
  }

  // Vertical Offset
  public int getOffVert() {
    return offset_vertical;
  }
  public void setOffVert(int val) {
    offset_vertical = val;
  }

  // Texture Regions
  public TextureRegion getRegionAt(int index) {
    return texture_region[index];
  }
  public int getTextureRegionLength() {return texture_region.length; }

}
