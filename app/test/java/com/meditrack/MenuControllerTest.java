package com.meditrack;

import com.meditrack.controller.MenuController;
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
    void testCurrentUserId() {
        // Test default user ID
        assertEquals(1, menuController.getCurrentUserId(), "Default user ID should be 1");

        // Note: setCurrentUserId calls loadUserData which requires database connection
        // So we only test the getter for now
        // In a real implementation, we would mock the database connection
    }

    @Test
    void testCleanupMethod() {
        // Test that cleanup method doesn't throw exception
        assertDoesNotThrow(() -> {
            menuController.cleanup();
        }, "Cleanup method should not throw exception");
    }
}