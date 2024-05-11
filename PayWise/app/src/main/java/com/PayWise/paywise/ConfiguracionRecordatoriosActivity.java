package com.example.myapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracionRecordatoriosActivity extends AppCompatActivity {

    private Spinner frecuenciaSpinner;
    private Button guardarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_recordatorios);

        // Vincular elementos de la interfaz de usuario
        frecuenciaSpinner = findViewById(R.id.spinner_frecuencia);
        guardarButton = findViewById(R.id.button_guardar);

        // Configurar clic del botón guardar
        guardarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarConfiguracion();
            }
        });
    }

    private void guardarConfiguracion() {
        // Obtener la frecuencia seleccionada por el usuario
        String frecuenciaSeleccionada = frecuenciaSpinner.getSelectedItem().toString();

        // Obtener una instancia de SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);

        // Editar SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("frecuencia", frecuenciaSeleccionada);

        // Guardar los cambios
        editor.apply();
    }

    private String obtenerFrecuenciaGuardada() {
        SharedPreferences sharedPreferences = getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
        return sharedPreferences.getString("frecuencia", "");
    }

}

