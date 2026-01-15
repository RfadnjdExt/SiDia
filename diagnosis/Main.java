package diagnosis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    // Global State
    private static List<Penyakit> daftarPenyakit = new ArrayList<>();
    private static List<Gejala> allGejala = new ArrayList<>();
    private static Diagnosa diagnosaEngine;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Load Initial Data
        loadData();
        
        // Initialize Rule Engine
        diagnosaEngine = new RuleDiagnosa(daftarPenyakit);

        // ASCII Banner
        System.out.println("  ___ _ ___  _      ");
        System.out.println(" / __(_)   \\(_)__ _ ");
        System.out.println(" \\__ \\ | |) | / _` |");
        System.out.println(" |___/_|___/|_\\__,_|");
        System.out.println("                    ");
        System.out.println("=== SiDia: Asisten kesehatan AI & dokumentasi yang ramah ===");

        boolean running = true;
        while (running) {
            System.out.println("\n=== MENU UTAMA ===");
            System.out.println("1. Mulai Diagnosis");
            System.out.println("2. Menu Admin (Kelola Penyakit)");
            System.out.println("3. Lihat Riwayat");
            System.out.println("4. Keluar");
            System.out.print("Pilih menu (1-4): ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    runDiagnosis();
                    break;
                case "2":
                    runAdminMenu();
                    break;
                case "3":
                    runViewHistory();
                    break;
                case "4":
                    running = false;
                    System.out.println("Terima kasih telah menggunakan SiDia. Semoga sehat selalu!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        }
        
        scanner.close();
    }

    private static void loadData() {
        daftarPenyakit.clear();
        allGejala.clear();
        
        try (Scanner fileScanner = new Scanner(new File("penyakit.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(";");
                if (parts.length < 3) continue; // Ensure we have Name, Symptoms, and Advice

                String namaPenyakit = parts[0];
                String[] gejalaNames = parts[1].split(",");
                String saran = parts[2];
                
                List<Gejala> gejalaList = new ArrayList<>();

                for (String gName : gejalaNames) {
                    Gejala g = new Gejala(gName.trim());
                    gejalaList.add(g);
                    
                    // Add to unique list of all symptoms if not exists
                    boolean exists = false;
                    for(Gejala existing : allGejala) {
                        if(existing.getNama().equalsIgnoreCase(g.getNama())) {
                            exists = true;
                            break;
                        }
                    }
                    if(!exists) allGejala.add(g);
                }
                daftarPenyakit.add(new Penyakit(namaPenyakit, gejalaList, saran));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: File database 'penyakit.txt' tidak ditemukan. Membuat baru...");
            try {
                new File("penyakit.txt").createNewFile();
            } catch (IOException ioException) {
                System.out.println("Gagal membuat file penyakit.txt");
            }
        }
    }

    private static void runDiagnosis() {
        if (daftarPenyakit.isEmpty()) {
            System.out.println("Database penyakit masih kosong. Silakan tambah data terlebih dahulu.");
            return;
        }

        System.out.println("\n--- Mulai Diagnosis ---");
        System.out.print("Masukkan Nama Anda: ");
        String namaPasien = scanner.nextLine();
        System.out.print("Masukkan Usia Anda: ");
        String usiaPasien = scanner.nextLine();
        
        System.out.println("\nHalo " + namaPasien + ", mari kita cek kesehatan Anda.");
        System.out.println("Jawab pertanyaan berikut dengan 'y' (ya) atau 'n' (tidak).");

        List<String> gejalaInput = new ArrayList<>();

        for (Gejala g : allGejala) {
            System.out.print("Apakah Anda mengalami " + g.getNama() + "? (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) {
                gejalaInput.add(g.getNama());
            }
        }

        String hasil = diagnosaEngine.cekPenyakit(gejalaInput);

        System.out.println("\n=== Hasil Diagnosa ===");
        System.out.println("Nama: " + namaPasien);
        System.out.println("Usia: " + usiaPasien);
        System.out.println("Diagnosa: " + hasil);
        
        // History Saving
        try (PrintWriter writer = new PrintWriter(new FileWriter("riwayat_pasien.txt", true))) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
            writer.println(dtf.format(now) + " | " + namaPasien + " | " + usiaPasien + " | " + hasil.replace("\nSaran:", " | Saran:"));
            System.out.println("(Rekam medis telah disimpan ke riwayat_pasien.txt)");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan riwayat: " + e.getMessage());
        }
    }

    private static void runAdminMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- ADMIN: KELOLA PENYAKIT ---");
            System.out.println("1. Lihat Daftar Penyakit");
            System.out.println("2. Tambah Penyakit Baru");
            System.out.println("3. Hapus Penyakit");
            System.out.println("4. Kembali ke Menu Utama");
            System.out.print("Pilih menu admin (1-4): ");
            
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    runListDiseases();
                    break;
                case "2":
                    runAddDisease();
                    break;
                case "3":
                    runDeleteDisease();
                    break;
                case "4":
                    back = true;
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void runListDiseases() {
        System.out.println("\n--- Daftar Penyakit Terdaftar ---");
        if (daftarPenyakit.isEmpty()) {
            System.out.println("(Kosong)");
            return;
        }
        for (int i = 0; i < daftarPenyakit.size(); i++) {
            Penyakit p = daftarPenyakit.get(i);
            System.out.println((i + 1) + ". " + p.getNama());
        }
    }

    private static void runAddDisease() {
        System.out.println("\n--- Tambah Data Penyakit ---");
        System.out.print("Masukkan Nama Penyakit: ");
        String nama = scanner.nextLine();
        
        System.out.print("Masukkan Gejala (pisahkan dengan koma, cth: demam,batuk): ");
        String gejala = scanner.nextLine();
        
        System.out.print("Masukkan Saran Pengobatan: ");
        String saran = scanner.nextLine();
        
        // Format: NamaPenyakit;Gejala1,Gejala2,Gejala3;SaranPengobatan
        String newEntry = nama + ";" + gejala + ";" + saran;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter("penyakit.txt", true))) {
            writer.println(newEntry);
            System.out.println("Data penyakit berhasil ditambahkan!");
            
            // Reload & Refresh
            loadData();
            diagnosaEngine = new RuleDiagnosa(daftarPenyakit);
            
        } catch (IOException e) {
            System.out.println("Gagal menyimpan data penyakit: " + e.getMessage());
        }
    }

    private static void runDeleteDisease() {
        System.out.println("\n--- Hapus Data Penyakit ---");
        runListDiseases();
        if (daftarPenyakit.isEmpty()) return;

        System.out.print("Masukkan NOMOR penyakit yang ingin dihapus (0 untuk batal): ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index == -1) return;

            if (index >= 0 && index < daftarPenyakit.size()) {
                Penyakit removed = daftarPenyakit.remove(index);
                System.out.println("Menghapus " + removed.getNama() + "...");
                
                // Rewrite file
                saveAllDataToFile();
                
                // Refresh
                loadData();
                diagnosaEngine = new RuleDiagnosa(daftarPenyakit);
                System.out.println("Data berhasil dihapus.");
            } else {
                System.out.println("Nomor tidak valid.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka.");
        }
    }

    private static void saveAllDataToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("penyakit.txt"))) {
            for (Penyakit p : daftarPenyakit) {
                // Now using the helper method in Penyakit.java
                String gejalaStr = p.getGejalaStr(); 
                writer.println(p.getNama() + ";" + gejalaStr + ";" + p.getSaran());
            }
        } catch (IOException e) {
            System.out.println("Gagal rewrite file: " + e.getMessage());
        }
    }

    private static void runViewHistory() {
        System.out.println("\n--- Riwayat Pasien ---");
        File file = new File("riwayat_pasien.txt");
        if (!file.exists()) {
            System.out.println("Belum ada riwayat terekam.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Gagal membaca riwayat: " + e.getMessage());
        }
    }
}
