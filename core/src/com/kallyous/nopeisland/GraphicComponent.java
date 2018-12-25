package com.kallyous.nopeisland;



import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;



/** ========================= GRAPHIC COMPONENT ========================= **/

public class GraphicComponent {

  private static final String TAG = "GraphicComponent";



// ========================= DATA BEGIN ========================= //

  // Entity owning this instance of GraphicComponent
  Entity entity;

  // Texture to be split and used
  Texture texture;

  // Texture sections after splitting the texture
  private TextureRegion texture_region[];

  // Size of regions/frames and how much of them there is in the texture sheet
  private int sheet_cols, sheet_rows;

  // Index of the current texture region to be rendered at runtime
  private int region_index;

  // Sprite holding all crazy transformation stuff
  private Sprite sprite;

// ========================= DATA END ========================= //




// ========================= CONSTRUCTION BEGIN ========================= //

  // Default constructor assumes the GUI texture
  GraphicComponent(Entity entity) {
    this.entity = entity;
    this.texture = NopeIslandGame.uiTexture;
    setupDefaultGraphics();
  }

  // This additional constructor just takes also a name and index for the texture region
  GraphicComponent(Entity entity, int region_index) {
    this.entity = entity;
    this.texture = NopeIslandGame.uiTexture;
    setupDefaultGraphics();
    setRegionIndex(region_index);
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




// ========================= RENDER / UPDATE BEGIN ========================= //

  public void draw(SpriteBatch batch) {
    sprite.draw(batch);
  }

  public void update(float dt) { sprite.setPosition(entity.getX(), entity.getY()); }

// ========================= RENDER / UPDATE END ========================= //




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
