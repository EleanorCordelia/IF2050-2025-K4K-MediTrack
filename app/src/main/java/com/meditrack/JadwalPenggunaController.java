package com.meditrack;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JadwalPenggunaController implements Initializable {

    // FXML-injected components
    @FXML private Label monthYearLabel;
    @FXML private Button prevMonthButton;
    @FXML private Button nextMonthButton;
    @FXML private GridPane calendarGrid;
    @FXML private Label selectedDateActivitiesLabel;
    @FXML private VBox activityListContainer;
    @FXML private Button addActivityButton;
    @FXML private Button editActivityButton;

    // Class members
    private YearMonth currentYearMonth;
    private LocalDate selectedDate;
    private List<Button> dayButtons = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedDate = LocalDate.now();
        currentYearMonth = YearMonth.from(selectedDate);

        prevMonthButton.setOnAction(event -> navigateMonth(-1));
        nextMonthButton.setOnAction(event -> navigateMonth(1));

        updateCalendarView();
        updateActivityListForDate(selectedDate);
    }

    private void navigateMonth(int monthsToAdd) {
        currentYearMonth = currentYearMonth.plusMonths(monthsToAdd);
        updateCalendarView();
    }

    /**
     * Membangun ulang dan menampilkan seluruh view kalender untuk bulan yang sedang aktif.
     * Termasuk indikator titik untuk hari-hari yang memiliki jadwal.
     */
    private void updateCalendarView() {
        monthYearLabel.setText(formatMonthYear(currentYearMonth));
        calendarGrid.getChildren().clear();
        dayButtons.clear();

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int firstDayOfWeekColumn = firstOfMonth.getDayOfWeek().getValue() % 7;

        int daysInMonth = currentYearMonth.lengthOfMonth();
        LocalDate today = LocalDate.now();

        int row = 0;
        int col = firstDayOfWeekColumn;
        for (int day = 1; day <= daysInMonth; day++) {
            final LocalDate date = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(), day);

            // Konten di dalam sel hari (Angka dan Indikator)
            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            VBox dayCellContent = new VBox(dayLabel);
            dayCellContent.setAlignment(Pos.TOP_CENTER);
            dayCellContent.setPadding(new Insets(5,0,0,0));
            dayCellContent.setSpacing(3);

            // --- FITUR INTERAKTIF: INDIKATOR TITIK ---
            if (!getActivitiesForDate(date).isEmpty()) {
                Circle activityIndicator = new Circle(4, Color.web("#10217D"));
                dayCellContent.getChildren().add(activityIndicator);
            }

            Button dayButton = new Button();
            dayButton.setGraphic(dayCellContent);
            dayButton.setPrefSize(120, 90); // Ukuran sel agar lebih lega
            dayButton.getStyleClass().add("calendar-day-button"); // Untuk styling via CSS

            // --- LOGIKA KLIK ---
            dayButton.setOnAction(event -> {
                selectedDate = date;
                updateActivityListForDate(date);
                highlightSelectedDay(dayButton);
            });

            // Styling default, hari ini, dan hari yang dipilih
            if (date.equals(today)) {
                dayButton.setStyle("-fx-background-color: #EBF8FF; -fx-background-radius: 8; -fx-border-color: #90CDF4; -fx-border-radius: 8;");
            } else {
                dayButton.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #E2E8F0; -fx-border-radius: 8;");
            }
            if (date.equals(selectedDate)) {
                highlightSelectedDay(dayButton); // Highlight tanggal yang dipilih saat render ulang
            }


            calendarGrid.add(dayButton, col, row);
            dayButtons.add(dayButton);

            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Memberi highlight visual pada tombol hari yang diklik.
     */
    private void highlightSelectedDay(Button clickedButton) {
        LocalDate today = LocalDate.now();
        for (Button button : dayButtons) {
            // Reset semua tombol ke style default atau style 'hari ini'
            int day = Integer.parseInt(((Label)((VBox)button.getGraphic()).getChildren().get(0)).getText());
            LocalDate buttonDate = currentYearMonth.atDay(day);
            if (buttonDate.equals(today)) {
                button.setStyle("-fx-background-color: #EBF8FF; -fx-background-radius: 8; -fx-border-color: #90CDF4; -fx-border-radius: 8;");
            } else {
                button.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #E2E8F0; -fx-border-radius: 8;");
            }
        }
        // Terapkan style 'terpilih' pada tombol yang diklik
        clickedButton.setStyle("-fx-background-color: #10217D; -fx-background-radius: 8; -fx-border-color: #10217D; -fx-border-radius: 8;");
        // Ubah warna teks di dalamnya menjadi putih
        ((Label)((VBox)clickedButton.getGraphic()).getChildren().get(0)).setTextFill(Color.WHITE);
    }

    /**
     * Memperbarui daftar aktivitas di sidebar kanan dengan transisi fade.
     */
    private void updateActivityListForDate(LocalDate date) {
        // --- FITUR INTERAKTIF: TRANSISI FADE ---
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), activityListContainer);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            selectedDateActivitiesLabel.setText("Pada " + date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) + ", Carlos akan:");
            activityListContainer.getChildren().clear();

            List<Activity> activities = getActivitiesForDate(date);

            if (activities.isEmpty()) {
                Label noActivityLabel = new Label("(Tidak ada aktivitas terjadwal)");
                noActivityLabel.setStyle("-fx-text-fill: #94A3B8; -fx-font-style: italic;");
                activityListContainer.getChildren().add(noActivityLabel);
            } else {
                for (Activity activity : activities) {
                    activityListContainer.getChildren().add(createActivityCard(activity));
                }
            }

            FadeTransition fadeIn = new FadeTransition(Duration.millis(200), activityListContainer);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }

    /**
     * Membuat satu kartu (card) visual untuk sebuah aktivitas.
     * Termasuk efek hover, tooltip, dan menu klik kanan.
     */
    private HBox createActivityCard(Activity activity) {
        HBox card = new HBox();
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #F8FAFC; -fx-background-radius: 10; -fx-border-color: #E2E8F0; -fx-border-radius: 10;");
        card.setPadding(new Insets(12, 15, 12, 0)); // Padding di kanan, atas, bawah
        card.setSpacing(12);

        // Baris warna di kiri sebagai indikator
        Rectangle colorBar = new Rectangle(6, 50, Color.web("#4A90E2")); // Biru
        if (activity.getLevel().contains("Tinggi")) {
            colorBar.setFill(Color.web("#D9534F")); // Merah
        } else if (activity.getLevel().contains("Rendah")) {
            colorBar.setFill(Color.web("#5CB85C")); // Hijau
        }

        // VBox untuk detail teks
        VBox textDetails = new VBox(3);
        Label nameLabel = new Label(activity.getName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #334155;");
        Label timeLabel = new Label(activity.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + activity.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B;");
        Label levelLabel = new Label(activity.getLevel());
        levelLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #94A3B8;");
        textDetails.getChildren().addAll(nameLabel, timeLabel, levelLabel);

        card.getChildren().addAll(colorBar, textDetails);

        // --- FITUR INTERAKTIF: HOVER & TOOLTIP ---
        DropShadow shadow = new DropShadow(8, Color.rgb(0,0,0,0.1));
        card.setOnMouseEntered(e -> {
            card.setEffect(shadow);
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #94A3B8; -fx-border-radius: 10;");
        });
        card.setOnMouseExited(e -> {
            card.setEffect(null);
            card.setStyle("-fx-background-color: #F8FAFC; -fx-background-radius: 10; -fx-border-color: #E2E8F0; -fx-border-radius: 10;");
        });

        if (activity.getNotes() != null && !activity.getNotes().isEmpty()) {
            Tooltip tooltip = new Tooltip("Catatan: " + activity.getNotes());
            tooltip.setStyle("-fx-font-size: 12px;");
            Tooltip.install(card, tooltip);
        }

        // --- FITUR INTERAKTIF: MENU KLIK KANAN ---
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit Aktivitas");
        MenuItem deleteItem = new MenuItem("Hapus Aktivitas");

        editItem.setOnAction(e -> handleEditAktivitas());
        deleteItem.setOnAction(e -> {
            // Logika Hapus:
            // 1. Tampilkan dialog konfirmasi
            // 2. Jika "OK", hapus data aktivitas dari sumbernya (misal: database/list)
            // 3. Panggil updateActivityListForDate(selectedDate); untuk refresh UI
            showAlert(Alert.AlertType.INFORMATION, "Simulasi", "Aktivitas '" + activity.getName() + "' akan dihapus.");
            updateActivityListForDate(selectedDate); // Refresh
        });

        contextMenu.getItems().addAll(editItem, deleteItem);
        card.setOnContextMenuRequested(event -> contextMenu.show(card, event.getScreenX(), event.getScreenY()));

        return card;
    }

    /**
     * Menyediakan data jadwal dummy untuk demonstrasi.
     */
    private List<Activity> getActivitiesForDate(LocalDate date) {
        // (Isi method ini sama seperti sebelumnya, tidak perlu diubah)
        List<Activity> activities = new ArrayList<>();
        if (date.isEqual(LocalDate.of(2025, 6, 23))) {
            activities.add(new Activity("Capstone Project I", date, LocalTime.of(7, 0), date, LocalTime.of(10, 0), "Aktivitas Sedang", "Mengerjakan bab 3."));
            activities.add(new Activity("Kelas DRPL", date, LocalTime.of(16, 0), date, LocalTime.of(17, 0), "Aktivitas Sedang", ""));
        } else if (date.isEqual(LocalDate.of(2025, 6, 24))) {
            activities.add(new Activity("Rapat Tim Meditrack", date, LocalTime.of(9, 0), date, LocalTime.of(11, 0), "Aktivitas Tinggi", "Persiapan presentasi akhir."));
        } else if (date.isEqual(LocalDate.now())) {
            activities.add(new Activity("Olahraga Pagi", LocalDate.now(), LocalTime.of(7, 0), LocalDate.now(), LocalTime.of(8, 0), "Aktivitas Tinggi", "Lari 5km"));
            activities.add(new Activity("Mengerjakan Tugas", LocalDate.now(), LocalTime.of(10, 0), LocalDate.now(), LocalTime.of(12, 30), "Aktivitas Sedang", "Tugas RPL dan PBO"));
            activities.add(new Activity("Istirahat/Makan Siang", LocalDate.now(), LocalTime.of(12, 30), LocalDate.now(), LocalTime.of(13, 30), "Aktivitas Rendah", ""));
        }
        return activities;
    }

    private String formatMonthYear(YearMonth yearMonth) {
        return yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
    }

    @FXML
    private void handleEditAktivitas() {
        showAlert(Alert.AlertType.INFORMATION, "Fitur Dalam Pengembangan", "Fitur Edit Aktivitas akan diimplementasikan untuk mengubah aktivitas yang ada.");
    }

    @FXML
    private void handleShowTambahJadwal() {
        // (Logika untuk menampilkan pop-up Tambah Jadwal tetap sama)
        // Disarankan untuk menggunakan path absolut jika file FXML ada di folder resources
        try {
            // 1. Muat file FXML untuk pop-up
            // Pastikan path ini benar sesuai struktur proyek Anda
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tambahJadwal.fxml"));
            Parent root = loader.load();

            // 2. Dapatkan controller dari pop-up untuk mengirim data
            TambahJadwalController tambahJadwalController = loader.getController();

            // 3. Kirim tanggal yang sedang dipilih di kalender ke pop-up
            // Ini akan mengisi form 'Tambah Jadwal' dengan tanggal yang relevan
            tambahJadwalController.setInitialDateTime(selectedDate);

            // 4. Konfigurasi dan tampilkan Stage (jendela) baru sebagai pop-up
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Ini membuat jendela utama tidak bisa diklik selama pop-up terbuka
            popupStage.initOwner(addActivityButton.getScene().getWindow()); // Pop-up ini 'milik' jendela utama
            popupStage.setTitle("Tambah Jadwal Aktivitas Baru");

            Scene scene = new Scene(root);
            popupStage.setScene(scene);
            popupStage.setResizable(false); // Agar ukuran pop-up tidak bisa diubah

            // Tampilkan pop-up dan tunggu sampai ditutup
            popupStage.showAndWait();

            // 5. Setelah pop-up ditutup (baik disimpan atau dibatalkan), refresh tampilan di jendela utama
            // Ini penting agar jadwal baru langsung muncul di daftar
            System.out.println("Pop-up Tambah Jadwal ditutup, me-refresh tampilan...");
            updateActivityListForDate(selectedDate);
            updateCalendarView(); // Juga refresh kalender untuk menampilkan indikator titik baru

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Memuat Form", "Gagal memuat file /fxml/tambahJadwal.fxml. Pastikan file ada dan tidak rusak.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Tak Terduga", "Terjadi kesalahan: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Kelas inner statis untuk merepresentasikan data aktivitas.
     */
    public static class Activity {
        // (Isi kelas Activity ini sama seperti sebelumnya, tidak perlu diubah)
        private String name;
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalDate endDate;
        private LocalTime endTime;
        private String level;
        private String notes;

        public Activity(String name, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String level, String notes) {
            this.name = name;
            this.startDate = startDate;
            this.startTime = startTime;
            this.endDate = endDate;
            this.endTime = endTime;
            this.level = level;
            this.notes = notes;
        }

        public String getName() { return name; }
        public LocalTime getStartTime() { return startTime; }
        public LocalTime getEndTime() { return endTime; }
        public String getLevel() { return level; }
        public String getNotes() { return notes; }
    }
}