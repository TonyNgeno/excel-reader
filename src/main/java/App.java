;

import com.excel.utilities.DbConnection;
import com.excel.model.Users;
import com.mysql.cj.result.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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

public class App {

    //void read excel file
    List<Users> getUserModels() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File("uploads/users.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<org.apache.poi.ss.usermodel.Row> itr = sheet.iterator();
            List<Users> models = new ArrayList<>();
            int numRow = 0;
            while (itr.hasNext()) {
                if (numRow != 0) {
                    Row row = (Row) itr.next();
                    Users userModel = new Users(
                            row.getCell(0).getStringCellValue(),
                            row.getCell(2).getStringCellValue(),
                            row.getCell(1).getNumericCellValue()
                    );
                    models.add(userModel);
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

    void saveRecords(List<Users> lists) {
        try {
            dbConnection = DbConnection.();
            if (lists == null) {
                return;
            }

            lists.forEach(list -> {
                addToDb(list);
            });

        } catch (SQLException ex) {
            Logger.getLogger(App.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(App.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void addToDb(Users list) {
        try {
            String sql = "INSERT INTO users(name,age,town) VALUES(?,?,?)";
            PreparedStatement ps = dbConnection.connect().;
            ps.setString(1, list.getName());
            ps.setInt(2, list.getAge());
            ps.setString(3, list.getTown());
            dbConnection.execute(ps);

        } catch (SQLException ex) {
            Logger.getLogger(App.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    Test read excel and insert files to db
     */
    public static void main(String[] args) {
        List<UserModel> l = new App().getUserModels();
        System.out.println(l.size());
        new App().saveRecords(l);
    }
}