package com.freebird.wx.common.util;

import com.google.gson.Gson;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class ResponseUtil {

    private static Gson gson = new Gson();

    public static ModelAndView outputJsonResponse(HttpServletResponse response, Object model) {
        return outputJsonResponse(null, response, model);
    }

    public static ModelAndView outputJsonResponse(HttpServletRequest request,
                                                  HttpServletResponse response, Object model) {
        response.setContentType("application/x-javascript;charset=utf-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache,no-store,must-revalidate");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            String json = null;
            try {
                json = gson.toJson(model);
            } catch (IllegalArgumentException e) {
                if (e.getMessage().indexOf("is an array") != -1) {
                    json = gson.toJson(model);
                } else {
                    e.printStackTrace();
                }
            }
            out.write(json);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
