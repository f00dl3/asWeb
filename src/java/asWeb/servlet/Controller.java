/*
by Anthony Stump
Created: 10 Feb 2018
Updated: 7 Jun 2018
For support of non-RESTful API calls
*/

package asWeb.servlet;

import asWebRest.action.GetWebAccessLogAction;
import asWebRest.action.GetWebUIserAuthAction;
import asWebRest.dao.WebAccessLogDAO;
import asWebRest.dao.WebUIserAuthDAO;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

public class Controller extends HttpServlet {
    
    @Override
    public void doGet(
        HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {
        process(request, response);
    }

    @Override
    public void doPost(
        HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        CommonBeans cb = new CommonBeans();
        WebCommon wc = new WebCommon();
        
        String uri = request.getRequestURI();
        HttpSession session = request.getSession();
        
        int lastIndex = uri.lastIndexOf("/");
        String action = uri.substring(lastIndex + 1);
        String dispatchUrl = null;
        
        session.setAttribute("sessionInitiated", true);
        
        switch(action) {
           
            case "Session":
                dispatchUrl = "/Session.jsp";
                if(wc.isSet(request.getParameter("paramName")) && wc.isSet(request.getParameter("paramValue"))) {
                    switch(request.getParameter("paramName")) {
                        
                        case "clientIp":
                            String clientIp = request.getParameter("paramValue");
                            session.setAttribute("clientIp", clientIp);
                            break;
                            
                        case "clientBrowser":
                            String clientBrowser = request.getParameter("paramValue");
                            session.setAttribute("clientBrowser", clientBrowser);
                            break;
                        
                        case "hiddenFeatures":
                            String hiddenFeatures;
                            hiddenFeatures = request.getParameter("paramValue");
                            if(hiddenFeatures.equals("Disabled")) { hiddenFeatures = null; }
                            session.setAttribute("hiddenFeatures", hiddenFeatures);
                            break;
                            
                        case "loggedIn":
                            String loggedIn = request.getParameter("paramValue");
                            session.setAttribute("loggedIn", loggedIn);
                            break;
                            
                        case "userAndPass":
                            final File cachePath = new File(cb.getPathChartCache());
                            wc.deleteDir(cachePath);
                            GetWebUIserAuthAction getWebUIserAuthAction = new GetWebUIserAuthAction(new WebUIserAuthDAO());
                            GetWebAccessLogAction getWebAccessLogAction = new GetWebAccessLogAction(new WebAccessLogDAO());
                            String loginCheck = "false";
                            String[] loginCredentials;
                            loginCredentials = request.getParameter("paramValue").split("::");
                            String userName = loginCredentials[0];
                            String pass = loginCredentials[1];
                            String hashWord = "";
                            try { hashWord = wc.cryptIt(pass); } catch (Exception e) { e.printStackTrace(); }
                            String webUIserAuth = getWebUIserAuthAction.getWebUIserAuth(userName);
                            if(webUIserAuth.equals(hashWord) && WebCommon.isSet(hashWord)) { loginCheck = "true"; }
                            JSONArray webAccessLogs = getWebAccessLogAction.getWebAccessLogs();
                            session.setAttribute("userName", userName);
                            session.setAttribute("loggedIn", loginCheck);
                            break;
                            
                    }
                }
                JSONObject sessionVariables = new JSONObject();
                Enumeration keys = session.getAttributeNames();
                while (keys.hasMoreElements())
                {
                  String key = (String)keys.nextElement();
                  sessionVariables.put(key, session.getValue(key));
                }
                request.setAttribute("sessionVariables", sessionVariables.toString());
                break;

            default:
                dispatchUrl = "/login.jsp";
                break;

        }

        if (dispatchUrl != null) {
                RequestDispatcher rd = request.getRequestDispatcher(dispatchUrl);
                rd.forward(request, response);
        }
            
    }
}
