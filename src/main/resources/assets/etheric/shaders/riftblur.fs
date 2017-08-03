#version 120

uniform sampler2D bgl_RenderedTexture;
uniform int time;

void main() {

	vec4 color = texture2D(bgl_RenderedTexture, gl_TexCoord[0].st);
    
    gl_FragColor = color;
	
}