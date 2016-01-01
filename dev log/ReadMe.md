# Developmen log

This readme file has all of the logs, but any folders in this directory hold special resources, such as paper notes, images, etc.

### Dec 23 7:01 PM

Overlap is just not working for me.. literally, I can't seem to get the text to work, and I have already spent so much time trying to get it to work.  I would use Tiled, but it would be new as well, and I shouldn't get into any more trouble.  I am Thinking of using iForce2D's box2D editor, as a basic world builder alongside with Ashley.  Hopefully I will get a basic system working from then.

### Dec 24 1:30 PM

Rube is working so well for me, I didn't realize how much of a world editor it is.  The hardest thing about implementing it, is the fact that it was originally designed for C, however there are language agnostic export methods, but that exports a json file.  This is good for libGDX, as it already has a built in json enterperator, but I needed a library to load the save file into the game.  Luckily it didn't take me very long to find one, I am using [tescott's RubeLoader](https://github.com/tescott/RubeLoader).

So I have built a test program for rube, and it [works](https://goo.gl/photos/MZ9e1C5kf2eM4sWt9) very well.  Hopefully I can create a game in now 3 weeks.

### Dec 27 8:22 AM

I have created one of the opening scenes in rube, which has received some popularity on twitter, where a space station is crushed by an asteroid.

[![Stranded Station Explosion](http://img.youtube.com/vi/2ATo21JDGEU/0.jpg)](https://youtu.be/2ATo21JDGEU "Stranded Station Explosion")

This looks very nice, however with the code I am using, The textures are rendered a little offset.  This is super annoying, but once I get it fixed, I shouldn't get any other problems with rendering the world.

![World Rendered in-game](https://goo.gl/c0EKMP)

### Dec 27 7:54 pm

I finally fixed it! I somewhat stupidly used sin/cos wrong.  Let me explain.

In RUBE, the image location is set relative to the Box2D Body it belongs to.  In order to get the location of the image, it isn't as simple as adding the location to the body position, as you need to take into account the rotation of the body as well.  In the image above, I was running the following code.

    pos.x = image.center.x * MathUtils.sin(image.body.getAngle());
    pos.y = image.center.y * MathUtils.cos(iamge.body.getAngle());

This definitely did not work.  The solution is more complex than this.  

    a = arctan2(y, x)
    d = v(x²+y²)
    x = sin(? + a)*d
    y = cos(? + a)*d

This works, however arctan has a possibility of being 180° off.  The final code I used is

    a = Math.atan2(image.center.y, image.center.x);
    d = Math.sqrt(Math.pow(image.center.x, 2) + Math.pow(image.center.y, 2));
    
    tmp.set(MathUtils.sin(a) * d, MathUtils.cos(a) * d);
    
    if(tmp.x == -image.center.x && tmp.y == -image.center.y) {
        a += Math.PI;
    }
    
    a += image.body.getAngle();
    
    pos.add(MathUtils.sin(a) * d, MathUtils.cos(a) * d);

You can find this code in [`Stranded/core/src/com/ttocsneb/stranded/ashley/RubeRenderSystem.java:line 127`](https://github.com/ttocsneb/stranded/blob/master/Stranded/core/src/com/ttocsneb/stranded/ashley/RubeRendererSystem.java)

### Dec 28 12:47 PM

I have fixed the Gradle issues, where it was not possible to run the project through the command line.  Just remember to allways manage dependancies with gradle.  If you are having similar issues, you might be ignoring some important files. I was ignoring the Gradle Wrapper file: `gradle/wrapper/gradle-wrapper.jar`, and any 3rd party libraries you are using. *The libraries you manually include.*

### Dec 31 2:29 PM

I am currently setting up a system to hold multiple scenes.  It works almost the same as my DirectedGame System.  I made it so that you could controll these scenes from one parent class.  It also allows for smooth transitions between scenes.  The only problem I find with this system is that anything you want to transition must be in the scene you are transitioning to/from, which in hindsight is obvious.  I need to make a few changes to my test class but everything should work.

### Jan 1 11:06 AM

Happy New Year!

I am having a few troubles with transitions between scenes, and I don't want to deal with it, I am just going to not have transitions between scenes, or at least not for now.

### Jan 2 11:57 AM

I just realized how stupid the Scene system was.  The way it was set up, made it as useful as regular screens, just with a different name.  The reason it is stupid is the fact that I am using Ashley.  It allows you to organize your code, and it works very nicely.  I can use it in place of what I was originally planning to do, which would be much easier.  So I'm scrapping commit for scenes. And if anyone asks.. you didn't see anything.
