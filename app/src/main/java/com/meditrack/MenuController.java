package com.meditrack;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent; // Ditambahkan untuk onMouseClicked
import javafx.scene.layout.AnchorPane; // Ditambahkan
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;      // Ditambahkan
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

public class MenuController implements Initializable {

    //<editor-fold desc="FXML Declarations for Sidebar and Topbar">
    @FXML private VBox sidebar;
    @FXML private Button menuButton;
    @FXML private Button rekomendasiButton;
    @FXML private Button obatButton;
    @FXML private Button laporanButton;
    @FXML private Button konsultasiButton;
    @FXML private Button jadwalButton;
    @FXML private Button pengaturanButton;
    @FXML private Button logoutButton;
    //</editor-fold>

    //<editor-fold desc="FXML Declarations for Main Content StackPane and Views">
    @FXML private StackPane mainContentStackPane;
    @FXML private AnchorPane dashboardViewPane;
    @FXML private VBox rekomendasiViewPane;
    @FXML private VBox obatViewPane;
    @FXML private VBox laporanViewPane;
    @FXML private VBox konsultasiViewPane;
    @FXML private VBox jadwalViewPane;
    @FXML private VBox pengaturanViewPane;
    //</editor-fold>

    //<editor-fold desc="FXML Declarations for Dashboard View Elements">
    @FXML private Text greetingText;
    @FXML private Button unduhRingkasanButton; // Pastikan fx:id ini ada di FXML
    @FXML private Label gejalaTerkiniValue;
    @FXML private Label asupanNutrisiValue;
    @FXML private Label statusTubuhValue;
    @FXML private Label levelAktivitasValue;
    @FXML private LineChart<String, Number> healthChart;
    @FXML private ListView<JadwalItem> jadwalListView;
    @FXML private Label detakJantungValue;
    @FXML private Label stresValue;
    @FXML private Label tidurValue;
    @FXML private Button generateRekomendasiButton; // Pastikan fx:id ini ada di FXML
    @FXML private TilePane kategoriObatPane;
    @FXML private TilePane konsultasiCepatPane;
    @FXML private HBox dokterListContainer;
    @FXML private ImageView testimonialImageView;
    @FXML private Label testimonialQuoteLabel;
    @FXML private Label testimonialAuthorLabel;
    @FXML private Label testimonialAuthorRoleLabel;
    @FXML private Button prevTestimonialButton;
    @FXML private Button nextTestimonialButton;
    @FXML private HBox founderListContainer;
    //</editor-fold>

    //<editor-fold desc="FXML Declarations for Specific View Elements">
    @FXML private ListView<String> obatListView;
    @FXML private LineChart<String, Number> laporanHealthChart;
    @FXML private TextField cariDokterField;
    @FXML private ListView<String> konsultasiDokterListView;
    @FXML private ListView<JadwalItem> jadwalDetailListView;
    @FXML private TextField namaPenggunaField;
    @FXML private TextField emailField;
    @FXML private CheckBox notifikasiCheckBox;
    //</editor-fold>

    private List<Pane> allContentViews;
    private List<Testimonial> testimonials;
    private int currentTestimonialIndex = 0;
    private Button currentActiveButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allContentViews = Arrays.asList(
                dashboardViewPane, rekomendasiViewPane, obatViewPane, laporanViewPane,
                konsultasiViewPane, jadwalViewPane, pengaturanViewPane
        );

        currentActiveButton = menuButton;
        setActiveButton(menuButton);
        showView(dashboardViewPane);

        populateDashboardData();
        loadTestimonials();
        displayTestimonial();
        populateFounderList();

