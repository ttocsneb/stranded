package com.gushikustudios.rube.loader;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.gushikustudios.rube.RubeScene;

public class RubeSceneSyncLoader extends SynchronousAssetLoader<RubeScene, RubeSceneSyncLoader.RubeSceneParameters>
{
   private final RubeSceneLoader mLoader;
   
   static public class RubeSceneParameters extends AssetLoaderParameters<RubeScene>
   {
      // TODO: Add any special parameter definitions here
   }
   
   public RubeSceneSyncLoader(FileHandleResolver resolver)
   {
      this(null,resolver);
   }
   
   /**
    * Specifies a pre-defined Box2D world to add scene objects to.
    * 
    * @param world
    * @param resolver
    */
   public RubeSceneSyncLoader(World world, FileHandleResolver resolver)
   {
      super(resolver);
      mLoader = new RubeSceneLoader(world);
   }

   @Override
   public RubeScene load(AssetManager assetManager, String fileName, FileHandle file, RubeSceneParameters parameter)
   {
      return mLoader.addScene(file);
   }

   @SuppressWarnings("rawtypes")
   @Override
   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, RubeSceneParameters parameter)
   {
      return null;
   }
}
