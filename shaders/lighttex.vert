#version 120

varying vec4 fragPos;
void main() {
	gl_TexCoord[0] = gl_MultiTexCoord0;
    fragPos = gl_Vertex;
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}
