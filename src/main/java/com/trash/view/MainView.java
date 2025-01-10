package com.trash.view;

import com.trash.controller.HistoryController;
import com.trash.model.HistoryModel;
import com.trash.controller.WasteController;
import com.trash.model.RequestPickupModel;
import com.trash.model.WasteModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainView extends JFrame {

    public MainView() {
        setTitle("e-Waste");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        // Panel utama dengan layout GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.white); // Latar belakang hitam
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spasi antar elemen
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Menambahkan label judul
        JLabel lblTitle = new JLabel("e-WASTE", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitle.setForeground(new Color(107, 142, 35));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(lblTitle, gbc);

        // Tombol-tombol dengan GridBagConstraints yang rapi
        addButton(panel, gbc, "Add Waste", e -> openAddWasteDialog(), 1);
        addButton(panel, gbc, "View Waste", e -> openViewWasteDialog(), 2);
        addButton(panel, gbc, "Search Waste", e -> openSearchWasteDialog(), 3);
        addButton(panel, gbc, "Generate Report", e -> generatePdfReport(), 4);
        addButton(panel, gbc, "View Summary", e -> openSummaryDialog(), 5);
        addButton(panel, gbc, "Refresh", e -> refreshView(), 6);
        addButton(panel, gbc, "View History", e -> openHistoryDialog(), 7);
        addButton(panel, gbc, "Request Pickup", e -> openRequestPickupDialog(), 8);

        // Tambahkan panel ke frame
        add(panel, BorderLayout.CENTER);
    }

    // Fungsi untuk menambahkan tombol secara dinamis dengan gaya yang konsisten
    private void addButton(JPanel panel, GridBagConstraints gbc, String text,
                           java.awt.event.ActionListener actionListener, int gridY) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        styleButton(button);
        gbc.gridy = gridY;
        panel.add(button, gbc);
    }

    // Fungsi untuk menata tombol dengan warna dan desain konsisten
    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(250, 50)); // Ukuran tombol lebih besar
        button.setBackground(new Color(107, 142, 35)); // Warna hijau muda
        button.setForeground(Color.WHITE); // Warna teks putih
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }





    private JButton createButton(String text, java.awt.event.ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        button.setPreferredSize(new Dimension(200, 40)); // Same size for all buttons
        return button;
    }

    private void refreshView() {
        WasteController controller = new WasteController();
        List<WasteModel> wasteList = controller.getAllWaste();
        JOptionPane.showMessageDialog(this, "Data refreshed!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private void generatePdfReport() {
        WasteController controller = new WasteController();
        String filePath = "e_waste_report.pdf";
        controller.generatePdfReport(filePath);
        JOptionPane.showMessageDialog(this, "PDF Report generated at " + filePath, "Report Generated", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openSearchWasteDialog() {
        JDialog dialog = new JDialog(this, "Search Waste", true);
        dialog.setSize(600, 400);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(new Color(107, 142, 35));
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        searchPanel.setBackground(new Color(107, 142, 35));


        JLabel lblKeyword = new JLabel("Keyword:");
        searchPanel.setBackground(new Color(107, 142, 35));
        JTextField txtKeyword = new JTextField(20);
        searchPanel.setBackground(new Color(107, 142, 35));
        JButton btnSearch = new JButton("Search");
        searchPanel.setBackground(new Color(107, 142, 35));
        btnSearch.setFocusPainted(false);

        searchPanel.add(lblKeyword);
        searchPanel.add(txtKeyword);
        searchPanel.add(btnSearch);

        String[] columnNames = {"ID", "Category", "Description"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setBackground(Color.WHITE); // Set background of the table
        table.setForeground(Color.BLACK);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(107, 142, 35)); // Set background color for the column headers
        tableHeader.setForeground(Color.WHITE);

        btnSearch.addActionListener(e -> {
            String keyword = txtKeyword.getText();
            if (!keyword.isEmpty()) {
                WasteController controller = new WasteController();
                List<WasteModel> searchResults = controller.searchWaste(keyword);
                tableModel.setRowCount(0); // Clear previous results
                for (WasteModel waste : searchResults) {
                    Object[] row = {waste.getId(), waste.getCategory(), waste.getDescription()};
                    tableModel.addRow(row);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please enter a keyword to search.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        dialog.add(searchPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openAddWasteDialog() {
        JDialog dialog = new JDialog(this, "Add Waste", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(3, 2));
        dialog.getContentPane().setBackground(new Color(107, 142, 35)); // Hijau tua

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setForeground(Color.WHITE);
        JTextField txtCategory = new JTextField();
        txtCategory.setBackground(new Color(107, 142, 35)); // Latar belakang hijau muda
        txtCategory.setForeground(Color.white);
        JLabel lblDescription = new JLabel("Description:");
        lblDescription.setForeground(Color.WHITE);
        JTextField txtDescription = new JTextField();
        txtDescription.setBackground(new Color(107, 142, 35)); // Latar belakang hijau muda
        txtDescription.setForeground(Color.white);
        JButton btnSave = new JButton("Save");
        styleButton(btnSave);

        btnSave.addActionListener(e -> {
            String categoryName = txtCategory.getText().trim();  // Menghilangkan spasi ekstra
            String description = txtDescription.getText().trim();  // Menghilangkan spasi ekstra

            if (categoryName.isEmpty() || description.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                WasteController controller = new WasteController();
                int categoryId = controller.getCategoryIdByName(categoryName);

                if (categoryId == -1) {
                    JOptionPane.showMessageDialog(dialog, "Category not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                WasteModel waste = new WasteModel(categoryId, description);



                if (controller.addWaste(waste)) {
                    JOptionPane.showMessageDialog(dialog, "Waste added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add waste.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });



        dialog.add(lblCategory);
        dialog.add(txtCategory);
        dialog.add(lblDescription);
        dialog.add(txtDescription);
        dialog.add(new JLabel());
        dialog.add(btnSave);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    private void openViewWasteDialog() {
        JDialog dialog = new JDialog(this, "View Waste", true);
        dialog.setSize(600, 400);

        String[] columnNames = {"ID", "Category", "Description"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(107, 142, 35)); // Set background color for the column headers
        tableHeader.setForeground(Color.WHITE);
        WasteController controller = new WasteController();
        List<WasteModel> wasteList = controller.getAllWaste();

        // Menambahkan data ke tabel dengan kategori dan deskripsi yang benar
        for (WasteModel waste : wasteList) {
            Object[] row = {waste.getId(), waste.getCategory(), waste.getDescription()};
            tableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnEdit = new JButton("Edit");
        btnEdit.setBackground(new Color(107, 142, 35));
        btnEdit.setForeground(Color.WHITE);
        JButton btnDelete = new JButton("Delete");
        btnDelete.setBackground(new Color(255, 74, 0));
        btnDelete.setForeground(Color.WHITE);

        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String categoryName = (String) tableModel.getValueAt(selectedRow, 1);
                String description = (String) tableModel.getValueAt(selectedRow, 2);

                String newCategoryName = JOptionPane.showInputDialog(dialog, "Update Category:", categoryName);
                String newDescription = JOptionPane.showInputDialog(dialog, "Update Description:", description);

                if (newCategoryName != null && newDescription != null) {
                    int categoryId = controller.getCategoryIdByName(newCategoryName);

                    if (categoryId == -1) {
                        JOptionPane.showMessageDialog(dialog, "Category not found!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (controller.updateWaste(id, categoryId, newDescription)) {
                        JOptionPane.showMessageDialog(dialog, "Waste updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        tableModel.setValueAt(newCategoryName, selectedRow, 1);
                        tableModel.setValueAt(newDescription, selectedRow, 2);
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to update waste.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a row to edit.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                WasteController controller1 = new WasteController();
                if (controller1.deleteWaste(id)) {
                    JOptionPane.showMessageDialog(dialog, "Waste deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.removeRow(selectedRow);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to delete waste.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a row to delete.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openRequestPickupDialog() {
        JDialog dialog = new JDialog(this, "Request Pickup", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(7, 2));
        dialog.getContentPane().setBackground(new Color(107, 142, 35));

        WasteController wasteController = new WasteController();
        List<String> categories = wasteController.getUniqueCategories();
        JComboBox<String> cmbWasteCategory = new JComboBox<>();
        for (String category : categories) {
            cmbWasteCategory.addItem(category);
        }

        // Komponen lainnya
        JLabel lblWasteCategory = new JLabel("Waste Category:");
        lblWasteCategory.setForeground(Color.WHITE);
        JLabel lblDescription = new JLabel("Waste Description:");
        lblDescription.setForeground(Color.WHITE);
        JTextArea txtDescription = new JTextArea();
        txtDescription.setRows(3);
        JScrollPane scrollDescription = new JScrollPane(txtDescription);
        txtDescription.setBackground(new Color(107, 142, 35)); // Light blue background
        txtDescription.setForeground(Color.white);
        JLabel lblWeight = new JLabel("Weight:");
        lblWeight.setForeground(Color.WHITE);
        JTextField txtWeight = new JTextField();
        txtWeight.setBackground(new Color(107, 142, 35)); // Light blue background
        txtWeight.setForeground(Color.white);
        JLabel lblRequestDate = new JLabel("Request Date:");
        lblRequestDate.setForeground(Color.WHITE);
        JTextField txtRequestDate = new JTextField();
        txtRequestDate.setBackground(new Color(107, 142, 35)); // Light blue background
        txtRequestDate.setForeground(Color.white);
        JLabel lblAddress = new JLabel("Address:");
        lblAddress.setForeground(Color.WHITE);
        JTextField txtAddress = new JTextField();
        txtAddress.setBackground(new Color(107, 142, 35)); // Light blue background
        txtAddress.setForeground(Color.white);

        JButton btnRequest = new JButton("Request Pickup");
        btnRequest.setBackground(new Color(39, 77, 12)); // Red background
        btnRequest.setForeground(Color.WHITE);

        btnRequest.addActionListener(e -> {
            try {
                String wasteCategory = cmbWasteCategory.getSelectedItem().toString();
                int categoryId = wasteController.getCategoryIdByName(wasteCategory);

                // Validasi waste_id di waste
                if (categoryId == -1) {
                    JOptionPane.showMessageDialog(dialog, "Selected category is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Validasi waste_id ada di tabel waste
                if (!wasteController.isCategoryIdValid(categoryId)) {
                    JOptionPane.showMessageDialog(dialog, "Waste ID not found in waste table!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String description = txtDescription.getText();
                double weight = 0;
                try {
                    weight = Double.parseDouble(txtWeight.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Weight must be a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String requestDate = txtRequestDate.getText();
                String address = txtAddress.getText();

                if (description.isEmpty() || address.isEmpty() || requestDate.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (weight <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Weight must be greater than zero!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Format tanggal
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                try {
                    LocalDate parsedDate = LocalDate.parse(requestDate, formatter);
                    Date sqlDate = Date.valueOf(parsedDate);

                    // Insert data ke requestpickup
                    RequestPickupModel request = new RequestPickupModel(categoryId, description, weight, sqlDate, address);
                    boolean success = wasteController.addRequestPickup(request);
                    if (success) {
                        JOptionPane.showMessageDialog(dialog, "Pickup request added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Failed to request pickup.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid date format! Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "An unexpected error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });



        // Menambahkan komponen ke dialog
        dialog.add(lblWasteCategory);
        dialog.add(cmbWasteCategory);
        dialog.add(lblDescription);
        dialog.add(scrollDescription);
        dialog.add(lblWeight);
        dialog.add(txtWeight);
        dialog.add(lblRequestDate);
        dialog.add(txtRequestDate);
        dialog.add(lblAddress);
        dialog.add(txtAddress);
        dialog.add(new JLabel());  // Kosongkan sel untuk pemisahan
        dialog.add(btnRequest);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }



    private void openHistoryDialog() {
        JDialog dialog = new JDialog(this, "View History", true);
        dialog.setSize(600, 400);
        dialog.getContentPane().setBackground(new Color(107, 142, 35));

        String[] columnNames = {"ID", "Waste Category", "Description", "Weight", "Request Date", "Address"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(new Color(107, 142, 35)); // Set header background color
        tableHeader.setForeground(Color.WHITE);

        HistoryController controller = new HistoryController();
        List<HistoryModel> historyList = controller.getHistory();

        for (HistoryModel history : historyList) {
            Object[] row = {
                    history.getId(),
                    history.getWasteCategory(),
                    history.getDescription(),
                    history.getWeight(),
                    history.getRequestDate(),
                    history.getAddress()
            };
            tableModel.addRow(row);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane);

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }



    private void openSummaryDialog() {
        WasteController controller = new WasteController();
        int totalWaste = controller.getTotalWasteCount();
        List<String> categories = controller.getUniqueCategories();

        StringBuilder summary = new StringBuilder();
        summary.append("Total Waste Entries: ").append(totalWaste).append("\n\n");
        summary.append("Categories: \n");
        for (String category : categories) {
            summary.append("- ").append(category).append("\n");
        }

        JOptionPane.showMessageDialog(this, summary.toString(), "Waste Summary", JOptionPane.INFORMATION_MESSAGE);
    }
}
