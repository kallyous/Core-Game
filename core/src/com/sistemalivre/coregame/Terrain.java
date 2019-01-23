package com.sistemalivre.coregame;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;



public class Terrain {

  public static final String TAG = "Terrain";

  IntArray land;

  IntArray water;

  IntArray wall;

  Terrain() {
    JsonValue default_terrain_data = AssetManager.data("DefaultTerrain");
    Log.v(TAG, default_terrain_data.prettyPrint(JsonWriter.OutputType.json, 50));
    land = new IntArray(default_terrain_data.get("land").asIntArray());
  }

}
