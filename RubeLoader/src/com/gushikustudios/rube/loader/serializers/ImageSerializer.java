package com.gushikustudios.rube.loader.serializers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Json.ReadOnlySerializer;
import com.gushikustudios.rube.RubeDefaults;
import com.gushikustudios.rube.RubeScene;
import com.gushikustudios.rube.loader.serializers.utils.RubeImage;
import com.gushikustudios.rube.loader.serializers.utils.RubeVertexArray;

public class ImageSerializer extends ReadOnlySerializer<RubeImage>
{
   private final Vector2 mTmp = new Vector2();
   private RubeScene scene;
   
   public ImageSerializer(RubeScene scene)
   {
	   this.scene = scene;
   }
   
   @SuppressWarnings("rawtypes")
   @Override
   public RubeImage read(Json json, JsonValue jsonData, Class type)
   {
      // Images reference bodies based on indexing in the .json file. -1 means no body reference
      Array<Body> bodies = scene.getBodies();
      
      RubeImage defaults = RubeDefaults.Image.image;
      
      RubeImage image = new RubeImage();
      
      image.angleInRads = json.readValue("angle", float.class, defaults.angleInRads, jsonData);
      int bodyIndex = json.readValue("body", int.class, jsonData);
      
      if(bodyIndex >= 0)
      {
         bodyIndex += scene.getCurrentBodyOffset();
         if ((bodies != null) && (bodyIndex < bodies.size))
         {
            image.body = bodies.get(bodyIndex);
         }
         else
         {
            throw new RuntimeException("RubeImage creation error.  bodies: " + bodies + ", bodyIndex: " + bodyIndex);
         }
      }
      
      image.center.set(json.readValue("center", Vector2.class, defaults.center, jsonData));
      
      RubeVertexArray corners = json.readValue("corners", RubeVertexArray.class, jsonData);
      if(corners != null)
      {
         mTmp.set(corners.x[0],corners.y[0]).sub(corners.x[1], corners.y[1]);
         image.width = mTmp.len();
         mTmp.set(corners.x[1],corners.y[1]).sub(corners.x[2], corners.y[2]);
         image.height = mTmp.len();
      }
      
      image.file = json.readValue("file", String.class, jsonData);
      image.filter = json.readValue("filter", int.class, defaults.filter, jsonData);
      image.flip = json.readValue("flip", boolean.class, defaults.flip, jsonData);
      image.name = json.readValue("name", String.class, jsonData);
      image.opacity = json.readValue("opacity", float.class, defaults.opacity, jsonData);
      int [] colorArray = json.readValue("colorTint", int[].class,RubeDefaults.Image.colorArray,jsonData);
      image.color.set((float)colorArray[0]/255, (float)colorArray[1]/255, (float)colorArray[2]/255, (float)colorArray[3]/255);
      
      image.renderOrder = json.readValue("renderOrder", int.class, defaults.renderOrder, jsonData);
      image.scale = json.readValue("scale", float.class, defaults.scale, jsonData);
      
      scene.parseCustomProperties(json, image, jsonData);
      String name = json.readValue("name", String.class, jsonData);
      if (name != null)
      {
         scene.putNamed(name, image);
      }
      return image;
   }
}
