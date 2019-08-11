#version 120
#define NB_MAX_LIGHTS 20

struct Light{
  vec2 position;
  vec3 color;
  float falloff;
  float quadraticFalloff;
  float brightness;
};


uniform int nbOfLights;
uniform Light lights[NB_MAX_LIGHTS];
uniform sampler2D tex;

varying vec4 fragPos;

void main() {
	vec4 color = texture2D(tex, gl_TexCoord[0].st);
    vec3 finalColor = vec3(0);
    vec2 correctedFragPos = fragPos.xy;
    for(int i = 0; i<nbOfLights; i++){
            float distance = length(correctedFragPos - lights[i].position);
            float brightness = lights[i].brightness/(lights[i].quadraticFalloff*pow(distance, 2)+distance*lights[i].falloff);
            finalColor += color.rgb*lights[i].color*vec3(brightness);
    }
    if(finalColor.r > color.r){
        finalColor.r = color.r;
    }
    if(finalColor.g > color.g){
        finalColor.g = color.g;
    }
    if(finalColor.b > color.b){
        finalColor.b = color.b;
    }
	gl_FragColor = vec4(finalColor, color.a);
}
