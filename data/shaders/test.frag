uniform sampler2D tex;

vec3 overlay(vec3 base, vec3 blend)
{
	float r = (base.r < 0.5 ? (2.0 * base.r * blend.r) : (1.0 - 2.0 * (1.0 - base.r) * (1.0 - blend.r)));
	float g = (base.g < 0.5 ? (2.0 * base.g * blend.g) : (1.0 - 2.0 * (1.0 - base.g) * (1.0 - blend.g)));
	float b = (base.b < 0.5 ? (2.0 * base.b * blend.b) : (1.0 - 2.0 * (1.0 - base.b) * (1.0 - blend.b)));
	return vec3(r,g,b);
}

vec3 myOverlay(vec3 base, vec3 blend)
{
	float r = (base.r < 0.5 ? (2.0 * base.r * (blend.r)) : (1.0 - 2.0 * (1.0 - base.r) * (1.0 - blend.r)));
	float g = (base.g < 0.5 ? (2.0 * base.g * (blend.g)) : (1.0 - 2.0 * (1.0 - base.g) * (1.0 - blend.g)));
	float b = (base.b < 0.5 ? (2.0 * base.b * (blend.b)) : (1.0 - 2.0 * (1.0 - base.b) * (1.0 - blend.b)));
	return vec3(r,g,b);
}

vec3 softLight(vec3 base, vec3 blend)
{
	float r = ((blend.r < 0.5) ? (2.0 * base.r * blend.r + base.r * base.r * (1.0 - 2.0 * blend.r)) : (sqrt(base.r) * (2.0 * blend.r - 1.0) + 2.0 * base.r * (1.0 - blend.r)));
	float g = ((blend.g < 0.5) ? (2.0 * base.g * blend.g + base.g * base.g * (1.0 - 2.0 * blend.g)) : (sqrt(base.g) * (2.0 * blend.g - 1.0) + 2.0 * base.g * (1.0 - blend.g)));
	float b = ((blend.b < 0.5) ? (2.0 * base.b * blend.b + base.b * base.b * (1.0 - 2.0 * blend.b)) : (sqrt(base.b) * (2.0 * blend.b - 1.0) + 2.0 * base.b * (1.0 - blend.b)));
	return vec3(r,g,b);
}

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
		//get light specific properties
		float tick = gl_LightSource[i].position.w;
		float constAtt = gl_LightSource[i].ambient.r;
		float quadAtt = gl_LightSource[i].ambient.g;
		
		//calc distance to light source
		vec2 pos = gl_LightSource[i].position.xy;
		float dist = length(vec2(x,y) - pos);
		
		float temp = dist / gl_LightSource[i].position.z ;
		//temp += 1/(quadAtt*dist*dist);
		
		float attentuation = temp*tick;
		
		if(temp <= 1.0)
			value -= max(0.0, 1.0 - attentuation);
	}
	//clamp value between 0 and ambience
	value = min(ambience, value);
	value = max(0.0, value);
	
	vec3 base = texture2D(tex,gl_TexCoord[0].st).rgb;
	vec3 blend = vec3(1,1,1) * (1-value);
	
	gl_FragColor = vec4(myOverlay(base,blend),value);
}
