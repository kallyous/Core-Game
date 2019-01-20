package com.sistemalivre.coregame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.Hashtable;



// ========================== AssetManager ========================== //

public class AssetManager {

  private static final String TAG = "AssetManager";

  public static final int GRAPHIC_ASSET = 0;
  public static final int AUDIO_ASSET = 1;



// ========================== DATA ========================== //

  // If we need any JSON parsing somewhere, we have this one here.
  public static JsonReader jsonreader = new JsonReader();

  // Holds all assets by their names
  private static Hashtable<String, Asset> assets;

  // Holds the creatures templates data
  private final JsonValue creatures;




// ========================== CONSTRUCTION ========================== //

  AssetManager() {

    assets = new Hashtable<>();

    // Sets up the basic defaults
    JsonValue default_graphics = jsonreader.parse(
        Gdx.files.internal("data/GraphicAssets.json") );

    Log.v(TAG, "Default Graphics Name: " + (String)default_graphics.name);

    for (JsonValue val : default_graphics) {
      try {
        Log.v(TAG, "Loading " + val.name);
        String data = val.getString("data");
        GraphicAsset asset = new GraphicAsset(val.name, data);
        assets.put(val.name, asset);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }

    // Creatures.json
    creatures = jsonreader.parse(
        Gdx.files.internal("data/Creatures.json") );

    // Plants.json

    // Structures.json

    // Pickups.json

    // Worlds.json

  }




// ========================== LOGIC ========================== //

  JsonValue creatures() {
    return creatures;
  }


  JsonValue creature(String name) {
    return creatures.get(name);
  }

  static Asset asset(String name) {
    return assets.get(name);
  }

  static Texture texture(String name) {
    /** Get a texture:
     1. Assume there is a texture with this name. This means there is a asset
        with the same name.
     2. Get the asset of this name and cast into a GraphicAsset
     3. Call texture() from the asset. This cause the texture to be returned
        if already loaded or loaded then returned if not yet loaded.
     **/
    return ((GraphicAsset)assets.get(name)).texture();
  }

  static JsonValue data(String name) {
    return assets.get(name).data();
  }

}





// ========================== Asset ========================== //

abstract class Asset {

  private static final String TAG = "Asset";



// ========================== DATA ========================== //

  protected int type;

  protected JsonValue data;

  protected String name;

  protected String path;


// ========================== CONSTRUCTION ========================== //

  Asset(String name, String path) {
    this.name = name;
    this.path = path;
  }

  abstract void loadAsset();

  abstract void dispose();


// ========================== LOGIC ========================== //

  protected void loadData() {
    data = AssetManager.jsonreader.parse(Gdx.files.internal(path));
  }

  // TODO: ensure no modification by passing a deep copy
  JsonValue data() {
    if (data == null)
      loadData();
    return data;
  }

  int type() {
    return type;
  }

  String name() {
    return name;
  }

  void destroy() {
    dispose();
    data = null;
  }

}




// ========================== GraphicAsset ========================== //

class GraphicAsset extends Asset {

  private static final String TAG = "GraphicAsset";



// ========================== DATA ========================== //

  private Texture texture;

  private TextureRegion[] regions;




// ========================== CONSTRUCTION ========================== //

  GraphicAsset(String name, String path) {
    super(name, path);
    type = AssetManager.GRAPHIC_ASSET;
  }


  @Override
  void loadAsset() {
    if (data == null) loadData();
    texture = new Texture(
        Gdx.files.internal( data.getString("path") ));
  }


  @Override
  void dispose() {
    texture = null;
    regions = null;
  }

// ========================== LOGIC ========================== //

  Texture texture() {
    if (texture == null)
      loadAsset();
    return texture;
  }

  TextureRegion[] textureRegions() {
    if (regions == null)
      setupTextureRegions();
    return regions;
  }

  private void setupTextureRegions() {
    texture(); // Ensures texture has been loaded

    int sheet_cols = data.getInt("cols");
    int sheet_rows = data.getInt("rows");

    // Splits regions based on the data loaded.
    TextureRegion[][] tempReg =
        TextureRegion.split(
            texture,
            texture.getWidth()/sheet_cols,
            texture.getHeight()/sheet_rows);

    // Creates the TextureRegion array with the calculated number or children.
    regions = new TextureRegion[sheet_cols*sheet_rows];

    // Assign a region to each array index.
    int index = 0;
    for(int i = 0; i < sheet_rows; i++){
      for(int j = 0; j < sheet_cols; j++){
        regions[index++] = tempReg[i][j];
      }
    }

  }

}




