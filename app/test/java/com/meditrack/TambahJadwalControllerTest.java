package com.meditrack;

import com.meditrack.model.Jadwal;
import com.meditrack.dao.JadwalDAO;
import javafx.embed.swing.JFXPanel;  // Untuk inisialisasi JavaFX
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;


import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TambahJadwalControllerTest {

    private TambahJadwalController controller;

    @BeforeEach
    void setUp() {
        controller = new TambahJadwalController() {
            @Override
            protected void showAlert(Alert.AlertType type, String title, String content) {
                // Override biar gak error di test headless
                System.out.println("Dummy Alert: " + title + " - " + content);
            }
        };

        // Inisialisasi fields agar tidak null
        controller.activityNameField = new TextField();
        controller.startDatePicker = new DatePicker(LocalDate.now());
        controller.endDatePicker = new DatePicker(LocalDate.now());
        controller.startHourSpinner = new Spinner<>(0, 23, 0);
        controller.startMinuteSpinner = new Spinner<>(0, 59, 0);
        controller.endHourSpinner = new Spinner<>(0, 23, 0);
        controller.endMinuteSpinner = new Spinner<>(0, 59, 0);
    }

    @Test
    @DisplayName("Test isInputValid() gagal jika nama aktivitas kosong")
    void testIsInputValid_NamaKosong() {
        controller.activityNameField.setText("");  // Kosong
        assertFalse(controller.isInputValid(), "Validasi harus gagal jika nama aktivitas kosong");
    }

    @Test
    @DisplayName("Test setInitialDateTime() mengatur tanggal dengan benar")
    void testSetInitialDateTime() {
        LocalDate today = LocalDate.now();
        controller.setInitialDateTime(today);

        assertEquals(today, controller.startDatePicker.getValue(), "Tanggal mulai harus sama dengan hari ini");
        assertEquals(today, controller.endDatePicker.getValue(), "Tanggal selesai harus sama dengan hari ini");
    }

    @Test
    @DisplayName("Test isInputValid() gagal jika nama aktivitas kosong")
    void testIsInputValidFailsWhenActivityNameEmpty() {
        controller.activityNameField.setText("");
        boolean result = controller.isInputValid();
        assertFalse(result, "Input validasi gagal jika nama aktivitas kosong");
    }

    @Test
    @DisplayName("Test setEditMode() mengisi data dengan benar")
    void testSetEditMode() {
        Jadwal dummyJadwal = new Jadwal(
                1,
                "Dummy Aktivitas",
                LocalDate.now(),
                LocalTime.of(9, 0),
                LocalDate.now(),
                LocalTime.of(10, 0),
                "Tinggi",
                "Catatan Dummy"
        );

        controller.setEditMode(dummyJadwal);

        assertEquals("Dummy Aktivitas", controller.activityNameField.getText());
        assertEquals(LocalDate.now(), controller.startDatePicker.getValue());
        assertEquals(9, controller.startHourSpinner.getValueFactory().getValue());
        assertEquals(0, controller.startMinuteSpinner.getValueFactory().getValue());
        assertEquals("Tinggi", controller.activityCategoryChoiceBox.getValue());
    }
}
