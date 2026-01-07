from abc import ABC, abstractmethod

class Diagnosa(ABC):
    @abstractmethod
    def cek_penyakit(self, gejala_input):
        pass


class Penyakit:
    def __init__(self, nama, gejala):
        self.__nama = nama
        self.__gejala = gejala

    def get_nama(self):
        return self.__nama

    def cocok(self, gejala_input):
        return all(g in gejala_input for g in self.__gejala)


class RuleDiagnosa(Diagnosa):
    def __init__(self, daftar_penyakit):
        self.__daftar_penyakit = daftar_penyakit

    def cek_penyakit(self, gejala_input):
        for penyakit in self.__daftar_penyakit:
            if penyakit.cocok(gejala_input):
                return penyakit.get_nama()
        return "Tidak terdeteksi penyakit yang sesuai."


if __name__ == "__main__":
    flu = Penyakit("Flu", ["demam", "batuk", "pilek"])
    tipes = Penyakit("Tipes", ["demam tinggi", "mual", "lemas"])
    covid = Penyakit("Covid-19", ["demam", "batuk", "sesak nafas"])

    daftar_penyakit = [flu, tipes, covid]

    diagnosa = RuleDiagnosa(daftar_penyakit)

    print("=== Sistem Diagnosa Penyakit ===")
    gejala_input = input("Masukkan gejala (pisahkan dengan koma): ").lower().split(", ")

    hasil = diagnosa.cek_penyakit(gejala_input)
    print("Hasil Diagnosa:", hasil)
