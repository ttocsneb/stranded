package com.gushikustudios.rube.loader.serializers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.ReadOnlySerializer;
import com.badlogic.gdx.utils.JsonValue;

public class Vector2Serializer extends ReadOnlySerializer<Vector2>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Vector2 read(Json json, JsonValue jsonData, Class type) 
	{
		Vector2 vector = new Vector2(); 
		
		vector.x = json.readValue("x", float.class, 0.0f, jsonData);
		vector.y = json.readValue("y", float.class, 0.0f, jsonData);
				
		return vector;
	}

}
