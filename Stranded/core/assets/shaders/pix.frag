varying vec4 v_color;
varying vec2 v_texCoord0;
varying float v_amount;

uniform sampler2D u_sampler2D;

//The amount of invertedness...
uniform float u_amount;

//The resolution of the screen.
uniform vec2 u_resolution;

void main() {
	
	vec2 res = vec2(1.0, u_resolution.x/u_resolution.y);
	vec2 size = vec2(res.x/u_amount, res.y/u_amount);
	vec2 uv = v_texCoord0 - mod(v_texCoord0, size);
	
	gl_FragColor = texture2D(u_sampler2D, uv) * v_color;
}