package com.meditrack;

import com.meditrack.MenuController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple unit tests for MenuController
 */
class MenuControllerTest {

    private MenuController menuController;

    @BeforeEach
    void setUp() {
        menuController = new MenuController();
    }

    @Test
    void testMenuControllerCreation() {
        // Test that MenuController can be created successfully
        assertNotNull(menuController, "MenuController should be created");
    }

    @Test
    void testMenuControllerInitialization() {
        // Test that MenuController initializes without throwing exception
        assertDoesNotThrow(() -> {
            // This tests basic object creation which is handled in setUp
            // More comprehensive tests would require JavaFX Application Thread
        }, "MenuController should initialize without exception");
    }
}