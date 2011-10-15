package cz.vutbr.fit.tam.paint3d;

import android.content.Context;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

public class ViewActivity extends Activity {
	public static final String TAG = "3Dpaint|ViewActivity";
	
	private TouchGLSurfaceView view;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new TouchGLSurfaceView(this);
		view.renderer.vertices = getIntent().getFloatArrayExtra("vertices");
		setContentView(view);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		view.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		view.onResume();
	}
	
	class TouchGLSurfaceView extends GLSurfaceView {
		private OpenGLRenderer renderer;
		private float prevX;
		private float prevY;
		
		public TouchGLSurfaceView(Context context) {
			super(context);
			renderer = new OpenGLRenderer();
			setRenderer(renderer);
			setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent e) {
			float x = e.getX();
			float y = e.getY();
			if (e.getAction() == MotionEvent.ACTION_MOVE) {
				renderer.angleX += (x - prevX) / 2;
				renderer.angleY += (y - prevY) / 2;
				requestRender();
			}
			prevX = x;
			prevY = y;
			return true;
		}
	}
}
