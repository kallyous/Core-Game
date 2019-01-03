package com.kallyous.nopeisland;



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
      System.out.println(TAG + ": no creatures layer found ");
      return null;
    }

    // Get the objects of the creatures layer
    MapObjects map_objects = creat_layer.getObjects();

    // If there is no objects, there is nothing to do
    if (map_objects.getCount() == 0) {
      System.out.println(TAG + ": creatures layer is empty ");
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
      Creature creature = new Creature(creature_name, spritesheet_name, 13, 21, 131);

      // Set display name
      creature.setDisplayName(display_name);

      // Set controllable
      creature.setControllable(controllable);

      // Apply position
      creature.setPosition(x,y);

      /* Setup Sprite Sheet: O AssetManager sabe as dimensões e localizações de todas as
       * spritesheets carregadas e as conhece pelos nomes. O componente gráfico apenas
       * solicita essas informações para o AssetManager, bem como uma referência para
       * uma instância ativa da textura/spritesheet desejada. O Asset manager por sua
       * vez retorna essas informações na forma de um JsonValue. */
      //creature.graphic_comp.setupStriteSheet(spritesheet_name);
      //creature.graphic_comp = new GraphicComponent(creature, spritesheet_name, 13, 21, 131);

      // build final creature
      applyCreaturePrototype(creature, prototype);

      // Add it to the array to return
      creeps.add(creature);

    }

    return creeps;

  }

  private static void applyCreaturePrototype(Creature creature_obj, String prototype_name) {

    // Gets current prototype data
    JsonValue prototype = NopeIslandGame.asset_manager.creature(prototype_name);

    JsonValue creature_json = protoypeFactory(prototype);

    System.out.println(TAG + ": JsonValue of build creature: ");
    System.out.println( creature_json.toString() );

    creature_obj.body_comp.setHealthPtsMax( creature_json.getFloat("health") );
    creature_obj.body_comp.setHealthPtsCurr( creature_json.getFloat("health") );

  }



private static JsonValue protoypeFactory(JsonValue arc_creature) {

    JsonValue creature = arc_creature;
    JsonValue prototype = NopeIslandGame.asset_manager.creature( creature.getString("prototype") );

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
            System.out.println(TAG + ": " + e.getMessage() + " (" + val.name + ")");
          }

        }

      }

    }

    // Retorna o resultado da operação
    return creature;
}


  private static Creature loadCreatureFromMapObject(MapObject mobj) {
    return null;
  }














// ========================= LOGIC END ========================= //

}



class OldCreatureLoader {

  private static final String TAG = "OldCreatureLoader";




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

    // If the creatures layer is present, we gather all objects in it
    MapObjects objs =  creat_layer.getObjects();

    // Of the layer has no objects, return
    if (objs.getCount() == 0) {
      System.out.println(TAG + ": Nenhuma criatura encontrada na camada creatures");
      return null;
    }
    else
      System.out.println(TAG + ": Criaturas presentes, carregando... ");

    // Now we are sure we got at least one creature to be loaded, lets prepare an array
    Array<Creature> array = new Array<Creature>();

    // Get a iterator to load all object's data one by one
    Iterator<MapObject> itr_mo = objs.iterator();

    // Load all creatures and insert them into the array
    while (itr_mo.hasNext())
      array.add( loadCreatureFromMapObject(itr_mo.next()) );

    // Debug stuff
    System.out.println(TAG + ": Criaturas carregadas do mapa: ");
    try {
      for (int i = 0; i < array.size; i++)
        System.out.println("\t" + array.get(i).getName() );
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Done
    return array;

  }


  // Here goes the core of the creature parsing and loading process
  private static Creature loadCreatureFromMapObject(MapObject mobj) {

    // Get the properties of the creature
    MapProperties prop = mobj.getProperties();

    try {
      JsonValue prototype = NopeIslandGame.asset_manager.creature( "fighter" );
      System.out.println(TAG + ":\n\t" + prototype);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Show all present properties
    System.out.println(TAG + ": Dumping data of " + mobj.getName() );
    Iterator<String> itr_str = prop.getKeys();
    while (itr_str.hasNext()) {
      String key = itr_str.next();
      Object val = prop.get( key );
      System.out.println("\t" + key + " : " + val.toString() );
    }

    // Creates new creature with the objects name as the creature name
    Creature creature = new Creature(mobj.getName());

    try {
      creature.setDisplayName( prop.get("display_name").toString() );
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      creature.setControllable( Boolean.parseBoolean(prop.get("controllable").toString()) );
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Sets the position given from the map object to the creature
    try {
      creature.setPosition(
          Float.parseFloat(prop.get("x").toString()),
          Float.parseFloat(prop.get("y").toString())
      );
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }

    // All creatures are selectable
    creature.command_comp.enableCommand("SelectCommand");

    // Ready to go
    return creature;

  }



}
