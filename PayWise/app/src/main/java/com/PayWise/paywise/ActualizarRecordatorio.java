package com.PayWise.paywise;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;

import java.util.UUID;

import DataBase.Conexion;


public class ActualizarRecordatorio extends AppCompatActivity{

    private Conexion conexion = new Conexion(this);
    private String nombre, fecha, hora, lista, repita, seleccionlista;
    private ImageButton seleccionarFechaButton, seleccionarHoraButton;
    TextView nombreTextView, fechaTextView, horaTextView;
    Spinner listaSpinner;


    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actualizar_recordatorio);

        cursor = conexion.buscarRecordatorioPorNombre(getIntent().getStringExtra("name"));

        int nombreIndex = cursor.getColumnIndex("nombre");
        int fechaIndex = cursor.getColumnIndex("fecha");
        int horaIndex = cursor.getColumnIndex("hora");
        int listaIndex = cursor.getColumnIndex("lista");

        if(cursor.moveToFirst()){
            nombre = cursor.getString(nombreIndex);
            fecha = cursor.getString(fechaIndex);
            hora = cursor.getString(horaIndex);
            lista = cursor.getString(listaIndex);
        }

        nombreTextView = findViewById(R.id.textNombre);
        fechaTextView = findViewById(R.id.textDate);
        horaTextView = findViewById(R.id.TextTime);

        nombreTextView.setText(nombre);
        fechaTextView.setText(fecha);
        horaTextView.setText(hora);

        listaSpinner = findViewById(R.id.spinner_lista);

        ArrayAdapter<CharSequence> listaAdapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_spinner_lista, android.R.layout.simple_spinner_item);
        listaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listaSpinner.setAdapter(listaAdapter);

        if (lista != null) {
            int posicionLista = listaAdapter.getPosition(lista);
            listaSpinner.setSelection(posicionLista);
        }

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

        // Configurar el listener para el Spinner
        listaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el elemento seleccionado
                seleccionlista = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                seleccionlista = (String) parent.getItemAtPosition(0);
            }
        });
    }


    public void eliminarRecordatorio(View view){
        int id=0;
        int idIndex = cursor.getColumnIndex("id");
        if(cursor.moveToFirst()){
            id = cursor.getInt(idIndex);
        }
        conexion.eliminarRecordatorio(id);

        this.irASegundoLayout(view);
    }

    private void mostrarSelectorFecha() {
        final Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int día = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ActualizarRecordatorio.this,
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(ActualizarRecordatorio.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String horaSeleccionada = hourOfDay + ":" + minute;
                        horaTextView.setText(horaSeleccionada);
                    }
                }, hora, minuto, true);
        timePickerDialog.show();
    }

    private String generateKey(){
        return UUID.randomUUID().toString();
    }

    private Data guardarData(String titulo, String detalle, int id_noti){
        return new Data.Builder()
                .putString("titulo", titulo)
                .putString("detalle", detalle)
                .putInt("id_noti", id_noti)
                .build();
    }
    public void irASegundoLayout(View view) {

        String nametext =nombreTextView.getText().toString();
        String datetext = fechaTextView.getText().toString();
        String timetext = horaTextView.getText().toString();
        String listatext = seleccionlista;

        ContentValues values = new ContentValues();
        values.put("nombre", nametext);
        values.put("lista", listatext);
        values.put("fecha", datetext);
        values.put("hora", timetext);
        Conexion conexion = new Conexion(this);
        conexion.actualizarRecordatorio(nombre, values);

        Calendar selectedCalendar = Calendar.getInstance();
        selectedCalendar.set(Calendar.HOUR_OF_DAY, obtenerHora(timetext)); // Establecer la hora
        selectedCalendar.set(Calendar.MINUTE, obtenerMinuto(timetext)); // Establecer los minutos
        selectedCalendar.set(Calendar.SECOND, 0); // Establecer los segundos
        selectedCalendar.set(Calendar.DAY_OF_MONTH, obtenerDia(datetext)); // Establecer el día
        selectedCalendar.set(Calendar.MONTH, obtenerMes(datetext) - 1);

        Long alertTime =  selectedCalendar.getTimeInMillis() - System.currentTimeMillis();
        if(alertTime>0){
            String tag = generateKey();
            int random = (int) (Math.random()*50+1);
            Data data = guardarData(nametext, listatext, random);
            WorkManagerNoti .guardarNoti(alertTime, data, tag);
        }

        Intent intent = new Intent(this, Recordatorios.class);
        startActivity(intent);
    }

    private int obtenerHora(String hora) {
        String[] partes = hora.split(":");
        return Integer.parseInt(partes[0]);
    }

    private int obtenerMinuto(String hora) {
        String[] partes = hora.split(":");
        return Integer.parseInt(partes[1]);
    }

    private int obtenerDia(String fecha) {
        String[] partes = fecha.split("/");
        return Integer.parseInt(partes[0]);
    }

    private int obtenerMes(String fecha) {
        String[] partes = fecha.split("/");
        return Integer.parseInt(partes[1]);
    }

}
