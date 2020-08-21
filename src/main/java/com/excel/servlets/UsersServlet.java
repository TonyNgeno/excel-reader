package com.excel.servlets;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/users")
public class UsersServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ServletContext servcont = getServletContext();

    response.setContentType("text/html");

    String filename = "./WEB-INF/users.xlsx";

    ServletContext context = getServletContext();
    InputStream inp = context.getResourceAsStream(filename);
    Connection dbConnection = (Connection) servcont.getAttribute("dbConnection");

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
