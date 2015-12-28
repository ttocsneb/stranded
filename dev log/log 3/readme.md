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
    d = √(x²+y²)
    x = sin(θ + a)*d
    y = cos(θ + a)*d

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