package com.gushikustudios.rube;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gushikustudios.rube.loader.serializers.utils.RubeImage;

/**
 * A simple encapsulation of a {@link World}. Plus the data needed to run the simulation.
 * @author clement.vayer
 *
 */
public class RubeScene implements Cloneable
{
   
	/** Box2D {@link World} */
	private World world;
	
	private Array<Body> mBodies;
	private Array<RubeImage> mImages;
	private Map<Body,Array<RubeImage>> mBodyImageMap;
	private Array<Fixture> mFixtures;
	private Array<Joint> mJoints;
	private Map<String,Array<Object>> mItemsByName;
	private Map<Object,Map<String, Object>> mCustomPropertiesMap;
	private int mBodyOffset; // determines where bodies have been inserted.  Needed for image referencing for separate files.
	
	/** Simulation steps wanted per second */
	public int   stepsPerSecond;
	/** Iteration steps done in the simulation to calculates positions */
	public int   positionIterations;
	/** Iteration steps done in the simulation to calculates velocities */
	public int   velocityIterations;
	
	public RubeScene()
	{
		stepsPerSecond 		= RubeDefaults.World.stepsPerSecond;
		positionIterations 	= RubeDefaults.World.positionIterations;
		velocityIterations 	= RubeDefaults.World.velocityIterations;
		
		mCustomPropertiesMap = new HashMap<Object, Map<String, Object>>();
		mBodyImageMap = new HashMap<Body,Array<RubeImage>>();
		
		mItemsByName = new HashMap<String,Array<Object>>();
		
	}

	@SuppressWarnings("unchecked")
	public void parseCustomProperties(Json json,Object item, JsonValue jsonData)
	{
		Array<Map<String,?>> customProperties = json.readValue("customProperties", Array.class, HashMap.class, jsonData);
		if (customProperties != null)
		{
			for (Map<String, ?> property : customProperties)
			{

				String propertyName = (String)property.get("name");
				if (property.containsKey("string"))
				{
					setCustom(item, propertyName, (String)property.get("string"));
				}
				else if (property.containsKey("int"))
				{
					// Json stores things as Floats.  Convert to integer here.
					setCustom(item, propertyName,(Integer)((Float)property.get("int")).intValue());
				}
				else if (property.containsKey("float"))
				{
				   try
				   {
					setCustom(item, propertyName, (Float) property.get("float"));
				   }
				   catch (Exception ex)
				   {
				      // probably a string.
				      Long hex = Long.parseLong((String)property.get("float"), 16);
				      Float f = Float.intBitsToFloat(hex.intValue());
				      setCustom(item, propertyName, f);
				   }
				}
				else if (property.containsKey("vec2"))
				{
					setCustom(item, propertyName, json.readValue(Vector2.class, (JsonValue)property.get("vec2")));
				}
				else if (property.containsKey("bool"))
				{
					setCustom(item, propertyName, (Boolean)property.get("bool"));
				}
				else if (property.containsKey("color"))
				{
					Array<Float> colorArray = (Array<Float>) property.get("color");
					setCustom(item, propertyName, new Color(colorArray.get(0)/255, colorArray.get(1)/255, colorArray.get(2)/255, colorArray.get(3)/255));
				}
			}
		}
	}
	
	
   public Map<String, Object> getCustomPropertiesForItem(Object item, boolean createIfNotExisting)
   {
      if (mCustomPropertiesMap.containsKey(item))
      {
         return mCustomPropertiesMap.get(item);
      }

      if (!createIfNotExisting)
      {
         return null;
      }

      Map<String, Object> props = new HashMap<String, Object>();
      mCustomPropertiesMap.put(item, props);

      return props;
   }
   
   
   public void setCustom(Object item, String propertyName, Object object)
   {
      getCustomPropertiesForItem(item, true).put(propertyName, object);
   }
   

   public Object getCustom(Object item, String propertyName, Object defaultVal)
   {
      Map<String, Object> props= getCustomPropertiesForItem(item, false);
      if (null == props)
         return defaultVal;
      if (props.containsKey(propertyName))
      {
         return props.get(propertyName);
      }
      return defaultVal;
   }
   
