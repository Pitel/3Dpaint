package cz.vutbr.fit.tam.paint3d;

import android.opengl.GLES11;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import java.nio.*;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements Renderer {
	public float angle;
	private FloatBuffer triangle;
	final public static float triangleCoords[] = {
		-0.5f,	-0.25f,	0,
		0.5f,	-0.25f,	0,
		0.0f,	0.559016994f,	0
	};
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES11.glClearColor(0, 0, 0, 1);
		
		final ByteBuffer buffer = ByteBuffer.allocateDirect(triangleCoords.length * 4); //float = 4 B
		buffer.order(ByteOrder.nativeOrder());
		triangle = buffer.asFloatBuffer();
		triangle.put(triangleCoords);
		triangle.position(0);
		
		GLES11.glEnableClientState(GLES11.GL_VERTEX_ARRAY);
	}
	
	@Override
	public void onDrawFrame(GL10 gl) {
		GLES11.glClear(GLES11.GL_COLOR_BUFFER_BIT | GLES11.GL_DEPTH_BUFFER_BIT);
		
		GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
		GLES11.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, -5, 0, 0, 0, 0, 1, 0);
		
		GLES11.glRotatef(angle, 0, 0, 1);
		
		GLES11.glColor4f(1, 1, 1, 1);
		GLES11.glVertexPointer(3, GLES11.GL_FLOAT, 0, triangle);
		GLES11.glDrawArrays(GLES11.GL_TRIANGLES, 0, 3);
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		GLES11.glViewport(0, 0, width, height);
		
		float ratio = (float) width / height;
		GLES11.glMatrixMode(GLES11.GL_PROJECTION);
		GLES11.glLoadIdentity();
		GLU.gluPerspective(gl, 60, (float) width / (float) height, 0.1f, 10);
	}
}
