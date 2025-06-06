package com.meditrack;

import com.meditrack.dao.JadwalDAO;
import com.meditrack.model.Jadwal;
import com.meditrack.util.SQLiteConnection;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    // Class members
    private YearMonth currentYearMonth;
    private LocalDate selectedDate;
    private List<Button> dayButtons = new ArrayList<>();
    private JadwalDAO jadwalDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Connection conn = SQLiteConnection.getConnection();
            jadwalDAO = new JadwalDAO(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Koneksi Gagal", "Tidak dapat terhubung ke database.");
        }

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
            Label dayLabel = new Label(String.valueOf(day));
            dayLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            VBox dayCellContent = new VBox(dayLabel);
            dayCellContent.setAlignment(Pos.TOP_CENTER);
            dayCellContent.setPadding(new Insets(5, 0, 0, 0));
            dayCellContent.setSpacing(3);

            if (hasActivitiesOnDate(date)) {
                Circle activityIndicator = new Circle(4, Color.web("#10217D"));
                dayCellContent.getChildren().add(activityIndicator);
            }

            Button dayButton = new Button();
            dayButton.setGraphic(dayCellContent);
            dayButton.setPrefSize(120, 90);
            dayButton.getStyleClass().add("calendar-day-button");

            dayButton.setOnAction(event -> {
                selectedDate = date;
                updateActivityListForDate(date);
                highlightSelectedDay(dayButton);
            });

            if (date.equals(today)) {
                dayButton.setStyle("-fx-background-color: #EBF8FF; -fx-background-radius: 8; -fx-border-color: #90CDF4; -fx-border-radius: 8;");
            } else {
                dayButton.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #E2E8F0; -fx-border-radius: 8;");
            }
            if (date.equals(selectedDate)) {
                highlightSelectedDay(dayButton);
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

    private void highlightSelectedDay(Button clickedButton) {
        LocalDate today = LocalDate.now();
        for (Button button : dayButtons) {
            VBox content = (VBox) button.getGraphic();
            Label label = (Label) content.getChildren().get(0);
            int day = Integer.parseInt(label.getText());
            LocalDate buttonDate = currentYearMonth.atDay(day);

            if (buttonDate.equals(today)) {
                button.setStyle("-fx-background-color: #EBF8FF; -fx-background-radius: 8; -fx-border-color: #90CDF4; -fx-border-radius: 8;");
            } else {
                button.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-border-color: #E2E8F0; -fx-border-radius: 8;");
            }
            label.setTextFill(Color.BLACK); // Reset warna teks ke hitam
        }

        clickedButton.setStyle("-fx-background-color: #10217D; -fx-background-radius: 8; -fx-border-color: #10217D; -fx-border-radius: 8;");
        Label clickedLabel = (Label) ((VBox) clickedButton.getGraphic()).getChildren().get(0);
        clickedLabel.setTextFill(Color.WHITE);
    }

    private void updateActivityListForDate(LocalDate date) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), activityListContainer);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            selectedDateActivitiesLabel.setText("Pada " + date.format(DateTimeFormatter.ofPattern("dd MMMM uuuu")) + ", Anda akan:");
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

    private HBox createActivityCard(Activity activity) {
        HBox card = new HBox(12);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #F8FAFC; -fx-background-radius: 10; -fx-border-color: #E2E8F0; -fx-border-radius: 10;");
        card.setPadding(new Insets(12, 15, 12, 0));

        Rectangle colorBar = new Rectangle(6, 50, Color.web("#4A90E2")); // Biru (Sedang)
        if ("Tinggi".equalsIgnoreCase(activity.getLevel())) {
            colorBar.setFill(Color.web("#D9534F")); // Merah
        } else if ("Rendah".equalsIgnoreCase(activity.getLevel())) {
            colorBar.setFill(Color.web("#5CB85C")); // Hijau
        }

        VBox textDetails = new VBox(3);
        Label nameLabel = new Label(activity.getName());
        nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #334155;");
        Label timeLabel = new Label(activity.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) + " - " + activity.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B;");
        textDetails.getChildren().addAll(nameLabel, timeLabel);

        card.getChildren().addAll(colorBar, textDetails);

        DropShadow shadow = new DropShadow(8, Color.rgb(0, 0, 0, 0.1));
        card.setOnMouseEntered(e -> {
            card.setEffect(shadow);
            card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10; -fx-border-color: #94A3B8; -fx-border-radius: 10;");
        });
        card.setOnMouseExited(e -> {
            card.setEffect(null);
            card.setStyle("-fx-background-color: #F8FAFC; -fx-background-radius: 10; -fx-border-color: #E2E8F0; -fx-border-radius: 10;");
        });

        if (activity.getNotes() != null && !activity.getNotes().isEmpty()) {
            Tooltip.install(card, new Tooltip("Catatan: " + activity.getNotes()));
        }

        ContextMenu contextMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Ubah Aktivitas");
        MenuItem deleteItem = new MenuItem("Hapus Aktivitas");

        editItem.setOnAction(e -> handleEditAktivitas(activity));
        deleteItem.setOnAction(e -> handleDeleteActivity(activity));

        contextMenu.getItems().addAll(editItem, deleteItem);
        card.setOnContextMenuRequested(event -> contextMenu.show(card, event.getScreenX(), event.getScreenY()));

        return card;
    }

    private void handleDeleteActivity(Activity activity) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Apakah Anda yakin ingin menghapus jadwal '" + activity.getName() + "'?", ButtonType.YES, ButtonType.NO);
        confirmation.setTitle("Konfirmasi Hapus");
        confirmation.setHeaderText(null);
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                jadwalDAO.deleteJadwal(activity.getId());
                updateActivityListForDate(selectedDate);
                updateCalendarView(); // Refresh kalender untuk menghilangkan titik indikator
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Gagal Menghapus", "Terjadi kesalahan saat menghapus data dari database.");
            }
        }
    }

    // ✅ DITAMBAHKAN KEMBALI: Method untuk handle edit yang hilang
    private void handleEditAktivitas(Activity activity) {
        try {
            // Ambil data Jadwal lengkap dari database menggunakan ID
            Optional<Jadwal> jadwalToEdit = jadwalDAO.getJadwalById(activity.getId());

            if (jadwalToEdit.isPresent()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tambahJadwal.fxml"));
                Parent root = loader.load();

                TambahJadwalController controller = loader.getController();
                // Kirim objek Jadwal (bukan Activity) ke mode edit
                controller.setEditMode(jadwalToEdit.get());

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.initOwner(activityListContainer.getScene().getWindow());
                popupStage.setTitle("Ubah Jadwal Aktivitas");
                popupStage.setScene(new Scene(root));
                popupStage.setResizable(false);
                popupStage.showAndWait();

                // Setelah popup ditutup, cukup refresh tampilan.
                // Logika update ke DB sudah ditangani di dalam TambahJadwalController.
                updateActivityListForDate(selectedDate);
                updateCalendarView();
            } else {
                showAlert(Alert.AlertType.ERROR, "Data Tidak Ditemukan", "Aktivitas yang akan diubah tidak ditemukan di database.");
                // Mungkin data sudah dihapus, refresh saja
                updateActivityListForDate(selectedDate);
                updateCalendarView();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Memuat Form", "Gagal memuat file /fxml/tambahJadwal.fxml.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Database", "Gagal mengambil data untuk diubah.");
        }
    }

    private List<Activity> getActivitiesForDate(LocalDate date) {
        List<Activity> activities = new ArrayList<>();
        try {
            List<Jadwal> jadwals = jadwalDAO.getJadwalByDate(date);
            for (Jadwal jadwal : jadwals) {
                activities.add(new Activity(
                        jadwal.getId(),
                        jadwal.getNamaAktivitas(),
                        jadwal.getTanggalMulai(),
                        jadwal.getWaktuMulai(),
                        jadwal.getTanggalSelesai(),
                        jadwal.getWaktuSelesai(),
                        jadwal.getKategori(),
                        jadwal.getCatatan()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Gagal Mengambil Data", "Tidak dapat mengambil data jadwal dari database.");
        }
        return activities;
    }

    private boolean hasActivitiesOnDate(LocalDate date) {
        try {
            return jadwalDAO.hasJadwalOnDate(date);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String formatMonthYear(YearMonth yearMonth) {
        return yearMonth.format(DateTimeFormatter.ofPattern("MMMM uuuu"));
    }

    @FXML
    private void handleShowTambahJadwal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tambahJadwal.fxml"));
            Parent root = loader.load();
            TambahJadwalController tambahJadwalController = loader.getController();
            tambahJadwalController.setInitialDateTime(selectedDate);

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(addActivityButton.getScene().getWindow());
            popupStage.setTitle("Tambah Jadwal Aktivitas Baru");
            popupStage.setScene(new Scene(root));
            popupStage.setResizable(false);
            popupStage.showAndWait();

            // Setelah pop-up ditutup, refresh view
            updateActivityListForDate(selectedDate);
            updateCalendarView();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Memuat Form", "Gagal memuat file /fxml/tambahJadwal.fxml.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Kelas inner untuk Activity di-update untuk menyimpan ID
    public static class Activity {
        private final int id;
        private final String name;
        private final LocalDate startDate;
        private final LocalTime startTime;
        private final LocalDate endDate;
        private final LocalTime endTime;
        private final String level;
        private final String notes;

        public Activity(int id, String name, LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String level, String notes) {
            this.id = id;
            this.name = name;
            this.startDate = startDate;
            this.startTime = startTime;
            this.endDate = endDate;
            this.endTime = endTime;
            this.level = level;
            this.notes = notes;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public LocalDate getStartDate() { return startDate; }
        public LocalTime getStartTime() { return startTime; }
        public LocalDate getEndDate() { return endDate; }
        public LocalTime getEndTime() { return endTime; }
        public String getLevel() { return level; }
        public String getNotes() { return notes; }
    }

    @FXML
    private void handleEditAktivitas() {
        showAlert(Alert.AlertType.INFORMATION, "Edit Aktivitas", "Silakan pilih aktivitas yang ingin diedit.");
    }



}