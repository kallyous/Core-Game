package com.kallyous.nopeisland;



import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;





/** ========================= GRAPHIC COMPONENT ========================= **/

public class GraphicComponent {

  private static final String TAG = "GraphicComponent";



// ========================= DATA SETUP BEGIN ========================= //

  // Sprite holding all crazy transformation stuff
  public Sprite sprite;

  // Entity owning this instance of GraphicComponent
  public Entity entity;

  // Texture to be split and used
  Texture texture;

  // Texture sections after splitting the texture
  private TextureRegion texture_region[];

  // Size of regions/frames and how much of them there is in the texture sheet
  private int sheet_cols, sheet_rows;

  // Index of the current texture region to be rendered at runtime
  private int region_index;

// ========================= DATA SETUP END ========================= //




// ========================= CONSTRUCTION BEGIN ========================= //

  // Default constructor assumes the GUI texture and (?) symbol
  GraphicComponent(Entity entity) {
    this.entity = entity;
    this.texture = NopeIslandGame.uiTexture;
    setupDefaultGraphics();
  }



  // This additional one just takes also a name and texture region index
  GraphicComponent(Entity entity, int region_index) {
    this.entity = entity;
    this.texture = NopeIslandGame.uiTexture;
    setupDefaultGraphics();
    setRegionIndex(region_index);
  }



  // Makes a graphic component from an arbitrary region of a given texture
  GraphicComponent(
      Entity entity, Texture texture,
      int region_x, int region_y, int width, int height) {

    this.entity = entity;

    this.texture = texture;

    texture_region = new TextureRegion[1];

    texture_region[0] = new TextureRegion(
        this.texture, region_x, region_y,
        width, height);

    region_index = 0;

    sprite = new Sprite(texture_region[region_index]);

    System.out.println(TAG + ": " + entity.getName() +
      " created with custom graphics and a single region.");

  // ------------------------- Entity Size Setup -------------------------- //

    this.entity.setWidth(width);

    this.entity.setHeight(height);

  // ---------------------------------------------------------------------- //

  }



  // Default graphics loads the '?' symbol from the GUI texture
  private void setupDefaultGraphics() {
    System.out.println(TAG + ": Entity " + entity.getName() + " created with generic graphics.");
    sheet_cols = 16;
    sheet_rows = 16;
    region_index = 0;
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

// ========================= CONSTRUCTION END ========================= //




// ========================= LOGIC BEGIN ========================= //

  public void draw(SpriteBatch batch) {
    sprite.draw(batch);
  }



  public void update(float dt) { sprite.setPosition(entity.getX(), entity.getY()); }

// ========================= LOGIC END ========================= //




// ========================= GETTERS / SETTERS BEGIN ========================= //

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

// ========================= GETTERS / SETTERS END ========================= //


}
