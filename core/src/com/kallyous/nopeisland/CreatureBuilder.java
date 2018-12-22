package com.kallyous.nopeisland;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.JsonValue;



public class CreatureBuilder {

  // ========================================== DATA ========================================== //

  // Attribute holders
  private static float str, agil, vit, intel, will, cha;
  // Status holders
  private static float health, stamina, mana, water, food, rest, morale;
  // Action Points holder
  private static int actions;
  // Level holder
  private static float creat_lvl;
  // Offsets and collision box
  private static int offset_horizontal, offset_vertical, collbox_width, collbox_height;



  // ======================================= CONSTRUCTION ===================================== //

  // Searches internal catalog for a creature with the given name and build it at the given lvl.
  public static Creature buildFromCatalog(String name, int lvl) throws NoSuchFieldException {
    // JSON to load
    JsonValue creat_main, creat_class, creat_race, creat_type, creat_body;

    // Loads JSON data starting by the Catalog entry
    for ( JsonValue creat : Bestiary.creatureCatalog.get("creatures") ) {
      System.out.println("Read " + creat.getString("name"));
      if ( creat.getString("name").equals(name) ) {
        System.out.println("Creature found on the catalog.");
        creat_main = creat;
        // Race JSON
        for ( JsonValue race : Bestiary.creatureRaces.get("races") ) {
          if ( race.getString("name").equals(creat_main.getString("race")) ) {
            System.out.println("Race found.");
            creat_race = race;
            // Type JSON
            for ( JsonValue type : Bestiary.creatureTypes.get("types") ) {
              if ( type.getString("name").equals(creat_race.getString("type")) ) {
                System.out.println("Creature Type found.");
                creat_type = type;
                // Body JSON
                for ( JsonValue body : Bestiary.bodyTypes.get("bodies") ) {
                  if ( creat_type.getString("body_type")
                      .equals(body.getString("name")) ) {
                    System.out.println("Body Type found.");
                    creat_body = body;
                    // Class JSON
                    for ( JsonValue class_val : Bestiary.creatureClasses
                        .get("classes") ) {
                      if ( creat_main.getString("class")
                          .equals(class_val.getString("name")) ) {
                        System.out.println("Class found.");
                        creat_class = class_val;
                        // Build this creature using the loaded data
                        System.out.println("All data successfully loaded."
                            + " Calling CreatureBuilder");
                        return generate(name, creat_main, creat_race,
                            creat_type, creat_body, creat_class, lvl);
                      }
                    }
                    throw new NoSuchFieldException("Class not found: "
                        + creat_main.getString("class"));
                  }
                }
                throw new NoSuchFieldException("Body Type not found: "
                    + creat_type.getString("body_type"));
              }
            }
            throw new NoSuchFieldException("Creature Type not found: "
                + creat_race.getString("type"));
          }
        }
        throw new NoSuchFieldException("Race not found: " + creat_main.getString("race"));
      }
    }
    throw new NoSuchFieldException("Creature not found: " + name);
  }

