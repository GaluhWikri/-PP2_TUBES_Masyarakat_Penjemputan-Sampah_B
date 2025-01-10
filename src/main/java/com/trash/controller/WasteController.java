
package com.trash.controller;

import com.trash.db.DatabaseConnector;
import com.trash.model.WasteModel;
import com.trash.model.RequestPickupModel;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



public class WasteController {


    // Menambah data waste
    public boolean addWaste(WasteModel waste) {
        String query = "INSERT INTO waste (category_id, description) VALUES (?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Pastikan waste.getCategoryId() dan waste.getDescription() tidak null
            if (waste.getCategoryId() <= 0 || waste.getDescription() == null || waste.getDescription().isEmpty()) {
                System.out.println("Invalid input: Category ID or Description is invalid.");
                return false;
            }

            stmt.setInt(1, waste.getCategoryId());  // Menetapkan ID kategori
            stmt.setString(2, waste.getDescription());  // Menetapkan deskripsi

            // Debugging log
            System.out.println("Executing query: " + query);
            System.out.println("Category ID: " + waste.getCategoryId());
            System.out.println("Description: " + waste.getDescription());


            // Eksekusi query
            int rowsInserted = stmt.executeUpdate();
            System.out.println("Rows inserted: " + rowsInserted); // Log jumlah baris yang terinsert
            return rowsInserted > 0;
        } catch (SQLException e) {
            // Menangani error dan menampilkan lebih banyak informasi untuk debugging
            e.printStackTrace();
            return false;
        }
    }



    // Mengambil semua data waste
    public List<WasteModel> getAllWaste() {
        List<WasteModel> wasteList = new ArrayList<>();
        String query = "SELECT w.id, c.name AS category, w.description FROM waste w JOIN category c ON w.category_id = c.id";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String category = rs.getString("category");
                String description = rs.getString("description");

                // Menambahkan data waste yang sudah lengkap ke dalam list
                wasteList.add(new WasteModel(id, category, description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wasteList;
    }

    // Menghapus data waste
    public boolean deleteWaste(int id) {
        String query = "DELETE FROM waste WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Memperbarui data waste dengan kategori baru
    public boolean updateWaste(int wasteId, int categoryId, String description) {
        String query = "UPDATE waste SET category_id = ?, description = ? WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, categoryId);
            stmt.setString(2, description);
            stmt.setInt(3, wasteId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mencari data waste berdasarkan keyword
    public List<WasteModel> searchWaste(String keyword) {
        List<WasteModel> wasteList = new ArrayList<>();
        String query = "SELECT w.id, c.name AS category, w.description FROM waste w JOIN category c ON w.category_id = c.id WHERE c.name LIKE ? OR w.description LIKE ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String category = rs.getString("category");
                    String description = rs.getString("description");
                    wasteList.add(new WasteModel(id, category, description));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wasteList;
    }

    // Mengekspor laporan PDF
    public void generatePdfReport(String filePath) {
        List<WasteModel> wasteList = getAllWaste();
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            document.add(new Paragraph("Waste Report"));
            document.add(new Paragraph("Generated on: " + new java.util.Date().toString()));
            document.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(3);
            table.addCell("ID");
            table.addCell("Category");
            table.addCell("Description");

            for (WasteModel waste : wasteList) {
                table.addCell(String.valueOf(waste.getId()));
                table.addCell(waste.getCategory());
                table.addCell(waste.getDescription());
            }

            document.add(table);
            document.close();
            System.out.println("PDF Report Generated: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mendapatkan jumlah total waste
    public int getTotalWasteCount() {
        String query = "SELECT COUNT(*) AS total FROM waste";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e)            {
            e.printStackTrace();
        }
        return 0;
    }

    // Mendapatkan daftar kategori unik
    public List<String> getUniqueCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT DISTINCT name FROM category";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    // Mendapatkan id kategori berdasarkan nama kategori
    public int getCategoryIdByName(String categoryName) {
        String query = "SELECT id FROM category WHERE name = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int categoryId = rs.getInt("id");
                System.out.println("Category found with ID: " + categoryId);  // Debugging log
                return categoryId;
            } else {
                System.out.println("Category not found: " + categoryName);  // Debugging log
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;  // Kategori tidak ditemukan
    }

    public boolean isCategoryIdValid(int categoryId) {
        String query = "SELECT COUNT(*) FROM waste WHERE category_id = ?";  // Cek berdasarkan category_id
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, categoryId);  // Gunakan category_id di waste untuk validasi
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Jika lebih dari 0 berarti ID kategori valid di tabel waste
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Kategori tidak ditemukan dalam waste
    }



    public boolean addRequestPickup(RequestPickupModel request) {
        String checkWasteQuery = "SELECT COUNT(*) FROM waste WHERE id = ?";  // Cek apakah waste_id valid
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkWasteQuery)) {
            checkStmt.setInt(1, request.getCategoryId());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // waste_id valid, lanjutkan dengan insert
                String insertQuery = "INSERT INTO requestpickup (waste_id, description, weight, request_date, address) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, request.getCategoryId());
                    insertStmt.setString(2, request.getDescription());
                    insertStmt.setDouble(3, request.getWeight());
                    insertStmt.setDate(4, request.getRequestDate());  // Pastikan menggunakan java.sql.Date
                    insertStmt.setString(5, request.getAddress());
                    int rowsInserted = insertStmt.executeUpdate();
                    return rowsInserted > 0;
                }
            } else {
                // Jika waste_id tidak ditemukan di waste
                System.out.println("Invalid waste_id: " + request.getCategoryId());
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }






}
