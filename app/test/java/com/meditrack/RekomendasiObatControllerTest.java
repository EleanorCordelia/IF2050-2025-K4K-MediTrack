package com.meditrack;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RekomendasiObatControllerTest {

    private RekomendasiObatController controller;

    @BeforeAll
    static void initToolkit() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX platform already started, ignore
        }
    }

    @BeforeEach
    void setUp() {
        controller = new RekomendasiObatController();
    }

    @Test
    @Order(1)
    void testUpdateCardStyleSelesai() {
        VBox card = new VBox();
        Label statusLabel = new Label();
        controller.updateCardStyle(card, statusLabel, "Selesai");

        assertTrue(card.getStyle().contains("#f0fdf4"));
        assertTrue(statusLabel.getStyle().contains("#b7eb8f"));
    }

    @Test
    @Order(2)
    void testUpdateCardStyleBelum() {
        VBox card = new VBox();
        Label statusLabel = new Label();
        controller.updateCardStyle(card, statusLabel, "Belum");

        assertTrue(card.getStyle().contains("#ffe2e5"));
        assertTrue(statusLabel.getStyle().contains("#ff8995"));
    }

    @Test
    @Order(3)
    void testUpdateCardStyleDitolak() {
        VBox card = new VBox();
        Label statusLabel = new Label();
        controller.updateCardStyle(card, statusLabel, "Ditolak");

        assertTrue(card.getStyle().contains("#ffe2e5"));
        assertTrue(statusLabel.getStyle().contains("#ff4d4f"));
    }

    @Test
    @Order(4)
    void testUpdateCardStyleDefault() {
        VBox card = new VBox();
        Label statusLabel = new Label();
        controller.updateCardStyle(card, statusLabel, "Unknown");

        assertTrue(card.getStyle().contains("#ffffff"));
        assertTrue(statusLabel.getStyle().contains("#8c9299"));
    }
}
