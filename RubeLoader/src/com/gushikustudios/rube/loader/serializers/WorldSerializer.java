package com.gushikustudios.rube.loader.serializers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Json.ReadOnlySerializer;
import com.gushikustudios.rube.RubeDefaults;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.serializers.utils.RubeImage;

public class WorldSerializer extends ReadOnlySerializer<World>
{
	private final BodySerializer 	bodySerializer;
	private final JointSerializer 	jointSerializer;
	private final ImageSerializer imageSerializer;
	private RubeScene scene;
	
	public WorldSerializer(RubeScene scene, Json _json)
	{
		this.scene = scene;
		
		bodySerializer = new BodySerializer(scene,_json);
		_json.setSerializer(Body.class, bodySerializer);
		
		jointSerializer = new JointSerializer(scene,_json);
		_json.setSerializer(Joint.class, jointSerializer);
		
		imageSerializer = new ImageSerializer(scene);
		_json.setSerializer(RubeImage.class, imageSerializer);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public World read(Json json, JsonValue jsonData, Class type) 
	{
	   World world = scene.getWorld();
	   if (world == null)
	   {
	      boolean allowSleep = json.readValue("allowSleep", boolean.class, RubeDefaults.World.allowSleep, jsonData);
	      boolean autoClearForces = json.readValue("autoClearForces", boolean.class, RubeDefaults.World.autoClearForces, jsonData);
	      boolean continuousPhysics = json.readValue("continuousPhysics", boolean.class, RubeDefaults.World.continuousPhysics, jsonData);
	      boolean warmStarting = json.readValue("warmStarting", boolean.class, RubeDefaults.World.warmStarting, jsonData);

	      Vector2 gravity = json.readValue("gravity", Vector2.class, RubeDefaults.World.gravity, jsonData);

		   world = new World(gravity, allowSleep);
		   world.setAutoClearForces(autoClearForces);
		   world.setContinuousPhysics(continuousPhysics);
		   world.setWarmStarting(warmStarting);
		}
		// else ignore world settings and use the ones that were previously loaded
		scene.parseCustomProperties(json, world, jsonData);
		
		// Bodies
		bodySerializer.setWorld(world);
		Array<Body> bodies = json.readValue("body", Array.class, Body.class, jsonData);
		if (bodies != null)
		{
		   if (scene.getBodies() == null)
		   {
		      scene.setBodies(bodies);
		   }
		   else
		   {
		      scene.addBodies(bodies);
		   }
		}
		// Joints
		// joints are done in two passes because gear joints reference other joints
		// First joint pass
		jointSerializer.init(world, bodies, null);
		Array<Joint> joints = json.readValue("joint", Array.class, Joint.class, jsonData);
		if (joints != null)
		{
		   if (scene.getJoints() == null)
		   {
		      scene.setJoints(joints);
		   }
		   else
		   {
		      scene.getJoints().addAll(joints);
		   }
		}
		// Second joint pass
		jointSerializer.init(world, bodies, joints);
		joints = json.readValue("joint", Array.class, Joint.class, jsonData);
		
		
		// Images
		Array<RubeImage> images = json.readValue("image", Array.class, RubeImage.class, jsonData);
		if (images != null)
		{
		   if (scene.getImages() == null)
		   {
		      scene.setImages(images);
		   }
		   else
		   {
		      scene.getImages().addAll(images);
		   }
		   
		   for (int i = 0; i < images.size; i++)
		   {
		      RubeImage image = images.get(i);
		      scene.setMappedImage(image.body, image);
		   }
		}
		
		return world;
	}

}
