package com.sistemalivre.coregame;



// ========================= CombatComponent ========================= //

class CombatComponent {

  private static final String TAG = "CombatComponent";



// ========================= DATA ========================= //

  Entity owner;




// ========================= CONSTRUCTION ========================= //

  CombatComponent(Entity owner) {
    this.owner = owner;
  }

}