   public Object getCustom(Object item, String propertyName)
   {
      Map<String, Object> props= getCustomPropertiesForItem(item, false);
      if (null == props)
         return null;
      if (props.containsKey(propertyName))
      {
         return props.get(propertyName);
      }
      return null;
   }
   
   
   public void clear()
   {
      if (mBodies != null)
      {
         mBodies.clear();
      }
      
      if (mFixtures != null)
      {
         mFixtures.clear();
      }
      
      if (mJoints != null)
      {
         mJoints.clear();
      }
      
      if (mImages != null)
      {
         mImages.clear();
      }
      
      if (mCustomPropertiesMap != null)
      {
         mCustomPropertiesMap.clear();
      }
      
      if (mBodyImageMap != null)
      {
         mBodyImageMap.clear();
      }
      
      world = null;
   }
	
	/**
	 * Convenience method to update the Box2D simulation with the parameters read from the scene.
	 */
	public void step()
	{
		if(world != null)
		{
			float dt = 1.0f/stepsPerSecond;
			world.step(dt, velocityIterations, positionIterations);
		}
	}

   public void setBodies(Array<Body> mBodies)
   {
      this.mBodies = mBodies;
   }
   
   public void addBodies(Array<Body> bodies)
   {
      if (bodies != null)
      {
         mBodyOffset = mBodies.size; // determine where the new bodies are inserted at
         mBodies.addAll(bodies); // appends the passed body array to the end of the current body array
      }
   }
   
   public int getCurrentBodyOffset()
   {
      return mBodyOffset;
   }

   public Array<Body> getBodies()
   {
      return mBodies;
   }
   
   

   public void addFixtures(Array<Fixture> fixtures)
   {
	   if (fixtures != null)
	   {
		   if (mFixtures == null)
		   {
			   mFixtures = new Array<Fixture>();
		   }
		   mFixtures.addAll(fixtures);
	   }
   }
   
   public World getWorld()
   {
      return world;
   }
   
   public void setWorld(World world)
   {
      this.world = world;
   }

   public Array<Fixture> getFixtures()
   {
      return mFixtures;
   }

   public void setJoints(Array<Joint> mJoints)
   {
      this.mJoints = mJoints;
   }

   public Array<Joint> getJoints()
   {
      return mJoints;
   }
   
   public void setImages(Array<RubeImage> images)
   {
      mImages = images;
   }
   
   public Array<RubeImage> getImages()
   {
      return mImages;
   }
   
   public void setMappedImage(Body body, RubeImage image)
   {
      Array<RubeImage> images = mBodyImageMap.get(body);
      // if the mapping hasn't been created yet...
      if (images == null)
      {
         // initialize the key's value...
         images = new Array<RubeImage>(false,1); // expectation is that most, if not all, bodies will have a single image.
         images.add(image);
         mBodyImageMap.put(body, images);
      }
      else
      {
         //TODO: Sort based on render order of the image
         images.add(image);
      }
   }
   
   public Array<RubeImage> getMappedImage(Body body)
   {
      return mBodyImageMap.get(body);
   }
   
   public void putNamed(String name, Object item)
   {
      Array<Object> items = mItemsByName.get(name);
      
      if (items == null)
      {
         items = new Array<Object>(false,1);
         items.add(item);
         mItemsByName.put(name,items);
      }
      else
      {
    	  items.add(item);
      }
   }
   
   
   @SuppressWarnings("unchecked")
   public <T> Array<T> getNamed(Class<T> type, String name)
   {
	  return (Array<T>) mItemsByName.get(name);  
   }
   
   public void printStats() {
	   System.out.println("Body count: " + ((mBodies != null) ? mBodies.size : 0));
	   System.out.println("Fixture count: " + ((mFixtures != null) ? mFixtures.size : 0));
	   System.out.println("Joint count: " + ((mJoints != null) ? mJoints.size : 0));
	   System.out.println("Image count: " + ((mImages != null) ? mImages.size : 0));
	   
   }
}
