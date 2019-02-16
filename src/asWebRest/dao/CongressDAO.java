/*
by Anthony Stump
Refactored: 5 Apr 2018
Updated: 7 Apr 2018
*/

package asWebRest.dao;

import asWebRest.shared.WebCommon;
import java.sql.ResultSet;
import org.json.JSONArray;
import org.json.JSONObject;

public class CongressDAO {
    
    WebCommon wc = new WebCommon();
    
    public JSONArray getCongressHack() {
	final String GET_Contacts_SQL = "SELECT"
		+ " Agriculture,"
		+ " Appropriations,"
		+ " Approps_Cardinals,"
		+ " Armed_Services,"
		+ " Bl_Dogs,"
		+ " Board,"
		+ " Budget,"
		+ " Campaign_Contact,"
		+ " Campaign_Contact_Title,"
		+ " Camp_Contact_Email,"
		+ " Camp_Contact_Phone,"
		+ " Camp_Office_Address,"
		+ " Camp_Office_Fax,"
		+ " CBC,"
		+ " CHAPAC,"
		+ " CHC,"
		+ " City_State,"
		+ " Consultant_Website,"
		+ " Contact_CoS,"
		+ " CoS_Cell,"
		+ " CoS_Direct_Phone,"
		+ " CoS_Email,"
		+ " CoS_Fax,"
		+ " CoS_Personal_Email,"
		+ " DC_Home_Phone,"
		+ " DCCC_Ext,"
		+ " DCCC_LD,"
		+ " DCCC_Leadership,"
		+ " DCCC_Phone,"
		+ " DCCC_Vmail,"
		+ " District,"
		+ " Dist_Off_Address,"
		+ " Dist_Off_City,"
		+ " Dist_Off_Fax,"
		+ " Dist_Off_Phone,"
		+ " Dist_Off_State,"
		+ " Dist_Off_Zip,"
		+ " Education_Workforce,"
		+ " Election_Results_2012,"
		+ " Energy_Commerce,"
		+ " Ethics_SoOC,"
		+ " Finance_Director,"
		+ " Finance_Director_Email,"
		+ " Finance_Director_Phone,"
		+ " Financial_Services,"
		+ " First,"
		+ " Foreign_Affairs_IR,"
		+ " Freshman,"
		+ " Frontline,"
		+ " Hill_Fax,"
		+ " Hill_Phone,"
		+ " HOB_Address,"
		+ " Homeland_Security,"
		+ " Hon,"
		+ " House_Administration,"
		+ " House_Leadership,"
		+ " Judiciary,"
		+ " Last,"
		+ " Leadership_PAC_Name,"
		+ " LD_Cell_Phone,"
		+ " LD_Direct_Phone,"
		+ " LD_Fax,"
		+ " LD_Hill_Email,"
		+ " LD_Name,"
		+ " LD_Personal_Email,"
		+ " LPAC,"
		+ " Maritial_Status,"
		+ " Member_Cell,"
		+ " Member_Email,"
		+ " Member_Name,"
		+ " Member_Other_Phone,"
		+ " Mentors,"
		+ " Natural_Resources,"
		+ " NDC,"
		+ " Nickname,"
		+ " Non_Specialty,"
		+ " Oversight_Govt_Reform,"
		+ " PAC_Fundraiser,"
		+ " PAC_Fundraiser_Email,"
		+ " PAC_Fundraiser_Fax,"
		+ " PAC_Fundraiser_Phone,"
		+ " PACWkBk_Pass,"
		+ " PACWkBk_User,"
		+ " Press_Sec_Cell_Phone,"
		+ " Press_Sec_Direct_Phone,"
		+ " Press_Sec_Email,"
		+ " Press_Sec_Fax,"
		+ " Press_Sec_Name,"
		+ " Press_Sec_Personal_Email,"
		+ " Primary_Home_Address,"
		+ " Primary_Home_City,"
		+ " Primary_Home_Phone,"
		+ " Primary_Home_State,"
		+ " Primary_Home_Zip,"
		+ " Pro_Dem,"
		+ " Ranking_Member,"
		+ " Religion,"
		+ " Rules,"
		+ " Scheduler,"
		+ " Scheduler_Cell,"
		+ " Scheduler_Direct_Phone,"
		+ " Scheduler_Email,"
		+ " Scheduler_Fax,"
		+ " Scheduler_Personal_Email,"
		+ " Science,"
		+ " SEEC,"
		+ " Select_Intelligence,"
		+ " Small_Business,"
		+ " Spouse,"
		+ " State,"
		+ " T_I,"
		+ " TEAM_Builders,"
		+ " Term,"
		+ " VA,"
		+ " Ways_Means,"
		+ " Women,"
		+ " Zip"
		+ " FROM Core.DNC_Contacts"
		+ " ORDER BY Last, First DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(GET_Contacts_SQL, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Agriculture", resultSet.getString("Agriculture"))
                    .put("Appropriations", resultSet.getString("Appropriations"))
                    .put("Approps_Cardinals", resultSet.getString("Approps_Cardinals"))
                    .put("Armed_Services", resultSet.getString("Armed_Services"))
                    .put("Bl_Dogs", resultSet.getString("Bl_Dogs"))
                    .put("Board", resultSet.getString("Board"))
                    .put("Budget", resultSet.getString("Budget"))
                    .put("Campaign_Contact", resultSet.getString("Campaign_Contact"))
                    .put("Campaign_Contact_Title", resultSet.getString("Campaign_Contact_Title"))
                    .put("Camp_Contact_Email", resultSet.getString("Camp_Contact_Email"))
                    .put("Camp_Contact_Phone", resultSet.getString("Camp_Contact_Phone"))
                    .put("Camp_Office_Address", resultSet.getString("Camp_Office_Address"))
                    .put("Camp_Office_Fax", resultSet.getString("Camp_Office_Fax"))
                    .put("CBC", resultSet.getString("CBC"))
                    .put("CHAPAC", resultSet.getString("CHAPAC"))
                    .put("CHC", resultSet.getString("CHC"))
                    .put("City_State", resultSet.getString("City_State"))
                    .put("Consultant_Website", resultSet.getString("Consultant_Website"))
                    .put("Contact_CoS", resultSet.getString("Contact_CoS"))
                    .put("CoS_Cell", resultSet.getString("CoS_Cell"))
                    .put("CoS_Direct_Phone", resultSet.getString("CoS_Direct_Phone"))
                    .put("CoS_Email", resultSet.getString("CoS_Email"))
                    .put("CoS_Fax", resultSet.getString("CoS_Fax"))
                    .put("CoS_Personal_Email", resultSet.getString("CoS_Personal_Email"))
                    .put("DCCC_Ext", resultSet.getString("DCCC_Ext"))
                    .put("DCCC_LD", resultSet.getString("DCCC_LD"))
                    .put("DCCC_Leadership", resultSet.getString("DCCC_Leadership"))
                    .put("DCCC_Phone", resultSet.getString("DCCC_Phone"))
                    .put("DCCC_Vmail", resultSet.getString("DCCC_Vmail"))
                    .put("DC_Home_Phone", resultSet.getString("DC_Home_Phone"))
                    .put("District", resultSet.getInt("District"))
                    .put("Dist_Off_Address", resultSet.getString("Dist_Off_Address"))
                    .put("Dist_Off_City", resultSet.getString("Dist_Off_City"))
                    .put("Dist_Off_Fax", resultSet.getString("Dist_Off_Fax"))
                    .put("Dist_Off_Phone", resultSet.getString("Dist_Off_Phone"))
                    .put("Dist_Off_State", resultSet.getString("Dist_Off_State"))
                    .put("Dist_Off_Zip", resultSet.getInt("Dist_Off_Zip"))
                    .put("Education_Workforce", resultSet.getString("Education_Workforce"))
                    .put("Election_Results_2012", resultSet.getString("Election_Results_2012"))
                    .put("Energy_Commerce", resultSet.getString("Energy_Commerce"))
                    .put("Ethics_SoOC", resultSet.getString("Ethics_SoOC"))
                    .put("Finance_Director", resultSet.getString("Finance_Director"))
                    .put("Finance_Director_Email", resultSet.getString("Finance_Director_Email"))
                    .put("Finance_Director_Phone", resultSet.getString("Finance_Director_Phone"))
                    .put("Financial_Services", resultSet.getString("Financial_Services"))
                    .put("First", resultSet.getString("First"))
                    .put("Foreign_Affairs_IR", resultSet.getString("Foreign_Affairs_IR"))
                    .put("Freshman", resultSet.getString("Freshman"))
                    .put("Frontline", resultSet.getString("Frontline"))
                    .put("Hill_Fax", resultSet.getString("Hill_Fax"))
                    .put("Hill_Phone", resultSet.getString("Hill_Phone"))
                    .put("HOB_Address", resultSet.getString("HOB_Address"))
                    .put("Homeland_Security", resultSet.getString("Homeland_Security"))
                    .put("Hon", resultSet.getString("Hon"))
                    .put("House_Administration", resultSet.getString("House_Administration"))
                    .put("House_Leadership", resultSet.getString("House_Leadership"))
                    .put("Judiciary", resultSet.getString("Judiciary"))
                    .put("Last", resultSet.getString("Last"))
                    .put("Leadership_PAC_Name", resultSet.getString("Leadership_PAC_Name"))
                    .put("LD_Cell_Phone", resultSet.getString("LD_Cell_Phone"))
                    .put("LD_Direct_Phone", resultSet.getString("LD_Direct_Phone"))
                    .put("LD_Fax", resultSet.getString("LD_Fax"))
                    .put("LD_Hill_Email", resultSet.getString("LD_Hill_Email"))
                    .put("LD_Name", resultSet.getString("LD_Name"))
                    .put("LD_Personal_Email", resultSet.getString("LD_Personal_Email"))
                    .put("LPAC", resultSet.getString("LPAC"))
                    .put("Maritial_Status", resultSet.getString("Maritial_Status"))
                    .put("Member_Cell", resultSet.getString("Member_Cell"))
                    .put("Member_Email", resultSet.getString("Member_Email"))
                    .put("Member_Name", resultSet.getString("Member_Name"))
                    .put("Member_Other_Phone", resultSet.getString("Member_Other_Phone"))
                    .put("Mentors", resultSet.getString("Mentors"))
                    .put("Natural_Resources", resultSet.getString("Natural_Resources"))
                    .put("NDC", resultSet.getString("NDC"))
                    .put("Nickname", resultSet.getString("Nickname"))
                    .put("Non_Specialty", resultSet.getString("Non_Specialty"))
                    .put("Oversight_Govt_Reform", resultSet.getString("Oversight_Govt_Reform"))
                    .put("PAC_Fundraiser", resultSet.getString("PAC_Fundraiser"))
                    .put("PAC_Fundraiser_Email", resultSet.getString("PAC_Fundraiser_Email"))
                    .put("PAC_Fundraiser_Fax", resultSet.getString("PAC_Fundraiser_Fax"))
                    .put("PAC_Fundraiser_Phone", resultSet.getString("PAC_Fundraiser_Phone"))
                    .put("PACWkBk_Pass", resultSet.getString("PACWkBk_Pass"))
                    .put("PACWkBk_User", resultSet.getString("PACWkBk_User"))
                    .put("Press_Sec_Cell_Phone", resultSet.getString("Press_Sec_Cell_Phone"))
                    .put("Press_Sec_Direct_Phone", resultSet.getString("Press_Sec_Direct_Phone"))
                    .put("Press_Sec_Email", resultSet.getString("Press_Sec_Email"))
                    .put("Press_Sec_Fax", resultSet.getString("Press_Sec_Fax"))
                    .put("Press_Sec_Personal_Email", resultSet.getString("Press_Sec_Personal_Email"))
                    .put("Primary_Home_Address", resultSet.getString("Primary_Home_Address"))
                    .put("Primary_Home_City", resultSet.getString("Primary_Home_City"))
                    .put("Primary_Home_Phone", resultSet.getString("Primary_Home_Phone"))
                    .put("Primary_Home_State", resultSet.getString("Primary_Home_State"))
                    .put("Primary_Home_Zip", resultSet.getString("Primary_Home_Zip"))
                    .put("Pro_Dem", resultSet.getString("Pro_Dem"))
                    .put("Ranking_Member", resultSet.getString("Ranking_Member"))
                    .put("Religion", resultSet.getString("Religion"))
                    .put("Rules", resultSet.getString("Rules"))
                    .put("Scheduler", resultSet.getString("Scheduler"))
                    .put("Scheduler_Cell", resultSet.getString("Scheduler_Cell"))
                    .put("Scheduler_Direct_Phone", resultSet.getString("Scheduler_Direct_Phone"))
                    .put("Scheduler_Email", resultSet.getString("Scheduler_Email"))
                    .put("Scheduler_Fax", resultSet.getString("Scheduler_Fax"))
                    .put("Scheduler_Personal_Email", resultSet.getString("Scheduler_Personal_Email"))
                    .put("Science", resultSet.getString("Science"))
                    .put("SEEC", resultSet.getString("SEEC"))
                    .put("Select_Intelligence", resultSet.getString("Select_Intelligence"))
                    .put("Small_Business", resultSet.getString("Small_Business"))
                    .put("Spouse", resultSet.getString("Spouse"))
                    .put("State", resultSet.getString("State"))
                    .put("TEAM_Builders", resultSet.getString("TEAM_Builders"))
                    .put("Term", resultSet.getString("Term"))
                    .put("T_I", resultSet.getString("T_I"))
                    .put("VA", resultSet.getString("VA"))
                    .put("Ways_Means", resultSet.getString("Ways_Means"))
                    .put("Women", resultSet.getString("Women"))
                    .put("Zip", resultSet.getInt("Zip"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

}

