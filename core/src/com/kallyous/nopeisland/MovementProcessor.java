package com.kallyous.nopeisland;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Queue;

import java.util.Vector;


public class MovementProcessor {

    // === DATA === //

    private Vector<Entity> path;
    private Entity cursor;
    private boolean draw_cursor = false;
    private boolean draw_path = false;



    // === CONSTRUCTION === //

    MovementProcessor() {
        path = new Vector<Entity>(12);
        cursor = new Entity("cursor", 13);
    }



    // === LOGIC === //

    public void processMovement(Entity entity, Vector3 target, boolean draw_elements) {

        Queue<GraphMapVertex> reverse_path = new Queue<GraphMapVertex>();
        Queue<GraphMapVertex> final_path = new Queue<GraphMapVertex>();

        int target_x = (int)(target.x/32);
        int target_y =  (int)(target.y/32);

        // This path comes from exit to entrance. It is reversed.
        reverse_path = this.findPath(entity, target_x, target_y);

        // Visual elements.
        cursor.setTilePosition(target_x, target_y);
        draw_cursor = draw_elements;
        draw_path = draw_elements;

        // Draw stuff if told to do so.
        if (draw_elements) this.buildVisualPath(reverse_path);

        // Unreverses the path.
        while (reverse_path.size > 0)
            final_path.addFirst(reverse_path.removeFirst());

        // Removes node the entity is currently in from the path.
        final_path.removeFirst();

        // Path done.
        entity.setActivePath(final_path);

        // Debug.
        System.out.println(entity.getName() + " location: "
                + entity.getTileX() + ", " + entity.getTileY());
        System.out.println(entity.getName() + " movement target: "
                + target_x + ", " + target_y );
    }

    private Queue<GraphMapVertex> findPath(Entity entity, int target_x, int target_y) {
        GraphMapVertex entrance =
                entity.getGraphMap().getVertexAt( entity.getTileX(), entity.getTileY() );
        GraphMapVertex exit = entity.getGraphMap().getVertexAt(target_x, target_y);
        Queue<GraphMapVertex> path = this.BreadthFirstSearch(entrance, exit);
        return path;
    }

    // Breadth First Search for Path Finding
    private Queue<GraphMapVertex> BreadthFirstSearch(GraphMapVertex entrance,GraphMapVertex exit) {
        Vector<GraphMapVertex> visited = new Vector<GraphMapVertex>();
        Queue<GraphMapVertex> frontier = new Queue<GraphMapVertex>();
        Queue<GraphMapVertex> path;
        GraphMapVertex current;
        // Starts with entrance.
        frontier.addLast(entrance);
        // Loops while there is vertex available on queue.
        while (frontier.size > 0) {
            // Pop a vertex from queue for parsing.
            current = frontier.removeFirst();
            // Early Exit
            if (current == exit) {
                // We won't really parse it, but let's set this for sake of consistency.
                current.visit();
                // Add to the list of parsed vertexes.
                visited.add(current);
                // And lets get out.
                break;
            }
            // Check if vertex has been visited.
            if ( !current.isVisited() ) {
                // We are parsing it now, so mark it as visited.
                current.visit();
                // Get all it's neighbors and enqueue for parsing later.
                for (GraphMapEdge edge : current.getEdges()) {
                    // Unless the the neighbor has been visited already. Then skip.
                    if (!edge.getTarget().isVisited()) {
                        // Remembers from were we reached this vertex.
                        edge.getTarget().setSourceEdge(edge);
                        // Enqueue at the tail.
                        frontier.addLast(edge.getTarget());
                    }
                }
                // Add this vertex to the list of visited vertexes.
                visited.add(current);
            }
        }
        // At this point we can tarce back all the path from entrance to exit.
        path = this.reversePath(exit);
        // Lets clear everything in the frontier and visited lists.
        for (GraphMapVertex vertex : frontier)
            vertex.clear();
        for (GraphMapVertex vertex : visited)
            vertex.clear();
        // Now we are ready to return.
        return path;
    }

