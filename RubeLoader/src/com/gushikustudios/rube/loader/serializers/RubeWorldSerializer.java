package com.gushikustudios.rube.loader.serializers;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Json.ReadOnlySerializer;
import com.gushikustudios.rube.RubeDefaults;
import com.gushikustudios.rube.RubeScene;

public class RubeWorldSerializer extends ReadOnlySerializer<RubeScene>
{
	private WorldSerializer mWorldSerializer;
	private RubeScene scene;
	private boolean mScenePopulated;
   
	public RubeWorldSerializer(Json json)
	{
		scene = new RubeScene();
		mWorldSerializer = new WorldSerializer(scene,json);
		json.setSerializer(World.class, mWorldSerializer);
		json.setIgnoreUnknownFields(true);
	}
	
	public void resetScene()
	{
	   if (scene != null)
	   {
	      World world = scene.getWorld();
	      if (world != null)
	      {
	         world.dispose();
	      }
	      scene.clear();
	   }
	   mScenePopulated = false;
	}
	
	public void usePreexistingWorld(World world)
	{
	   if (scene != null)
	   {
	      scene.setWorld(world);
	   }
	   mScenePopulated = true; // prevent a new world from being defined
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public RubeScene read(Json json, JsonValue jsonData, Class type) 
	{
	   if (!mScenePopulated)
	   {
	      scene.stepsPerSecond 		= json.readValue("stepsPerSecond", 		int.class, RubeDefaults.World.stepsPerSecond, 		jsonData);
	      scene.positionIterations 	= json.readValue("positionIterations", 	int.class, RubeDefaults.World.positionIterations, 	jsonData);
	      scene.velocityIterations 	= json.readValue("velocityIterations", 	int.class, RubeDefaults.World.velocityIterations, 	jsonData);
	      scene.setWorld(json.readValue(World.class,	jsonData));
	      mScenePopulated = true;
	   }
	   else
	   {
	      // ignore scene related items.  The read below will add items to the world and scene previously read
	      json.readValue(World.class, jsonData);
	   }
		return scene;
	}
}
