package com.example.listycitylab3;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;

public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);
        void updateCity(int position, City city);
    }
    private AddCityDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }
    static AddCityFragment newInstance (City city, int position) {
        Bundle args = new Bundle();
        args.putSerializable("city", city);
        args.putInt("position", position);
        AddCityFragment fragment = new AddCityFragment();
        fragment. setArguments (args) ;
        return fragment;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        City city = null;
        int position = -1;
        Bundle args = getArguments();
        if (args != null) {
            city = (City) args.getSerializable("city");
            position = args.getInt("position", -1);
            if (city != null) {
                editCityName.setText(city.getName());
                editProvinceName.setText(city.getProvince());
            }
        }
        City finalCity = city;
        int finalPosition = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add/edit a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Okay", (dialog, which) -> {
                            String cityName = editCityName.getText().toString();
                            String provinceName = editProvinceName.getText().toString();
                        if (finalCity != null && finalPosition != -1) {
                        // Update existing city
                            finalCity.setName(cityName);
                            finalCity.setProvince(provinceName);
                            listener.updateCity(finalPosition, finalCity);
                        } else {
                            // Add new city
                            listener.addCity(new City(cityName, provinceName));
                        }
                })
                .create();
    }
}
