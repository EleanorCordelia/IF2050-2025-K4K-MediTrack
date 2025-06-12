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
    @FXML public VBox mainContainer;
    @FXML public HBox headerBox;
    @FXML public TextField activityNameField;
    @FXML public DatePicker startDatePicker;
    @FXML public Spinner<Integer> startHourSpinner;
    @FXML public Spinner<Integer> startMinuteSpinner;
    @FXML public DatePicker endDatePicker;
    @FXML public Spinner<Integer> endHourSpinner;
    @FXML public Spinner<Integer> endMinuteSpinner;
    @FXML public ChoiceBox<String> activityCategoryChoiceBox;
    @FXML public TextArea notesTextArea;
    @FXML public Button saveButton;
    @FXML public Button cancelButton;
    @FXML public Label labelWaktuMulai, labelWaktuSelesai, labelKategori, labelCatatan;
    //</editor-fold>

    private JadwalDAO jadwalDAO;
    private Integer editingActivityId;

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

    private void setInitialValues() {
        LocalDate today = LocalDate.now();
        startDatePicker.setValue(today);
        endDatePicker.setValue(today);
        
        LocalTime now = LocalTime.now();
        LocalTime nextHour = now.plusHours(1).withMinute(0);
        
        startHourSpinner.getValueFactory().setValue(now.getHour());
        startMinuteSpinner.getValueFactory().setValue(now.getMinute());
        endHourSpinner.getValueFactory().setValue(nextHour.getHour());
        endMinuteSpinner.getValueFactory().setValue(nextHour.getMinute());
        
        activityCategoryChoiceBox.setValue("Sedang");
    }
    
    private void setupListeners() {
        startDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null && endDatePicker.getValue() != null && newDate.isAfter(endDatePicker.getValue())) {
                endDatePicker.setValue(newDate);
            }
        });
        
        endDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null && startDatePicker.getValue() != null && newDate.isBefore(startDatePicker.getValue())) {
                startDatePicker.setValue(newDate);
            }
        });
    }

    public void setInitialDateTime(LocalDate date) {
        if (date != null) {
            if (startDatePicker == null) startDatePicker = new DatePicker();
            if (endDatePicker == null) endDatePicker = new DatePicker();
            if (startHourSpinner == null) startHourSpinner = new Spinner<>(0, 23, 0);
            if (startMinuteSpinner == null) startMinuteSpinner = new Spinner<>(0, 59, 0);
            if (endHourSpinner == null) endHourSpinner = new Spinner<>(0, 23, 0);
            if (endMinuteSpinner == null) endMinuteSpinner = new Spinner<>(0, 59, 0);

            startDatePicker.setValue(date);
            endDatePicker.setValue(date);

            LocalTime now = LocalTime.now();
            LocalTime nextHour = now.plusHours(1).withMinute(0);
            startHourSpinner.getValueFactory().setValue(now.getHour());
            startMinuteSpinner.getValueFactory().setValue(now.getMinute());
            endHourSpinner.getValueFactory().setValue(nextHour.getHour());
            endMinuteSpinner.getValueFactory().setValue(nextHour.getMinute());
        }
    }

    public void setEditMode(Jadwal jadwal) {
        if (jadwal != null) {
            if (activityNameField == null) activityNameField = new TextField();
            if (startDatePicker == null) startDatePicker = new DatePicker();
            if (startHourSpinner == null) startHourSpinner = new Spinner<>(0, 23, 0);
            if (startMinuteSpinner == null) startMinuteSpinner = new Spinner<>(0, 59, 0);
            if (endDatePicker == null) endDatePicker = new DatePicker();
            if (endHourSpinner == null) endHourSpinner = new Spinner<>(0, 23, 0);
            if (endMinuteSpinner == null) endMinuteSpinner = new Spinner<>(0, 59, 0);
            if (activityCategoryChoiceBox == null) activityCategoryChoiceBox = new ChoiceBox<>();
            if (notesTextArea == null) notesTextArea = new TextArea();

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
    
    @FXML
    private void handleSave() {
        try {
            if (!validateInput()) {
                return;
            }
            
            initializeDAO();
            
            String namaAktivitas = activityNameField.getText().trim();
            LocalDate tanggalMulai = startDatePicker.getValue();
            LocalTime waktuMulai = LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue());
            LocalDate tanggalSelesai = endDatePicker.getValue();
            LocalTime waktuSelesai = LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue());
            String kategori = activityCategoryChoiceBox.getValue();
            String catatan = notesTextArea.getText().trim();
            
            Jadwal jadwal = new Jadwal();
            jadwal.setNamaAktivitas(namaAktivitas);
            jadwal.setTanggalMulai(tanggalMulai);
            jadwal.setWaktuMulai(waktuMulai);
            jadwal.setTanggalSelesai(tanggalSelesai);
            jadwal.setWaktuSelesai(waktuSelesai);
            jadwal.setKategori(kategori);
            jadwal.setCatatan(catatan);
            
            if (editingActivityId != null) {
                jadwal.setId(editingActivityId);
                jadwalDAO.updateJadwal(jadwal);
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Jadwal berhasil diperbarui!");
            } else {
                jadwalDAO.addJadwal(jadwal);
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Jadwal berhasil ditambahkan!");
            }
            
            closeWindow();
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan jadwal: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleCancel() {
        closeWindow();
    }
    
    private boolean validateInput() {
        if (activityNameField.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Nama aktivitas tidak boleh kosong!");
            return false;
        }
        
        if (startDatePicker.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Tanggal mulai harus dipilih!");
            return false;
        }
        
        if (endDatePicker.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Tanggal selesai harus dipilih!");
            return false;
        }
        
        if (activityCategoryChoiceBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Kategori aktivitas harus dipilih!");
            return false;
        }
        
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        LocalTime startTime = LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue());
        LocalTime endTime = LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue());
        
        if (startDate.isAfter(endDate)) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Tanggal mulai tidak boleh setelah tanggal selesai!");
            return false;
        }
        
        if (startDate.equals(endDate) && startTime.isAfter(endTime)) {
            showAlert(Alert.AlertType.WARNING, "Input Tidak Valid", "Waktu mulai tidak boleh setelah waktu selesai!");
            return false;
        }
        
        return true;
    }
    
    private void initializeDAO() throws SQLException {
        if (jadwalDAO == null) {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:meditrack.db");
            jadwalDAO = new JadwalDAO(conn);
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}