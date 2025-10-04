public class Product {
    private String id;
    private String judul;
    private double harga;
    private String genre;
    private String penulis;
    private int tahunTerbit;
    private String format;

    // Constructor with all attributes
    public Product(String id, String judul, double harga, String genre,
                   String penulis, int tahunTerbit, String format) {
        this.id = id;
        this.judul = judul;
        this.harga = harga;
        this.genre = genre;
        this.penulis = penulis;
        this.tahunTerbit = tahunTerbit;
        this.format = format;
    }

    // --- Setters ---
    public void setId(String id) {
        this.id = id;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }

    public void setTahunTerbit(int tahunTerbit) {
        this.tahunTerbit = tahunTerbit;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    // --- Getters ---
    public String getId() {
        return this.id;
    }

    public String getJudul() {
        return this.judul;
    }

    public double getHarga() {
        return this.harga;
    }

    public String getGenre() {
        return this.genre;
    }

    public String getPenulis() {
        return this.penulis;
    }

    public int getTahunTerbit() {
        return this.tahunTerbit;
    }

    public String getFormat() {
        return this.format;
    }
}
