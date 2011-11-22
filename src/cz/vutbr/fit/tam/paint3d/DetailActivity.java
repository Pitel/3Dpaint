package cz.vutbr.fit.tam.paint3d;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity {

    public static final String TAG = "3Dpaint|DetailActivity";
    private TouchGLSurfaceView tGLSView;
    private Painting painting;
    private TextView created;
    private TextView count;
    private TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        tGLSView = (TouchGLSurfaceView) findViewById(R.id.touch);
        painting = new Painting(this);
        painting.getById(getIntent().getExtras().getInt("paintingId"));
        tGLSView.renderer.vertices = painting.getVertices();

        title = (TextView) findViewById(R.id.title);
        title.setText(painting.name);

        created = (TextView) findViewById(R.id.created);
        created.setText(painting.created);

        count = (TextView) findViewById(R.id.count);
        count.setText(getResources().getQuantityString(R.plurals.points, painting.paintingPointSet.size(), painting.paintingPointSet.size()));
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
                //Toast.makeText(this, "Not implemented!", Toast.LENGTH_LONG).show();
            	painting.exportToObj();
                return true;
            case R.id.collada:
                //Toast.makeText(this, "Not implemented!", Toast.LENGTH_LONG).show();
            	painting.exportToCollada();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goHome(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
