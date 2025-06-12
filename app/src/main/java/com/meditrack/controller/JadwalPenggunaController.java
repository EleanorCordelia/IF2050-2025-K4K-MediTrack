package com.meditrack.controller;

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
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    @FXML private Button editActivityButton;
    @FXML private Button backToMenuButton;

    // Class members
    private YearMonth currentYearMonth;
    private LocalDate selectedDate;
    private List<Button> dayButtons = new ArrayList<>();
    public JadwalDAO jadwalDAO;

    @FXML
    private void handleBackToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backToMenuButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("MediTrack - Menu");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka halaman menu: " + e.getMessage());
        }
    }

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

    private String formatMonthYear(YearMonth yearMonth) {
        String month = yearMonth.getMonth().getDisplayName(TextStyle.FULL, new Locale("id", "ID"));
        return month.substring(0, 1).toUpperCase() + month.substring(1) + " " + yearMonth.getYear();
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

    private boolean hasActivitiesOnDate(LocalDate date) {
        try {
            List<Jadwal> activities = jadwalDAO.getJadwalByDate(date);
            return !activities.isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Activity> getActivitiesForDate(LocalDate date) {
        List<Activity> activities = new ArrayList<>();
        try {
            List<Jadwal> jadwalList = jadwalDAO.getJadwalByDate(date);
            for (Jadwal jadwal : jadwalList) {
                Activity activity = new Activity(
                        jadwal.getId(),
                        jadwal.getNamaAktivitas(),
                        jadwal.getTanggalMulai(),
                        jadwal.getWaktuMulai(),
                        jadwal.getTanggalSelesai(),
                        jadwal.getWaktuSelesai(),
                        jadwal.getKategori(),
                        jadwal.getCatatan()
                );
                activities.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activities;
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
            label.setTextFill(Color.BLACK);
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

        Rectangle colorBar = new Rectangle(6, 50, Color.web("#4A90E2"));
        if ("Tinggi".equalsIgnoreCase(activity.getLevel())) {
            colorBar.setFill(Color.web("#D9534F"));
        } else if ("Rendah".equalsIgnoreCase(activity.getLevel())) {
            colorBar.setFill(Color.web("#5CB85C"));
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
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Apakah Anda yakin ingin menghapus jadwal '" + activity.getName() + "'?",
                ButtonType.YES, ButtonType.NO);
        confirmation.setTitle("Konfirmasi Hapus");
        confirmation.setHeaderText(null);

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                System.out.println("Attempting to delete activity with ID: " + activity.getId());

                // AKTIFKAN DEBUG INI:
                jadwalDAO.debugDatabaseStructure(); // Uncomment baris ini

                boolean success = jadwalDAO.deleteJadwal(activity.getId());
                System.out.println("Delete result: " + success);

                if (success) {
                    updateCalendarView();
                    updateActivityListForDate(selectedDate);
                    showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Jadwal berhasil dihapus!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Gagal", "Gagal menghapus jadwal. Data mungkin tidak ditemukan.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus jadwal: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleEditAktivitas() {
        List<Activity> activities = getActivitiesForDate(selectedDate);
        if (activities.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Tidak Ada Aktivitas", "Tidak ada aktivitas pada tanggal yang dipilih untuk diedit.");
            return;
        }

        if (activities.size() == 1) {
            handleEditAktivitas(activities.get(0));
            return;
        }

        ChoiceDialog<Activity> dialog = new ChoiceDialog<>(activities.get(0), activities);
        dialog.setTitle("Pilih Aktivitas");
        dialog.setHeaderText("Pilih aktivitas yang ingin diedit:");
        dialog.setContentText("Aktivitas:");

        Optional<Activity> result = dialog.showAndWait();
        if (result.isPresent()) {
            handleEditAktivitas(result.get());
        }
    }

    private void handleEditAktivitas(Activity activity) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tambahJadwal.fxml"));
            Parent root = loader.load();

            TambahJadwalController controller = loader.getController();

            Jadwal jadwal = new Jadwal();
            jadwal.setId(activity.getId());
            jadwal.setNamaAktivitas(activity.getName());
            jadwal.setTanggalMulai(activity.getStartDate());
            jadwal.setWaktuMulai(activity.getStartTime());
            jadwal.setTanggalSelesai(activity.getEndDate());
            jadwal.setWaktuSelesai(activity.getEndTime());
            jadwal.setKategori(activity.getLevel());
            jadwal.setCatatan(activity.getNotes());

            controller.setEditMode(jadwal);

            Stage stage = new Stage();
            stage.setTitle("Edit Jadwal");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            updateCalendarView();
            updateActivityListForDate(selectedDate);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka form edit jadwal: " + e.getMessage());
        }
    }

    @FXML
    private void handleShowTambahJadwal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tambahJadwal.fxml"));
            Parent root = loader.load();

            TambahJadwalController controller = loader.getController();
            controller.setInitialDateTime(selectedDate);

            Stage stage = new Stage();
            stage.setTitle("Tambah Jadwal");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            updateCalendarView();
            updateActivityListForDate(selectedDate);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Gagal membuka form tambah jadwal: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Activity {
        private Integer id;
        private String name;
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalDate endDate;
        private LocalTime endTime;
        private String level;
        private String notes;

        public Activity(Integer id, String name, LocalDate startDate, LocalTime startTime,
                        LocalDate endDate, LocalTime endTime, String level, String notes) {
            this.id = id;
            this.name = name;
            this.startDate = startDate;
            this.startTime = startTime;
            this.endDate = endDate;
            this.endTime = endTime;
            this.level = level;
            this.notes = notes;
        }

        public Integer getId() { return id; }
        public String getName() { return name; }
        public LocalDate getStartDate() { return startDate; }
        public LocalTime getStartTime() { return startTime; }
        public LocalDate getEndDate() { return endDate; }
        public LocalTime getEndTime() { return endTime; }
        public String getLevel() { return level; }
        public String getNotes() { return notes; }

        @Override
        public String toString() {
            return name + " (" + startTime.format(DateTimeFormatter.ofPattern("HH:mm")) + ")";
        }
    }
}