package com.PayWise.paywise;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import DataBase.Conexion;
import Entity.Deuda;

public class CrearDeuda extends AppCompatActivity {

    private EditText id, empresa, monto, fecha, hora;
    private Spinner tipo;
    private ImageButton seleccionarFechaButton, seleccionarHoraButton;
    private Button btnSelectImage;
    private ImageView imageView;
    private byte[] imageByteArray;
    private Deuda deuda = new Deuda();
    private String selecciontipo;
    Calendar calendar = Calendar.getInstance();
    DateTimeFormatter dateFormatterBD = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private static final int REQUEST_CODE_CAMERA = 1;
    private static final int REQUEST_CODE_GALLERY = 2;
    private static final int REQUEST_CODE_PERMISSIONS = 3;

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

        imageView = findViewById(R.id.imageView);
        btnSelectImage = findViewById(R.id.btnImagen);

        btnSelectImage.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS);
                } else {
                    showImagePickerDialog();
                }
            } else {
                showImagePickerDialog();
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
            if (imageByteArray != null) {
                deuda.setImagen(imageByteArray);
            }

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
                if (imageByteArray != null) {
                    values.put("imagen", deuda.getImagen());
                }

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

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);
    }

    private void showImagePickerDialog() {
        String[] options = {"Tomar Foto", "Seleccionar de la Galería"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona una opción");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                openCamera();
            } else {
                openGallery();
            }
        });
        builder.show();
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA && data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
                imageByteArray = convertBitmapToByteArray(photo);
            } else if (requestCode == REQUEST_CODE_GALLERY && data != null) {
                Uri selectedImage = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    imageView.setImageBitmap(bitmap);
                    imageByteArray = convertBitmapToByteArray(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImagePickerDialog();
            } else {
                Toast.makeText(this, "Permisos denegados", Toast.LENGTH_SHORT).show();
            }
        }
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
