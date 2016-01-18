package com.ttocsneb.stranded.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;


/**
 * @author TtocsNeb
 *
 */
public class BulletComponent implements Component {
	
	public Body body;
	
	public boolean inUse;
	
	public float time;
}
