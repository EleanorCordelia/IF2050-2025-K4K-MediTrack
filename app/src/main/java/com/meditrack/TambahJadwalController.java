package com.meditrack;

import com.meditrack.dao.JadwalDAO;
import com.meditrack.model.Jadwal;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class TambahJadwalController implements Initializable {

    //<editor-fold desc="Konstanta untuk Styling">
    // Mendefinisikan warna sebagai konstanta agar mudah diubah dan konsisten
    private static final String PRIMARY_COLOR = "#4682B4"; // SteelBlue
    private static final String PRIMARY_COLOR_HOVER = "#3572A5";
    private static final String SECONDARY_COLOR = "#F0F0F0";
    private static final String SECONDARY_COLOR_HOVER = "#E0E0E0";
    private static final String BORDER_COLOR = "#CCCCCC";
    private static final String BORDER_FOCUS_COLOR = "#4A90E2";
    private static final String FONT_COLOR_DARK = "#333333";
    private static final String FONT_COLOR_LIGHT = "#FFFFFF";
    private static final String INPUT_BG_COLOR = "#F9F9F9";
    //</editor-fold>

    //<editor-fold desc="FXML Declarations">
    @FXML private VBox mainContainer;
    @FXML private HBox headerBox;
    @FXML private TextField activityNameField;
    @FXML private DatePicker startDatePicker;
    @FXML private Spinner<Integer> startHourSpinner;
    @FXML private Spinner<Integer> startMinuteSpinner;
    @FXML private DatePicker endDatePicker;
    @FXML private Spinner<Integer> endHourSpinner;
    @FXML private Spinner<Integer> endMinuteSpinner;
    @FXML private ChoiceBox<String> activityCategoryChoiceBox;
    @FXML private TextArea notesTextArea;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label labelWaktuMulai, labelWaktuSelesai, labelKategori, labelCatatan;
    //</editor-fold>

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        applyStyles(); // Menerapkan semua styling secara terprogram
        setupControls();
        setInitialValues();
        setupListeners();
    }

    /**
     * Metode ini bertanggung jawab untuk menerapkan semua styling ke elemen UI
     * secara terprogram menggunakan Java.
     */
    private void applyStyles() {
        // Gaya untuk container utama
        mainContainer.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-border-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        // Gaya untuk header
        headerBox.setStyle("-fx-border-color: #E0E0E0; -fx-border-width: 0 0 1 0;");
        headerBox.setPadding(new Insets(0, 20, 0, 20));

        // Gaya untuk judul
        activityNameField.setStyle("-fx-background-color: transparent; -fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: " + FONT_COLOR_DARK + ";");

        // Gaya untuk semua label section
        String labelStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #555555; -fx-padding: 15 0 5 0;";
        labelWaktuMulai.setStyle(labelStyle);
        labelWaktuSelesai.setStyle(labelStyle);
        labelKategori.setStyle(labelStyle);
        labelCatatan.setStyle(labelStyle);

        // Gaya untuk input fields (DatePicker, Spinner, ChoiceBox, TextArea)
        String inputStyle = "-fx-background-color: " + INPUT_BG_COLOR + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-radius: 8; -fx-background-radius: 8; -fx-font-size: 14px;";
        String inputFocusStyle = "-fx-border-color: " + BORDER_FOCUS_COLOR + ";";

        applyFocusableNodeStyle(startDatePicker, inputStyle, inputFocusStyle);
        applyFocusableNodeStyle(startHourSpinner, inputStyle, inputFocusStyle);
        applyFocusableNodeStyle(startMinuteSpinner, inputStyle, inputFocusStyle);
        applyFocusableNodeStyle(endDatePicker, inputStyle, inputFocusStyle);
        applyFocusableNodeStyle(endHourSpinner, inputStyle, inputFocusStyle);
        applyFocusableNodeStyle(endMinuteSpinner, inputStyle, inputFocusStyle);
        applyFocusableNodeStyle(activityCategoryChoiceBox, inputStyle, inputFocusStyle);
        applyFocusableNodeStyle(notesTextArea, inputStyle, inputFocusStyle);

        // Gaya untuk Tombol Simpan (Primary)
        String saveButtonStyle = "-fx-background-radius: 8; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand; -fx-pref-height: 40px; -fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: " + FONT_COLOR_LIGHT + ";";
        String saveButtonHoverStyle = saveButtonStyle + "-fx-background-color: " + PRIMARY_COLOR_HOVER + ";";
        saveButton.setStyle(saveButtonStyle);
        saveButton.setOnMouseEntered(e -> saveButton.setStyle(saveButtonHoverStyle));
        saveButton.setOnMouseExited(e -> saveButton.setStyle(saveButtonStyle));

        // Gaya untuk Tombol Batal (Secondary)
        String cancelButtonStyle = "-fx-background-radius: 8; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand; -fx-pref-height: 40px; -fx-background-color: " + SECONDARY_COLOR + "; -fx-text-fill: " + FONT_COLOR_DARK + "; -fx-border-color: " + BORDER_COLOR + "; -fx-border-width: 1;";
        String cancelButtonHoverStyle = cancelButtonStyle + "-fx-background-color: " + SECONDARY_COLOR_HOVER + ";";
        cancelButton.setStyle(cancelButtonStyle);
        cancelButton.setOnMouseEntered(e -> cancelButton.setStyle(cancelButtonHoverStyle));
        cancelButton.setOnMouseExited(e -> cancelButton.setStyle(cancelButtonStyle));
    }

    // Helper untuk menerapkan style fokus pada elemen
    private void applyFocusableNodeStyle(Control control, String baseStyle, String focusStyle) {
        control.setStyle(baseStyle);
        control.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                control.setStyle(baseStyle + focusStyle);
            } else {
                control.setStyle(baseStyle);
            }
        });
    }

    private void setupControls() {
        activityCategoryChoiceBox.getItems().addAll("Tinggi", "Sedang", "Rendah");
        configureTimeSpinner(startHourSpinner, 0, 23);
        configureTimeSpinner(startMinuteSpinner, 0, 59);
        configureTimeSpinner(endHourSpinner, 0, 23);
        configureTimeSpinner(endMinuteSpinner, 0, 59);
    }

    private void configureTimeSpinner(Spinner<Integer> spinner, int min, int max) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max));
        spinner.setEditable(true); // Pastikan spinner bisa diedit
        StringConverter<Integer> twoDigitConverter = new StringConverter<>() {
            @Override public String toString(Integer value) { return String.format("%02d", value); }
            @Override public Integer fromString(String string) { return Integer.parseInt(string); }
        };
        spinner.getValueFactory().setConverter(twoDigitConverter);
    }

    public void setInitialDateTime(LocalDate date) {
        if (date != null) {
            // Mengatur nilai DatePicker sesuai tanggal yang dikirim
            startDatePicker.setValue(date);
            endDatePicker.setValue(date);

            // Biarkan waktu tetap diatur ke nilai default (jam saat ini)
            LocalTime now = LocalTime.now();
            LocalTime nextHour = now.plusHours(1).withMinute(0);
            startHourSpinner.getValueFactory().setValue(now.getHour());
            startMinuteSpinner.getValueFactory().setValue(now.getMinute());
            endHourSpinner.getValueFactory().setValue(nextHour.getHour());
            endMinuteSpinner.getValueFactory().setValue(nextHour.getMinute());
        }
    }

    public void setInitialData(JadwalPenggunaController.Activity activity) {
        // Set date picker value
        startDatePicker.setValue(activity.getStartDate());

        // Set hour and minute spinners
        startHourSpinner.getValueFactory().setValue(activity.getStartTime().getHour());
        startMinuteSpinner.getValueFactory().setValue(activity.getStartTime().getMinute());

        // Set other fields if needed (like nama aktivitas, kategori, catatan, dll.)
        activityNameField.setText(activity.getName());
        // ... tambahkan field lain sesuai kebutuhan
    }


    private void setInitialValues() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalTime nextHour = now.plusHours(1).withMinute(0);

        startDatePicker.setValue(today);
        endDatePicker.setValue(today);
        activityCategoryChoiceBox.setValue("Aktivitas Sedang");

        startHourSpinner.getValueFactory().setValue(now.getHour());
        startMinuteSpinner.getValueFactory().setValue(now.getMinute());
        endHourSpinner.getValueFactory().setValue(nextHour.getHour());
        endMinuteSpinner.getValueFactory().setValue(nextHour.getMinute());
    }

    private void setupListeners() {
        startDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null && endDatePicker.getValue().isBefore(newDate)) {
                endDatePicker.setValue(newDate);
            }
        });
    }

    @FXML
    private void handleSave() {
        if (!isInputValid()) {
            return;
        }

        // Siapkan objek Jadwal
        Jadwal jadwal = new Jadwal(
                editingActivityId,  // Tambahkan ID di sini (penting untuk UPDATE)
                activityNameField.getText().trim(),
                startDatePicker.getValue(),
                LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue()),
                endDatePicker.getValue(),
                LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue()),
                activityCategoryChoiceBox.getValue(),
                notesTextArea.getText().isEmpty() ? null : notesTextArea.getText()
        );

        String dbURL = "jdbc:sqlite:" + System.getProperty("user.dir") + "/meditrack.db";

        try (Connection connection = DriverManager.getConnection(dbURL)) {
            JadwalDAO jadwalDAO = new JadwalDAO(connection);

            if (editingActivityId != -1) {
                // Mode edit: update data
                jadwalDAO.updateJadwal(jadwal);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Jadwal berhasil diperbarui.");
            } else {
                // Mode tambah: insert data baru
                jadwalDAO.addJadwal(jadwal);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Jadwal berhasil disimpan.");
            }

            closeWindow();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal terhubung atau menyimpan ke file database SQLite.\n\nError: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Terjadi kesalahan tak terduga: " + e.getMessage());
        }
    }

    private boolean isInputValid() {
        String activityName = activityNameField.getText().trim();
        if (activityName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Nama aktivitas tidak boleh kosong.");
            return false;
        }

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        LocalTime startTime = LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue());
        LocalTime endTime = LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue());

        if (startDate.isAfter(endDate) || (startDate.isEqual(endDate) && startTime.isAfter(endTime))) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Waktu mulai tidak boleh setelah waktu selesai.");
            return false;
        }
        return true;
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) mainContainer.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private int editingActivityId = -1;  // Tambahkan field ini di atas, biar bisa dipakai

    public void setEditMode(Jadwal jadwal) {
        if (jadwal != null) {
            activityNameField.setText(jadwal.getNamaAktivitas());
            startDatePicker.setValue(jadwal.getTanggalMulai());
            startHourSpinner.getValueFactory().setValue(jadwal.getWaktuMulai().getHour());
            startMinuteSpinner.getValueFactory().setValue(jadwal.getWaktuMulai().getMinute());
            endDatePicker.setValue(jadwal.getTanggalSelesai());
            endHourSpinner.getValueFactory().setValue(jadwal.getWaktuSelesai().getHour());
            endMinuteSpinner.getValueFactory().setValue(jadwal.getWaktuSelesai().getMinute());
            activityCategoryChoiceBox.setValue(jadwal.getKategori());
            notesTextArea.setText(jadwal.getCatatan());
            this.editingActivityId = jadwal.getId();
        }
    }

    private Jadwal editedJadwal;  // untuk getEditedJadwal()

    public Jadwal getEditedJadwal() {
        return editedJadwal;
    }
}