package com.meditrack;

import com.meditrack.dao.DaftarObatDAO;
import com.meditrack.dao.PenggunaDAO;
import com.meditrack.model.DaftarObat;
import com.meditrack.dao.RekomendasiDAO;
import com.meditrack.model.DaftarObat;
import com.meditrack.model.Pengguna;
import com.meditrack.model.Rekomendasi;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.meditrack.model.KondisiAktual;
import com.meditrack.dao.KondisiAktualDAO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RekomendasiObatController {

    @FXML
    public VBox rekomendasiContainer;

    @FXML
    public Label userNameLabel;

    public final RekomendasiDAO rekomendasiDAO = new RekomendasiDAO();
    public final DaftarObatDAO daftarObatDAO = new DaftarObatDAO();
    public final PenggunaDAO penggunaDAO = new PenggunaDAO();
    public final List<Rekomendasi> allRekomendasi = new ArrayList<>();
    private final KondisiAktualDAO kondisiAktualDAO = new KondisiAktualDAO();

    @FXML
    public void initialize() {
        userNameLabel.setText("Carlos");
        loadRekomendasi();
    }

    public void loadRekomendasi() {
        allRekomendasi.clear();
        allRekomendasi.addAll(rekomendasiDAO.getAllRekomendasi());
        renderCards();
    }

    public void renderCards() {
        rekomendasiContainer.getChildren().clear();
        for (Rekomendasi rekom : allRekomendasi) {
            VBox card = createRekomendasiCard(rekom);
            rekomendasiContainer.getChildren().add(card);
        }
    }

    public VBox createRekomendasiCard(Rekomendasi rekom) {
        VBox card = new VBox();
        card.setSpacing(8);
        card.setPadding(new Insets(16));
        card.setPrefWidth(900);
        card.setStyle("-fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #e5e7eb;");

        Label tanggalLabel = new Label(rekom.getTanggalRekomendasi());
        tanggalLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #8c9299;");

        Label statusLabel = new Label(rekom.getStatusRekomendasi());
        statusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #8c9299; -fx-border-color: #8c9299;" +
                "-fx-border-width: 1; -fx-border-radius: 12; -fx-padding: 2 8;");

        HBox headerBox = new HBox(8, tanggalLabel, statusLabel);

        DaftarObat obat = daftarObatDAO.getObatById(rekom.getIdObat());
        String namaObat = obat != null ? obat.getNamaObat() : "Obat Tidak Ditemukan";

        Label namaObatLabel = new Label(namaObat);
        namaObatLabel.setStyle("-fx-font-size: 22px; -fx-text-fill: #111112; -fx-font-family: 'Poppins';");

        String dosisInfo = obat != null ? obat.getDosis() + ", " + obat.getWaktuKonsumsi() : "Informasi dosis tidak tersedia";
        Label detailLabel = new Label(dosisInfo);
        detailLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #8c9299;");

        String alasanLengkap = rekom.getAlasan().split(" - ").length > 1 ?
                rekom.getAlasan().split(" - ")[1] : rekom.getAlasan();
        Label alasanLabel = new Label(alasanLengkap);
        alasanLabel.setWrapText(true);
        alasanLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: rgba(164,170,177,0.82);");

        Button detailButton = new Button("Detail");
        detailButton.setOnAction(e -> showDetailPopup(rekom));

        card.getChildren().addAll(headerBox, namaObatLabel, detailLabel, alasanLabel, detailButton);
        updateCardStyle(card, statusLabel, rekom.getStatusRekomendasi());
        return card;
    }

    public void updateCardStyle(VBox card, Label statusLabel, String status) {
        String backgroundColor;
        String borderColor;
        String textColor;

        switch (status) {
            case "Selesai":
                backgroundColor = "#f0fdf4";
                borderColor = "#b7eb8f";
                textColor = "#b7eb8f";
                break;
            case "Belum":
                backgroundColor = "#ffe2e5";
                borderColor = "#ff8995";
                textColor = "#ff8995";
                break;
            case "Ditolak":
                backgroundColor = "#ffe2e5";
                borderColor = "#ff4d4f";
                textColor = "#ff4d4f";
                break;
            default:
                backgroundColor = "#ffffff";
                borderColor = "#e5e7eb";
                textColor = "#8c9299";
                break;
        }

        card.setStyle(
                "-fx-background-color: " + backgroundColor + ";" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1;"
        );

        statusLabel.setText(status);
        statusLabel.setStyle(
                "-fx-font-size: 12px; -fx-text-fill: " + textColor + ";" +
                        "-fx-border-color: " + textColor + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 12;" +
                        "-fx-padding: 2 8;"
        );
    }

    public void showDetailPopup(Rekomendasi rekom) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Detail Rekomendasi");

        VBox root = new VBox(20);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f9fdf7; -fx-border-radius: 20; -fx-background-radius: 20;");

        DaftarObat obat = daftarObatDAO.getObatById(rekom.getIdObat());
        String nama = obat != null ? obat.getNamaObat() : "Obat Tidak Ditemukan";

        Label title = new Label(nama);
        title.setFont(Font.font("Poppins", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#111112"));

        // Tampilkan detail lengkap dari kolom database
        Label dosisLabel = new Label("Dosis: " + (obat != null ? obat.getDosis() : "N/A"));
        dosisLabel.setFont(Font.font("Poppins", 14));
        dosisLabel.setWrapText(true);

        Label waktuKonsumsiLabel = new Label("Waktu Konsumsi: " + (obat != null ? obat.getWaktuKonsumsi() : "N/A"));
        waktuKonsumsiLabel.setFont(Font.font("Poppins", 14));
        waktuKonsumsiLabel.setWrapText(true);

        Label efekSampingLabel = new Label("Efek Samping: " + (obat != null ? obat.getEfekSamping() : "N/A"));
        efekSampingLabel.setFont(Font.font("Poppins", 14));
        efekSampingLabel.setWrapText(true);

        Label statusKonsumsiLabel = new Label("Status Konsumsi: " + (obat != null ? obat.getStatusKonsumsi() : "N/A"));
        statusKonsumsiLabel.setFont(Font.font("Poppins", 14));
        statusKonsumsiLabel.setWrapText(true);

        Label deskripsiLabel = new Label("Deskripsi: " + (obat != null ? obat.getDeskripsi() : "N/A"));
        deskripsiLabel.setFont(Font.font("Poppins", 14));
        deskripsiLabel.setWrapText(true);

        Label caraKonsumsiLabel = new Label("Cara Konsumsi: " + (obat != null ? obat.getCaraKonsumsi() : "N/A"));
        caraKonsumsiLabel.setFont(Font.font("Poppins", 14));
        caraKonsumsiLabel.setWrapText(true);

        Label urutanKonsumsiLabel = new Label("Urutan Konsumsi: " + (obat != null ? obat.getUrutanKonsumsi() : "N/A"));
        urutanKonsumsiLabel.setFont(Font.font("Poppins", 14));
        urutanKonsumsiLabel.setWrapText(true);

        // Tombol Tutup
        Button close = new Button("Tutup");
        close.setStyle("-fx-background-color: #1d4ed8; -fx-text-fill: white; -fx-font-size: 14px;");
        close.setOnAction(e -> popup.close());

        root.getChildren().addAll(
                title,
                dosisLabel,
                waktuKonsumsiLabel,
                efekSampingLabel,
                statusKonsumsiLabel,
                deskripsiLabel,
                caraKonsumsiLabel,
                urutanKonsumsiLabel,
                close
        );

        Scene scene = new Scene(root, 600, 600);
        popup.setScene(scene);
        popup.showAndWait();
    }

    private String getDescription(String namaObat) {
        if (namaObat.toLowerCase().contains("multivitamin")) {
            return "Multivitamin & mineral paling umum; mendukung energi, imun, dan kesehatan tubuh.";
        }
        return "Deskripsi tidak tersedia.";
    }

    private String getUsageInstructions(String namaObat) {
        if (namaObat.toLowerCase().contains("multivitamin")) {
            return "- Minum satu kapsul setelah makan pagi.\n- Ikuti petunjuk dokter jika sedang hamil.";
        }
        return "- Gunakan sesuai anjuran pada kemasan.";
    }

    private String getPrecautions(String namaObat) {
        if (namaObat.toLowerCase().contains("multivitamin")) {
            return "• Dapat menyebabkan mual, konstipasi, atau diare ringan.\n" +
                    "• Jangan konsumsi bersamaan dengan herbal atau multivitamin lain untuk menghindari overdosis.\n" +
                    "• Simpan jauh dari jangkauan anak-anak.";
        }
        return "• Konsultasikan dengan dokter jika Anda sedang hamil atau menyusui.";
    }

    @FXML
    public void onTerimaAll() {
        for (int i = 0; i < allRekomendasi.size(); i++) {
            Rekomendasi rekom = allRekomendasi.get(i);
            rekom.setStatusRekomendasi("Selesai");
            rekomendasiDAO.updateRekomendasi(rekom);

            VBox card = (VBox) rekomendasiContainer.getChildren().get(i);
            Label statusLabel = (Label) ((HBox) card.getChildren().get(0)).getChildren().get(1);
            updateCardStyle(card, statusLabel, "Selesai");
        }
        showGreenConfirmationDialog();
    }

    @FXML
    public void onTolakAll() {
        for (int i = 0; i < allRekomendasi.size(); i++) {
            Rekomendasi rekom = allRekomendasi.get(i);
            rekom.setStatusRekomendasi("Ditolak");
            rekomendasiDAO.updateRekomendasi(rekom);

            VBox card = (VBox) rekomendasiContainer.getChildren().get(i);
            Label statusLabel = (Label) ((HBox) card.getChildren().get(0)).getChildren().get(1);
            updateCardStyle(card, statusLabel, "Ditolak");
        }

        Pengguna penggunaAktif = penggunaDAO.getPenggunaAktif();
        int userId = penggunaAktif != null ? penggunaAktif.getIdPengguna() : -1;
        if (userId != -1) {
            showPopup(userId);
        } else {
            System.out.println("Tidak ada pengguna aktif.");
        }
    }

    public void generateNewRecommendation(int userId) {
        for (Rekomendasi r : rekomendasiDAO.getAllRekomendasi()) {
            if (r.getIdPengguna() == userId && r.getStatusRekomendasi().equals("Ditolak")) {
                rekomendasiDAO.deleteRekomendasi(r.getIdRekomendasi());
                System.out.println("Deleted: " + r.getAlasan());
            }
        }

        List<String> kondisiAktualList = List.of("demam", "nyeri", "flu ringan", "kelelahan", "infeksi ringan", "batuk pilek");
        List<String> jadwalList = List.of("pagi hari", "siang hari", "sore hari", "malam hari");
        List<DaftarObat> allObat = daftarObatDAO.getAllObat();

        Random random = new Random();
        String kondisi = kondisiAktualList.get(random.nextInt(kondisiAktualList.size()));
        String jadwal = jadwalList.get(random.nextInt(jadwalList.size()));
        DaftarObat selectedObat = allObat.get(random.nextInt(allObat.size()));

        Rekomendasi newRekomendasi = new Rekomendasi();
        newRekomendasi.setIdPengguna(userId);
        newRekomendasi.setIdObat(selectedObat.getIdObat());
        newRekomendasi.setAlasan(selectedObat.getNamaObat() + " - Membantu mengatasi " + kondisi + " di " + jadwal);
        newRekomendasi.setTanggalRekomendasi(LocalDate.now() + " " + LocalTime.now());
        newRekomendasi.setStatusRekomendasi("Diajukan");

        rekomendasiDAO.insertRekomendasi(newRekomendasi);
        System.out.println("Berhasil insert rekomendasi: " + newRekomendasi.getAlasan());
        loadRekomendasi();
    }

    @FXML
    public void onPrevPage() {
        System.out.println("Prev Page clicked");
    }

    @FXML
    public void onNextPage() {
        System.out.println("Next Page clicked");
    }

    public void showPopup(int userId) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Tidak cocok dengan rekomendasi sebelumnya?");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #ffe2e5;");

        ImageView logoView = new ImageView(new Image("/images/logo.png"));
        logoView.setFitWidth(48);
        logoView.setPreserveRatio(true);

        Label messageLabel = new Label("Tidak cocok dengan rekomendasi sebelumnya?");
        messageLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 24));
        messageLabel.setTextFill(Color.web("#831843"));
        messageLabel.setWrapText(true);
        messageLabel.setAlignment(Pos.CENTER);

        Button generateButton = new Button("Generate Ulang");
        generateButton.setStyle("-fx-background-color: #1d4ed8; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-background-radius: 8;");
        generateButton.setOnAction(e -> {
            generateNewRecommendation(userId);
            loadRekomendasi();
            popupStage.close();
        });

        root.getChildren().addAll(logoView, messageLabel, generateButton);

        Scene scene = new Scene(root, 600, 400);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    public void showGreenConfirmationDialog() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Konfirmasi Rekomendasi");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #edf7e6; -fx-background-radius: 20;");

        ImageView logoView = new ImageView(new Image("/images/logo.png"));
        logoView.setFitWidth(48);
        logoView.setPreserveRatio(true);
        logoView.setStyle("-fx-background-color: white; -fx-border-radius: 16; -fx-padding: 8;");

        Label titleLabel = new Label("Terima kasih telah menyetujui rekomendasi kami!");
        titleLabel.setFont(Font.font("Poppins", FontWeight.SEMI_BOLD, 24));
        titleLabel.setTextFill(Color.web("#535651"));
        titleLabel.setWrapText(true);
        titleLabel.setAlignment(Pos.CENTER);

        Label messageLabel = new Label("Sekarang, yuk mulai jalani langkah-langkah konsumsi suplemen dan obat secara bertahap.\n" +
                "Semoga tubuhmu tetap fit, penuh energi, dan siap menghadapi hari!");
        messageLabel.setFont(Font.font("Poppins", 16));
        messageLabel.setTextFill(Color.web("#6b6f67"));
        messageLabel.setWrapText(true);
        messageLabel.setAlignment(Pos.CENTER);

        Button closeButton = new Button("Tutup");
        closeButton.setStyle("-fx-background-color: #1d4ed8; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 8;");
        closeButton.setOnAction(e -> popupStage.close());

        root.getChildren().addAll(logoView, titleLabel, messageLabel, closeButton);

        Scene scene = new Scene(root, 600, 400);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    private String dummyAIRekomendasi(int heartRate, String tingkatStres, double durasiOlahraga, double jumlahLangkah) {
        // Dummy logic untuk memilih obat/suplemen berdasarkan parameter sederhana
        if (heartRate > 85 && "Tinggi".equalsIgnoreCase(tingkatStres) && durasiOlahraga > 40) {
            return "Vitamin C + Zinc"; // mendukung daya tahan tubuh tinggi
        } else if (heartRate < 75 && "Rendah".equalsIgnoreCase(tingkatStres) && durasiOlahraga < 20) {
            return "Multivitamin"; // untuk menjaga kebugaran harian
        } else if (jumlahLangkah > 7000) {
            return "Vitamin D3"; // cocok untuk yang banyak aktivitas
        } else {
            return "Omega 3 Fish Oil"; // suplemen default
        }
    }

    @FXML
    private void onGenerateDummyRecommendation() {
        int userId = 1; // Sesuaikan dengan user aktif di session
        List<KondisiAktual> kondisiList = kondisiAktualDAO.getKondisiAktualByUserId(userId);

        for (KondisiAktual kondisi : kondisiList) {
            String namaObat = dummyAIRekomendasi(
                    kondisi.getDetakJantung(),
                    kondisi.getTingkatStres(),
                    kondisi.getDurasiOlahraga(),
                    kondisi.getJumlahLangkah()
            );

            // Cari obat yang cocok
            List<DaftarObat> matchedObat = daftarObatDAO.cariObatByKeyword(namaObat);
            DaftarObat selectedObat;
            if (!matchedObat.isEmpty()) {
                selectedObat = matchedObat.get(0);
            } else {
                selectedObat = daftarObatDAO.getRandomObat();
            }

            // Insert rekomendasi baru
            Rekomendasi newRekom = new Rekomendasi();
            newRekom.setIdPengguna(userId);
            newRekom.setIdObat(selectedObat.getIdObat());
            newRekom.setAlasan("Berdasarkan kondisi aktual kamu, kami merekomendasikan: " + selectedObat.getNamaObat());
            newRekom.setTanggalRekomendasi(LocalDate.now() + " " + LocalTime.now());
            newRekom.setStatusRekomendasi("Diajukan");
            rekomendasiDAO.insertRekomendasi(newRekom);
        }

        loadRekomendasi(); // Refresh tampilan rekomendasi
    }
}
