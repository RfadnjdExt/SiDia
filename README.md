# 🏥 Sistem Diagnosa Penyakit (AI-Based)

<p align="center">
  <img src="logo.jpg" alt="Logo Sistem Diagnosa Penyakit" width="200"/>
</p>


![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![OOP](https://img.shields.io/badge/OOP-Principles-blue?style=for-the-badge)
![AI](https://img.shields.io/badge/AI-Probability-green?style=for-the-badge)

Sistem pakar sederhana berbasis **Java** untuk mendiagnosa penyakit berdasarkan gejala yang diinputkan pengguna. Proyek ini dirancang untuk memenuhi standar tugas mata kuliah **Pemrograman Berorientasi Objek (OOP)** dengan menerapkan prinsip *Encapsulation*, *Inheritance*, dan *Polymorphism*.

## ✨ Fitur Unggulan (Premium)

1.  **🧠 Kecerdasan Buatan (Probabilitas)**
    *   Menggunakan logika persentase kecocokan (bukan sekadar `if-else`).
    *   Jika gejala cocok sebagian (misal 66%), sistem tetap memberikan hasil dengan tingkat keyakinan tertentu.
    *   **Smart Threshold**: Jika kecocokan < 50%, sistem menyarankan konsultasi ke dokter.

2.  **🗣️ Interaktif & User-Friendly**
    *   Input menggunakan metode tanya-jawab ("Apakah Anda demam? y/n") untuk menghindari kesalahan ketik (typo).
    *   Meminta identitas pasien (Nama & Usia) untuk personalisasi.

3.  **📂 Database Eksternal (`penyakit.txt`)**
    *   Data penyakit, gejala, dan saran pengobatan disimpan di file terpisah.
    *   Anda bisa menambah pengetahuan (knowledge base) baru tanpa perlu mengedit/compile ulang kode Java.

4.  **💾 Rekam Medis Otomatis**
    *   Setiap diagnosa otomatis disimpan ke file `riwayat_pasien.txt` lengkap dengan *timestamp*.

## 🛠️ Struktur Kode (OOP)

Proyek ini membagi peran sesuai prinsip **MVC (Model-View-Controller)** sederhana atau pembagian tugas mahasiswa:

*   **Mhs 1 (Input)**: Menangani interaksi user di `Main.java` (Scanner, Identitas).
*   **Mhs 2 (Logic/Rule)**: Menangani logika diagnosa di `RuleDiagnosa.java`, `Penyakit.java`, `Gejala.java`.
*   **Mhs 3 (Output)**: Menangani tampilan hasil dan penyimpanan file log.

## 🚀 Cara Menjalankan

### Prasyarat
*   Java Development Kit (JDK) 8 atau lebih baru.

### Langkah-langkah

1.  **Clone Repository**
    ```bash
    git clone https://github.com/RfadnjdExt/Diagnosa-Penyakit-AI.git
    cd Diagnosa-Penyakit-AI
    ```

2.  **Compile Kode**
    ```bash
    javac diagnosis/*.java
    ```

3.  **Jalankan Program**
    ```bash
    java diagnosis.Main
    ```

## 📝 Format Data (`penyakit.txt`)

Untuk menambah penyakit baru, edit file `penyakit.txt` dengan format:
```text
NamaPenyakit;Gejala1,Gejala2,Gejala3;SaranPengobatan
```

**Contoh:**
```text
Flu;demam,batuk,pilek;Istirahat cukup dan minum vitamin C
Tipes;demam tinggi,mual,lemas;Hindari makanan pedas dan istirahat total
```

## 📸 Contoh Penggunaan

```text
=== Sistem Diagnosa Penyakit (Premium) ===
Masukkan Nama Anda: Budi
Masukkan Usia Anda: 25

Halo Budi, mari kita cek kesehatan Anda.
Jawab pertanyaan berikut dengan 'y' (ya) atau 'n' (tidak).
Apakah Anda mengalami demam? (y/n): y
Apakah Anda mengalami batuk? (y/n): y
Apakah Anda mengalami pilek? (y/n): y

=== Hasil Diagnosa ===
Nama: Budi
Usia: 25
Diagnosa: Flu (100%)
Saran: Istirahat cukup dan minum vitamin C

(Rekam medis telah disimpan ke riwayat_pasien.txt)
```

## 👥 Kontributor
*   **Developer**: [RfadnjdExt](https://github.com/RfadnjdExt)

---
*Dibuat untuk tujuan edukasi.*
