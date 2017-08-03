package etheric.client.handler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import etheric.Etheric;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class ShaderHandler implements IResourceManagerReloadListener {

	public static ShaderHandler instance = new ShaderHandler();
	
	public int riftBlurProgram = 0;
	
	private ShaderHandler() {
		
	}

	public void linkProgram(int program) {
		if (OpenGlHelper.shadersSupported) {
			OpenGlHelper.glUseProgram(program);
		}
	}

	public void unlinkProgram() {
		linkProgram(0);
	}

	public void init() {
		riftBlurProgram = loadProgram("/assets/" + Etheric.MODID + "/shaders/riftblur.vs",
				"/assets/" + Etheric.MODID + "/shaders/riftblur.fs");
	}

	public int loadProgram(String vsh, String fsh) {
		if (OpenGlHelper.shadersSupported) {
			int vertexShader = createShader(vsh, OpenGlHelper.GL_VERTEX_SHADER);
			int fragmentShader = createShader(fsh, OpenGlHelper.GL_FRAGMENT_SHADER);
			int program = OpenGlHelper.glCreateProgram();
			OpenGlHelper.glAttachShader(program, vertexShader);
			OpenGlHelper.glAttachShader(program, fragmentShader);
			OpenGlHelper.glLinkProgram(program);
			return program;
		}
		return 0;
	}

	public int createShader(String fileName, int type) {
		int shader = OpenGlHelper.glCreateShader(type);

		if (shader != 0) {
			ARBShaderObjects.glShaderSourceARB(shader, readFile(fileName));

			OpenGlHelper.glCompileShader(shader);
			OpenGlHelper.glCompileShader(shader);

			if (GL20.glGetShaderi(shader, OpenGlHelper.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
			}
		}

		return shader;
	}
	
	private String getLogInfo(int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}

	public String readFile(String fileName) {
		InputStream in = ShaderHandler.class.getResourceAsStream(fileName);
		String file = "";

		if (in != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			file = reader.lines().collect(Collectors.joining("\n"));
		}

		return file;
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		init();
	}

}
