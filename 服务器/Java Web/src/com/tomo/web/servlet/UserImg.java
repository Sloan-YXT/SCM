package com.tomo.web.servlet;

import jdk.swing.interop.SwingInterOpUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.Buffer;
import java.util.Arrays;

public class UserImg extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            req.setCharacterEncoding("utf-8");
            //String path = req.getParameter("path");
            //String content = req.getParameter("content");
            //byte[] test=new byte[100000];
            //ha.read(test);
            //BufferedReader bf = new BufferedReader(new InputStreamReader(req.getInputStream()));
            //InputStream ha = req.getInputStream();
            DataInputStream ha = new DataInputStream(req.getInputStream());
            DataOutputStream stdout = new DataOutputStream(System.out);
            /*
            int m;
            {
                int times=0;
                while((m=ha.read())!=-1&&times<100)
                {
                    stdout.write(m);
                    //System.out.println(m);
                    times++;
                }
                stdout.flush();

            }
            */
            byte[] bpath = new byte[1024];
            int i;
            for(i=0;;i++)
            {
                bpath[i] =(byte)ha.read();
                if(bpath[i]=='\n')
                {
                    break;
                }
            }
            String path=new String(bpath).substring(0,i-1);
            //cider \r
            System.out.println(path.length());
            //bf.close();

            String content;
            StringBuilder bd = new StringBuilder();
//            String f;
//            while((f=bf.readLine())!=null)
//            {
//                bd.append(f);
//                //bd.append("\n");
//            }
//            content = bd.toString();
            System.out.println(path);
            File pic = new File("/home/ubuntu/Java Web/WebRoot/img/user/"+path);
            if(!pic.exists())
            {pic.createNewFile();}
            System.out.println("/home/ubuntu/Java Web/WebRoot/img/user/"+path);
            //BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pic)));
            //BufferedWriter writer1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/home/ubuntu/Java Web/classes/artifacts/second_hand_war_exploded/img/user/"+path)));
            FileOutputStream writer = new FileOutputStream(pic);
            File file2 = new File("/home/ubuntu/Java Web/classes/second_hand_war_exploded/img/user/"+path);
            if(!file2.exists())
            {
                file2.createNewFile();
            }
            FileOutputStream writer1 = new FileOutputStream(file2);


            //writer.write(content.getBytes());
            //writer1.write(content.getBytes());
            DataOutputStream dwriter = new DataOutputStream(writer);
            DataOutputStream dwriter1 = new DataOutputStream(writer1);
            int m;
            byte[] box = new byte[3000];
            /*
            {
                byte[] box = new byte[3000];
                System.out.println(new String(box));
                ha.read(box);
                System.out.println(new String(box));
            }
            */
            int size = ha.readInt();
            ha.read();
            ha.read();
            System.out.println("size:"+size);
            while((m=ha.read(box))!=-1) {
                writer.write(box,0,m);
                writer1.write(box,0,m);
                Arrays.fill(box,(byte)0);
            }


            /*
            while((m=ha.read())!=-1) {
                writer.write(m);
                writer1.write(m);
            }
            */
            /*
            for(i=0;i<size;i++)
            {
                m = ha.read();
                writer.write(m);
                writer1.write(m);
            }
            */
            writer.flush();
            writer.close();
            writer1.flush();
            writer1.close();
        }
      catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
