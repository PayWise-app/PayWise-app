package com.PayWise.paywise;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import java.time.format.DateTimeFormatter;

import DataBase.Conexion;

public class CrearRecordatorio extends AppCompatActivity {

    private EditText name, date, hour, fechaTextView, horaTextView;
    private Spinner lista, repita;
    private ImageButton seleccionarFechaButton, seleccionarHoraButton;
    /*DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_recordatorio);
        name=findViewById(R.id.textNombre);
        date=findViewById(R.id.textDate);
        hour=findViewById(R.id.TextTime);
        lista=findViewById(R.id.spinner_lista);
        repita=findViewById(R.id.spinner_repita);
        fechaTextView = findViewById(R.id.textDate);
        seleccionarFechaButton = findViewById(R.id.FechaButton);

        seleccionarFechaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorFecha();
            }
        });
        fechaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorFecha();
            }
        });
        horaTextView = findViewById(R.id.TextTime);
        seleccionarHoraButton = findViewById(R.id.HoraButton);

        seleccionarHoraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorHora();
            }
        });

        horaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorHora();
            }
        });
    }

    public void irASegundoLayout(View view) {

        String nametext =name.getText().toString();
        String datetext = date.getText().toString();
        String timetext = hour.getText().toString();
        String listatext = lista.getTag().toString();
        String repitetext = repita.getTag().toString();



        ContentValues values = new ContentValues();
        values.put("nombre", nametext);
        values.put("lista", listatext);
        values.put("fecha", datetext);
        values.put("hora", timetext);
        values.put("repetir", repitetext);
        Conexion conexion = null;
        conexion.ingresarDatos(this, values);

        Intent intent = new Intent(this, Recordatorios.class);
        startActivity(intent);
    }

    private void mostrarSelectorFecha() {
        final Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int día = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CrearRecordatorio.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                        fechaTextView.setText(fechaSeleccionada);
                    }
                }, año, mes, día);
        datePickerDialog.show();
    }

    private void mostrarSelectorHora() {
        final Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CrearRecordatorio.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String horaSeleccionada = hourOfDay + ":" + minute;
                        horaTextView.setText(horaSeleccionada);
                    }
                }, hora, minuto, true);
        timePickerDialog.show();
    }

}
