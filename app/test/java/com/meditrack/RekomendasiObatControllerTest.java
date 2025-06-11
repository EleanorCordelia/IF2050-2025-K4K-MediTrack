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
            // JavaFX already initialized
        }
    }

    @BeforeEach
    void setUp() {
        controller = new RekomendasiObatController();
    }

    @Test
    @Order(1)
    void testUpdateCardStyle_Selesai() {
        VBox card = new VBox();
        Label statusLabel = new Label();
        controller.updateCardStyle(card, statusLabel, "Selesai");

        assertTrue(card.getStyle().contains("#f0fdf4"), "Card should have green background for 'Selesai'");
        assertTrue(statusLabel.getStyle().contains("#b7eb8f"), "Status label should have green border/text for 'Selesai'");
    }

    @Test
    @Order(2)
    void testUpdateCardStyle_Belum() {
        VBox card = new VBox();
        Label statusLabel = new Label();
        controller.updateCardStyle(card, statusLabel, "Belum");

        assertTrue(card.getStyle().contains("#ffe2e5"), "Card should have red-pink background for 'Belum'");
        assertTrue(statusLabel.getStyle().contains("#ff8995"), "Status label should have pink border/text for 'Belum'");
    }

    @Test
    @Order(3)
    void testUpdateCardStyle_Ditolak() {
        VBox card = new VBox();
        Label statusLabel = new Label();
        controller.updateCardStyle(card, statusLabel, "Ditolak");

        assertTrue(card.getStyle().contains("#ffe2e5"), "Card should have red-pink background for 'Ditolak'");
        assertTrue(statusLabel.getStyle().contains("#ff4d4f"), "Status label should have red border/text for 'Ditolak'");
    }

    @Test
    @Order(4)
    void testUpdateCardStyle_UnknownStatus() {
        VBox card = new VBox();
        Label statusLabel = new Label();
        controller.updateCardStyle(card, statusLabel, "RandomStatus");

        assertTrue(card.getStyle().contains("#ffffff"), "Card should have white background for unknown status");
        assertTrue(statusLabel.getStyle().contains("#8c9299"), "Status label should have gray border/text for unknown status");
    }
}
