package com.PayWise.paywise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Recordatorios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recordatorios);

    }

    public void irASegundoLayout(View view) {
        Intent intent = new Intent(this, CrearRecordatorio.class);
        startActivity(intent);
    }


}
