package com.example.pedalinhos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pedalinhos.domain.PedalinhoMarcao;

import java.util.List;

public class PedalinhoArrayAdapter extends ArrayAdapter<PedalinhoMarcao> {

    private final Context context;
    private final List<PedalinhoMarcao> values;


    public PedalinhoArrayAdapter(Context context, List<PedalinhoMarcao> values) {
        super(context, R.layout.list_pedalinhos_em_uso, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_pedalinhos_em_uso, parent, false);
        TextView textView = rowView.findViewById(R.id.label);
        ImageView imageView = rowView.findViewById(R.id.logo);
        PedalinhoMarcao pedalinhoMarcao = values.get(position);
        textView.setText(pedalinhoMarcao.toString());

        if (pedalinhoMarcao.isPedalinhoNaoNotificado()) {
            imageView.setImageResource(R.drawable.notify);
        } else if (pedalinhoMarcao.isPedalinhoEncerrado()) {
            imageView.setImageResource(R.drawable.timeout);
        }

        return rowView;
    }
}
