package com.PayWise.paywise;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

import DataBase.Conexion;

public class Recordatorios extends AppCompatActivity {

    private LinearLayout layout;
    private Conexion conexion;
    private ArrayList<String> nombres;
    private Spinner spinner;
    private String seleccionlista="Todos";

    @SuppressLint("UseCompatLoadingForColorStateLists")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordatorios);

        layout = findViewById(R.id.layoutprincipal);
        conexion = new Conexion(this);
        spinner = findViewById(R.id.spinner2);

        // Configurar el adaptador para el Spinner
        ArrayAdapter<CharSequence> adapterlista = ArrayAdapter.createFromResource(this,
                R.array.opciones_spinner_listaRecordatorios, android.R.layout.simple_spinner_item);
        adapterlista.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterlista);

        // Configurar el listener para el Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el elemento seleccionado
                seleccionlista = (String) parent.getItemAtPosition(position);

                // Limpiar los botones existentes antes de generar nuevos
                layout.removeAllViews();

                // Generar los nuevos botones basados en la selección del Spinner
                if (Objects.equals(seleccionlista, "Todos")) {
                    if (!conexion.obtenerNombres().isEmpty()) {
                        nombres = conexion.obtenerNombres();
                        crearBotones(nombres);
                    }
                } else {
                    if (!conexion.obtenerNombresPorLista(seleccionlista).isEmpty()) {
                        nombres = conexion.obtenerNombresPorLista(seleccionlista);
                        crearBotones(nombres);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                seleccionlista = null;
            }
        });

    }

    public void irASegundoLayout(View view) {
        Intent intent = new Intent(this, CrearRecordatorio.class);
        startActivity(intent);
    }

    private void crearBotones(ArrayList<String> nombres) {
        for (String dato : nombres) {
            Button button = new Button(this);
            button.setId(View.generateViewId()); // Genera un ID único para cada botón
            button.setText(dato);
            button.setTextColor(Color.WHITE);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            button.setAllCaps(false);

            // Aplicar las propiedades específicas que deseas
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, // Ancho
                    ViewGroup.LayoutParams.WRAP_CONTENT // Alto
            );
            layoutParams.setMargins(0, 10, 25, 0); // Margen superior de 10dp y margen derecho de 25dp
            button.setLayoutParams(layoutParams);
            button.setBackgroundTintList(getResources().getColorStateList(com.google.android.material.R.color.design_default_color_secondary_variant));// Agregar el botón al layout
            button.setMinHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
            layout.addView(button);
            // Aquí puedes agregar cualquier acción que desees que se realice cuando se haga clic en el botón
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Recordatorios.this, ActualizarRecordatorio.class);
                    intent.putExtra("name", button.getText().toString());
                    startActivity(intent);
                }
            });
        }
    }



}
