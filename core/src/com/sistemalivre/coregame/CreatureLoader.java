package com.sistemalivre.coregame;



import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Iterator;






/** ========================= OldCreatureLoader ========================= **/

public class CreatureLoader {

  private static final String TAG = "CreatureLoader";




// ========================= LOGIC BEGIN ========================= //

  public static Array<Creature> LoadCreaturesFromTiledMap(TiledMap tiled_map) {

    // First we isolate our desired layer
    MapLayer creat_layer = tiled_map.getLayers().get("creatures");

    // Nothing to to if layer not present
    if (creat_layer == null) {
      Log.w(TAG + " - no creatures layer found");
      return null;
    }

    // Get the objects of the creatures layer
    MapObjects map_objects = creat_layer.getObjects();

    // If there is no objects, there is nothing to do
    if (map_objects.getCount() == 0) {
      Log.w(TAG + " - creatures layer is empty");
      return null;
    }

    Array<Creature> creatures = loadCreaturesFromMapObjects(map_objects);

    return creatures;

  }

  private static Array<Creature> loadCreaturesFromMapObjects(MapObjects objects) {

    // We need an array at least the size of found entries in the map objects
    Array<Creature> creeps = new Array<Creature>(objects.getCount());

    // Prepare an Iterator for cycling through all map objects
    Iterator<MapObject> itr_mobj = objects.iterator();

    // For each object we visit, we attempt to load it's data and create a creature with it
    while (itr_mobj.hasNext()) {

      // Get the next boy
      MapObject obj = itr_mobj.next();

      // Loads creature's name
      String creature_name = obj.getName();

      // Loads his properties pack
      MapProperties props = obj.getProperties();

      // Extracts the display name from the pack
      String display_name = props.get("display_name").toString();

      // Extracts if the thing is controlled by the user
      Boolean controllable = Boolean.parseBoolean( props.get("controllable").toString() );

      // We need the prototype's name for building the creature
      String prototype = props.get("prototype").toString();

      // We will pipe it down to the graphics_component
      String spritesheet_name = props.get("spritesheet").toString();

      // Load and parse the location in the map
      float x = Float.parseFloat( props.get("x").toString() );
      float y = Float.parseFloat( props.get("y").toString() );

      // Prepare a new creature with the given name
      Creature creature = new Creature(
          creature_name,
          spritesheet_name,
          13, 21, 131
      );

      // Set display name
      creature.setDisplayName(display_name);

      // Set controllable
      creature.setControllable(controllable);

      // Apply position
      creature.setPosition(x,y);

      // build final creature
      applyCreaturePrototype(creature, prototype);

      // Setup creature's sprite offset
      creature.graphic_comp.setSpriteOffsetX(creature.getWidth()/2);

      // Add it to the array to return
      creeps.add(creature);

    }

    return creeps;

  }

  private static void applyCreaturePrototype(Creature creature_obj, String prototype_name) {

    // Gets current prototype data
    JsonValue prototype = CoreGame.asset_manager.creature(prototype_name);

    JsonValue creature_json = protoypeFactory(prototype);

    Log.d(TAG + " - JsonValue of build creature:\n" + creature_json.toString());

    creature_obj.body_comp.setHealthPtsMax( creature_json.getFloat("health") );
    creature_obj.body_comp.setHealthPtsCurr( creature_json.getFloat("health") );

    creature_obj.setWidth((int)creature_json.getFloat("width"));
    creature_obj.setHeight((int)creature_json.getFloat("height"));

  }



private static JsonValue protoypeFactory(JsonValue arc_creature) {

    // TODO: Maybe the asset manager shit should be final
    JsonValue prototype = CoreGame.asset_manager.creature( arc_creature.getString("prototype") );

    // Pseudo deep copy
    JsonValue creature = new JsonValue("creature");
    for (JsonValue val : arc_creature) {
      // In case of unceonvertible types, print a stacktrace and ignore it
      try {
        creature.addChild( val.name, new JsonValue(val.asString()) );
      } catch (Exception e) {
        Log.w(TAG + " - " + e.getMessage() + " (" + val.name + ")");
      }
    }

    if (prototype != null) {

      prototype = protoypeFactory(prototype);

      // Combines current data with loaded prototype's data
      for (JsonValue val : prototype) { // Cycle thourgh all values in prototype

        // If value already exists in the creature, we apply a operation
        if (creature.get(val.name) != null) {

          // If value is numeric, sum
          if (val.isNumber()) {

            // Prepare new value
            Double new_val = (creature.getDouble(val.name) + val.asDouble());

            // Set creature's property new values
            creature.get(val.name).set( String.valueOf(new_val) );

          }

          // If is array, we shall add all it's elements into de creature, skipping duplicates

          // If dictionary, we shall add the new entries and it's values, skipping duplicates

          // If it is a string, then we ignore the prototype version (effectively an override)

        }

        // If the property doesn't exists in the creature yet, add it.
        else {
          // In case of unceonvertible types, print a stacktrace and ignore it
          try {
            creature.addChild(val.name, new JsonValue( val.asString() ) );
          } catch (Exception e) {
            Log.w(TAG + " - " + e.getMessage() + " (" + val.name + ")");
          }

        }

      }

    }

    // Retorna o resultado da operação
    return creature;
}


  // TODO: 03/01/19 Usar esta função para desemaranhar o processo de carregamento e cosntrução de criaturas
  private static Creature loadCreatureFromMapObject(MapObject mobj) {
    return null;
  }

}