        populateObatListView();
        populateLaporanChart();
        populateKonsultasiDokterListView();
        populateJadwalDetailListView();
    }

    private void populateDashboardData() {
        gejalaTerkiniValue.setText("Pusing");
        asupanNutrisiValue.setText("15%");
        statusTubuhValue.setText("Perlu Perhatian");
        levelAktivitasValue.setText("Sibuk");
        detakJantungValue.setText("84 BPM");
        stresValue.setText("Tinggi");
        tidurValue.setText("3.5 Jam");

        populateHealthChart();
        populateKategoriObat();
        populateKonsultasiCepat();
        populateDokterList();
        populateJadwalRingkasanListView();
    }

    private void populateHealthChart() {
        XYChart.Series<String, Number> seriesKualitasTidur = new XYChart.Series<>();
        seriesKualitasTidur.setName("Kualitas Tidur");
        seriesKualitasTidur.getData().addAll(
                new XYChart.Data<>("Jan", 200), new XYChart.Data<>("Feb", 220), new XYChart.Data<>("Mar", 180),
                new XYChart.Data<>("Apr", 250), new XYChart.Data<>("Mei", 230), new XYChart.Data<>("Jun", 260)
        );

        XYChart.Series<String, Number> seriesTingkatStress = new XYChart.Series<>();
        seriesTingkatStress.setName("Tingkat Stress");
        seriesTingkatStress.getData().addAll(
                new XYChart.Data<>("Jan", 150), new XYChart.Data<>("Feb", 160), new XYChart.Data<>("Mar", 190),
                new XYChart.Data<>("Apr", 170), new XYChart.Data<>("Mei", 200), new XYChart.Data<>("Jun", 180)
        );

        XYChart.Series<String, Number> seriesTingkatHidrasi = new XYChart.Series<>();
        seriesTingkatHidrasi.setName("Tingkat Hidrasi");
        seriesTingkatHidrasi.getData().addAll(
                new XYChart.Data<>("Jan", 300), new XYChart.Data<>("Feb", 280), new XYChart.Data<>("Mar", 310),
                new XYChart.Data<>("Apr", 290), new XYChart.Data<>("Mei", 320), new XYChart.Data<>("Jun", 300)
        );
        if (healthChart != null) { // Null check
            healthChart.getData().clear();
            healthChart.getData().addAll(seriesKualitasTidur, seriesTingkatStress, seriesTingkatHidrasi);
            healthChart.setLegendVisible(false);
        }
    }

    private Image loadImage(String relativePath) {
        URL imageUrl = getClass().getResource(relativePath);
        if (imageUrl == null) {
            System.err.println("Cannot load image: " + relativePath + ". Using dummy.png as fallback.");
            imageUrl = getClass().getResource("/images/dummy.png");
            if (imageUrl == null) {
                System.err.println("Critical error: dummy.png not found at /images/dummy.png");
                return null;
            }
        }
        return new Image(imageUrl.toExternalForm());
    }


    private void populateKategoriObat() {
        if (kategoriObatPane == null) return;
        kategoriObatPane.getChildren().clear();
        String[] kategoriNames = {"Vitamin", "Minuman Bernutrisi", "Perawatan Kulit", "Kebugaran", "Homeopati"};
        String placeholderIconPath = "/images/dummy.png";

        for (String kategoriName : kategoriNames) {
            VBox card = new VBox(8);
            card.setAlignment(Pos.CENTER);
            card.setStyle("-fx-padding: 15px; -fx-background-color: white; -fx-background-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 8, 0, 0, 2); -fx-pref-width: 180px; -fx-pref-height: 220px; -fx-cursor: hand;");

            ImageView iconView = new ImageView(loadImage(placeholderIconPath));
            iconView.setFitHeight(120);
            iconView.setFitWidth(120);
            iconView.setPreserveRatio(true);

            Label nameLabel = new Label(kategoriName);
            nameLabel.setStyle("-fx-font-family: 'Poppins SemiBold'; -fx-font-size: 14px; -fx-text-fill: #10217D; -fx-text-alignment: center; -fx-wrap-text: true;");

            card.setOnMouseClicked(event -> handleKategoriObatClick(kategoriName));
            card.getChildren().addAll(iconView, nameLabel);
            kategoriObatPane.getChildren().add(card);
        }
    }

    @FXML
    private void handleJadwalButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/jadwalPengguna.fxml"));
            Parent jadwalRoot = loader.load();

            // (Optional) kalau kamu mau lewatkan data, kamu bisa ambil controllernya:
            // JadwalPenggunaController controller = loader.getController();
            // controller.setUser(userData); // misalnya set user

            Scene jadwalScene = new Scene(jadwalRoot);
            Stage jadwalStage = new Stage();
            jadwalStage.setTitle("Jadwal Pengguna");
            jadwalStage.setScene(jadwalScene);
            jadwalStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showErrorDialog("Gagal Membuka Jadwal", "Tidak dapat memuat halaman Jadwal Pengguna.");
        }
    }

    private void populateKonsultasiCepat() {
        if (konsultasiCepatPane == null) return;
        konsultasiCepatPane.getChildren().clear();
        String[] konsultasiNames = {"Hati", "Asma", "Paru-paru", "Oksigen", "Diabetes", "Resep"};
        String placeholderIconPath = "/images/dummy.png";
        Color[] circleColors = {
                Color.web("#FFF0F0",0.8), Color.web("#E6FFFA",0.8), Color.web("#FFF7E6",0.8),
                Color.web("#F0F5FF",0.8), Color.web("#E6F7FF",0.8), Color.web("#F9F0FF",0.8)
        };

        for (int i = 0; i < konsultasiNames.length; i++) {
            VBox itemBox = new VBox(8);
            itemBox.setAlignment(Pos.CENTER);
            itemBox.setStyle("-fx-cursor: hand;");

            StackPane stackPane = new StackPane();
            Circle circle = new Circle(38, circleColors[i % circleColors.length]);

            ImageView iconView = new ImageView(loadImage(placeholderIconPath));
            iconView.setFitHeight(35);
            iconView.setFitWidth(35);
            iconView.setPreserveRatio(true);

            stackPane.getChildren().addAll(circle, iconView);
            Label nameLabel = new Label(konsultasiNames[i]);
            nameLabel.setStyle("-fx-font-family: 'Poppins SemiBold'; -fx-font-size: 13px; -fx-text-fill: #333333;");

            final String namaKonsultasi = konsultasiNames[i];
            itemBox.setOnMouseClicked(event -> handleKonsultasiCepatClick(namaKonsultasi));
            itemBox.getChildren().addAll(stackPane, nameLabel);
            konsultasiCepatPane.getChildren().add(itemBox);
        }
    }

    private void populateDokterList() {
        if (dokterListContainer == null) return;
        dokterListContainer.getChildren().clear();
        String dokterFotoPath = "/images/doctor.png";
        List<Dokter> dokters = Arrays.asList(
                new Dokter("Dr. Y K Mishra", "Ahli Bedah Jantung, Jakarta, Indonesia", dokterFotoPath),
                new Dokter("Dr. Sandeep Vaishya", "Ahli Bedah Saraf, Jakarta, Indonesia", dokterFotoPath),
                new Dokter("Dr. Rajeev Verma", "Dokter Bedah Ortopedi, Jakarta, Indonesia", dokterFotoPath)
        );

        for (Dokter dokter : dokters) {
            VBox card = new VBox(8);
            card.setAlignment(Pos.CENTER);
            card.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-background-radius: 8px; -fx-border-color: #DBDDDE; -fx-border-radius: 8px; -fx-pref-width: 190px;");

            ImageView fotoDokter = new ImageView(loadImage(dokter.getFotoPath()));
            fotoDokter.setFitHeight(160);
            fotoDokter.setFitWidth(160);
            fotoDokter.setPreserveRatio(true);
            StyleUtil.applyImageRoundedCorners(fotoDokter, 8);

            Label namaDokter = new Label(dokter.getNama());
            namaDokter.setStyle("-fx-font-family: 'Poppins SemiBold'; -fx-font-size: 14px; -fx-text-fill: #151D48;");

            Label spesialisasiDokter = new Label(dokter.getSpesialisasi());
            spesialisasiDokter.setStyle("-fx-font-family: 'Poppins Regular'; -fx-font-size: 12px; -fx-text-fill: #6C87AE; -fx-min-height: 30px; -fx-wrap-text: true; -fx-text-alignment: center;");
            spesialisasiDokter.setAlignment(Pos.CENTER);

            Button konsultasiBtn = new Button("Konsultasi");
            konsultasiBtn.setStyle("-fx-background-color: white; -fx-text-fill: #10217D; -fx-border-color: #10217D; -fx-border-radius: 4px; -fx-font-family: 'Poppins Bold'; -fx-font-size: 13px; -fx-padding: 8px 15px;");
            konsultasiBtn.setMaxWidth(Double.MAX_VALUE);
            konsultasiBtn.setOnAction(event -> handleKonsultasiDokterAction(dokter));

            card.getChildren().addAll(fotoDokter, namaDokter, spesialisasiDokter, konsultasiBtn);
            dokterListContainer.getChildren().add(card);
        }
    }

    private void populateJadwalRingkasanListView() {
        if (jadwalListView == null) return;
        String placeholderIconPath = "/images/dummy.png";
        ObservableList<JadwalItem> jadwalItems = FXCollections.observableArrayList(
                new JadwalItem("08:00", "Olahraga Pagi", "08:00 - 10:00", placeholderIconPath),
                new JadwalItem("13:00", "Kelas DRPL", "13:00 - 15:00", placeholderIconPath),
                new JadwalItem("20:00", "Kerja Kelompok", "20:00 - 00:00", placeholderIconPath)
        );
        jadwalListView.setItems(jadwalItems);
        jadwalListView.setCellFactory(listView -> new JadwalListCell(this::loadImage));
    }

    private void populateJadwalDetailListView() {
        if (jadwalDetailListView == null) return;
        String placeholderIconPath = "/images/dummy.png";
        ObservableList<JadwalItem> items = FXCollections.observableArrayList(
                new JadwalItem("07:00", "Minum Obat Pagi", "Vitamin C, Paracetamol", placeholderIconPath),
                new JadwalItem("08:00", "Olahraga Pagi", "Lari pagi 30 menit", placeholderIconPath)
        );
        jadwalDetailListView.setItems(items);
        jadwalDetailListView.setCellFactory(listView -> new JadwalListCell(this::loadImage));
    }

    private void populateObatListView() {
        if (obatListView == null) return;
        ObservableList<String> items = FXCollections.observableArrayList(
                "Paracetamol 500mg - Pagi, Siang, Malam",
                "Amoxicillin 250mg - 3x sehari setelah makan"
        );
        obatListView.setItems(items);
    }

    private void populateLaporanChart() {
        if (laporanHealthChart == null) return;
        XYChart.Series<String, Number> seriesBeratBadan = new XYChart.Series<>();
        seriesBeratBadan.setName("Berat Badan (kg)");
        seriesBeratBadan.getData().addAll(
                new XYChart.Data<>("Minggu 1", 70), new XYChart.Data<>("Minggu 2", 69.5),
                new XYChart.Data<>("Minggu 3", 69), new XYChart.Data<>("Minggu 4", 68.5)
        );
        laporanHealthChart.getData().clear();
        laporanHealthChart.getData().add(seriesBeratBadan);
    }

    private void populateKonsultasiDokterListView() {
        if (konsultasiDokterListView == null) return;
        ObservableList<String> dokterOnline = FXCollections.observableArrayList(
                "Dr. Budi (Umum) - Online",
                "Dr. Citra (Anak) - Offline hingga 17:00"
        );
        konsultasiDokterListView.setItems(dokterOnline);
    }

    private void loadTestimonials() {
        if (testimonialImageView == null) return; // Jika elemen FXML tidak ada, jangan lanjutkan
        String placeholderTestimonialImg = "/images/dummy.png";
        testimonials = Arrays.asList(
                new Testimonial(
                        "“MediTrack adalah sebuah platform yang dirancang untuk membantu Anda merasa lebih baik...“",
                        "Anjali Sharma", "MediTrack Customer", placeholderTestimonialImg
                ),
                new Testimonial(
                        "“Platform ini sangat intuitif dan mudah digunakan...“",
                        "Budi Santoso", "Pengguna Aktif", placeholderTestimonialImg
                )
        );
        prevTestimonialButton.setOnAction(e -> showPreviousTestimonial());
        nextTestimonialButton.setOnAction(e -> showNextTestimonial());
    }

    private void displayTestimonial() {
        if (testimonials == null || testimonials.isEmpty() || testimonialImageView == null) return;
        Testimonial current = testimonials.get(currentTestimonialIndex);
        testimonialImageView.setImage(loadImage(current.getImagePath()));
        testimonialQuoteLabel.setText(current.getQuote());
        testimonialAuthorLabel.setText(current.getAuthor());
        testimonialAuthorRoleLabel.setText(current.getRole());
    }

    @FXML
    private void showPreviousTestimonial() {
        currentTestimonialIndex--;
        if (currentTestimonialIndex < 0) {
            currentTestimonialIndex = testimonials.size() - 1;
        }
        displayTestimonial();
    }

    @FXML
    private void showNextTestimonial() {
        currentTestimonialIndex++;
        if (currentTestimonialIndex >= testimonials.size()) {
            currentTestimonialIndex = 0;
        }
        displayTestimonial();
    }

    private void populateFounderList() {
        if (founderListContainer == null) return;
        founderListContainer.getChildren().clear();
        String placeholderFounderImg = "/images/dummy.png";
        List<Founder> founders = Arrays.asList(
                new Founder("Shalihah Ramadhanty Nurhaliza", "Founder", placeholderFounderImg),
                new Founder("Marzel Zhafir Nugroho", "Founder", placeholderFounderImg),
                new Founder("M. Faiz Fadhlurrahman", "Founder", placeholderFounderImg),
                new Founder("Thalita Zahra Sutejo", "Founder", placeholderFounderImg),
                new Founder("Eleanor Cordelia", "Founder", placeholderFounderImg)
        );

        for (Founder founder : founders) {
            VBox card = new VBox(5);
            card.setAlignment(Pos.CENTER);
            card.setStyle("-fx-background-color: white; -fx-padding: 15px; -fx-background-radius: 8px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 3); -fx-pref-width: 190px;");

            ImageView fotoFounder = new ImageView(loadImage(founder.getFotoPath()));
            fotoFounder.setFitHeight(160);
            fotoFounder.setFitWidth(160);
            StyleUtil.applyImageRoundedCorners(fotoFounder, 80);

            Label namaFounder = new Label(founder.getNama());
            namaFounder.setStyle("-fx-font-family: 'Poppins SemiBold'; -fx-font-size: 14px; -fx-text-fill: #151D48;");

            Label roleFounder = new Label(founder.getRole());
            roleFounder.setStyle("-fx-font-family: 'Poppins Regular'; -fx-font-size: 12px; -fx-text-fill: #6C87AE;");

            card.getChildren().addAll(fotoFounder, namaFounder, roleFounder);
            founderListContainer.getChildren().add(card);
        }
    }

    private void showView(Pane viewToShow) {
        allContentViews.forEach(pane -> {
            if (pane != null) {
                pane.setVisible(false);
                pane.setManaged(false);
            }
        });
        if (viewToShow != null) {
            viewToShow.setVisible(true);
            viewToShow.setManaged(true);
        } else {
            System.err.println("Error: Pane yang akan ditampilkan adalah null.");
        }
    }

    @FXML
    private void handleSidebarButton(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();

        // Blok untuk Tombol Logout (sudah benar)
        if (clickedButton == logoutButton) {
            System.out.println("Logout action triggered");
            try {
                URL landingFxmlUrl = getClass().getResource("/fxml/landing.fxml");
                if (landingFxmlUrl == null) {
                    System.err.println("Error: Cannot find /fxml/landing.fxml for logout.");
                    showErrorDialog("Gagal Logout", "Tidak dapat menemukan file landing.fxml.");
                    return;
                }
                Parent landingRoot = FXMLLoader.load(landingFxmlUrl);
                Scene landingScene = new Scene(landingRoot);
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.setScene(landingScene);
                currentStage.setTitle("MediTrack - Selamat Datang");
                currentStage.show();
            } catch (IOException e) {
                System.err.println("IOException during logout navigation:");
                e.printStackTrace();
                showErrorDialog("Gagal Logout", "Tidak dapat kembali ke halaman landing.");
            }
            return; // Keluar dari method setelah logout
        }

        // --- PENAMBAHAN LOGIKA UNTUK TOMBOL JADWAL ---
        // Jika tombol yang diklik adalah tombol Jadwal
        if (clickedButton == jadwalButton) {
            // Pesan ini untuk memastikan kliknya terdeteksi
            System.out.println("Tombol 'Jadwal' diklik, mencoba memuat FXML...");

            try {
                // 1. Dapatkan URL dan cek secara eksplisit
                URL fxmlUrl = getClass().getResource("/fxml/jadwalPengguna.fxml"); // Menggunakan path relatif dari Solusi Cepat
                // JIKA ANDA MENGIKUTI SOLUSI 2 (STRUKTUR FOLDER BARU), GUNAKAN BARIS DI BAWAH INI:
                // URL fxmlUrl = getClass().getResource("/fxml/jadwalPengguna.fxml");

                if (fxmlUrl == null) {
                    // Jika path salah, kita buat error yang jelas SEBELUM FXMLLoader gagal.
                    System.err.println("Error: fxmlUrl adalah null. File tidak ditemukan pada path yang diberikan.");
                    showErrorDialog("File FXML Tidak Ditemukan", "Tidak dapat menemukan 'jadwalPengguna.fxml'. Pastikan file berada di lokasi yang benar dan proyek sudah di-rebuild.");
                    return; // Hentikan eksekusi jika file tidak ada
                }

                System.out.println("URL FXML ditemukan: " + fxmlUrl.toExternalForm());

                // 2. Lanjutkan proses loading
                FXMLLoader loader = new FXMLLoader(fxmlUrl);
                Parent jadwalRoot = loader.load();

                System.out.println("FXML berhasil di-load. Menyiapkan Stage...");

                Scene jadwalScene = new Scene(jadwalRoot);
                Stage jadwalStage = new Stage();
                jadwalStage.setTitle("MediTrack - Jadwal Pengguna");
                // (Opsional) Coba nonaktifkan baris ikon ini untuk sementara jika menyebabkan error
                // jadwalStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));
                jadwalStage.setScene(jadwalScene);

                jadwalStage.show();
                System.out.println("Jendela Jadwal berhasil ditampilkan.");

            } catch (Exception e) {
                // 3. TANGKAP SEMUA JENIS EXCEPTION
                // Ini akan menangkap IOException, IllegalStateException, dll.
                System.err.println("Terjadi error saat mencoba membuka jendela jadwal!");
                e.printStackTrace(); // Cetak error lengkap di console
                showErrorDialog("Error Tak Terduga", "Gagal memuat jendela Jadwal. \n\nError: " + e.getClass().getSimpleName() + "\nMessage: " + e.getMessage());
            }

            // Jangan lanjutkan ke logika di bawah
            return;
        }
        // --- AKHIR DARI LOGIKA TOMBOL JADWAL ---

        // Logika untuk tombol sidebar lainnya (tidak berubah)
        setActiveButton(clickedButton);

        if (clickedButton == menuButton) {
            showView(dashboardViewPane);
        } else if (clickedButton == rekomendasiButton) {
            showView(rekomendasiViewPane);
        } else if (clickedButton == obatButton) {
            showView(obatViewPane);
        } else if (clickedButton == laporanButton) {
            showView(laporanViewPane);
        } else if (clickedButton == konsultasiButton) {
            showView(konsultasiViewPane);
        } else if (clickedButton == pengaturanButton) {
            showView(pengaturanViewPane);
        }
        // Perhatikan: kita tidak perlu lagi `else if (clickedButton == jadwalButton)` di sini
        // karena sudah ditangani di atas.
    }


    private void setActiveButton(Button activeButton) {
        List<Button> sidebarButtons = Arrays.asList(menuButton, rekomendasiButton, obatButton, laporanButton, konsultasiButton, jadwalButton, pengaturanButton);
        for (Button btn : sidebarButtons) {
            if (btn != null) {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #A5B2E4; -fx-font-family: 'Poppins Regular'; -fx-font-size: 15px; -fx-padding: 10px 20px; -fx-alignment: baseline-left; -fx-pref-width: 200px;");
                setButtonIcon(btn, false);
            }
        }
        if (activeButton != null && activeButton != logoutButton) { // Jangan set style aktif untuk logout
            activeButton.setStyle("-fx-background-color: #1751C2; -fx-text-fill: white; -fx-font-family: 'Poppins Medium'; -fx-font-size: 15px; -fx-padding: 10px 20px; -fx-background-radius: 8px; -fx-alignment: baseline-left; -fx-pref-width: 200px;");
            setButtonIcon(activeButton, true);
            currentActiveButton = activeButton;
        }
    }

    private void setButtonIcon(Button button, boolean isActive) {
        if (button == null || button.getGraphic() == null || !(button.getGraphic() instanceof ImageView)) {
            return; // Tidak bisa set ikon jika tombol atau graphicnya null atau bukan ImageView
        }

        String iconName = "";
        String baseIconResourcePath = "";

        if (button == menuButton) { iconName = "rekomendasi"; baseIconResourcePath = "/images/icon-rekomendasi.png";}
        else if (button == rekomendasiButton) { iconName = "rekomendasi"; baseIconResourcePath = "/images/icon-rekomendasi.png";}
        else if (button == obatButton) { iconName = "obat"; baseIconResourcePath = "/images/icon-obat.png";}
        else if (button == laporanButton) { iconName = "laporan"; baseIconResourcePath = "/images/icon-laporan.png";}
        else if (button == konsultasiButton) { iconName = "konsultasi"; baseIconResourcePath = "/images/icon-konsultasi.png";}
        else if (button == jadwalButton) { iconName = "jadwal"; baseIconResourcePath = "/images/icon-jadwal.png";}
        else if (button == pengaturanButton) { iconName = "pengaturan"; baseIconResourcePath = "/images/icon-pengaturan.png";}
        // Logout button icon diatur di FXML atau tidak diubah di sini

        if (baseIconResourcePath.isEmpty()) return;

        String finalIconPath = baseIconResourcePath;

        if (isActive) {
            String activePathAttempt = "/images/icon-" + iconName + "_active.png";
            URL activeUrl = getClass().getResource(activePathAttempt);
            if (activeUrl != null) {
                finalIconPath = activePathAttempt;
            }
        }

        ImageView iconView = (ImageView) button.getGraphic();
        Image newImage = loadImage(finalIconPath);
        if (newImage != null) {
            iconView.setImage(newImage);
        }
    }

    //<editor-fold desc="Action Handlers for FXML">
    @FXML
    private void handleUnduhRingkasanAction(ActionEvent event) {
        System.out.println("Tombol Unduh Ringkasan diklik.");
        showInfoDialog("Unduh Ringkasan", "Fitur unduh ringkasan akan diimplementasikan.");
    }

    @FXML
    private void handleGenerateRekomendasiAction(ActionEvent event) {
        System.out.println("Tombol Generate Rekomendasi diklik.");
        showInfoDialog("Generate Rekomendasi", "Memproses rekomendasi untuk Anda...");
    }

    private void handleKategoriObatClick(String kategori) {
        System.out.println("Kategori Obat diklik: " + kategori);
        showInfoDialog("Kategori Obat", "Anda memilih kategori: " + kategori);
    }

    private void handleKonsultasiCepatClick(String jenisKonsultasi) {
        System.out.println("Konsultasi Cepat diklik: " + jenisKonsultasi);
        showInfoDialog("Konsultasi Cepat", "Anda memilih konsultasi: " + jenisKonsultasi);
    }

    private void handleKonsultasiDokterAction(Dokter dokter) {
        System.out.println("Tombol Konsultasi diklik untuk: " + dokter.getNama());
        showInfoDialog("Konsultasi Dokter", "Memulai sesi konsultasi dengan " + dokter.getNama());
    }

    @FXML
    private void lihatSemuaObat(ActionEvent event) { // Ubah dari MouseEvent ke ActionEvent jika dari Button
        System.out.println("Tombol Lihat Semua Obat diklik.");
        showView(obatViewPane);
        setActiveButton(obatButton);
    }

    @FXML
    private void lihatSemuaKonsultasi(ActionEvent event) { // Ubah dari MouseEvent ke ActionEvent jika dari Button
        System.out.println("Tombol Lihat Semua Konsultasi Cepat diklik.");
        showView(konsultasiViewPane);
        setActiveButton(konsultasiButton);
    }

    @FXML
    private void handleLihatSemuaJadwal(ActionEvent event) {
        System.out.println("Tombol Lihat Semua Jadwal diklik.");
        if (jadwalViewPane != null && jadwalButton != null) {
            showView(jadwalViewPane);
            setActiveButton(jadwalButton);
        } else {
            System.err.println("Error: jadwalViewPane atau jadwalButton belum diinisialisasi.");
        }
    }

    @FXML
    private void handleLihatSemuaKondisi(ActionEvent event) {
        System.out.println("Tombol Lihat Semua Kondisi diklik.");
        showInfoDialog("Kondisi Hari Ini", "Menampilkan detail kondisi hari ini...");
    }

    @FXML
    private void handleMulaiRekomendasiAction(ActionEvent event) {
        System.out.println("Tombol Mulai Rekomendasi (di halaman Rekomendasi) diklik.");
        showInfoDialog("Mulai Rekomendasi", "Sistem akan memandu Anda untuk rekomendasi.");
    }

    @FXML
    private void handleTambahObatAction(ActionEvent event) {
        System.out.println("Tombol Tambah Obat Baru diklik.");
        if (obatListView != null) { // Pastikan obatListView sudah diinisialisasi
            String obatBaru = "Obat Baru Ditambahkan - " + (obatListView.getItems().size() + 1);
            obatListView.getItems().add(obatBaru);
            showInfoDialog("Tambah Obat", obatBaru);
        } else {
            System.err.println("Error: obatListView belum diinisialisasi.");
        }
    }

    @FXML
    private void handleEksporLaporanAction(ActionEvent event) {
        System.out.println("Tombol Ekspor Laporan diklik.");
        showInfoDialog("Ekspor Laporan", "Laporan akan diekspor dalam format PDF (simulasi).");
    }

    @FXML
    private void handleMulaiChatAction(ActionEvent event) {
        if (konsultasiDokterListView != null) { // Pastikan konsultasiDokterListView sudah diinisialisasi
            String dokterTerpilih = konsultasiDokterListView.getSelectionModel().getSelectedItem();
            if (dokterTerpilih == null && !konsultasiDokterListView.getItems().isEmpty()) {
                dokterTerpilih = konsultasiDokterListView.getItems().get(0);
            }
            if (dokterTerpilih != null) {
                System.out.println("Tombol Mulai Chat diklik untuk: " + dokterTerpilih);
                showInfoDialog("Mulai Chat", "Memulai chat dengan " + dokterTerpilih);
            } else {
                showInfoDialog("Mulai Chat", "Silakan pilih dokter terlebih dahulu.");
            }
        } else {
            System.err.println("Error: konsultasiDokterListView belum diinisialisasi.");
        }
    }

    @FXML
    private void handleTambahJadwalAction(ActionEvent event) {
        System.out.println("Tombol Tambah Jadwal Baru diklik.");
        JadwalItem itemBaru = new JadwalItem("Baru", "Aktivitas Baru", "Detail aktivitas baru", "/images/dummy.png");
        if (jadwalDetailListView != null) {
            jadwalDetailListView.getItems().add(itemBaru);
        }
        showInfoDialog("Tambah Jadwal", "Jadwal baru ditambahkan (placeholder).");
    }

    @FXML
    private void handleSimpanPengaturanAction(ActionEvent event) {
        // Pastikan namaPenggunaField, emailField, notifikasiCheckBox sudah di-@FXML dan diinisialisasi
        if (namaPenggunaField != null && emailField != null && notifikasiCheckBox != null) {
            String nama = namaPenggunaField.getText();
            String email = emailField.getText();
            boolean notif = notifikasiCheckBox.isSelected();
            System.out.println("Pengaturan disimpan: Nama=" + nama + ", Email=" + email + ", Notifikasi=" + notif);
            showInfoDialog("Simpan Pengaturan", "Pengaturan telah disimpan.");
        } else {
            System.err.println("Error: Salah satu field pengaturan belum diinisialisasi.");
        }
    }

    @FXML
    private void handleFooterLinkAction(MouseEvent event) { // Jika dari onMouseClicked
        Node source = (Node) event.getSource();
        if (source instanceof Label) {
            Label clickedLabel = (Label) source;
            System.out.println("Footer link diklik: " + clickedLabel.getText());
            showInfoDialog("Navigasi Footer", "Anda mengklik: " + clickedLabel.getText());
        }
    }

    @FXML
    private void handleSocialMediaLinkAction(MouseEvent event) {
        System.out.println("Ikon media sosial diklik.");
        showInfoDialog("Media Sosial", "Membuka halaman media sosial (simulasi).");
    }

    private void showInfoDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    //</editor-fold>

    //<editor-fold desc="Helper Classes">
    private static class Dokter {
        private final String nama;
        private final String spesialisasi;
        private final String fotoPath;

        public Dokter(String nama, String spesialisasi, String fotoPath) {
            this.nama = nama;
            this.spesialisasi = spesialisasi;
            this.fotoPath = fotoPath;
        }
        public String getNama() { return nama; }
        public String getSpesialisasi() { return spesialisasi; }
        public String getFotoPath() { return fotoPath; }
    }

    public static class JadwalItem {
        private final String time;
        private final String title;
        private final String detail;
        private final String iconPath;

        public JadwalItem(String time, String title, String detail, String iconPath) {
            this.time = time;
            this.title = title;
            this.detail = detail;
            this.iconPath = iconPath;
        }
        public String getTime() { return time; }
        public String getTitle() { return title; }
        public String getDetail() { return detail; }
        public String getIconPath() { return iconPath; }
    }

    static class JadwalListCell extends ListCell<JadwalItem> {
        private HBox hbox = new HBox(10);
        private Label timeLabel = new Label();
        private VBox textVBox = new VBox(2);
        private Label titleLabel = new Label();
        private Label detailLabel = new Label();
        private ImageView iconView = new ImageView();
        private Region spacer = new Region();
        private Function<String, Image> imageLoader;

        public JadwalListCell(Function<String, Image> imageLoader) {
            super();
            this.imageLoader = imageLoader;
            timeLabel.setStyle("-fx-font-family: 'Poppins SemiBold'; -fx-font-size: 13px; -fx-text-fill: #05004E;");
            titleLabel.setStyle("-fx-font-family: 'Poppins Medium'; -fx-font-size: 13px; -fx-text-fill: #151D48;");
            detailLabel.setStyle("-fx-font-family: 'Poppins Regular'; -fx-font-size: 11px; -fx-text-fill: rgba(70, 78, 95, 0.7);");
            iconView.setFitHeight(20);
            iconView.setFitWidth(20);
            iconView.setPreserveRatio(true);

            HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);
            textVBox.getChildren().addAll(titleLabel, detailLabel);
            hbox.getChildren().addAll(timeLabel, textVBox, spacer, iconView);
            hbox.setAlignment(Pos.CENTER_LEFT);
            setStyle("-fx-padding: 8px 12px; -fx-background-color: transparent; -fx-border-color: transparent transparent #f0f0f0 transparent; -fx-border-width: 1px;");
        }

        @Override
        protected void updateItem(JadwalItem item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
                setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
            } else {
                timeLabel.setText(item.getTime());
                titleLabel.setText(item.getTitle());
                detailLabel.setText(item.getDetail());
                if (imageLoader != null) {
                    Image img = imageLoader.apply(item.getIconPath());
                    if (img != null && img.getException() == null) {
                        iconView.setImage(img);
                    } else {
                        // Coba fallback ke dummy jika path spesifik gagal
                        Image dummyImg = imageLoader.apply("/images/dummy.png");
                        if (dummyImg != null && dummyImg.getException() == null) {
                            iconView.setImage(dummyImg);
                        } else {
                            iconView.setImage(null); // Jika dummy juga gagal
                        }
                    }
                } else {
                    iconView.setImage(null);
                }
                setGraphic(hbox);
                setStyle("-fx-padding: 8px 12px; -fx-background-color: transparent; -fx-border-color: transparent transparent #f0f0f0 transparent; -fx-border-width: 1px;");
            }
        }
    }

    private static class Testimonial {
        String quote;
        String author;
        String role;
        String imagePath;

        public Testimonial(String quote, String author, String role, String imagePath) {
            this.quote = quote;
            this.author = author;
            this.role = role;
            this.imagePath = imagePath;
        }
        public String getQuote() { return quote; }
        public String getAuthor() { return author; }
        public String getRole() { return role; }
        public String getImagePath() { return imagePath; }
    }

    private static class Founder {
        String nama;
        String role;
        String fotoPath;

        public Founder(String nama, String role, String fotoPath) {
            this.nama = nama;
            this.role = role;
            this.fotoPath = fotoPath;
        }
        public String getNama() { return nama; }
        public String getRole() { return role; }
        public String getFotoPath() { return fotoPath; }
    }

    public static class StyleUtil {
        public static void applyImageRoundedCorners(ImageView imageView, double cornerRadius) {
            if (imageView == null) return;

            double targetWidth = imageView.getFitWidth();
            double targetHeight = imageView.getFitHeight();

            // Jika fitWidth/Height tidak di-set, coba ambil dari gambar aktual
            if (targetWidth <= 0 && imageView.getImage() != null) targetWidth = imageView.getImage().getWidth();
            if (targetHeight <= 0 && imageView.getImage() != null) targetHeight = imageView.getImage().getHeight();

            // Jika masih 0, jangan lakukan clip atau set ukuran default kecil
            if (targetWidth <= 0 || targetHeight <= 0) {
                // System.err.println("Tidak bisa menerapkan clip pada ImageView karena ukuran tidak valid (<=0)");
                return;
            }

            Rectangle clip = new Rectangle(targetWidth, targetHeight);
            clip.setArcWidth(cornerRadius * 2);
            clip.setArcHeight(cornerRadius * 2);
            imageView.setClip(clip);
        }
    }
    //</editor-fold>
}