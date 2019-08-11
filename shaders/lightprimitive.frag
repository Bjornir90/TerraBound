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

varying vec2 fragPos;
varying vec4 color;

void main() {
    vec3 objectColor = color.rgb;
    vec3 finalColor = vec3(0);
    for(int i = 0; i<nbOfLights; i++){
            float distance = length(fragPos - lights[i].position);
            float brightness = lights[i].brightness/(lights[i].quadraticFalloff*pow(distance, 2)+distance*lights[i].falloff);
            finalColor += objectColor*lights[i].color*vec3(brightness);
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
