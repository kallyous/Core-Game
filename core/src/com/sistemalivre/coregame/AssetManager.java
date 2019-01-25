package com.sistemalivre.coregame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;



// ========================== AssetManager ========================== //

/**
 * The AssetManager is a service locator pattern oriented class.
 * It holds all assets in the game, and is made available from
 * {@code Game.asset_manager} anywhere in the game code.
 * <p>
 * It is initialized as a static variable in the {@link Game} class during the
 * startup phase, inside of Game.create() .
 * <p>
 * There is a method call for each asset type in the form of
 * {@code Game.asset_manager.assetType(asset_name_as_string)} .
 * <p>
 * The AssetManager locates and casts the asset in the desired type, or
 * returns {@code null} if the type isn't compatible.
 * <p>
 * The asset types are listed as constants, and used to determine if the
 * requested asset can be cast into the desired type.
 * <p>
 * At startup, the AssetManager will look for a set of JSON data files inside
 * asset/data for initializing the game assets.
 * The game initialization will fail if those files are not found or if they
 * have syntax errors within.
 * <p>
 * Those files are:
 * <ul>
 * <li>data/GraphicAssets.json
 * <li>data/Creatures.json
 * <li>More to come...
 * </ul>
 *
 * @author Lucas Carvalho Flores
 */
public class AssetManager {

  private static final String TAG = "AssetManager";



// ========================== DATA ========================== //

  protected static final int GRAPHIC_ASSET = 0;
  protected static final int AUDIO_ASSET = 1;

  /**
   * Static JsonReader ready to be used anywhere in the game.
   * Merely for convenience.
   */
  public static JsonReader jsonreader = new JsonReader();

  /**
   * Holds all asset objects for the game.
   */
  private static ObjectMap<String, Asset> assets;

  /**
   * Holds all loaded creatures' data as a JsonValue object.
   */
  private final JsonValue creatures;




// ========================== CREATION ========================== //

  /**
   * Load default data files and sets them into de asset manager.
   */
  AssetManager() {

    assets = new ObjectMap<>();

    Log.i(TAG, "Loading default graphic assets data...");

    /* Load the default graphics for terrain, interface and for some base
    * creatures. */
    JsonValue default_graphics = jsonreader.parse(
        Gdx.files.internal("data/GraphicAssets.json") );

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

    Log.i(TAG, "Loading default creatures data...");

    /* Load the base creatures for the game. Those all use graphic assets
    * loaded from Default Graphics. */
    creatures = jsonreader.parse(
        Gdx.files.internal("data/Creatures.json") );

    Log.v(TAG, "\n"
        + creatures.prettyPrint(JsonWriter.OutputType.json, 50));

    // Pickups.json

    // Worlds.json

  }




// ========================== LOGIC ========================== //

  /**
   * Return a JsonValue containing data about all creatures available in the game.
   @return com.badlogic.gdx.utils.JsonValue
   */
  JsonValue creatures() {
    return creatures;
  }


  /**
   Return JsonValue containing data about a specific creature prototype.
   <p>
   This method does not loads data from parent prototype for the requested
   creature, but holds the name of the creature's prototype/parent.
   @param name   The name of the creature/prototype requested.
   @return {@code com.badlogic.gdx.utils.JsonValue} or {@code null} if such
   creature does not exist.
   */
  JsonValue creature(String name) {
    return creatures.get(name);
  }


  /**
   Return an {@link Asset} object with the given name.
   @param name   The name of the desired asset.
   @return {@link Asset} with given name or {@code null} if no asset with
   that name is found.
   */
  static Asset asset(String name) {
    return assets.get(name);
  }


  /**
   Return the texture held by the {@link Asset} with the given name.
   @param name   The name of the asset holding the texture.
   @return {@code com.badlogic.gdx.graphics.Texture}, or null if no
   {@link Asset} with that name is found.
   */
  static Texture texture(String name) {
    Texture texture = ((GraphicAsset)assets.get(name)).texture();
    if (texture == null)
      Log.e(TAG, "Texture " + name + " not found");
    return texture;
  }


  /**
   Return a JsonValue holding the data about an {@link Asset} with the given
   name.
   @param name   The name of the asset from which the data is desired.
   @return {@code com.badlogic.gdx.utils.JsonValue}, or null if no
   {@link Asset} with that name is found.
   */
  static JsonValue data(String name) {
    JsonValue data = assets.get(name).data();
    if (data == null)
      Log.e(TAG, "Data " + name + " not found.");
    return data;
  }

}





// ========================== Asset ========================== //

/**
 A container for any asset loaded into and used by the game.
 <p>
 The Asset class has methods for retrieving both data about the asset and the
 asset itself, just like holds both things.
 <p>
 As an abstract class, Asset is not made to be used directly.<br>
 Every asset type to be used in the game needs to have an appropriate subclass
 of Asset to hold it.
 @author Lucas Carvalho Flores
 */
abstract class Asset {

  private static final String TAG = "Asset";



// ========================== DATA ========================== //

  /** The type of the asset held by the Asset object. */
  protected int type;

