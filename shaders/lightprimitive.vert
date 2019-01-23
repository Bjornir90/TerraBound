#version 120


varying vec2 fragPos;
varying vec4 color;
void main() {
	color = gl_Color;
	vec4 vPos = gl_Vertex;
    fragPos = vec2(vPos);
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}
