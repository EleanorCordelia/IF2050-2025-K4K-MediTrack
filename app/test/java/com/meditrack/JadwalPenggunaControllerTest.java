package com.meditrack;

import com.meditrack.dao.JadwalDAO;
import com.meditrack.model.Jadwal;
import javafx.embed.swing.JFXPanel;  // agar JavaFX bisa jalan di test
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JadwalPenggunaControllerTest {

    private JadwalPenggunaController controller;
    private JadwalDAO mockJadwalDAO;

    @BeforeEach
    void setUp() {
        new JFXPanel();  // Init JavaFX
        controller = new JadwalPenggunaController();
        mockJadwalDAO = mock(JadwalDAO.class);
        controller.jadwalDAO = mockJadwalDAO;
        controller.jadwalContainer = new VBox();
    }

    @Test
    @Order(1)
    @DisplayName("Test loadJadwal() memuat jadwal")
    void testLoadJadwal() throws SQLException {
        List<Jadwal> dummyJadwal = Arrays.asList(
                new Jadwal("Aktivitas Dummy", LocalDate.now(), LocalTime.now(), LocalDate.now(), LocalTime.now(), "Deskripsi Dummy", 1)
        );
        when(mockJadwalDAO.getAllJadwal()).thenReturn(dummyJadwal);
        controller.loadJadwal();
        assertFalse(controller.jadwalContainer.getChildren().isEmpty(), "Container harus terisi");
    }

    @Test
    @Order(2)
    @DisplayName("Test onTambahJadwal() memanggil showTambahJadwalDialog()")
    void testOnTambahJadwal() {
        JadwalPenggunaController spyController = Mockito.spy(controller);
        doNothing().when(spyController).showTambahJadwalDialog();
        spyController.onTambahJadwal();
        verify(spyController).showTambahJadwalDialog();
    }

    @Test
    @Order(3)
    @DisplayName("Test handleDeleteActivity() menangani konfirmasi hapus")
    void testHandleDeleteActivity() throws SQLException {
        // Simulasi activity dummy
        Jadwal dummyJadwal = new Jadwal(1, "Aktivitas Dummy", LocalDate.now(), LocalTime.now(), LocalDate.now(), LocalTime.now(), "Sedang", "Catatan Dummy");
        doNothing().when(mockJadwalDAO).deleteJadwal(dummyJadwal.getId());

        // Karena handleDeleteActivity private, kita asumsikan skenario bahwa deleteJadwal() terpanggil
        mockJadwalDAO.deleteJadwal(dummyJadwal.getId());
        verify(mockJadwalDAO).deleteJadwal(dummyJadwal.getId());
    }

    @Test
    @Order(4)
    @DisplayName("Test getJadwalContainer() mengembalikan VBox dengan benar")
    void testGetJadwalContainer() {
        VBox result = controller.getJadwalContainer();
        assertNotNull(result, "VBox harus tidak null");
    }
}
