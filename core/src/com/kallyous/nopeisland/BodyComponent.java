package com.kallyous.nopeisland;




/** ========================= BodyComponent ========================= **/

class BodyComponent {

  private static final String TAG = "BodyComponent";




// ========================= DATA BEGIN ========================= //

  public Entity owner;
  
  private float health_pts_max, health_pts_curr;
  private int action_pts_max, action_pts_curr;

  private boolean acted_this_turn = false;
  private boolean acted_last_turn = false;

// ========================= DATA END ========================= //




// ========================= CREATION BEGIN ========================= //

  BodyComponent(Entity owner) {
    this.owner = owner;

    health_pts_max = 10;
    health_pts_curr = health_pts_max;

    action_pts_max = 2;
    action_pts_curr = action_pts_max;

  }
  
  // ========================= CREATION END ========================= //




// ========================= LOGIC BEGIN ========================= //



// ========================= LOGIC END ========================= //
  
  
  
  
// ========================= GETTERS / SETTERS BEGIN ========================= //

  public float getHealthPtsMax() {
    return health_pts_max;
  }


  public void setHealthPtsMax(float health_pts_max) {
    this.health_pts_max = health_pts_max;
  }


  public float getHealthPtsCurr() {
    return health_pts_curr;
  }


  public void setHealthPtsCurr(float health_pts_curr) {
    this.health_pts_curr = health_pts_curr;
  }


  public int getActionPtsMax() {
    return action_pts_max;
  }


  public void setActionPtsMax(int action_pts_max) {
    this.action_pts_max = action_pts_max;
  }


  public int getActionPtsCurr() {
    return action_pts_curr;
  }


  public void setActionPtsCurr(int action_pts_curr) {
    this.action_pts_curr = action_pts_curr;
  }


  public boolean isActedThisTurn() {
    return acted_this_turn;
  }


  public void setActedThisTurn(boolean acted_this_turn) {
    this.acted_this_turn = acted_this_turn;
  }


  public boolean isActedLastTurn() {
    return acted_last_turn;
  }


  public void setActedLastTurn(boolean acted_last_turn) {
    this.acted_last_turn = acted_last_turn;
  }

// ========================= GETTERS / SETTERS END ========================= //


}