package com.kallyous.nopeisland;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;



public class CreatureLoader {

  private static final String TAG = "CreatureLoader";

  public static Array<Creature> LoadCreaturesFromTiledMap(TiledMap tiled_map) {

    // First we isolate our desired layer
    MapLayer creat_layer = tiled_map.getLayers().get("creatures");

    // If the layer does not exist, return
    if (creat_layer == null) {
      System.out.println(TAG + ": Camada creatures não encontrada ");
      return null;
    }
    else
      System.out.println(TAG + ": Camada creatures encontrada ");

    MapObjects objs =  creat_layer.getObjects();

    if (objs.getCount() == 0) {
      System.out.println(TAG + ": Nenhuma criatura encontrada na camada creatures");
      return null;
    }
    else
      System.out.println(TAG + ": Criaturas presentes, carregando... ");

    Array<Creature> array = new Array<Creature>();

    Iterator<MapObject> itr_mo = objs.iterator();

    while (itr_mo.hasNext()) {
      MapObject obj = itr_mo.next();
      MapProperties prop = obj.getProperties();
      Creature creature = new Creature(obj.getName());
      creature.setPosition(
          Float.parseFloat(prop.get("x").toString()),
          Float.parseFloat(prop.get("y").toString())
      );
      creature.command_comp.enableCommand("SelectCommand");
      array.add(creature);
    }

    System.out.println(TAG + ": Criaturas carregadas do mapa: ");

    for (int i = 0; i < array.size; i++)
      System.out.println("\t" + array.get(i).getName() );

    return array;

  }

}
