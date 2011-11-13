package cz.vutbr.fit.tam.paint3d;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class DetailActivity extends Activity {

    public static final String TAG = "3Dpaint|DetailActivity";
    private TouchGLSurfaceView tGLSView;
    private Painting painting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        tGLSView = (TouchGLSurfaceView) findViewById(R.id.touch);
        painting = new Painting(this);
        painting.getById(getIntent().getExtras().getInt("paintingId"));
        tGLSView.renderer.vertices = painting.getVertices();
    }

    @Override
    protected void onPause() {
        super.onPause();
        tGLSView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tGLSView.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.obj:
                return true;
            case R.id.collada:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