    // Build Reverse Path
    private Queue<GraphMapVertex> reversePath(GraphMapVertex exit) {
        Queue<GraphMapVertex> path = new Queue<GraphMapVertex>();
        Queue<GraphMapVertex> use_path = new Queue<GraphMapVertex>();
        GraphMapVertex current;

        path.addLast(exit);

        // Crashing with NullPointerException here when attempting to move a single square.
        try {
            current = exit.getSourceEdge().getSource();
            while (current != null) {
                path.addLast(current);
                try {
                    current = current.getSourceEdge().getSource();
                }
                catch (NullPointerException e) {
                    break;
                }
            }
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }

        return path;
    }



    // === GRAPHIC === //

    private void buildVisualPath(Queue<GraphMapVertex> track) {
        path = new Vector<Entity>();
        GraphMapVertex step;
        for (GraphMapVertex v : track) {
            step = v;
            Entity e = new Entity("trackstep", 12);
            e.setTilePosition(step.getX(), step.getY());
            path.add(e);
        }
    }

    public void drawCursor(SpriteBatch batch) {
        cursor.sprite.draw(batch);
    }

    public void drawPath(SpriteBatch batch) {
        for (Entity step : path) {
            try{
                step.sprite.draw(batch);
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public void draw(SpriteBatch batch) {
        if (draw_cursor)
            drawCursor(batch);
        if (draw_path)
            drawPath(batch);
    }

}

/**
public class MovementProcessor {

    // === INNER CLASSES === //
    public class MapTile{
        public int tile_x,tile_y, tile_id;
        public boolean reachable, passable;
        public Sprite sprite;

        public MapTile(int x, int y){
            this.tile_x = x;
            this.tile_y = y;
        }

        public void setSprite(TextureRegion region){
            sprite = new Sprite(region);
            sprite.setAlpha(0.75f);
            sprite.setPosition(this.tile_x * 32, this.tile_y * 32);
        }
    }

    public class Location {

        // Tile coordinates for this location
        public int x;
        public int y;

        // as this location been visited by the path finding algorithm yet?
        // (We are implementing A* for this)
        public boolean visited;

        // Neighbors: n = neighbor, Y = coord y, X = coord x, p = plus one in this coord,
        // m = minus one in this coord
        public boolean n_Yp, n_Ym, n_Xp, n_Xm;

        // Initializes the location with x and y
        public Location(int x, int y){
            this.x = x;
            this.y = y;
            n_Xm = n_Xp = n_Ym = n_Yp = false; // Not sure if will use this shit
        }
    }



    // === DATA === //

    public TiledMap current_map;
    public TiledMapTileLayer terrain_layer, features_layer;
    TerrainCatalog terrain_catalog = new TerrainCatalog();
    public MapTile mapTiles[][];
    public boolean tilesAtReach[][];
    private int cre_tile_x, cre_tile_y, cre_reach;
    private Texture mov_markers = new Texture(Gdx.files.internal("graphic/mov_markers.png"));
    private int markers_sheet_cols = 8;
    public int markers_sheet_rows = 4;
    private TextureRegion path_markers[];
    private Creature creature;

    // New Stuff
    private boolean draw_marker;
    // Texture
    public Texture texture;
    // Texture Sections
    public TextureRegion texture_region[];
    // Size of regions/frames and how much of them there is.
    public int region_width, region_height, sheet_cols, sheet_rows;
    // Region's to be rendered index at texture_region.
    public int region_index;

    // Sprite
    public Sprite sprite;


    // === CONSTRUCTION === //

    MovementProcessor(){
        texture = new Texture(Gdx.files.internal("graphic/interface.png"));
        // Splits regions based on the data loaded.
        TextureRegion[][] tempReg = TextureRegion.split(mov_markers,
                mov_markers.getWidth()/markers_sheet_cols, mov_markers.getHeight()/markers_sheet_rows);
        // Creates the TextureRegion array with the calculated number or children
        path_markers = new TextureRegion[markers_sheet_cols*markers_sheet_rows];
        // Assign a region to each array index.
        int index = 0;
        for(int i = 0; i < markers_sheet_rows; i++){
            for(int j = 0; j < markers_sheet_cols; j++){
                path_markers[index++] = tempReg[i][j];
            }
        }
        // Creates the sprites and puts it facing to the informed initial position, usually facing the camera.
        //sprite = new Sprite(texture_region[region_index]);

        // NEW (TEMPORARY) STUFF
        this.sheet_cols = 16;
        this.sheet_rows = 16;
        region_index = 13;
        setupTextureRegions();
        draw_marker = false;
    }

    private void setupTextureRegions() {
        // Splits regions based on the data loaded.
        TextureRegion[][] tempReg = TextureRegion.split(texture,
                texture.getWidth()/sheet_cols, texture.getHeight()/sheet_rows);
        // Creates the TextureRegion array with the calculated number or children.
        texture_region = new TextureRegion[sheet_cols*sheet_rows];
        // Assign a region to each array index.
        int index = 0;
        for(int i = 0; i < sheet_rows; i++){
            for(int j = 0; j < sheet_cols; j++){
                texture_region[index++] = tempReg[i][j];
            }
        }
        // Creates the sprites and puts it facing to the informed initial position.
        sprite = new Sprite(texture_region[region_index]);
    }


    // === LOGIC === //

    public void findBestPathTo(Creature creature, TiledMap map){
        return;
    }

    public void processMovement(Entity entity, GraphMap graph, Vector3 target_point) {
        System.out.println(entity.getName() + " shall pass!");
    }

    // Process Entity Movement (OLD)
    public void processMovement(Entity entity, TiledMap map, Vector3 target_point){
        System.out.println("Movement process called for " + entity.getName());



        //this.creature = creature;

        // Process creature positions in tile coordinates and loads the reachable tiles
        //getTilesAtReach(creature);
        // Process tilesAtReach and loads passableTiles with the final possible tiles the creature
        // can move to with the current AP.
        //getPassableTiles(creature, map);
        // Loads path_sprites with the sprites to be draw at each reachable and passable tile.
        //buildSprites();

        // Now, it is just make a loop that cycles through all elements on path_sprites and draws
        // the sprite for each element that has one.

    }

    public void getTilesAtReach(Creature creature){
        // Debug
        int out_x, out_y, out_bool;
        String output = creature.getName() + " at ";

        // Gets creature tile positions and reach
        cre_tile_x = (int)creature.getX()/16;
        cre_tile_y = (int)creature.getY()/16;
        cre_reach = (int)creature.getActionPoints();

        // Debug
        output += "("+cre_tile_x+","+cre_tile_y+") with "+cre_reach+" of reach:\n\n";

        // Creates the boolean array
        int array_dimensions= (2 * cre_reach) + 1;
        this.tilesAtReach = new boolean[array_dimensions][array_dimensions];

        // (re)Initiates mapTiles with the appropriate size
        this.mapTiles = new MapTile[array_dimensions][array_dimensions];

        // Initial tile coordinates for the target tile being verified
        int target_tile_x = cre_tile_x - cre_reach;
        int target_tile_y = cre_tile_y + cre_reach;

        for(int i = 0; i < this.mapTiles.length; i++){
            for(int j = 0; j < this.mapTiles[i].length; j++){

                // Does the calculation for if the tiles is within reach
                this.mapTiles[j][i] = new MapTile(target_tile_x, target_tile_y);
                this.mapTiles[j][i].reachable = isTileAtReach(target_tile_x,
                        target_tile_y,cre_tile_x,cre_tile_y,cre_reach);

                target_tile_x += 1; // Increments the array index for x
            }
            output += "\n";
            target_tile_y -= 1; // Increments target tile y
            target_tile_x = cre_tile_x - cre_reach; // Resets target tile x
        }

        // Debug verification
        output = creature.getName() + " at " + "("+cre_tile_x+","+cre_tile_y+") with "
                + cre_reach + " of reach:\n\n";
        for(int n = 0; n < mapTiles.length; n++){
            for(int m = 0; m < mapTiles[n].length; m++){
                if(this.mapTiles[m][n].reachable) out_bool = 1; else out_bool = 0;
                output += "  ("+mapTiles[m][n].tile_x+","+mapTiles[m][n].tile_y+"):"+out_bool+" ";
            }
            output += "\n";
        }
        System.out.print(output);

    }

    public boolean isTileAtReach(int tileX, int tileY, int creatureX, int creatureY, int range){
        // Translate tile x
        int trans_tile_x = tileX - creatureX;
        // translate tile y
        int trans_tile_y = tileY - creatureY;
        // Transform translated tile x into it's module
        if(trans_tile_x < 0) trans_tile_x = -trans_tile_x;
        // Transform translated file y into it's module
        if(trans_tile_y < 0) trans_tile_y = -trans_tile_y;
        // If the sum of the tile's coordinates modules equals or is lower than range,
        // tile is within range.
        if(trans_tile_x + trans_tile_y <= range) return true; else return false;
    }

    public void getPassableTiles(Creature creature, TiledMap map){

        this.current_map = map;
        this.terrain_layer = (TiledMapTileLayer)current_map.getLayers().get("terrain");
        this.features_layer = (TiledMapTileLayer)current_map.getLayers().get("features_bot");

        int tile_id;
        int feat_id;

        for(int i = 0; i < this.mapTiles.length; i++){
            for(int j = 0; j < this.mapTiles[i].length; j++){

                // Debug
                System.out.println("\nParsing tile at " + mapTiles[j][i].tile_x
                        + "," + mapTiles[j][i].tile_y);

                if(mapTiles[j][i].reachable){

                    // Debug
                    System.out.println("Tile is within reach.");

                    // There are several situations which tiles can be null, so we need a
                    // try/catch here.
                    // If the terrain is not identified or null, it absolutely is not passable,
                    // so we give it 99999.
                    try {
                        // Get the id of the terrain at the given tile
                        tile_id = terrain_layer.getCell(mapTiles[j][i].tile_x,
                                mapTiles[j][i].tile_y).getTile().getId();
                        System.out.println("Terrain at " + mapTiles[j][i].tile_x
                                + ","+  mapTiles[j][i].tile_y + " has id " + tile_id);
                    }catch(Exception e){
                        // Give it the 'unpassable' id if it was null or some other shit.
                        tile_id = 99999;
                        System.out.println("Terrain at " + mapTiles[j][i].tile_x
                                + ","+  mapTiles[j][i].tile_y + " has no id ");
                    }

                    // Same goes for features on its layers. Different from terrain,
                    // null tiles are valid empty space, so we give it 88888.
                    try{
                        // Get the id of the object/feature at the given tile.
                        feat_id = features_layer.getCell(mapTiles[j][i].tile_x,
                                mapTiles[j][i].tile_y).getTile().getId();
                        System.out.println("Feature found at "  + mapTiles[j][i].tile_x
                                + "," +  mapTiles[j][i].tile_y + " with id of " + feat_id);
                    }catch(Exception k){
                        // If there is no feature there, give it the 'void' id.
                        // Not really void, just empty, but void sounds more cool.
                        feat_id = 88888;
                        System.out.println("No feature seen at " + mapTiles[j][i].tile_x
                                + "," +  mapTiles[j][i].tile_y + " at features_layer.");
                    }

                    // Gives both to the function that runs the code to
                    // determinate if tile is passable or not.
                    mapTiles[j][i].passable = isTilePassable(creature, tile_id, feat_id);
                }else{
                    System.out.println("Tile is out of reach.");
                }
            }
        }
    }

    public boolean isTilePassable(Creature creature, int tile_id, int feature_id){

        System.out.print("Received tile_id: " + tile_id);
        tile_id -= 1;
        System.out.println(", effective tile_id: " + tile_id);
        System.out.print("Received feature_id: " + feature_id);
        feature_id -= 1;
        System.out.println(", effective feature_id: " + feature_id);

        // Un-passable overrides all movements
        if(Arrays.asList(terrain_catalog.overworldCatalog.get("unpassable")).contains(tile_id))
            return false;
        System.out.println("False to 'unpassable'");

        // Now we compare creature movement types with the terrain type to see if the terrain
        // is passable
        // Not implemented, since there are no indoors maps yet, there will be this check:
        //   ´if(currentMap.properties == "outside" && creature.moves_air) return true;´
        // this is a pseudo code, since there is no such properties in the maps.
        if(creature.movesAir()) return true;
        System.out.println("False to 'creature.movesAir()'");

        // Is it a hole? Only fliers can pass over holes.
        for(int hollow_id : terrain_catalog.overworldCatalog.get("hollow").asIntArray()){
            if(tile_id == hollow_id){
                System.out.println("True to 'hollow'");
                if(creature.movesAir()) return true;
                return false;
            }
        }
        System.out.println("False to 'hollow'");

        // Is it a wall? We do not test for fliers here, because it has been tested/overridden
        // before and there is no inside maps yet. If we got here, fucker don't fly.//
        for(int wall_id : terrain_catalog.overworldCatalog.get("walls").asIntArray()){
            if(tile_id == wall_id){
                System.out.println("True to 'wall'");
                if(creature.movesWalls()) return true;
                return false;
            }
        }
        System.out.println("False to 'wall'");

        // Is it water? If it is and the creature moves through water, only a obstacle can stop it.
        for(int water_id : terrain_catalog.overworldCatalog.get("water").asIntArray()){
            if(tile_id == water_id){
                System.out.println("True to 'water'");
                if(creature.movesWater()){
                    // Now let's see if there is no objects blocking the way
                    for(int block_feat_id : terrain_catalog.overworldCatalog
                            .get("features_block").asIntArray()){
                        if(feature_id == block_feat_id){
                            System.out.println("True to 'feature'");
                            System.out.println("Feature id is " + block_feat_id);
                            // Even if there is a blockage, if the creature moves through walls...
                            if(creature.movesWalls()) return true;
                            return false;
                        }
                    }
                    return true;
                }else{
                    return false;
                }
            }
        }
        System.out.println("False to 'water'");

        // Land. The same logical structure goes for land. Seriously, it is identical.
        for(int land_id : terrain_catalog.overworldCatalog.get("land").asIntArray()){
            if(tile_id == land_id){
                System.out.println("True to 'land'");
                if(creature.movesLand()){
                    // Now let's see if there is no objects blocking the way
                    System.out.println("   in features_block id list: ");
                    for(int block_feat_id : terrain_catalog.overworldCatalog
                            .get("features_block").asIntArray()){
                        System.out.println("      " + block_feat_id);
                        if(feature_id == block_feat_id){
                            System.out.println("Feature id is " + block_feat_id);
                            // Even if there is a blockage, if the creature moves through walls...
                            if(creature.movesWalls()) return true;
                            return false;
                        }
                    }
                    return true;
                }else{
                    return false;
                }
            }
        }
        System.out.println("False to 'land'");

        // Now, if nothing above happened, screw it!
        System.out.print("Tile with id " + tile_id + " and feature id " + feature_id + ":\n");
        System.out.print("Error: Terrain not properly evaluated. YOU SHALL NOT PASS!\n");
        return false;
    }

    public void buildSprites(){
        for(int l = 0; l < mapTiles.length; l++){
            for(int k = 0; k < mapTiles[l].length; k++){
                if(mapTiles[k][l].passable) mapTiles[k][l].setSprite(path_markers[1]);
            }
        }
    }

    public void drawMarker(SpriteBatch batch) {
        if (draw_marker)
            sprite.draw(batch);
    }

    public void drawTilesAtReach(SpriteBatch batch){
        for(int l = 0; l < mapTiles.length; l++){
            for(int k = 0; k < mapTiles[l].length; k++){
                if(mapTiles[k][l].passable) mapTiles[k][l].sprite.draw(batch);
            }
        }
    }
}
*/