#version 120

uniform int time;

void main() {

	vec4 vert = gl_Vertex;
    
	gl_TexCoord[0] = gl_MultiTexCoord0;
    gl_Position = gl_ModelViewProjectionMatrix * vert;
	
}