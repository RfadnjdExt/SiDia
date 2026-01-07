package diagnosis;

import java.util.List;

public class RuleDiagnosa implements Diagnosa {
    private List<Penyakit> daftarPenyakit;

    public RuleDiagnosa(List<Penyakit> daftarPenyakit) {
        this.daftarPenyakit = daftarPenyakit;
    }

    @Override
    public String cekPenyakit(List<String> gejalaInput) {
        Penyakit bestMatch = null;
        int maxPersentase = 0;

        for (Penyakit penyakit : daftarPenyakit) {
            int persentase = penyakit.hitungPersentase(gejalaInput);
            if (persentase > maxPersentase) {
                maxPersentase = persentase;
                bestMatch = penyakit;
            }
        }

        // Final Polish: Threshold Check
        if (maxPersentase < 50) {
            return "Gejala tidak spesifik, silakan hubungi dokter.";
        }

        if (bestMatch != null) {
            return bestMatch.getNama() + " (" + maxPersentase + "%)\nSaran: " + bestMatch.getSaran();
        }
        
        return "Tidak terdeteksi penyakit yang sesuai.";
    }
}
