/*
by Anthony Stump
Created: 10 Feb 2018
Updated: 17 Apr 2018
For support of non-RESTful API calls
*/

package asWeb.servlet;

import asWeb.customActions.SessionActions;
import asWebRest.shared.WebCommon;
import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        
        WebCommon wc = new WebCommon();
        
        String uri = request.getRequestURI();
        HttpSession session = request.getSession();
        
        int lastIndex = uri.lastIndexOf("/");
        String action = uri.substring(lastIndex + 1);
        String dispatchUrl = null;
        
        session.setAttribute("SessionInitiated", "true");
        
        switch(action) {
           
            case "Session":
                dispatchUrl = "/Session.jsp";
                String hiddenFeatures;
                if(wc.isSet(request.getParameter("hiddenFeatures"))) {
                    hiddenFeatures = request.getParameter("hiddenFeatures");
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
                /* try { userName = request.getParameter("asUser"); } catch (Exception e) { e.printStackTrace(); }
                if(WebCommon.isSet(hashWord)) { try { hashWord = WebCommon.cryptIt(request.getParameter("asPass")); } catch (Exception e) { e.printStackTrace(); } }
                GetWebUIserAuthAction getWebUIserAuthAction = new GetWebUIserAuthAction(new WebUIserAuthDAO());
                GetWebAccessLogAction getWebAccessLogAction = new GetWebAccessLogAction(new WebAccessLogDAO());
                String webUIserAuth = getWebUIserAuthAction.getWebUIserAuth(userName);
                if(webUIserAuth.equals(hashWord) && WebCommon.isSet(hashWord)) { loginCheck = true; }
                JSONArray webAccessLogs = getWebAccessLogAction.getWebAccessLogs();
                request.setAttribute("webAccessLogs", webAccessLogs.toString());               
                request.setAttribute("loginCheck", loginCheck);
                request.setAttribute("userCrypt", hashWord); */
                break;

        }

        if (dispatchUrl != null) {
                RequestDispatcher rd = request.getRequestDispatcher(dispatchUrl);
                rd.forward(request, response);
        }
            
    }
}
