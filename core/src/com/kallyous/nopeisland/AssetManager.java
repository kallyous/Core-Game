package com.kallyous.nopeisland;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;



public class AssetManager {

  private static final String TAG = "AssetManager";

  // Holds the creatures templates data
  private JsonValue creatures;

  // If we need any JSON parsing somewere int the runninggame, we have one here
  public JsonReader jreader;

  //static public final JsonValue creatureTypes = new JsonReader().parse(Gdx.files.internal("data/creature/creature-types.json"));

  AssetManager() {

    String debug_str;

    // Prepare the JSON Reader
    jreader = new JsonReader();

    // Creatures.json
    creatures = jreader.parse( Gdx.files.internal("data/Creatures.json") );

    debug_str = creatures.prettyPrint(JsonWriter.OutputType.json, 50);
    System.out.println(TAG + ": Creatures.json content is\n" + debug_str);

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
