uniform sampler2D tex;

void main()
{
	int numLights = int(gl_LightSource[0].specular.r);
	vec2 screenDimentions = gl_LightSource[0].specular.gb;
	float ambience = gl_LightSource[0].specular.a;
	
	//transform fragCoord to have upper left origin
	float x = gl_FragCoord.x;
	float y = screenDimentions.y - (gl_FragCoord.y); //origin bottom left
	
	float value = ambience;
	for(int i = 0; i < numLights; i++)
	{
		//calc distance to light source
		vec2 pos = gl_LightSource[i].position.xy;
		float dist = length(vec2(x,y) - pos);
		
		float temp = dist / gl_LightSource[i].position.z ;
		if(temp <= 1.0)
			value -= 1.0 - temp;
	}
	vec3 base = texture2D(tex,gl_TexCoord[0].st);
	vec3 blend = vec3(0,0,0);
	float r = (base.r < 0.5 ? (2.0 * base.r * blend.r) : (1.0 - 2.0 * (1.0 - base.r) * (1.0 - blend.r)));
	float g = (base.g < 0.5 ? (2.0 * base.g * blend.g) : (1.0 - 2.0 * (1.0 - base.g) * (1.0 - blend.g)));
	float b = (base.b < 0.5 ? (2.0 * base.b * blend.b) : (1.0 - 2.0 * (1.0 - base.b) * (1.0 - blend.b)));
	gl_FragColor = vec4(r,g,b,value);
}
