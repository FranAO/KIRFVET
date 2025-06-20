package com.example.kirfvet.pets;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kirfvet.R;
import com.example.kirfvet.main.MainActivity;
import com.example.kirfvet.shop.Tienda;
import com.example.kirfvet.utils.InfoUsuario;
import com.example.kirfvet.AgendarCitas;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarioCitas extends AppCompatActivity {

    private TextView tvMesActual;
    private ImageButton btnMesAnterior, btnMesSiguiente;
    private RecyclerView recyclerCalendario, recyclerCitas;
    private MaterialButton btnAgregarCita;
    private TextView tvFechaSeleccionada;
    private View layoutEstadoVacio;
    private Calendar calendar;
    private SimpleDateFormat monthFormat;
    private SimpleDateFormat dayFormat;
    private int selectedDay = -1;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendario_citas);
        
        // Configurar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        // Inicializar variables
        calendar = Calendar.getInstance();
        monthFormat = new SimpleDateFormat("MMMM yyyy", new Locale("es", "ES"));
        dayFormat = new SimpleDateFormat("d MMM", new Locale("es", "ES"));

        // Ajustar padding del menú de navegación inferior para que se pegue al borde real de la pantalla
        View bottomNav = findViewById(R.id.bottomNavigation);
        ViewCompat.setOnApplyWindowInsetsListener(bottomNav, (v, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            v.setPadding(0, 0, 0, bottomInset);
            return insets;
        });

        // Inicializar vistas
        initializeViews();
        setupListeners();
        updateCalendar();
    }

    private void initializeViews() {
        tvMesActual = findViewById(R.id.tvMesActual);
        btnMesAnterior = findViewById(R.id.btnMesAnterior);
        btnMesSiguiente = findViewById(R.id.btnMesSiguiente);
        recyclerCalendario = findViewById(R.id.recyclerCalendario);
        recyclerCitas = findViewById(R.id.recyclerCitas);
        btnAgregarCita = findViewById(R.id.btnAgregarCita);
        tvFechaSeleccionada = findViewById(R.id.tvFechaSeleccionada);
        layoutEstadoVacio = findViewById(R.id.layoutEstadoVacio);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Marcar el ítem del calendario como seleccionado
        bottomNavigation.setSelectedItemId(R.id.navigation_calendar);

        // Configurar RecyclerViews
        recyclerCalendario.setLayoutManager(new GridLayoutManager(this, 7));
        recyclerCitas.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupListeners() {
        btnMesAnterior.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        btnMesSiguiente.setOnClickListener(v -> {
            calendar.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        btnAgregarCita.setOnClickListener(v -> {
            // TODO: Implementar lógica para agregar cita
            Toast.makeText(this, "Agregar cita", Toast.LENGTH_SHORT).show();
        });

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                android.content.Intent intent = new android.content.Intent(this, MainActivity.class);
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_calendar) {
                return true;
            } else if (itemId == R.id.navigation_cart) {
                android.content.Intent intent = new android.content.Intent(this, Tienda.class);
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_user) {
                android.content.Intent intent = new android.content.Intent(this, InfoUsuario.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_add) {
                android.content.Intent intent = new android.content.Intent(this, AgendarCitas.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Asegurarse de que el ítem de 'calendario' esté seleccionado al volver a esta actividad
        bottomNavigation.setSelectedItemId(R.id.navigation_calendar);
    }

    private void updateCalendar() {
        // Actualizar título del mes
        tvMesActual.setText(monthFormat.format(calendar.getTime()));

        // Obtener días del mes
        List<CalendarDay> days = getDaysInMonth();
        
        // Actualizar RecyclerView del calendario
        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this::onDaySelected);
        recyclerCalendario.setAdapter(calendarAdapter);

        // Si hay un día seleccionado, actualizar la vista de citas
        if (selectedDay != -1) {
            updateAppointmentsView(selectedDay);
        }
    }

    private List<CalendarDay> getDaysInMonth() {
        List<CalendarDay> days = new ArrayList<>();
        Calendar tempCalendar = (Calendar) calendar.clone();
        
        // Ir al primer día del mes
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);
        
        // Agregar días vacíos hasta el primer día de la semana
        int firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i < firstDayOfWeek; i++) {
            days.add(new CalendarDay("", false, false));
        }

        // Agregar los días del mes
        int daysInMonth = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= daysInMonth; i++) {
            boolean isToday = i == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) &&
                    calendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
                    calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR);
            boolean isSelected = i == selectedDay;
            days.add(new CalendarDay(String.valueOf(i), isToday, isSelected));
        }

        return days;
    }

    private void onDaySelected(int day) {
        selectedDay = day;
        updateCalendar();
        updateAppointmentsView(day);
    }

    private void updateAppointmentsView(int day) {
        // Actualizar fecha seleccionada
        Calendar selectedDate = (Calendar) calendar.clone();
        selectedDate.set(Calendar.DAY_OF_MONTH, day);
        tvFechaSeleccionada.setText(dayFormat.format(selectedDate.getTime()));

        // TODO: Cargar citas del día seleccionado
        // Por ahora, mostramos el estado vacío
        layoutEstadoVacio.setVisibility(View.VISIBLE);
        recyclerCitas.setVisibility(View.GONE);
    }

    // Clase para representar un día en el calendario
    private static class CalendarDay {
        String day;
        boolean isToday;
        boolean isSelected;

        CalendarDay(String day, boolean isToday, boolean isSelected) {
            this.day = day;
            this.isToday = isToday;
            this.isSelected = isSelected;
        }
    }

    // TODO: Implementar adaptadores para RecyclerViews
    private class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DayViewHolder> {
        private List<CalendarDay> days;
        private OnDayClickListener listener;

        CalendarAdapter(List<CalendarDay> days, OnDayClickListener listener) {
            this.days = days;
            this.listener = listener;
        }

        @Override
        public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_calendario_dia, parent, false);
            return new DayViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DayViewHolder holder, int position) {
            CalendarDay day = days.get(position);
            holder.tvDia.setText(day.day);
            holder.itemView.setSelected(day.isSelected);
            
            if (!day.day.isEmpty()) {
                holder.itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onDayClick(Integer.parseInt(day.day));
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return days.size();
        }

        class DayViewHolder extends RecyclerView.ViewHolder {
            TextView tvDia;

            DayViewHolder(View itemView) {
                super(itemView);
                tvDia = itemView.findViewById(R.id.tvDia);
            }
        }
    }

    interface OnDayClickListener {
        void onDayClick(int day);
    }
}