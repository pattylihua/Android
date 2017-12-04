package com.example.travelinsingapore;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private ArrayList<MapsActivity.AddressInfo> data;
    private static int viewHolderCount = 0;
    FavoriteActivity pa;
    Context parentContext;

    FavoriteAdapter(Context context, ArrayList<MapsActivity.AddressInfo> data, FavoriteActivity pa){
        this.parentContext = context;
        this.data = data;
        this.pa = pa;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIDForListItem = R.layout.favoriteviewholder;
        LayoutInflater inflater = LayoutInflater.from(parentContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIDForListItem,parent,shouldAttachToParentImmediately);
        FavoriteViewHolder favoriteViewHolder = new FavoriteViewHolder(view);
        viewHolderCount++;
        return favoriteViewHolder;
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name;
        TextView address;
        Button delete;

        FavoriteViewHolder(View v){
            super(v);
            v.setOnClickListener(this);
            name = (TextView)v.findViewById(R.id.item_name);
            address = (TextView)v.findViewById(R.id.item_address);
            delete = (Button)v.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rowToDelete = address.getText().toString();
                    try{
                        pa.mfDb.delete(MyFavorites.Entry.TABLE_NAME, MyFavorites.Entry.COL_ADRESS + "= ?", new String[] {rowToDelete});
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                    pa.recreate();
                }
            });
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            Intent intent = new Intent(context, MapsActivity.class);
            String message = name.getText().toString();
            intent.putExtra(MainActivity.KEY,message);
            context.startActivity(intent);
        }

        public void bind(int position ){
            String locationName = data.get(position).name;
            String locationAddress = data.get(position).address;
            name.setText(locationName);
            address.setText(locationAddress);
            delete.setText("delete");
        }
    }
}
