package diagnosis;

import java.util.List;

public class Penyakit {
    private String nama;
    private List<Gejala> daftarGejala;
    private String saran;

    public Penyakit(String nama, List<Gejala> daftarGejala, String saran) {
        this.nama = nama;
        this.daftarGejala = daftarGejala;
        this.saran = saran;
    }

    public String getNama() {
        return nama;
    }

    public String getSaran() {
        return saran;
    }

    public int hitungPersentase(List<String> gejalaInput) {
        int matches = 0;
        for (Gejala gejala : daftarGejala) {
            for (String input : gejalaInput) {
                if (input.trim().equalsIgnoreCase(gejala.getNama().trim())) {
                    matches++;
                    break;
                }
            }
        }
        if (daftarGejala.isEmpty()) return 0;
        return (int) (((double) matches / daftarGejala.size()) * 100);
    }

    public boolean cocok(List<String> gejalaInput) {
         return hitungPersentase(gejalaInput) == 100;
    }
}
