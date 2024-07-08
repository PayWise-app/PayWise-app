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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import DataBase.Conexion;
import Entity.Deuda;

public class Deudas extends AppCompatActivity {

    private LinearLayout layout;
    private Conexion conexion;
    private ArrayList<Deuda> deudas;
    private Spinner spinner;
    private String selecciontipo="Todos";

    @SuppressLint("UseCompatLoadingForColorStateLists")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deudas);

        layout = findViewById(R.id.layoutprincipal);
        conexion = new Conexion(this);
        spinner = findViewById(R.id.spinner2);

        // Configurar el adaptador para el Spinner
        ArrayAdapter<CharSequence> adapterlista = ArrayAdapter.createFromResource(this,
                R.array.opciones_spinner_listaRecordatorios, android.R.layout.simple_spinner_item);
        adapterlista.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterlista);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selecciontipo = (String) parent.getItemAtPosition(position);

                layout.removeAllViews();

                if (Objects.equals(selecciontipo, "Todos")) {
                    if (!conexion.obtenerIds().isEmpty()) {
                        deudas = conexion.obtenerIds();
                        crearBotones(deudas);
                    }
                } else {
                    if (!conexion.obtenerDeudasPorMes(String.valueOf(position)).isEmpty()) {
                        deudas = conexion.obtenerDeudasPorMes(String.valueOf(position));
                        crearBotones(deudas);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selecciontipo = null;
            }
        });

    }

    public void irASegundoLayout(View view) {
        Intent intent = new Intent(this, CrearDeuda.class);
        startActivity(intent);
    }

    private void crearBotones(ArrayList<Deuda> deudas) {
        for (Deuda deuda : deudas) {
            Button button = new Button(this);
            button.setId(View.generateViewId()); // Genera un ID único para cada botón
            button.setText(deuda.getEmpresa() + " - " + deuda.getTipo());
            button.setTag(deuda.getId());
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            button.setAllCaps(false);

            // Aplicar las propiedades específicas que deseas
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, // Ancho
                    ViewGroup.LayoutParams.WRAP_CONTENT // Alto
            );
            layoutParams.setMargins(0, 10, 25, 0); // Margen superior de 10dp y margen derecho de 25dp
            button.setLayoutParams(layoutParams);

            LocalDate inicioSemana = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate finSemana = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
            if (deuda.getEstado().equals("Pagado")) {
                button.setTextColor(Color.WHITE);
                button.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_green_dark));
            } else if (deuda.getFecha().isBefore(LocalDate.now())) {
                button.setTextColor(Color.WHITE);
                button.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_red_dark));
            } else if (deuda.getFecha().isBefore(finSemana) && deuda.getFecha().isAfter(inicioSemana)) {
                button.setTextColor(Color.BLACK);
                button.setBackgroundTintList(getResources().getColorStateList(android.R.color.holo_orange_light));
            } else {
                button.setTextColor(Color.WHITE);
                button.setBackgroundTintList(getResources().getColorStateList(android.R.color.black));
            }
            button.setMinHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
            layout.addView(button);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Deudas.this, ActualizarDeuda.class);
                    intent.putExtra("id", button.getTag().toString());
                    startActivity(intent);
                }
            });
        }
    }



}