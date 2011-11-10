package cz.vutbr.fit.tam.paint3d;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final String TAG = "3Dpaint|MainActivity";
    public PaintingSet paintingSet;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
        
        paintingSet = new PaintingSet(this);
        paintingSet.getAll();
        setListAdapter(new PaintingListAdapter(this, paintingSet));
    }

    public void goAdd(View v) {
        startActivity(new Intent(this, AddActivity.class));
    }

    public void onItemClick(AdapterView<?> arg0, View view, int i, long _id) {
        startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("paintingId", (int) _id));
    }

    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, final long arg3) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setMessage("Opravdu smazat kresbu?");
        ad.setTitle("Potvrzení smazání");
        ad.setCancelable(true);
        ad.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Painting p = new Painting(MainActivity.this);
                p.getById((int) arg3);
                p.delete();
                paintingSet.getAll();
                ((PaintingListAdapter) getListAdapter()).notifyDataSetChanged();
            }
        });
        ad.setNegativeButton("No!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        ad.show();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        paintingSet.getAll();
        ((PaintingListAdapter) getListAdapter()).notifyDataSetChanged();
    }
}
