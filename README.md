# IF2050 Dasar Rekayasa Perangkat Lunak 
# Nama Proyek: MediTrack

## Anggota Kelompok 4K

| No | Nama                         | NIM       |
|----|------------------------------|-----------|
| 1  | Shalihah Ramadhanty Nurhaliza | 14421017  |
| 2  | Marzel Zhafir Nugroho         | 14422006  |
| 3  | M. Faiz Fadhlurrahman         | 14422030  |
| 4  | Thalita Zahra Sutejo          | 18222023  |
| 5  | Eleanor Cordelia              | 18222059  |


## 📌 Penjelasan Singkat Aplikasi
MediTrack adalah aplikasi berbasis JavaFX yang bertujuan untuk membantu pengguna dalam memantau kondisi kesehatan mereka. Aplikasi ini menyediakan fitur seperti pencatatan jadwal kesehatan, konsultasi dengan dokter, rekomendasi obat dan suplemen, serta pengelolaan informasi akun pengguna. Data pengguna tersimpan secara lokal dalam database SQLite.

---

## 💻 Cara Menjalankan Aplikasi MediTrack

Ikuti langkah-langkah berikut untuk menjalankan aplikasi MediTrack secara lokal:

1. **Clone Repository dari GitHub**
   ```bash
   git clone <url-repo-github>
   cd <nama-folder-project>
   ```

2. **Checkout ke Branch `main`**
   ```bash
   git checkout main
   ```

3. **Pastikan Java dan Gradle Terinstal**
   - Java: versi 21 atau lebih baru
   - Gradle: gunakan wrapper bawaan (`./gradlew`) tanpa perlu menginstal manual

4. **Build Project dengan Gradle**
   ```bash
   ./gradlew build
   ```

5. **Pastikan Database SQLite Tersedia**
   - File database bernama `meditrack.db` harus berada di direktori utama proyek atau lokasi yang telah ditentukan dalam konfigurasi kode Java (biasanya di dalam folder `/resources` atau root).
   - Tidak perlu setup tambahan jika menggunakan SQLite, karena dependensinya langsung dibaca oleh aplikasi.

6. **Jalankan Aplikasi**
   ```bash
   ./gradlew run
   ```

7. **Jika terjadi error JavaFX**
   Atur konfigurasi JavaFX pada `build.gradle` dan `Run Configuration`, atau gunakan SDK yang sudah bundling JavaFX seperti Liberica JDK.


---

## Daftar Modul yang Diimplementasikan
| No | Nama Modul | Assignee | Screenshot |
|----|-------------|----------|------------|
| 1 | Autentikasi | Shali, Eleanor | ![Autentikasi](screenshots/autentikasi) |
| 2 | Menu | Eleanor | ![Menu](screenshots/menu) |
| 3 | Manajemen Pengguna | Marzel, Eleanor, Thalita | ![Manajemen Pengguna](screenshots/manajemen_pengguna) |
| 4 | Kondisi Aktual | Thalita | ![Kondisi Aktual](screenshots/kondisi_aktual) |
| 5 | Jadwal | Faiz, Eleanor | ![Jadwal](screenshots/jadwal) |
| 6 | Rekomendasi | Eleanor | ![Rekomendasi](screenshots/rekomendasi) |
| 7 | Landing | Eleanor | ![Landing](screenshots/landing) |


---

## Daftar Tabel Basis Data


### Tabel: `pengguna`

| Nama Atribut | Tipe Data |
|--------------|-----------|
| idPengguna | INTEGER |
| nama | TEXT |
| email | TEXT |
| password | TEXT |
| tanggalLahir | TEXT |
| jenisKelamin | TEXT |
| tinggiBadan | REAL |
| beratBadan | REAL |
| avatar_path | TEXT |


### Tabel: `detailjadwal`

| Nama Atribut | Tipe Data |
|--------------|-----------|
| idDetailJadwal | INTEGER |
| idJadwal | INTEGER |
| waktuMulai | TEXT |
| waktuSelesai | TEXT |
| deskripsi | TEXT |
| kategori | TEXT |


### Tabel: `daftarobat`

| Nama Atribut | Tipe Data |
|--------------|-----------|
| idObat | INTEGER |
| namaObat | TEXT |
| dosis | TEXT |
| waktuKonsumsi | TEXT |
| efekSamping | TEXT |
| statusKonsumsi | TEXT |
| deskripsi | TEXT |
| caraKonsumsi | TEXT |
| urutan_konsumsi | INTEGER |


### Tabel: `rekomendasi`

| Nama Atribut | Tipe Data |
|--------------|-----------|
| idRekomendasi | INTEGER |
| idPengguna | INTEGER |
| idObat | INTEGER |
| alasan | TEXT |
| tanggalRekomendasi | TEXT |
| statusRekomendasi | TEXT |


### Tabel: `kondisi_aktual`

| Nama Atribut | Tipe Data |
|--------------|-----------|
| id | INTEGER |
| user_id | INTEGER |
| tekanan_darah | TEXT |
| detak_jantung | INTEGER |
| suhu_tubuh | REAL |
| tingkat_stres | TEXT |
| durasi_tidur | REAL |
| durasi_olahraga | INTEGER |
| jumlah_langkah | INTEGER |
| tgl_pencatatan | DATETIME |


### Tabel: `jadwal`

| Nama Atribut | Tipe Data |
|--------------|-----------|
| id | INTEGER |
| id_pengguna | INTEGER |
| nama_aktivitas | TEXT |
| tanggal_mulai | TEXT |
| jam_mulai | TEXT |
| tanggal_selesai | TEXT |
| jam_selesai | TEXT |
| kategori | TEXT |
| catatan | TEXT |