  // Generates creatures from loaded resources.
  public static Creature generate(String name, JsonValue main, JsonValue race,
                                  JsonValue type, JsonValue body, JsonValue clas, int lvl) {

    // Creates creature to work with
    Creature creature = new Creature( name,
        new Texture(Gdx.files.internal(main.getString("spritesheet"))),
        main.getInt("sheet_cols"), main.getInt("sheet_rows"),
        main.get("frames").get("idle_bot").getInt(0) );

    // Basic Info
    creature.setDisplayName(main.getString("display"));
    creature.setCreatureRace(race.getString("name"));
    creature.setCreatureType(type.getString("name"));
    creature.setCreatureClass(clas.getString("name"));
    creature.setCreatureLevel(lvl);

    // Cast lvl to float for application in calculations.
    creat_lvl = (float)lvl;

    // Calculate Starting attributes
    startingAttributes(main, race, type, body);
    // Progress Attributes
    progressAttributes(main, race, type, body, clas);

    // Calculate base status for the creature at current level
    calcStatus(main, race, type, body, clas);

    // That is it for now. Set shits up.
    creature.setStrength(str);
    creature.setAgility(agil);
    creature.setVitality(vit);
    creature.setIntelligence(intel);
    creature.setWillpower(will);
    creature.setCharisma(cha);
    creature.setMaxHealth(health);
    creature.setHealth(health);
    creature.setMaxStamina(stamina);
    creature.setStamina(stamina);
    creature.setMaxMana(mana);
    creature.setMana(mana);
    creature.setMaxWater(water);
    creature.setWater(water);
    creature.setMaxFood(food);
    creature.setFood(food);
    creature.setMaxRest(rest);
    creature.setRest(rest);
    creature.setMaxMorale(morale);
    creature.setMorale(morale);
    creature.setMaxActionPoints(actions);
    creature.setActionPoints(actions);

    // Set entity type.
    creature.setEntityType(Creature.CREATURE_ENTITY);

    // Load Sprite Settings
    loadSpriteSettings(creature, main);

    // Done
    return creature;
  }



  // ===================================== LOGIC / CALCULATION ================================ //

  // Loads the starting attributes at level 1 for this creature.
  private static void startingAttributes(JsonValue main, JsonValue race,
                                         JsonValue type, JsonValue body) {
    str = body.getFloat("strength")
        + type.getFloat("str_mod")
        + race.getFloat("str_mod")
        + main.getFloat("str_mod");
    agil = body.getFloat("agility")
        + type.getFloat("agil_mod")
        + race.getFloat("agil_mod")
        + main.getFloat("agil_mod");
    vit = body.getFloat("vitality")
        + type.getFloat("vit_mod")
        + race.getFloat("vit_mod")
        + main.getFloat("vit_mod");
    intel = body.getFloat("intelligence")
        + type.getFloat("int_mod")
        + race.getFloat("int_mod")
        + main.getFloat("int_mod");
    will = body.getFloat("willpower")
        + type.getFloat("will_mod")
        + race.getFloat("will_mod")
        + main.getFloat("will_mod");
    cha = body.getFloat("charisma")
        + type.getFloat("char_mod")
        + race.getFloat("char_mod")
        + main.getFloat("char_mod");
  }

  // Calculates and applies the attributes progression up to the given level.
  private static void progressAttributes(JsonValue main, JsonValue race,
                                         JsonValue type, JsonValue body, JsonValue clas) {
    // TODO
  }