  /**
   JsonValue holding data and metadata about the asset held by the Asset object.
   <p>
   Lazy, this will be loaded when the asset is first requested at runtime.
   */
  protected JsonValue data;

  /**
   Name of the asset, must be unique.
   <p>
   This is the name by which the asset will be identified every time the asset
   or anything related to it is requested through the {@link AssetManager} .
   */
  protected String name;

  /**
   Location in the filesystem of the data file to load.
   <p>
   Thou the data is actually loaded when the asset is first requested, the path
   to the JSON data file in the system must be provided at the creation of the
   asset object.
   */
  protected String path;




// ========================== CREATION ========================== //

  /**
   Create an Asset identified by {@code name}, and with data file located at {@code data_path} .
   <p>
   All lookups will be made against the Asset objects' names.<br>
   When something is requested, it will be resolved in the following order:
   <ol>
   <li>Locate Asset object identified by given name;
   <li>If the asset material inside it is available, return it.
   <li>If not, see if the data about the Asset object is loaded;
   <li>If the data is loaded, get the resource file path, load and return it.
   <li>If the data is not loaded, look at data_path and load the data about
       the resource.
   <li>Look inside the asset's data for the resource file path, load then
       return it.
   </ol>
   @param name        The unique name for the asset object.
   @param data_path   The filesystem path for the data file containing all the
                      information about the this asset's resource.
   */
  Asset(String name, String data_path) {
    this.name = name;
    this.path = data_path;
  }




// ========================== ABSTRACT ========================== //

  /**
   Each type of asset resource may need different routines for proper loading.
   */
  abstract void loadAsset();


  /**
   Each type of asset resource may need different disposal methods.
   */
  abstract void dispose();




// ========================== LOGIC ========================== //

  /**
   Load the asset's data and stores it into the Asset object.
   <p>
   Using the data_path provided during asset creation, looks for the data file
   into the filesystem and loads the data from it into the asset object.
   */
  protected void loadData() {
    data = AssetManager.jsonreader.parse(Gdx.files.internal(path));
  }


  /**
   Return the data about the asset. Loads the data if necessary.
   <p>
   TODO: ensure no modification by passing a deep copy
   @return {@code com.badlogic.gdx.utils.JsonValue}
   */
  JsonValue data() {
    if (data == null)
      loadData();
    return data;
  }


  /**
   Return the asset type.
   @return int
   */
  int type() {
    return type;
  }


  /**
   Return the asset's name.
   @return String
   */
  String name() {
    return name;
  }


  /**
   Run the dispose method then unloads the asset data.
   <p>
   This method doesn't makes the asset unavailable, but releases it's resources.
   The asset remain accessible from {@code Game.asset_manager} from anywhere.
   <p>
   Requesting the asset's resources or event it's data simply causes the asset
   object to load everything again into the memory then return the desired
   piece.
   */
  void destroy() {
    dispose();
    data = null;
  }

}





// ========================== GraphicAsset ========================== //

/**
 Container for graphical assets.
 <p>
 Holds data and graphic resources. Also sets the texture regions defined in the
 data file provided, alongside with means to access them.
 */
class GraphicAsset extends Asset {

  private static final String TAG = "GraphicAsset";



// ========================== DATA ========================== //

  /**
   The texture resource held by this container.
   */
  private Texture texture;

  /**
   Texture regions set by the data file for this container's Texture.
   */
  private TextureRegion[] regions;




// ========================== CONSTRUCTION ========================== //

  /**
   Construct a GraphicAsset object with provided data path and unique name.
   <p>
   @param name      Unique name for this asset. Must not collide with other
                    assets name, regardless of type.
   @param data_path Path to data file in the filesystem.
   */
  GraphicAsset(String name, String data_path) {
    super(name, data_path);
    type = AssetManager.GRAPHIC_ASSET;
  }




// ========================== LOGIC ========================== //

  /**
   Load the texture specified by the data file into the container.
   */
  @Override
  void loadAsset() {
    if (data == null) loadData();
    texture = new Texture(
        Gdx.files.internal( data.getString("path") ));
  }


  /**
   Clear references the texture regions and the texture itself.
   */
  @Override
  void dispose() {
    texture = null;
    regions = null;
  }


  /**
   Return the texture held by this container.
   @return Texture
   */
  Texture texture() {
    if (texture == null)
      loadAsset();
    return texture;
  }


  /**
   Return an array containing all texture regions of this container's Texture.
   @return TextureRegion[]
   */
  TextureRegion[] textureRegions() {
    if (regions == null)
      setupTextureRegions();
    return regions;
  }


  /**
   Set the texture regions for this container's Texture.
   <p>
   The information about the regions is provided by the data file of the asset.
   <p>
   This method simply reads this information and creates the defined regions.
   */
  private void setupTextureRegions() {
    texture(); // Ensures texture has been loaded

    int sheet_cols = data.getInt("cols");
    int sheet_rows = data.getInt("rows");

    // Split regions based on the data loaded.
    TextureRegion[][] tempReg =
        TextureRegion.split(
            texture,
            texture.getWidth()/sheet_cols,
            texture.getHeight()/sheet_rows);

    // Create the TextureRegion array with the calculated number or children.
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




