package cz.vutbr.fit.tam.paint3d;

import android.opengl.GLES11;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import java.nio.*;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLRenderer implements Renderer {
	public float angle;
	private FloatBuffer trail;
	final public static float verts[] = {
		-0.5f,	-0.5f,	0,
		0.5f,	-0.5f,	0,
		0.5f,	0.5f,	0,
		-0.5f,	0.5f,	0
	};
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		GLES11.glClearColor(0, 0, 0, 1);
		
		final ByteBuffer buffer = ByteBuffer.allocateDirect(verts.length * 4); //float = 4 B
		buffer.order(ByteOrder.nativeOrder());
		trail = buffer.asFloatBuffer();
		trail.put(verts);
		trail.position(0);
		
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
		GLES11.glVertexPointer(3, GLES11.GL_FLOAT, 0, trail);
		GLES11.glDrawArrays(GLES11.GL_LINE_STRIP, 0, verts.length / 3);
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
