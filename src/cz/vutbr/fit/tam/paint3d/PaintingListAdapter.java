package cz.vutbr.fit.tam.paint3d;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PaintingListAdapter extends BaseAdapter {

    private static final String TAG = "3DPaint|PaintingListAdapter";
    private PaintingSet paintingSet;
    private LayoutInflater mInflater;

    public PaintingListAdapter(Context context, PaintingSet paintingSet) {
        this.paintingSet = paintingSet;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return this.paintingSet.size();
    }

    public Painting getItem(int i) {
        return (Painting) this.paintingSet.get(i);
    }

    public long getItemId(int i) {
        return ((Painting) this.paintingSet.get(i)).paintingId;
    }

    public View getView(final int i, View convertView, ViewGroup parent) {
        final PaintingViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item, parent, false);

            holder = new PaintingViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.created = (TextView) convertView.findViewById(R.id.created);
            convertView.setTag(holder);
        } else {
            holder = (PaintingViewHolder) convertView.getTag();
        }

        holder.name.setText(this.getItem(i).name);
        holder.created.setText(this.getItem(i).created);

        return convertView;
    }
}

class PaintingViewHolder {

    TextView name;
    TextView created;
}
