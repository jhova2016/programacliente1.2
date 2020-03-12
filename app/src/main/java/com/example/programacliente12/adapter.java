package com.example.programacliente12;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.ViewHolderDatos> implements View.OnClickListener {


    //ArrayList<String> ListDatos;
    ArrayList<elemento> ListDatos;
    private View.OnClickListener Listener;

    public adapter(ArrayList<elemento> listDatos) {
        ListDatos = listDatos;
    }



    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter,parent,false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.dato.setText(ListDatos.get(position).getNombre());
        holder.tipo.setText(ListDatos.get(position).getTipo());

    }

    @Override
    public int getItemCount() {
        return ListDatos.size();
    }

    public  void  setOnClickListener(View.OnClickListener listener)
    {
        this.Listener=listener;
    }

    @Override
    public void onClick(View v) {
        if(Listener!=null)
        {
            Listener.onClick(v);
        }

    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView dato;
        TextView tipo;
        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            dato=itemView.findViewById(R.id.item);
            tipo=itemView.findViewById(R.id.tipo);
        }


    }
}
