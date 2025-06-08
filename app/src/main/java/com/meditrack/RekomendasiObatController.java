package com.meditrack;

import com.meditrack.dao.DaftarObatDAO;
import com.meditrack.dao.PenggunaDAO;
import com.meditrack.dao.RekomendasiDAO;
import com.meditrack.model.DaftarObat;
import com.meditrack.model.Pengguna;
import com.meditrack.model.Rekomendasi;
import com.meditrack.util.SQLiteConnection;
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RekomendasiObatController {

    @FXML
    private VBox rekomendasiContainer;

    @FXML
    private Label userNameLabel;

    private final RekomendasiDAO rekomendasiDAO = new RekomendasiDAO();
    private final DaftarObatDAO daftarObatDAO = new DaftarObatDAO();
    private final PenggunaDAO penggunaDAO = new PenggunaDAO();
    private final List<Rekomendasi> allRekomendasi = new ArrayList<>();

    @FXML
    public void initialize() {
        userNameLabel.setText("Carlos");
        loadRekomendasi();
    }

    private void loadRekomendasi() {
        allRekomendasi.clear();
        allRekomendasi.addAll(rekomendasiDAO.getAllRekomendasi());
        renderCards();
    }

    private void renderCards() {
        rekomendasiContainer.getChildren().clear();
        for (Rekomendasi rekom : allRekomendasi) {
            VBox card = createRekomendasiCard(rekom);
            rekomendasiContainer.getChildren().add(card);
        }
    }

    private VBox createRekomendasiCard(Rekomendasi rekom) {
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

    private void updateCardStyle(VBox card, Label statusLabel, String status) {
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

    private void showDetailPopup(Rekomendasi rekom) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Detail Rekomendasi");

        VBox root = new VBox(20);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f9fdf7; -fx-border-radius: 20; -fx-background-radius: 20;");

        DaftarObat obat = daftarObatDAO.getObatById(rekom.getIdObat());

        Label namaObatLabel = new Label(obat.getNamaObat() + ": Multivitamin & Suplemen Penambah Stamina");
        namaObatLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 20));
        namaObatLabel.setTextFill(Color.BLACK);

        Label deskripsiObatLabel = new Label("Suplemen multivitamin yang membantu menjaga daya tahan dan energi tubuh, mengandung Vitamin A, B1, B2, B6, B12, C, D, E, nicotinamide, kalsium pantotenat, asam folat, lysin, kalsium, magnesium, zinc, dan lecithin (tergantung varian).");
        deskripsiObatLabel.setFont(Font.font("Poppins", 14));
        deskripsiObatLabel.setWrapText(true);

        Label caraKonsumsiLabel = new Label("Cara Konsumsi:");
        caraKonsumsiLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 16));
        caraKonsumsiLabel.setTextFill(Color.BLACK);

        Label langkahLabel = new Label(
                "1. Selesaikan sarapan lebih dulu: Pastikan makanan sudah cukup masuk untuk meminimalkan risiko mual.\n" +
                        "2. Tunggu 10–15 menit setelah makan: Beri waktu pencernaan bekerja agar penyerapan vitamin optimal.\n" +
                        "3. Minum Caviplex dengan air putih: 1 kaplet, tidak perlu dikunyah, langsung ditelan.\n" +
                        "4. Hindari kopi atau teh saat minum vitamin: Karena bisa mengganggu penyerapan nutrisi tertentu.\n" +
                        "5. Lanjutkan aktivitas seperti biasa: Vitamin akan bekerja mendukung energi dan daya tahanmu sepanjang hari.");
        langkahLabel.setFont(Font.font("Poppins", 14));
        langkahLabel.setWrapText(true);

        Button closeButton = new Button("Tutup");
        closeButton.setStyle("-fx-background-color: #1d4ed8; -fx-text-fill: white; -fx-font-size: 14px;");
        closeButton.setOnAction(e -> popupStage.close());

        root.getChildren().addAll(namaObatLabel, deskripsiObatLabel, caraKonsumsiLabel, langkahLabel, closeButton);

        Scene scene = new Scene(root, 600, 500);
        popupStage.setScene(scene);
        popupStage.showAndWait();
    }

    @FXML
    private void onTerimaAll() {
        for (int i = 0; i < allRekomendasi.size(); i++) {
            Rekomendasi rekom = allRekomendasi.get(i);
            rekom.setStatusRekomendasi("Selesai");
            rekomendasiDAO.updateRekomendasi(rekom);

            VBox card = (VBox) rekomendasiContainer.getChildren().get(i);
            Label statusLabel = (Label) ((HBox) card.getChildren().get(0)).getChildren().get(1);
            updateCardStyle(card, statusLabel, "Selesai");
        }
    }

    @FXML
    private void onTolakAll() {
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

    private void generateNewRecommendation(int userId) {
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
        DaftarObat selectedObat = allObat.stream()
                .filter(obat -> obat.getNamaObat().toLowerCase().contains(kondisi.split(" ")[0]))
                .findFirst()
                .orElse(allObat.get(random.nextInt(allObat.size())));

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
    private void onPrevPage() {
        System.out.println("Prev Page clicked");
    }

    @FXML
    private void onNextPage() {
        System.out.println("Next Page clicked");
    }

    private void showPopup(int userId) {
        // Fallback jika user klik "Tolak Semua"
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

}
