package com.carl.cl;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

@WebServlet(
        name = "CaoServlet", 
        urlPatterns = {"/cao.html"}
    )
public class LeakServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pageNum = req.getAttribute("page");
        List<String> picList = new ArrayList<String>();
	String baseurl = "http://cc.8sss.me/";
	Document doc = Jsoup.connect(baseurl + "thread0806.php").data("fid", "16").data("page", pageNum)
		.userAgent("Mozilla").get();
	Element div = doc.getElementById("ajaxtable");
	Elements d = div.select("tbody:nth-child(2) > tr > td >h3").select("a[href]");
	for (Element b : d) {
		Document detailPage = Jsoup.connect(baseurl + b.attr("href")).userAgent("Mozilla").get();
		Elements p = detailPage.select("div > input");
		for (Element c : p) {
			if (!c.attr("src").equals("")) {
				picList.add(c.attr("src"));
			}
		}
	}
        ServletOutputStream out = resp.getOutputStream();
        out.write((new Gson()).toJson(picList).getBytes());
        out.flush();
        out.close();
    }
    
}
