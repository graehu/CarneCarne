uniform int numLights;
uniform vec2 screenDimentions;
uniform float ambience;

void main()
{
	//transform fragCoord to have upper left origin
	float x = gl_FragCoord.x;
	float y = screenDimentions.y - (gl_FragCoord.y); //origin bottom left
	
	int lightCount = numLights;
	vec4 color = vec4(0,0,0,ambience);
	for(int i = 0; i < lightCount; i++)
	{
		//calc distance to light source
		float dist = length(vec2(x,y) - gl_LightSource[0].position.xy);
		
		//scale by radius (linear attentuation)
		float linearAttentuation = dist * (gl_LightSource[0].position.z / screenDimentions.x); //pixel radius
		
		//apply constant and quadratic attentuation
		float att = 1.0f / 	gl_LightSource[0].constantAttenuation +
							linearAttentuation +
							gl_LightSource[0].constantAttenuation * dist * dist;
		
		//calc final color for this light
		color += gl_LightSource[0].diffuse.rgb * att;
	}
	
	gl_FragColor = color;
}
