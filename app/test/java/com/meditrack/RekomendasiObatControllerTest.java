package com.meditrack;

import com.meditrack.dao.RekomendasiDAO;
import com.meditrack.dao.DaftarObatDAO;
import com.meditrack.dao.PenggunaDAO;
import com.meditrack.model.DaftarObat;
import com.meditrack.model.Pengguna;
import com.meditrack.model.Rekomendasi;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RekomendasiObatControllerTest {

    private RekomendasiObatController controller;
    private RekomendasiDAO rekomendasiDAO;
    private DaftarObatDAO daftarObatDAO;
    private PenggunaDAO penggunaDAO;

    @BeforeAll
    static void initToolkit() {
        // Initialize JavaFX Platform for testing
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Platform already started, ignore
        }
    }

    @BeforeEach
    void setUp() {
        rekomendasiDAO = mock(RekomendasiDAO.class);
        daftarObatDAO = mock(DaftarObatDAO.class);
        penggunaDAO = mock(PenggunaDAO.class);

        controller = new RekomendasiObatController();

        // Replace DAO dependencies using reflection (if they are not injectable)
        try {
            var rekomDAOField = RekomendasiObatController.class.getDeclaredField("rekomendasiDAO");
            rekomDAOField.setAccessible(true);
            rekomDAOField.set(controller, rekomendasiDAO);

            var obatDAOField = RekomendasiObatController.class.getDeclaredField("daftarObatDAO");
            obatDAOField.setAccessible(true);
            obatDAOField.set(controller, daftarObatDAO);

            var penggunaDAOField = RekomendasiObatController.class.getDeclaredField("penggunaDAO");
            penggunaDAOField.setAccessible(true);
            penggunaDAOField.set(controller, penggunaDAO);

            var rekomContainerField = RekomendasiObatController.class.getDeclaredField("rekomendasiContainer");
            rekomContainerField.setAccessible(true);
            rekomContainerField.set(controller, new VBox());

            var userNameLabelField = RekomendasiObatController.class.getDeclaredField("userNameLabel");
            userNameLabelField.setAccessible(true);
            userNameLabelField.set(controller, new Label());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    void testLoadRekomendasiWithEmptyList() {
        when(rekomendasiDAO.getAllRekomendasi()).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> {
            controller.initialize();
        });
    }

    @Test
    @Order(2)
    void testUpdateCardStyleForSelesai() {
        VBox card = new VBox();
        Label statusLabel = new Label();
        controller.updateCardStyle(card, statusLabel, "Selesai");

        assertTrue(card.getStyle().contains("#f0fdf4"), "Background color for Selesai should be greenish");
        assertTrue(statusLabel.getStyle().contains("#b7eb8f"), "Status label text color for Selesai should be greenish");
    }

    @Test
    @Order(3)
    void testUpdateCardStyleForBelum() {
        VBox card = new VBox();
        Label statusLabel = new Label();
        controller.updateCardStyle(card, statusLabel, "Belum");

        assertTrue(card.getStyle().contains("#ffe2e5"), "Background color for Belum should be reddish");
        assertTrue(statusLabel.getStyle().contains("#ff8995"), "Status label text color for Belum should be reddish");
    }

    @Test
    @Order(4)
    void testUpdateCardStyleForDitolak() {
        VBox card = new VBox();
        Label statusLabel = new Label();
        controller.updateCardStyle(card, statusLabel, "Ditolak");

        assertTrue(card.getStyle().contains("#ffe2e5"), "Background color for Ditolak should be reddish");
        assertTrue(statusLabel.getStyle().contains("#ff4d4f"), "Status label text color for Ditolak should be reddish");
    }

    @Test
    @Order(5)
    void testUpdateCardStyleForDefault() {
        VBox card = new VBox();
        Label statusLabel = new Label();
        controller.updateCardStyle(card, statusLabel, "UnknownStatus");

        assertTrue(card.getStyle().contains("#ffffff"), "Background color for unknown status should be white");
        assertTrue(statusLabel.getStyle().contains("#8c9299"), "Status label text color for unknown status should be gray");
    }
}
