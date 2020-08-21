package com.excel.servlets;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.Console;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

@WebServlet("/users")
public class UsersServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servcont = getServletContext();
        Connection dbConnection = (Connection) servcont.getAttribute("dbConnection");
        response.setContentType("text/html");
        String excelFilePath = "./users.xlsx";
        int batchSize = 20;
        Connection connection = null;
        try {
            FileInputStream inputStream = new FileInputStream(excelFilePath);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            PreparedStatement statement = dbConnection.prepareStatement("insert into users(name, age, town) values(?, ?, ?)");

            int count = 0;

            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();

                    int columnIndex = nextCell.getColumnIndex();

                    switch (columnIndex) {
                        case 0:
                            String name = nextCell.getStringCellValue();
                            statement.setString(1, name);
                            break;
                        case 1:
                            int age = (int) nextCell.getNumericCellValue();
                            statement.setInt(2, age);
                            break;
                        case 2:
                            String town = nextCell.getStringCellValue();
                            statement.setString(3, town);
                            break;
                    }

                }
                statement.addBatch();
                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }

            workbook.close();
            statement.executeBatch();
            dbConnection.commit();
            dbConnection.close();

            rowIterator.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        ServletContext context = getServletContext();


    }

    protected  void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ServletContext scx = getServletContext();
        Connection dbConnection = (Connection) scx.getAttribute("dbConnection");

        String name = request.getParameter("name");
        int age = Integer.parseInt(request.getParameter("age"));
        String town = request.getParameter("town");





        try {
            PreparedStatement statement = dbConnection.prepareStatement("insert into users(name, age, town) values(?, ?, ?)");
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, town);
            statement.executeUpdate();

            response.getWriter().print("OK");

        }catch (SQLException sqlEx){
            sqlEx.printStackTrace();
        }

    }

}