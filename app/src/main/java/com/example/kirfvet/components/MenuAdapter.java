package com.example.kirfvet.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kirfvet.R;

import java.util.List;

public class MenuAdapter extends ArrayAdapter<MenuItem> {
    private Context context;
    private List<MenuItem> items;
    private int layoutResource;

    public MenuAdapter(Context context, List<MenuItem> items, int layoutResource) {
        super(context, layoutResource, items);
        this.context = context;
        this.items = items;
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layoutResource, parent, false);
        }

        MenuItem item = items.get(position);

        // Configurar la imagen
        ImageView imageView = view.findViewById(R.id.imgIcono);
        imageView.setImageResource(item.getImageResourceId());

        // Configurar el texto
        TextView textView = view.findViewById(R.id.tvNombre);
        textView.setText(item.getTitle());

        return view;
    }
} 