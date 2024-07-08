package com.PayWise.paywise;

import DataBase.Conexion;
import Entity.Deuda;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ActualizarDeuda extends AppCompatActivity {
    private Button actualizarButton, cambiarEstadoButton;
    private Conexion conexion = new Conexion(this);
    Cursor cursor;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private Deuda deuda = new Deuda();
    private TextView empresaTextView, fechaTextView, horaTextView, idTextView, montoTextView;
    private ImageButton seleccionarFechaButton, seleccionarHoraButton;
    public String selecciontipo;
    Spinner tipoSpinner;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actualizar_deuda);

        cursor = conexion.buscarDeudasPorId(getIntent().getStringExtra("id"));

        int idIndex = cursor.getColumnIndex("id");
        int empresaIndex = cursor.getColumnIndex("empresa");
        int montoIndex = cursor.getColumnIndex("monto");
        int fechaIndex = cursor.getColumnIndex("fecha");
        int horaIndex = cursor.getColumnIndex("hora");
        int tipoIndex = cursor.getColumnIndex("tipo");
        int estadoIndex = cursor.getColumnIndex("estado");

        if (cursor.moveToFirst()) {
            deuda.setId(cursor.getString(idIndex));
            deuda.setEmpresa(cursor.getString(empresaIndex));
            deuda.setTipo(cursor.getString(tipoIndex));
            deuda.setMonto(cursor.getFloat(montoIndex));
            deuda.setFecha(LocalDate.parse(cursor.getString(fechaIndex), dateFormatter));
            deuda.setHora(LocalTime.parse(cursor.getString(horaIndex)));
            deuda.setEstado(cursor.getString(estadoIndex));
        }

        idTextView = findViewById(R.id.textNumber);
        empresaTextView = findViewById(R.id.editTextEmpresa);
        montoTextView = findViewById(R.id.editTextNumberDecimal);
        fechaTextView = findViewById(R.id.textDate);
        horaTextView = findViewById(R.id.TextTime);

        idTextView.setText(deuda.getId());
        empresaTextView.setText(deuda.getEmpresa());
        montoTextView.setText(String.valueOf(deuda.getMonto()));
        fechaTextView.setText(deuda.getFecha().format(dateFormatter));
        horaTextView.setText(deuda.getHora().toString());

        tipoSpinner = findViewById(R.id.spinner_lista);

        ArrayAdapter<CharSequence> listaAdapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_spinner_lista, android.R.layout.simple_spinner_item);
        listaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoSpinner.setAdapter(listaAdapter);

        if (!deuda.getTipo().isEmpty()) {
            int posicionLista = listaAdapter.getPosition(deuda.getTipo());
            tipoSpinner.setSelection(posicionLista);
        }

        this.seleccionarFechaButton = findViewById(R.id.FechaButton);

        seleccionarFechaButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mostrarSelectorFecha();
            }
        });
        fechaTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mostrarSelectorFecha();
            }
        });
        horaTextView = findViewById(R.id.TextTime);
        seleccionarHoraButton = findViewById(R.id.HoraButton);
        seleccionarHoraButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mostrarSelectorHora();
            }
        });
        horaTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mostrarSelectorHora();
            }
        });
        tipoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecciontipo = (String) parent.getItemAtPosition(position);
            }

            public void onNothingSelected(AdapterView<?> parent) {
                selecciontipo = (String) parent.getItemAtPosition(0);
            }
        });

        cambiarEstadoButton = findViewById(R.id.btnEstado);
        actualizarButton = findViewById(R.id.btnCheck);

        if ("Por pagar".equals(deuda.getEstado())) {
            cambiarEstadoButton.setVisibility(View.VISIBLE);
            actualizarButton.setVisibility(View.VISIBLE);
        }
        else{
            cambiarEstadoButton.setVisibility(View.GONE);
            actualizarButton.setVisibility(View.GONE);
        }

    }

    public void eliminarRecordatorio(View view) {
        String id = null;
        int idIndex = cursor.getColumnIndex("id");
        if (cursor.moveToFirst()) {
            id = cursor.getString(idIndex);
        }
        conexion.eliminarDeuda(id);
        this.irASegundoLayout(view);
    }

    private void mostrarSelectorFecha() {
        final Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int día = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ActualizarDeuda.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String fechaSeleccionada = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                        fechaTextView.setText(fechaSeleccionada);
                    }
                }, año, mes, día);
        datePickerDialog.show();
    }

    private void mostrarSelectorHora() {
        final Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(ActualizarDeuda.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String horaSeleccionada = String.format("%02d:%02d", hourOfDay, minute);
                        horaTextView.setText(horaSeleccionada);
                    }
                }, hora, minuto, true);
        timePickerDialog.show();
    }

    private String generateKey() {
        return UUID.randomUUID().toString();
    }

    private Data guardarData(String titulo, String detalle, int id_noti) {
        return new Data.Builder()
                .putString("titulo", titulo)
                .putString("detalle", detalle)
                .putInt("id_noti", id_noti)
                .build();
    }

    public void irASegundoLayout(View view) {
        try {
            deuda.setId(idTextView.getText().toString());
            deuda.setEmpresa(empresaTextView.getText().toString());
            deuda.setMonto(Float.parseFloat(montoTextView.getText().toString()));
            deuda.setFecha(LocalDate.parse(fechaTextView.getText().toString(), dateFormatter));
            deuda.setHora(LocalTime.parse(horaTextView.getText().toString()));
            deuda.setTipo(selecciontipo);

            ContentValues values = new ContentValues();

            values.put("id", deuda.getId());
            values.put("tipo", deuda.getTipo());
            values.put("empresa", deuda.getEmpresa());
            values.put("monto", deuda.getMonto());
            values.put("fecha", deuda.getFecha().format(dateFormatter));
            values.put("hora", deuda.getHora().toString());

            conexion.actualizarDeuda(deuda.getId(), values);

            Calendar selectedCalendar = Calendar.getInstance();

            selectedCalendar.set(Calendar.HOUR_OF_DAY, obtenerHora(deuda.getHora().toString()));
            selectedCalendar.set(Calendar.MINUTE, obtenerMinuto(deuda.getHora().toString()));
            selectedCalendar.set(Calendar.SECOND, 0);
            selectedCalendar.set(Calendar.DAY_OF_MONTH, obtenerDia(deuda.getFecha().format(dateFormatter)));
            selectedCalendar.set(Calendar.MONTH, obtenerMes(deuda.getFecha().format(dateFormatter)) - 1);

            Long alertTime = Long.valueOf(selectedCalendar.getTimeInMillis() - System.currentTimeMillis());
            if (alertTime > 0) {
                String tag = generateKey();
                int random = (int) (Math.random()*50+1);
                Data data = guardarData(deuda.getEmpresa()+" - "+deuda.getTipo(), "S/."+deuda.getMonto(), random);
                WorkManagerNoti .guardarNoti(alertTime, data, tag);
            }
            Intent intent = new Intent(this, Deudas.class);
            startActivity(intent);
        }catch (Exception e){
            mostrarAlerta("Error", "Se requiere llenar todos los campos");
        }
    }

    public void cambiarEstado(View view) {
        deuda.setEstado("Pagado");
        ContentValues values = new ContentValues();
        values.put("estado", deuda.getEstado());
        conexion.actualizarDeuda(deuda.getId(), values);
        irASegundoLayout(view);
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