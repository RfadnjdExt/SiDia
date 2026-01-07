package diagnosis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // --- Mhs 2 (Rule & Knowledge Base) Setup ---
        List<Penyakit> daftarPenyakit = new ArrayList<>();
        List<Gejala> allGejala = new ArrayList<>();

        // Load Data from File
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
            System.out.println("Error: File database 'penyakit.txt' tidak ditemukan.");
            return;
        }

        // Initialize Rule Engine
        Diagnosa diagnosaEngine = new RuleDiagnosa(daftarPenyakit);

        // --- Mhs 1 (Input) ---
        Scanner scanner = new Scanner(System.in);
        
        // ASCII Logo
        System.out.println("   _____  .__                                              ");
        System.out.println("  /  _  \\ |__|   |__| ____  _____      ____  ____  _____   ");
        System.out.println(" /  /_\\  \\|  |   |  |/  _ \\/     \\   _/ ___\\/  _ \\/     \\  ");
        System.out.println("/    |    \\  |   |  (  <_> )  Y Y  \\  \\  \\(  <_> )  Y Y  \\ ");
        System.out.println("\\____|__  /__/___|__|\\____/|__|_|  /   \\___>____/|__|_|  / ");
        System.out.println("        \\/  /_____/              \\/                   \\/  ");
        System.out.println("             (Robot Dr. AI - Siap Mendiagnosa)            \n");
        
        System.out.println("=== Sistem Diagnosa Penyakit (Premium) ===");
        
        // Final Polish: Patient Identity
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

        // --- Process ---
        String hasil = diagnosaEngine.cekPenyakit(gejalaInput);

        // --- Mhs 3 (Output) ---
        System.out.println("\n=== Hasil Diagnosa ===");
        System.out.println("Nama: " + namaPasien);
        System.out.println("Usia: " + usiaPasien);
        System.out.println("Diagnosa: " + hasil);
        
        // Final Polish: History Saving
        try (PrintWriter writer = new PrintWriter(new FileWriter("riwayat_pasien.txt", true))) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
            writer.println(dtf.format(now) + " | " + namaPasien + " | " + usiaPasien + " | " + hasil.replace("\nSaran:", " | Saran:"));
            System.out.println("\n(Rekam medis telah disimpan ke riwayat_pasien.txt)");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan riwayat: " + e.getMessage());
        }
        
        scanner.close();
    }
}
