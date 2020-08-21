package com.excel.apps;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.excel.model.Users;
import com.excel.utilities.DbConnection;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletContext;


public class App {

    String fileName;

    public App(String fileName, DbConnection dbConnection) {
        this.fileName = fileName;
        this.dbConnection = dbConnection;
    }

    public App(String name) {
    }

    //
    public void saveData() {
        saveRecords(getUserModels());
    }

    //void read excel file
    private List<Users> getUserModels() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(getFileName()));
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> itr = sheet.iterator();
            List<Users> models = new ArrayList<>();
            int numRow = 0;
            while (itr.hasNext()) {
                if (numRow != 0) {
                    Row row = itr.next();
                    Users user = new Users(
                            row.getCell(0).getStringCellValue(),
                            (int) row.getCell(1).getNumericCellValue(),
                            row.getCell(2).getStringCellValue()
                    );
                    models.add(user);
                } else {
                    itr.next();
                    numRow++;
                }
            }
            return models;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(App.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(App.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(App.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    //
    DbConnection dbConnection;

    private void saveRecords(List<Users> lists) {

            lists.forEach(list -> {
                addToDb(list);
            });

    }

    private void addToDb(Users list) {
        try {
            DbConnection dbConnection = null;

            PreparedStatement statement = dbConnection.connect().prepareStatement("insert into users(name, age, town) values(?, ?, ?)");
            statement.setString(1, list.getName());
            statement.setInt(2, list.getAge());
            statement.setString(3, list.getTown());
            statement.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(App.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public static void main(String[] args) {
        List<Users> l = new App("uploads/users.xlsx", null).getUserModels();
        System.out.println(l.size());
        new App("uploads/users.xlsx", null).saveRecords(l);
    }
}
