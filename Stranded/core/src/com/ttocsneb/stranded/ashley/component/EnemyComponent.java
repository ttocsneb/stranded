package com.ttocsneb.stranded.ashley.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;


/**
 * @author TtocsNeb
 *
 */
public class EnemyComponent implements Component {

	public Body body;
	public boolean active = false;
	public float health = 2;
	public float max = 2;
	
}
