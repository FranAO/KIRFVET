package com.example.kirfvet.utils;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kirfvet.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Formulario extends AppCompatActivity {

    // Componentes UI
    private TextInputEditText etFechaAtencion;
    private TextInputLayout tilFechaAtencion;
    private Spinner spinnerNombreMascota, spinnerNombreEspecie, spinnerVacuna;
    private Button btnRegistrarMascota, btnAgregarMascota, btnCancelar;
    private TextInputLayout tilNombreMascota, tilNombreEspecie, tilVacuna;
    private TextInputLayout tilNuevoNombreMascota, tilNuevaEspecie, tilEdad;
    private TextInputEditText etNuevoNombreMascota, etNuevaEspecie, etEdad;

    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formulario);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.svMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initComponents();
        setupListeners();
        setupSpinners();
    }

    private void initComponents() {
        // Vinculación de vistas
        bindViews();
        setupDatePickers();
    }

    private void bindViews() {
        // Sección Atención Veterinaria
        spinnerNombreMascota = findViewById(R.id.spinnerNombreMascota);
        spinnerNombreEspecie = findViewById(R.id.spinnerNombreEspecie);
        spinnerVacuna = findViewById(R.id.spinnerVacuna);
        btnRegistrarMascota = findViewById(R.id.btnRegistrarMascota);
        tilNombreMascota = findViewById(R.id.tilNombreMascota);
        tilNombreEspecie = findViewById(R.id.tilNombreEspecie);
        tilVacuna = findViewById(R.id.tilVacuna);
        etFechaAtencion = findViewById(R.id.etFechaAtencion);
        tilFechaAtencion = findViewById(R.id.tilFechaAtencion);

        // Sección Nueva Mascota
        etNuevoNombreMascota = findViewById(R.id.etNuevoNombreMascota);
        etNuevaEspecie = findViewById(R.id.etNuevaEspecie);
        etEdad = findViewById(R.id.etEdad);
        btnAgregarMascota = findViewById(R.id.btnAgregarMascota);
        tilNuevoNombreMascota = findViewById(R.id.tilNuevoNombreMascota);
        tilNuevaEspecie = findViewById(R.id.tilNuevaEspecie);
        tilEdad = findViewById(R.id.tilEdad);

        // Botón Cancelar
        btnCancelar = findViewById(R.id.btnCancelar);
    }

    private void setupDatePickers() {
        etFechaAtencion.setFocusable(false);
        etFechaAtencion.setOnClickListener(v -> mostrarDatePicker(etFechaAtencion));
    }

    private void setupSpinners() {
        // Configurar spinner de especies
        List<String> especies = new ArrayList<>();
        especies.add("Perro");
        especies.add("Gato");
        especies.add("Ave");
        especies.add("Otro");

        ArrayAdapter<String> especiesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                especies
        );
        especiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNombreEspecie.setAdapter(especiesAdapter);

        // Configurar spinner de vacunas
        List<String> vacunas = new ArrayList<>();
        vacunas.add("Rabia");
        vacunas.add("Parvovirus");
        vacunas.add("Triple Felina");
        vacunas.add("Leucemia Felina");
        vacunas.add("Otra");

        ArrayAdapter<String> vacunasAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                vacunas
        );
        vacunasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVacuna.setAdapter(vacunasAdapter);
    }

    private void mostrarDatePicker(TextInputEditText editText) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    String format = "dd/MM/yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                    editText.setText(sdf.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void setupListeners() {
        btnRegistrarMascota.setOnClickListener(v -> registrarAtencion());
        btnAgregarMascota.setOnClickListener(v -> agregarNuevaMascota());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void registrarAtencion() {
        if (!validarAtencion()) return;

        Toast.makeText(this, "Atención registrada exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
    }

    private boolean validarAtencion() {
        boolean valido = true;

        if (spinnerNombreMascota.getSelectedItem() == null) {
            tilNombreMascota.setError("Seleccione una mascota");
            valido = false;
        }

        if (spinnerNombreEspecie.getSelectedItem() == null) {
            tilNombreEspecie.setError("Seleccione una especie");
            valido = false;
        }

        if (spinnerVacuna.getSelectedItem() == null) {
            tilVacuna.setError("Seleccione una vacuna");
            valido = false;
        }

        String fecha = etFechaAtencion.getText().toString().trim();
        if (fecha.isEmpty()) {
            tilFechaAtencion.setError("Seleccione una fecha");
            valido = false;
        } else {
            try {
                new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fecha);
            } catch (Exception e) {
                tilFechaAtencion.setError("Formato inválido (dd/MM/yyyy)");
                valido = false;
            }
        }

        return valido;
    }

    private void agregarNuevaMascota() {
        if (!validarNuevaMascota()) return;

        Toast.makeText(this, "Mascota agregada exitosamente", Toast.LENGTH_SHORT).show();
        limpiarCamposMascota();
    }

    private boolean validarNuevaMascota() {
        boolean valido = true;

        if (etNuevoNombreMascota.getText().toString().trim().isEmpty()) {
            tilNuevoNombreMascota.setError("Nombre obligatorio");
            valido = false;
        }

        if (etNuevaEspecie.getText().toString().trim().isEmpty()) {
            tilNuevaEspecie.setError("Especie obligatoria");
            valido = false;
        }

        if (etEdad.getText().toString().trim().isEmpty()) {
            tilEdad.setError("Edad obligatoria");
            valido = false;
        }

        return valido;
    }

    private void limpiarCamposMascota() {
        etNuevoNombreMascota.setText("");
        etNuevaEspecie.setText("");
        etEdad.setText("");
    }
}