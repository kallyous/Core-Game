package com.kallyous.nopeisland;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;



public class Bestiary {

  static public final JsonValue creatureTypes = new JsonReader().parse(Gdx.files.internal("data/creature/creature-types.json"));

  static public final JsonValue creatureClasses = new JsonReader().parse(Gdx.files.internal("data/creature/classes.json"));

  static public final JsonValue creatureRaces = new JsonReader().parse(Gdx.files.internal("data/creature/races.json"));

  static public final JsonValue bodyTypes = new JsonReader().parse(Gdx.files.internal("data/creature/body-types.json"));

  static public final JsonValue bodyFeatures = new JsonReader().parse(Gdx.files.internal("data/creature/body-features.json"));

  static public final JsonValue creatureTraits = new JsonReader().parse(Gdx.files.internal("data/creature/traits.json"));

  static public final JsonValue creatureCatalog = new JsonReader().parse(Gdx.files.internal("data/creature/creature-catalog.json"));

  public Bestiary(){}

}
