package com.missionbit.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by missionbit on 6/26/17.
 */

public class Portal extends InteractiveTileObject{
    public Portal(World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds);
    }

    @Override
    public boolean onCollisionDetected() {
        Gdx.app.log("Portal", "Portal Hit");
        return false;
    }
}
