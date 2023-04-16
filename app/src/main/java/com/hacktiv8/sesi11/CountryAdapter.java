package com.hacktiv8.sesi11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {

    private Context context;

    private List<Country> countryList;

    public CountryAdapter(Context context, List<Country> countryList){
        this.context = context;
        this.countryList = countryList;
    }

    public CountryAdapter(Context context, List<Country> countryList, DeleteCountryListener deleteCountryListener, EditCountryListener editCountryListener){
        this.context = context;
        this.countryList = countryList;
        this.deleteCountryListener = deleteCountryListener;
        this.editCountryListener = editCountryListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.ViewHolder holder, int position) {
            Country country = countryList.get(position);
            holder.countryName.setText(country.getCountryName());
            holder.populationName.setText(String.valueOf(country.getPopulation()));
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView countryName;
        TextView populationName;
        Button editButton;
        Button deleteButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.country_name);
            populationName = itemView.findViewById(R.id.population);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            if(v.getId() == R.id.edit_button){
                //
                editCountryListener.onEditCountry(position);
            }

            else if(v.getId() == R.id.delete_button){
                //
                deleteCountryListener.onDeleteCountry(position);
            }

        }
    }

    public interface EditCountryListener{
        void onEditCountry(int position);
    }

    public interface DeleteCountryListener{
        void onDeleteCountry(int position);
    }

    private  EditCountryListener editCountryListener;

    private  DeleteCountryListener deleteCountryListener;
}
