package com.tomo.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class UserIcon extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataInputStream ha = new DataInputStream(req.getInputStream());
        int i;
        System.out.println("icon Post");
        byte[] bpath = new byte[1024];
        for(i=0;;i++)
        {
            bpath[i] =(byte)ha.read();
            if(bpath[i]=='\n')
            {
                break;
            }
        }
        String path=new String(bpath).substring(0,i-1);
        File file1 = new File("/home/ubuntu/Java Web/WebRoot/img/icon/"+path);
        File file2 = new File("/home/ubuntu/Java Web/classes/second_hand_war_exploded/img/icon"+path);
        if(!file1.exists())
        {
            file1.createNewFile();
        }
        if(!file2.exists())
        {
            file2.createNewFile();
        }
        FileOutputStream writer1 = new FileOutputStream("/home/ubuntu/Java Web/WebRoot/img/icon/"+path);
        FileOutputStream writer2 = new FileOutputStream("/home/ubuntu/Java Web/classes/second_hand_war_exploded/img/icon/"+path);
        DataOutputStream dwriter = new DataOutputStream(writer1);
        DataOutputStream dwriter1 = new DataOutputStream(writer2);
        int m;
        int size = ha.readInt();
        ha.read();
        ha.read();
        while((m=ha.read())!=-1) {
            writer1.write(m);
            writer2.write(m);
        }
        writer1.close();
        writer2.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("icon get");
        doPost(req,resp);
    }
}
