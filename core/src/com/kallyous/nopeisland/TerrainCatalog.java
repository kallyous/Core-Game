package com.kallyous.nopeisland;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;



public class TerrainCatalog {

  static public final JsonValue overworldCatalog = new JsonReader().parse(Gdx.files.internal("data/overworld-terrain.json"));

  static public final int[] LAND = overworldCatalog.get("land").asIntArray();

  static public final int[] WATER = overworldCatalog.get("water").asIntArray();

  static public final int[] WALLS = overworldCatalog.get("walls").asIntArray();

}
