package com.kallyous.nopeisland;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.util.Hashtable;



public class AssetManager {

  private static final String TAG = "AssetManager";


  // If we need any JSON parsing somewere int the runninggame, we have one here
  public JsonReader jsonreader;

  // Maps all loaded texture by name
  public Hashtable<String, Texture> textures;

  // Holds the creatures templates data
  private JsonValue creatures;

  //static public final JsonValue creatureTypes = new JsonReader().parse(Gdx.files.internal("data/creature/creature-types.json"));

  AssetManager() {

    String debug_str;

    // Prepare the JSON Reader
    jsonreader = new JsonReader();

    // Sets up the basic defaults
    textures = new Hashtable<String, Texture>();
    JsonValue default_textures = jsonreader.parse( Gdx.files.internal("data/GraphicAssets.json") );
    for(JsonValue val : default_textures) {
      try {
        Log.i(TAG + " - Loading " + val.name + " at '" + val.asString() + "'");
        Texture texture = new Texture( Gdx.files.internal(val.asString()) );
        textures.put(val.name, texture);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }


    // Creatures.json
    creatures = jsonreader.parse( Gdx.files.internal("data/Creatures.json") );

    debug_str = creatures.prettyPrint(JsonWriter.OutputType.json, 50);
    Log.i(TAG + " - Creatures.json content is\n" + debug_str);

    // Plants.json

    // Structures.json

    // Pickups.json

    // Worlds.json

  }

  public JsonValue creatures() {
    return creatures;
  }

  public JsonValue creature(String name) {
    return creatures.get(name);
  }

}
