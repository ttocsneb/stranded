package com.gushikustudios.rube.loader.serializers.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class RubeImage {
	public float angleInRads = 0.0f;
	public Body body = null;
	public final Vector2 center = new Vector2();
	public final Color color = new Color();
	public String file = null;
	public int filter;
	public String name = null;
	public float opacity = 1.0f;
	public int renderOrder = 0;
	public float scale = 1.0f;
	public float width = 0.0f;
	public float height = 0.0f;
	public boolean flip = false;
}
