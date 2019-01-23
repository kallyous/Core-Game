package com.sistemalivre.coregame;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Iterator;



public class StructureLoader {

  private static final String TAG = "StructureLoader";

  public static Array<Structure> loadStructuresFromTieldMap(TiledMap tiled_map) {

    // First we isolate our desired layer
    TiledMapTileLayer struct_layer = (TiledMapTileLayer)tiled_map.getLayers().get("structures");

    // Nothing to do if layer not present
    if (struct_layer == null) {
      Log.w(TAG, "no structures layer found");
      return null;
    }

    int width = struct_layer.getWidth();
    int height = struct_layer.getHeight();

    int size = 0;

    for (int i=0; i < width; i++)
      for (int j=0; j < height; j++)
        if (struct_layer.getCell(i, j) != null) size++;

    Log.v(TAG,"Structures array size: " + size);

    Array<Structure> structures = new Array<>(size);
    Structure str;

    for (int i=0; i < width; i++)
      for (int j=0; j < height; j++) {
        str = loadStructureFromTileCell(struct_layer.getCell(i,j));
        //str.setTileX(i);
        //str.setTileY(j);
        //structures.add(str);
      }


    return structures;
  }


  public static Structure loadStructureFromTileCell(TiledMapTileLayer.Cell cell) {

    try {
      TextureRegion region = cell.getTile().getTextureRegion();
      MapProperties props = cell.getTile().getProperties();
      Log.v(TAG, "Tile ID: " + cell.getTile().getId());

    }
    catch (NullPointerException e) {}

    return null;
  }

}
