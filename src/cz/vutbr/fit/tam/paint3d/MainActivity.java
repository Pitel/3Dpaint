package cz.vutbr.fit.tam.paint3d;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

    public void goAdd(View view) {
        startActivity(new Intent(this, AddActivity.class));
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long paintingId) {
        startActivity(new Intent(MainActivity.this, DetailActivity.class).putExtra("paintingId", (int) paintingId));
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, final long paintingId) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setMessage(R.string.delete_confirm_msg);
        ad.setIcon(android.R.drawable.ic_dialog_alert);
        ad.setTitle(R.string.delete_confirm_title);
        ad.setCancelable(true);
        ad.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                Painting p = new Painting(MainActivity.this);
                p.getById((int) paintingId);
                p.delete();
                paintingSet.getAll();
                ((PaintingListAdapter) getListAdapter()).notifyDataSetChanged();
            }
        });
        ad.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

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
