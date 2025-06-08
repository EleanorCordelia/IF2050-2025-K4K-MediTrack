package com.meditrack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions.*;
import com.meditrack.model.Dokter;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DokterTest {

    private Dokter dokter;

    @Test
    void testSomething() {
        System.out.println("Running test...");
        assertTrue(true);
    }


    @BeforeEach
    void setUp() {
        dokter = new Dokter();
    }

    @Test
    @DisplayName("Test ID Dokter Getter and Setter")
    void testIdDokter() {
        dokter.setIdDokter(101);
        assertEquals(101, dokter.getIdDokter(), "ID Dokter harus 101");
    }

    @Test
    @DisplayName("Test Nama Getter and Setter")
    void testNama() {
        dokter.setNama("Dr. John Doe");
        assertEquals("Dr. John Doe", dokter.getNama(), "Nama harus Dr. John Doe");
    }

    @Test
    @DisplayName("Test Email Getter and Setter")
    void testEmail() {
        dokter.setEmail("johndoe@example.com");
        assertEquals("johndoe@example.com", dokter.getEmail(), "Email harus johndoe@example.com");
    }

    @Test
    @DisplayName("Test Spesialisasi Getter and Setter")
    void testSpesialisasi() {
        dokter.setSpesialisasi("Kardiologi");
        assertEquals("Kardiologi", dokter.getSpesialisasi(), "Spesialisasi harus Kardiologi");
    }

    @Test
    @DisplayName("Test Nomor STR Getter and Setter")
    void testNomorSTR() {
        dokter.setNomorSTR("STR12345");
        assertEquals("STR12345", dokter.getNomorSTR(), "Nomor STR harus STR12345");
    }

    @Test
    @DisplayName("Test Jenis Kelamin Getter and Setter")
    void testJenisKelamin() {
        dokter.setJenisKelamin("Laki-laki");
        assertEquals("Laki-laki", dokter.getJenisKelamin(), "Jenis kelamin harus Laki-laki");
    }

    @Test
    @DisplayName("Test Tanggal Lahir Getter and Setter")
    void testTanggalLahir() {
        LocalDate birthDate = LocalDate.of(1990, 5, 20);
        dokter.setTanggalLahir(birthDate);
        assertEquals(birthDate, dokter.getTanggalLahir(), "Tanggal lahir harus sesuai");
    }

    @Test
    @DisplayName("Test Nomor Telepon Getter and Setter")
    void testNomorTelepon() {
        dokter.setNomorTelepon("08123456789");
        assertEquals("08123456789", dokter.getNomorTelepon(), "Nomor telepon harus 08123456789");
    }

    @Test
    @DisplayName("Test Alamat Getter and Setter")
    void testAlamat() {
        dokter.setAlamat("Jl. Merdeka No. 123");
        assertEquals("Jl. Merdeka No. 123", dokter.getAlamat(), "Alamat harus Jl. Merdeka No. 123");
    }

    @Test
    @DisplayName("Test toString() method")
    void testToString() {
        dokter.setIdDokter(101);
        dokter.setNama("Dr. John Doe");
        dokter.setSpesialisasi("Kardiologi");
        dokter.setNomorSTR("STR12345");
        dokter.setJenisKelamin("Laki-laki");
        dokter.setTanggalLahir(LocalDate.of(1990, 5, 20));

        String expectedString = "Dokter{idDokter=101, nama='Dr. John Doe', spesialisasi='Kardiologi', nomorSTR='STR12345', jenisKelamin='Laki-laki', tanggalLahir=1990-05-20}";
        assertEquals(expectedString, dokter.toString(), "toString harus sesuai format");
    }
}
