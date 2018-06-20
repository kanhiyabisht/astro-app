package com.example.astrodashalib.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.astrodashalib.R;
import com.example.astrodashalib.data.models.DialogListOption;

import java.util.ArrayList;

/**
 * Created by himanshu on 09/10/17.
 */

public class SimpleDialogAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private boolean isGrid;
    Context mContext;
    ArrayList<DialogListOption> dialogListOptions = new ArrayList<>();

    public SimpleDialogAdapter(Context context, ArrayList<DialogListOption> dialogListOptions) {
        layoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.dialogListOptions = dialogListOptions;
    }

    @Override
    public int getCount() {
        return dialogListOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;


        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.dialog_list_item, parent, false);


            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            viewHolder.imageView1 = (ImageView) convertView.findViewById(R.id.image_view1);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        DialogListOption dialogListOption = dialogListOptions.get(position);

        viewHolder.textView.setText(dialogListOption.itemName);
        if (dialogListOption.drawableId != null)
            viewHolder.imageView.setImageResource(dialogListOption.drawableId);
        else {
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.imageView1.setVisibility(View.GONE);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView,imageView1;
    }
}

