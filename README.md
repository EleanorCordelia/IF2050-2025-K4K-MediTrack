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


## Deskripsi Aplikasi
MediTrack adalah aplikasi berbasis JavaFX yang bertujuan untuk membantu pengguna dalam memantau kondisi kesehatan mereka. Aplikasi ini menyediakan fitur seperti pencatatan jadwal kesehatan, konsultasi dengan dokter, rekomendasi obat dan suplemen, serta pengelolaan informasi akun pengguna. Data pengguna tersimpan secara lokal dalam database SQLite.

---

## Cara Menjalankan Aplikasi MediTrack

Aplikasi MediTrack dapat dijalankan melalui dua cara:

---

### **1. Melalui Clone Repository dari GitHub**

Langkah-langkah untuk menjalankan aplikasi secara lokal menggunakan Git:

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
   - Gradle: gunakan wrapper bawaan (`./gradlew`) tanpa perlu instal manual

4. **Build Project dengan Gradle**
   ```bash
   ./gradlew clean build
   ```

5. **Pastikan Database SQLite Tersedia**
   - File database bernama `meditrack.db` harus berada di direktori utama proyek atau lokasi yang telah ditentukan di kode Java (biasanya `/resources` atau root project).
   - Tidak perlu setup server atau service tambahan.

6. **Jalankan Aplikasi**
   ```bash
   ./gradlew run
   ```

7. **Jika terjadi error JavaFX**
   - Pastikan konfigurasi JavaFX sudah sesuai di `build.gradle` dan `Run Configuration`
   - Alternatif: gunakan SDK yang sudah bundling JavaFX seperti **Liberica JDK Full**.

---

### **2. Melalui Download ZIP dari Halaman Release GitHub**

Jika tidak menggunakan Git, aplikasi bisa dijalankan dari ZIP hasil rilis:

1. **Download ZIP dari Release GitHub**
   - Kunjungi halaman repository GitHub MediTrack
   - Masuk ke tab `Releases`
   - Download file `Source code (zip)` atau file release custom yang disediakan
   - Ekstrak ZIP ke folder lokal

2. **Buka Folder Proyek di IntelliJ / Terminal**
   - Buka folder hasil ekstrak menggunakan IntelliJ IDEA, atau akses lewat terminal

3. **Build Project dengan Gradle**
   ```bash
   ./gradlew build
   ```

4. **Pastikan File `meditrack.db` Ada**
   - Letakkan file `meditrack.db` di root folder proyek atau folder database yang digunakan oleh kode

5. **Jalankan Aplikasi**
   ```bash
   ./gradlew run
   ```



---

## Daftar Modul yang Diimplementasikan
| No | Nama Modul | Assignee | Screenshot |
|----|-------------|----------|------------|
| 1 | Autentikasi | Shali, Eleanor | ![Autentikasi](screenshots/autentikasi) |
| 2 | Menu | Eleanor, Thalita | ![Menu](screenshots/menu) |
| 3 | Manajemen Pengguna | Marzel, Eleanor, Thalita | ![Manajemen Pengguna](screenshots/manajemen_pengguna) |
| 4 | Kondisi Aktual | Thalita | ![Kondisi Aktual](screenshots/kondisi_aktual) |
| 5 | Jadwal | Faiz, Eleanor, Thalita | ![Jadwal](screenshots/jadwal) |
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
