package com.meditrack;

import com.meditrack.model.Rekomendasi;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RekomendasiObatControllerTest {

    private RekomendasiObatController controller;

    @BeforeEach
    void setUp() {
        controller = new RekomendasiObatController();
    }

    @Test
    @Order(1)
    void testDummyAIRecommendationHighStress() {
        String result = controller.dummyAIRekomendasi(90, "Tinggi", 45, 3000);
        assertEquals("Vitamin C + Zinc", result);
    }

    @Test
    @Order(2)
    void testDummyAIRecommendationLowStress() {
        String result = controller.dummyAIRekomendasi(70, "Rendah", 15, 2000);
        assertEquals("Multivitamin", result);
    }

    @Test
    @Order(3)
    void testDummyAIRecommendationActive() {
        String result = controller.dummyAIRekomendasi(80, "Sedang", 30, 8000);
        assertEquals("Vitamin D3", result);
    }

    @Test
    @Order(4)
    void testDummyAIRecommendationDefault() {
        String result = controller.dummyAIRekomendasi(80, "Sedang", 30, 1000);
        assertEquals("Omega 3 Fish Oil", result);
    }
}
