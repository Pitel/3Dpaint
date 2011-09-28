package cz.vutbr.fit.tam.paint3d;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class ViewActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GLSurfaceView view = new GLSurfaceView(this);
		view.setRenderer(new OpenGLRenderer());
		setContentView(view);
	}
}
