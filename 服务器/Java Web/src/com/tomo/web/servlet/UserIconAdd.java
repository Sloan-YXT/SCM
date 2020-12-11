package com.tomo.web.servlet;

import com.tomo.common.DaoFactory;
import com.tomo.dao.IconDao;
import com.tomo.dao.UsersDao;
import com.tomo.entity.Icon;
import com.tomo.entity.Users;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UserIconAdd extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post");
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        System.out.println("name:"+username);
        String path = req.getParameter("path");
        System.out.println("path:"+path);
        UsersDao userDao= DaoFactory.getInstance("usersDao",UsersDao.class);
        Users user = userDao.findUnique("select * from users where username = ?",username);
        int userid = user.getUserId();
        Icon icon = new Icon();
        icon.setUserid(userid);
        icon.setPath(path);
        IconDao iconDao = DaoFactory.getInstance("iconDao",IconDao.class);
        iconDao.save(icon);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