  // Calculates the final base status for this creature.
  private static void calcStatus(JsonValue main, JsonValue race, JsonValue type,
                                 JsonValue body, JsonValue clas) {

    // Main health portion
    health = body.getFloat("base_health")
        + type.getFloat("health_flat_mod")
        + (creat_lvl*type.getFloat("health_lvl_mod"))
        + race.getFloat("health_flat_mod")
        + (creat_lvl*race.getFloat("health_lvl_mod"))
        + main.getFloat("health_flat_mod")
        + (creat_lvl*main.getFloat("health_lvl_mod"));
    // Vitality bonus
    health += vit * 5.0;
    // Percentage bonus
    health += health * ( type.getFloat("health_perc_mod")
        + race.getFloat("health_perc_mod")
        + main.getFloat("health_perc_mod") );

    // Main stamina portion
    stamina = body.getFloat("base_stamina")
        + type.getFloat("stamina_flat_mod")
        + (creat_lvl*type.getFloat("stamina_lvl_mod"))
        + race.getFloat("stamina_flat_mod")
        + (creat_lvl*race.getFloat("stamina_lvl_mod"))
        + main.getFloat("stamina_flat_mod")
        + (creat_lvl*main.getFloat("stamina_lvl_mod"));
    // Vitality, Agility and str bonus
    stamina += vit * 3;
    stamina += agil * 2;
    stamina += str * 1;
    // Percentage bonus
    stamina += stamina * ( type.getFloat("stamina_perc_mod")
        + race.getFloat("stamina_perc_mod")
        + main.getFloat("stamina_perc_mod") );

    // Main mana portion
    mana = body.getFloat("base_mana")
        + type.getFloat("mana_flat_mod")
        + (creat_lvl*type.getFloat("mana_lvl_mod"))
        + race.getFloat("mana_flat_mod")
        + (creat_lvl*race.getFloat("mana_lvl_mod"))
        + main.getFloat("mana_flat_mod")
        + (creat_lvl*main.getFloat("mana_lvl_mod"));
    // Willpower and Intelligence bonus
    mana += will * 2;
    mana += intel * 1;
    // Percentage bonus
    mana += mana * ( type.getFloat("mana_perc_mod")
        + race.getFloat("mana_perc_mod")
        + main.getFloat("mana_perc_mod") );

    // Main water portion
    water = body.getFloat("base_water")
        + type.getFloat("water_flat_mod")
        + (creat_lvl*type.getFloat("water_lvl_mod"))
        + race.getFloat("water_flat_mod")
        + (creat_lvl*race.getFloat("water_lvl_mod"))
        + main.getFloat("water_flat_mod")
        + (creat_lvl*main.getFloat("water_lvl_mod"));
    // Vitality bonus
    water += vit;
    // Percentage bonus
    water += water * ( type.getFloat("water_perc_mod")
        + race.getFloat("water_perc_mod")
        + main.getFloat("water_perc_mod") );

    // Main food portion
    food = body.getFloat("base_food")
        + type.getFloat("food_flat_mod")
        + (creat_lvl*type.getFloat("food_lvl_mod"))
        + race.getFloat("food_flat_mod")
        + (creat_lvl*race.getFloat("food_lvl_mod"))
        + main.getFloat("food_flat_mod")
        + (creat_lvl*main.getFloat("food_lvl_mod"));
    // Vitality bonus
    food += vit;
    // Percentage bonus
    food += food * ( type.getFloat("health_perc_mod")
        + race.getFloat("food_perc_mod")
        + main.getFloat("food_perc_mod") );

    // Main rest portion
    rest = body.getFloat("base_rest")
        + type.getFloat("rest_flat_mod")
        + (creat_lvl*type.getFloat("rest_lvl_mod"))
        + race.getFloat("rest_flat_mod")
        + (creat_lvl*race.getFloat("rest_lvl_mod"))
        + main.getFloat("rest_flat_mod")
        + (creat_lvl*main.getFloat("rest_lvl_mod"));
    // Vitality bonus
    rest += vit;
    // Percentage bonus
    rest += rest * ( type.getFloat("rest_perc_mod")
        + race.getFloat("rest_perc_mod")
        + main.getFloat("rest_perc_mod") );

    // Main morale portion
    morale = body.getFloat("base_morale")
        + type.getFloat("morale_flat_mod")
        + race.getFloat("morale_flat_mod")
        + main.getFloat("morale_flat_mod");
    // Percentage bonus
    morale += morale * ( type.getFloat("morale_perc_mod")
        + race.getFloat("morale_perc_mod")
        + main.getFloat("morale_perc_mod") );

    // Action Points
    actions = body.getInt("base_ap")
        + type.getInt("ap_mod")
        + race.getInt("ap_mod")
        + main.getInt("ap_mod");
  }

  // Evaluates sprite rendering offsets and collision box.
  private static void loadSpriteSettings(Creature creature, JsonValue main) {
    /** Offsets
     *  True: use generic offset (sprite_width / 2) or (sprite_height / 2)
     *  False: sets to 0
     *  Number: use this value */

    // Horizontal Offset
    if (main.getBoolean("custom_horiz_offset"))
      creature.setOffHoriz(main.getInt("offset_horizontal"));
    else
      creature.setOffHoriz(creature.getSpriteWidth() / 2);
    // Vertical Offset
    if (main.getBoolean("custom_vert_offset"))
      creature.setOffVert(main.getInt("offset_vertical"));
    else
      creature.setOffVert(creature.getSpriteHeight() / 2);

    creature.setFramesMeta(main.get("frames"));
  }
}
