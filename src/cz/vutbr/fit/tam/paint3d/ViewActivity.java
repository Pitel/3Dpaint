package cz.vutbr.fit.tam.paint3d;

import android.content.Context;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;

public class ViewActivity extends Activity {
	private GLSurfaceView view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new TouchGLSurfaceView(this);
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
				float dx = x - prevX;
				float dy = y - prevY;
				if (y > getHeight() / 2) {
					dx = dx * -1 ;
				}
				if (x < getWidth() / 2) {
					dy = dy * -1 ;
				}
				renderer.angle += (dx + dy) * (180f / 320f);
				requestRender();
			}
			prevX = x;
			prevY = y;
			return true;
		}
	}
}
