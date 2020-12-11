package com.tomo.web.servlet;

import com.mysql.jdbc.log.Log;
import com.tomo.common.DaoFactory;
import com.tomo.dao.ShopDao;
import com.tomo.entity.Shop;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class UserImgDel extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String shopid = req.getParameter("shopid");
        System.out.println("shopid");
        ShopDao shopdao = DaoFactory.getInstance("shopDao", ShopDao.class);
        Shop shop = shopdao.findUnique("select * from shop where shopid = ?",shopid);
        String picture="";
        if(!shop.getPicture().equals("")) {
             picture = shop.getPicture().substring(0, shop.getPicture().length() - 1);
        }
        System.out.println("picture path:"+picture);
        File file1 = new File("/home/ubuntu/Java Web/WebRoot/img/user/"+picture);
        File file2 = new File("/home/ubuntu/Java Web/classes/second_hand_war_exploded/img/user/"+picture);
        if(file1.exists())
        {
            file1.delete();
        }
        if(file2.exists())
        {
            file2.delete();
        }

    }
}
