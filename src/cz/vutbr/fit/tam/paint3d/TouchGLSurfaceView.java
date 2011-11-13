package cz.vutbr.fit.tam.paint3d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TouchGLSurfaceView extends GLSurfaceView {
    public static final String TAG = "3Dpaint|TouchGLSurfaceView";

        public OpenGLRenderer renderer;
        private float prevX;
        private float prevY;

        public TouchGLSurfaceView(Context context, AttributeSet attr) {
            super(context, attr);
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
