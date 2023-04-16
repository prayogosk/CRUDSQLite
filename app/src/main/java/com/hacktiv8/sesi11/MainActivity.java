package com.hacktiv8.sesi11;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, CountryAdapter.DeleteCountryListener, CountryAdapter.EditCountryListener {

    private Button addData;
    private RecyclerView listCountryRv;
    private List<Country> countryList;
    SQLiteDatabaseHandler db;
    CountryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new SQLiteDatabaseHandler(this);

        addData = findViewById(R.id.add_data);
        listCountryRv = findViewById(R.id.list_data);
        addData.setOnClickListener(this);

        loadDataCountry();

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        listCountryRv.setLayoutManager(lm);
    }

    private void loadDataCountry() {
        countryList = db.getAllCountries();
        adapter = new CountryAdapter(this, countryList, this, this);
        listCountryRv.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.add_data){
            //Show Form Add Data
            showFormAddData(null);
        }

    }

    private void showFormAddData(Country country) {
        AlertDialog.Builder formPopupBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.countr_layout, null);

        EditText countryNameInput = view.findViewById(R.id.country_name_input);
        EditText populationInput = view.findViewById(R.id.population_input);
        Button saveButton = view.findViewById(R.id.save_button);

        if (country != null) {
            countryNameInput.setText(country.getCountryName());
            populationInput.setText(String.valueOf(country.getPopulation()));

        }

        formPopupBuilder.setView(view);

        AlertDialog popupWindow = formPopupBuilder.create();
        popupWindow.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String countryName = countryNameInput.getText().toString();
                String population = populationInput.getText().toString();

                Country country = new Country(countryName, Long.parseLong(population));

                db.addCountry(country);

                loadDataCountry();

                popupWindow.dismiss();


            }
        });


    }

    @Override
    public void onDeleteCountry(int position) {
        Country countrySelect = countryList.get(position);
        db.deleteCountry(countrySelect);
        loadDataCountry();
    }

    @Override
    public void onEditCountry(int position) {
        Country countrySelect = countryList.get(position);
        showFormEditData(countrySelect);
    }

    private void showFormEditData(Country country) {
        AlertDialog.Builder formPopupBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.countr_layout, null);

        EditText countryNameInput = view.findViewById(R.id.country_name_input);
        EditText populationInput = view.findViewById(R.id.population_input);
        Button saveButton = view.findViewById(R.id.save_button);

        countryNameInput.setText(country.getCountryName());
        populationInput.setText(String.valueOf(country.getPopulation()));

        formPopupBuilder.setView(view);

        AlertDialog popupWindow = formPopupBuilder.create();
        popupWindow.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String countryName = countryNameInput.getText().toString();
                String population = populationInput.getText().toString();

                country.setCountryName(countryName);
                country.setPopulation(Long.parseLong(population));

                db.updateCountry(country);

                loadDataCountry();
                popupWindow.dismiss();
            }
        });
    }
}