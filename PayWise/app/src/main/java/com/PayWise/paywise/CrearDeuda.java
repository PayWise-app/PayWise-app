package com.PayWise.paywise;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import DataBase.Conexion;
import Entity.Deuda;

public class CrearDeuda extends AppCompatActivity {

    private EditText id, empresa, monto, fecha, hora;
    private Spinner tipo;
    private Switch excel;
    private ImageButton seleccionarFechaButton, seleccionarHoraButton;
    private Deuda deuda = new Deuda();
    private String selecciontipo;
    Calendar calendar = Calendar.getInstance();
    DateTimeFormatter dateFormatterBD = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter dateFormatterClass = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_deuda);
        id = findViewById(R.id.editTextNumber);
        empresa = findViewById(R.id.editTextEmpresa);
        monto = findViewById(R.id.editTextNumberDecimal);
        fecha = findViewById(R.id.textDate);
        seleccionarFechaButton = findViewById(R.id.FechaButton);

        seleccionarFechaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorFecha();
            }
        });
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorFecha();
            }
        });
        hora = findViewById(R.id.TextTime);
        seleccionarHoraButton = findViewById(R.id.HoraButton);

        seleccionarHoraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorHora();
            }
        });

        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorHora();
            }
        });

        tipo = findViewById(R.id.spinner);

        // Configurar el adaptador para el Spinner
        ArrayAdapter<CharSequence> adapterlista = ArrayAdapter.createFromResource(this,
                R.array.opciones_spinner_lista, android.R.layout.simple_spinner_item);
        adapterlista.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(adapterlista);

        // Configurar el listener para el Spinner
        tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el elemento seleccionado
                selecciontipo = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selecciontipo = (String) parent.getItemAtPosition(0);
            }
        });
    }

    private void mostrarSelectorFecha() {
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int día = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CrearDeuda.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String fechaSeleccionada = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                        fecha.setText(fechaSeleccionada);
                    }
                }, año, mes, día);
        datePickerDialog.show();
    }

    private void mostrarSelectorHora() {
        int horas = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CrearDeuda.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String horaSeleccionada = String.format("%02d:%02d", hourOfDay, minute);
                        hora.setText(horaSeleccionada);
                    }
                }, horas, minuto, true);
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

        try{
            deuda.setId(id.getText().toString());
            deuda.setEmpresa(empresa.getText().toString());
            deuda.setMonto(Float.parseFloat(monto.getText().toString()));
            deuda.setFecha(LocalDate.parse(fecha.getText().toString(), dateFormatterBD));
            if(!hora.getText().toString().isEmpty()){
                deuda.setHora(LocalTime.parse(hora.getText().toString(), timeFormatter));
            }
            else{
                deuda.setHora(LocalTime.parse("08:00", timeFormatter));
            }

            deuda.setTipo(selecciontipo);
            deuda.setEstado("Por pagar");

            Conexion conexion = new Conexion(this);

            if(conexion.existeId(deuda.getId())){
                mostrarAlerta("Error", "Esta deuda ya existe");
            }
            else{
                ContentValues values = new ContentValues();
                values.put("id", deuda.getId());
                values.put("tipo", deuda.getTipo());
                values.put("empresa", deuda.getEmpresa());
                values.put("monto", deuda.getMonto());
                values.put("fecha", deuda.getFecha().format(dateFormatterBD));
                values.put("hora", deuda.getHora().toString());
                values.put("estado", deuda.getEstado());

                conexion.ingresarDatos(this, values);

                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(Calendar.HOUR_OF_DAY, obtenerHora(deuda.getHora().toString()));
                selectedCalendar.set(Calendar.MINUTE, obtenerMinuto(deuda.getHora().toString()));
                selectedCalendar.set(Calendar.SECOND, 0);
                selectedCalendar.set(Calendar.DAY_OF_MONTH, obtenerDia(deuda.getFecha().format(dateFormatterBD)));
                selectedCalendar.set(Calendar.MONTH, obtenerMes(deuda.getFecha().format(dateFormatterBD)) - 1);

                Long alertTime = selectedCalendar.getTimeInMillis() - System.currentTimeMillis();
                if (alertTime > 0) {
                    String tag = generateKey();
                    int random = (int) (Math.random() * 50 + 1);
                    Data data = guardarData(deuda.getEmpresa()+" - "+deuda.getTipo(), "S/."+deuda.getMonto(), random);
                    WorkManagerNoti.guardarNoti(alertTime, data, tag);
                }

                Intent intent = new Intent(this, Deudas.class);
                startActivity(intent);
            }

        }catch (Exception e){
            mostrarAlerta("Error", "Se requiere llenar todos los campos");
        }

    }

    private void mostrarAlerta(String titulo, String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
