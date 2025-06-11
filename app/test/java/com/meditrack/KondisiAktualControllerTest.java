package com.meditrack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import com.meditrack.controller.KondisiAktualController;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("KondisiAktualController Tests")
class KondisiAktualControllerTest {

    private KondisiAktualController controller;

    @BeforeEach
    void setUp() {
        controller = new KondisiAktualController();
    }

    @Test
    @DisplayName("Should evaluate blood pressure correctly")
    void testEvaluateBloodPressure() throws Exception {
        // Get the private method using reflection
        Method method = KondisiAktualController.class.getDeclaredMethod("evaluateTekananDarah", String.class);
        method.setAccessible(true);

        // Test different blood pressure values
        assertEquals("Normal", method.invoke(controller, "115/75"));
        assertEquals("Normal", method.invoke(controller, "119/79"));
        assertEquals("Tinggi Normal", method.invoke(controller, "125/85"));
        assertEquals("Tinggi Normal", method.invoke(controller, "139/89"));
        assertEquals("Tinggi", method.invoke(controller, "140/90"));
        assertEquals("Tinggi", method.invoke(controller, "160/100"));
        assertEquals("Invalid", method.invoke(controller, "invalid"));
        assertEquals("Invalid", method.invoke(controller, "120"));
    }

    @Test
    @DisplayName("Should evaluate heart rate correctly")
    void testEvaluateHeartRate() throws Exception {
        // Get the private method using reflection
        Method method = KondisiAktualController.class.getDeclaredMethod("evaluateDetakJantung", Integer.class);
        method.setAccessible(true);

        // Test different heart rate values
        assertEquals("Normal", method.invoke(controller, 70));
        assertEquals("Normal", method.invoke(controller, 80));
        assertEquals("Normal", method.invoke(controller, 100));
        assertEquals("Normal", method.invoke(controller, 60));

        assertEquals("Rendah", method.invoke(controller, 50));
        assertEquals("Rendah", method.invoke(controller, 45));
        assertEquals("Rendah", method.invoke(controller, 59));

        assertEquals("Tinggi", method.invoke(controller, 101));
        assertEquals("Tinggi", method.invoke(controller, 120));
        assertEquals("Tinggi", method.invoke(controller, 110));
    }

    @Test
    @DisplayName("Should evaluate body temperature correctly")
    void testEvaluateBodyTemperature() throws Exception {
        // Get the private method using reflection
        Method method = KondisiAktualController.class.getDeclaredMethod("evaluateSuhuTubuh", Double.class);
        method.setAccessible(true);

        // Test different body temperature values
        assertEquals("Normal", method.invoke(controller, 36.5));
        assertEquals("Normal", method.invoke(controller, 37.0));
        assertEquals("Normal", method.invoke(controller, 36.8));
        assertEquals("Normal", method.invoke(controller, 37.2));

        assertEquals("Rendah", method.invoke(controller, 35.5));
        assertEquals("Rendah", method.invoke(controller, 36.0));
        assertEquals("Rendah", method.invoke(controller, 35.8));

        assertEquals("Demam", method.invoke(controller, 37.5));
        assertEquals("Demam", method.invoke(controller, 38.0));
        assertEquals("Demam", method.invoke(controller, 39.2));
    }
}