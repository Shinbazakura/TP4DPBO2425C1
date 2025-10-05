import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class ProductMenu extends JFrame {
    public static void main(String[] args) {
        // buat object window
        ProductMenu menu = new ProductMenu();

        // atur ukuran window
        menu.setSize(750, 700);

        // letakkan window di tengah layar
        menu.setLocationRelativeTo(null);

        // isi window
        menu.setContentPane(menu.mainPanel);

        // ubah warna background
        menu.getContentPane().setBackground(Color.WHITE);

        // tampilkan window
        menu.setVisible(true);

        // agar program ikut berhenti saat window diclose
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua produk
    private ArrayList<Product> listProduct;

    private JPanel mainPanel;
    private JTextField idField;
    private JTextField judulField;
    private JTextField hargaField;
    private JTable productTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox<String> genreComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel idLabel;
    private JLabel judulLabel;
    private JLabel hargaLabel;
    private JLabel genreLabel;
    private JLabel penulisLabel;
    private JTextField penulisField;
    private JLabel tahunTerbitLabel;
    private JSlider tahunTerbitSlider;
    private JLabel formatLabel;
    private JComboBox formatComboBox;
    private JLabel curYearLabel;

    // constructor
    public ProductMenu() {
        // inisialisasi listProduct
        listProduct = new ArrayList<>();

        // isi listProduct
        populateList();

        // isi tabel produk
        productTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] genreData = {"???", "Programming", "Fantasy", "Self-Help", "Finance", "Classic Literature", "Epic Poetry", "Philosophy", "Sci-Fi"};
        genreComboBox.setModel(new DefaultComboBoxModel<>(genreData));
        String[] formatData = {"???", "Hardcover", "Paperback", "eBook", "Audiobook"};
        formatComboBox.setModel(new DefaultComboBoxModel<>(formatData));

        // prepare slider
        // define year limit
        int startYear = 1800;
        int endYear = 2050;
        int defaultValue = 2025;
        // Create slider
        tahunTerbitSlider.setMinimum(startYear);
        tahunTerbitSlider.setMaximum(endYear);
        tahunTerbitSlider.setValue(defaultValue); // default year
        tahunTerbitSlider.setPaintTicks(true);
        tahunTerbitSlider.setPaintLabels(true);
        tahunTerbitSlider.setMajorTickSpacing(50);
        tahunTerbitSlider.setMinorTickSpacing(10);

        curYearLabel.setText("Tahun: " + defaultValue);




        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1){
                    insertData();
                } else {
                    updateData();
                }
            }
        });
        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: tambahkan konfirmasi sebelum menghapus data
                int result = JOptionPane.showConfirmDialog(
                        null,
                        "Yakin data dihapus ?",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });
        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        // saat salah satu baris tabel ditekan
        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = productTable.getSelectedRow();

                // simpan value textfield dan combo box
                String curId = productTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = productTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curHarga = productTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curGenre = productTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curPenulis = productTable.getModel().getValueAt(selectedIndex, 5).toString();
                String curTahunTerbit = productTable.getModel().getValueAt(selectedIndex, 6).toString();
                String curFormat = productTable.getModel().getValueAt(selectedIndex, 7).toString();

                // ubah isi textfield dan combo box
                idField.setText(curId);
                judulField.setText(curNama);
                hargaField.setText(curHarga);
                genreComboBox.setSelectedItem(curGenre);
                penulisField.setText(curPenulis);
                // tahunTerbitSlider.setValue(curTahunTerbit);
                try {
                    int year = Integer.parseInt(curTahunTerbit);
                    tahunTerbitSlider.setValue(year);
                    curYearLabel.setText("Tahun: " + year); // also update label
                } catch (NumberFormatException ex) {
                    System.err.println("Invalid year in table: " + curTahunTerbit);
                }
                formatComboBox.setSelectedItem(curFormat);

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("update");

                // tampilkan button delete
                deleteButton.setVisible(true);

            }
        });

        // ubah curYearLabel ketika value slider tahunTerbitSlider berubah
        tahunTerbitSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedYear = tahunTerbitSlider.getValue();
                curYearLabel.setText("Tahun: " + selectedYear);
            }
        });
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = { "No", "ID Produk", "Judul", "Harga", "Genre", "Penulis", "Tahun Terbit", "Format" };

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel tmp = new DefaultTableModel(null , cols);

        // isi tabel dengan listProduct
        for (int i = 0; i < listProduct.size(); i++) {
            Object[] row = { i + 1,
                    listProduct.get(i).getId(),
                    listProduct.get(i).getJudul(),
                    String.format( "%.2f", listProduct.get(i).getHarga()),
                    listProduct.get(i).getGenre(),
                    listProduct.get(i).getPenulis(),
                    listProduct.get(i).getTahunTerbit(),
                    listProduct.get(i).getFormat()
            };
            tmp.addRow(row);
        }

        return tmp; // return juga harus diganti
    }

    public void insertData() {
        try {
            // ambil value dari textfield dan combobox
            String id = idField.getText();
            String nama = judulField.getText();
            double harga = Double.parseDouble(hargaField.getText());
            String genre = genreComboBox.getSelectedItem().toString();
            String penulis = penulisField.getText();
            int tahunTerbit = tahunTerbitSlider.getValue();
            String format = formatComboBox.getSelectedItem().toString();


            // tambahkan data ke dalam list
            listProduct.add(new Product(id, nama, harga, genre, penulis, tahunTerbit, format));

            // update tabel
            productTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Harga harus berupa angka !", "Erroг", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateData() {
        try {
            // ambil data dari form
            String id = idField.getText();
            String nama = judulField.getText();
            double harga = Double.parseDouble(hargaField.getText());
            String genre = genreComboBox.getSelectedItem().toString();
            String penulis = penulisField.getText();
            int tahunTerbit = tahunTerbitSlider.getValue();
            String format = formatComboBox.getSelectedItem().toString();

            // ubah data produk di list
            listProduct.get(selectedIndex).setId(id);
            listProduct.get(selectedIndex).setJudul(nama);
            listProduct.get(selectedIndex).setHarga (harga);
            listProduct.get(selectedIndex).setGenre(genre);
            listProduct.get(selectedIndex).setPenulis(penulis);
            listProduct.get(selectedIndex).setTahunTerbit(tahunTerbit);
            listProduct.get(selectedIndex).setFormat(format);

            // update tabel
            productTable.setModel(setTable());

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog( null, "Data berhasil diubah");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog( null, "Harga harus berupa angka!", "Error", JOptionPane. ERROR_MESSAGE);
        }
    }


    public void deleteData() {
        // hapus data dari list
        listProduct.remove(selectedIndex);

        // update tabel
        productTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Delete Berhasil");
        JOptionPane.showMessageDialog( null, "Data berhasil dihapus");

    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        idField.setText("");
        judulField.setText("");
        hargaField.setText("");
        genreComboBox.setSelectedIndex(0);
        penulisField.setText("");
        tahunTerbitSlider.setValue(2025);
        formatComboBox.setSelectedIndex(0);

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }

    // panggil prosedur ini untuk mengisi list produk
    private void populateList() {
        listProduct.add(new Product(
                "B001",
                "Clean Code",
                450000.0,
                "Programming",
                "Robert C. Martin",
                2008,
                "Paperback"
        ));

        listProduct.add(new Product(
                "B002",
                "The Pragmatic Programmer",
                500000.0,
                "Programming",
                "Andrew Hunt, David Thomas",
                1999,
                "Hardcover"
        ));

        listProduct.add(new Product(
                "B003",
                "Harry Potter and the Sorcerer's Stone",
                150000.0,
                "Fantasy",
                "J.K. Rowling",
                1997,
                "Paperback"
        ));

        listProduct.add(new Product(
                "B004",
                "Atomic Habits",
                175000.0,
                "Self-Help",
                "James Clear",
                2018,
                "Hardcover"
        ));

        listProduct.add(new Product(
                "B005",
                "The Psychology of Money",
                160000.0,
                "Finance",
                "Morgan Housel",
                2020,
                "Paperback"
        ));

        listProduct.add(new Product(
                "B006",
                "Pride and Prejudice",
                250000.0,
                "Classic Literature",
                "Jane Austen",
                1813,
                "Hardcover"
        ));

        listProduct.add(new Product(
                "B007",
                "1984",
                270000.0,
                "Classic Literature",
                "George Orwell",
                1949,
                "Paperback"
        ));

        listProduct.add(new Product(
                "B008",
                "To Kill a Mockingbird",
                260000.0,
                "Classic Literature",
                "Harper Lee",
                1960,
                "Paperback"
        ));

        listProduct.add(new Product(
                "B009",
                "The Great Gatsby",
                240000.0,
                "Classic Literature",
                "F. Scott Fitzgerald",
                1925,
                "Paperback"
        ));

        listProduct.add(new Product(
                "B010",
                "Moby-Dick; or, The Whale",
                280000.0,
                "Classic Literature",
                "Herman Melville",
                1851,
                "Hardcover"
        ));

        listProduct.add(new Product(
                "B011",
                "Crime and Punishment",
                300000.0,
                "Classic Literature",
                "Fyodor Dostoevsky",
                1866,
                "Paperback"
        ));

        listProduct.add(new Product(
                "B012",
                "The Catcher in the Rye",
                250000.0,
                "Classic Literature",
                "J.D. Salinger",
                1951,
                "Paperback"
        ));

        listProduct.add(new Product(
                "B013",
                "The Adventures of Sherlock Holmes",
                270000.0,
                "Classic Literature",
                "Arthur Conan Doyle",
                1892,
                "Hardcover"
        ));

        listProduct.add(new Product(
                "B014",
                "Wuthering Heights",
                260000.0,
                "Classic Literature",
                "Emily Brontë",
                1847,
                "Paperback"
        ));

        listProduct.add(new Product(
                "B015",
                "Frankenstein; or, The Modern Prometheus",
                250000.0,
                "Classic Literature",
                "Mary Shelley",
                1818,
                "Paperback"
        ));

        listProduct.add(new Product(
                "B016",
                "The Hobbit",
                280000.0,
                "Fantasy",
                "J.R.R. Tolkien",
                1937,
                "Hardcover"
        ));

        listProduct.add(new Product(
                "B017",
                "The Lord of the Rings",
                600000.0,
                "Fantasy",
                "J.R.R. Tolkien",
                1954,
                "Hardcover"
        ));

    }
}