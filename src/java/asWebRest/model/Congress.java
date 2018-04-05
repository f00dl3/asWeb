/*
by Anthony Stump
Refactored: 5 Apr 2018
*/

package asWebRest.model;

import java.io.Serializable;

public class Congress implements Serializable {
	
	private static final long serialVerionUID = 1L;

	private String agriculture;
	private String appropriations;
	private String appropsCardinals;
	private String armedServices;
	private String blDogs;
	private String board;
	private String budget;
	private String campaignContact;
	private String campaignContactTitle;
	private String campContactEmail;
	private String campContactPhone;
	private String campOfficeAddress;
	private String campOfficeFax;
	private String cbc;
	private String chapac;
	private String chc;
	private String cityState;
	private String consultantWebsite;
	private String contactCoS;
	private String cosCell;
	private String cosDirectPhone;
	private String cosEmail;
	private String cosFax;
	private String cosPersonalEmail;
	private String dcccExt;
	private String dcccLD;
	private String dcccLeadership;
	private String dcccPhone;
	private String dcccVmail;
	private String dcHomePhone;
	private int district;
	private String distOffAddress;
	private String distOffCity;
	private String distOffFax;
	private String distOffPhone;
	private String distOffState;
	private int distOffZip;
	private String educationWorkforce;
	private String electionResults2012;
	private String energyCommerce;
	private String ethicsSoOC;
	private String financeDirector;
	private String financeDirectorEmail;
	private String financeDirectorPhone;
	private String financialServices;
	private String first;
	private String foreignAffairsIR;
	private String freshman;
	private String frontline;
	private String hillFax;
	private String hillPhone;
	private String hobAddress;
	private String homelandSecurity;
	private String hon;
	private String houseAdministration;
	private String houseLeadership;
	private String judiciary;
	private String last;
	private String leadershipPACName;
	private String ldCellPhone;
	private String ldDirectPhone;
	private String ldFax;
	private String ldHillEmail;
	private String ldName;
	private String ldPersonalEmail;
	private String lpac;
	private String maritialStatus;
	private String memberCell;
	private String memberEmail;
	private String memberName;
	private String memberOtherPhone;
	private String mentors;
	private String naturalResources;
	private String ndc;
	private String nickname;
	private String nonSpecialty;
	private String oversightGovtReform;
	private String pacFundraiser;
	private String pacFundraiserEmail;
	private String pacFundraiserFax;
	private String pacFundraiserPhone;
	private String pacWkBkPass;
	private String pacWkBkUser;
	private String pressSecCellPhone;
	private String pressSecDirectPhone;
	private String pressSecEmail;
	private String pressSecFax;
	private String pressSecName;
	private String pressSecPersonalEmail;
	private String primaryHomeAddress;
	private String primaryHomeCity;
	private String primaryHomePhone;
	private String primaryHomeState;
	private String primaryHomeZip;
	private String proDem;
	private String rankingMember;
	private String religion;
	private String rules;
	private String scheduler;
	private String schedulerCell;
	private String schedulerDirectPhone;
	private String schedulerEmail;
	private String schedulerFax;
	private String schedulerPersonalEmail;
	private String science;
	private String seec;
	private String selectIntelligence;
	private String smallBusiness;
	private String spouse;
	private String state;
	private String teamBuilders;
	private int term;
	private String ti;
	private String va;
	private String waysMeans;
	private String women;
	private int zip;
	
	public String getAgriculture() { return agriculture; }
	public String getAppropriations() { return appropriations; }
	public String getAppropsCardinals() { return appropsCardinals; }
	public String getArmedServices() { return armedServices; }
	public String getBlDogs() { return blDogs; }
	public String getBoard() { return board; }
	public String getBudget() { return budget; }
	public String getCampaignContact() { return campaignContact; }
	public String getCampaignContactTitle() { return campaignContactTitle; }
	public String getCampContactEmail() { return campContactEmail; }
	public String getCampContactPhone() { return campContactPhone; }
	public String getCampOfficeAddress() { return campOfficeAddress; }
	public String getCampOfficeFax() { return campOfficeFax; }
	public String getCBC() { return cbc; }
	public String getCHAPAC() { return chapac; }
	public String getCHC() { return chc; }
	public String getCityState() { return cityState; }
	public String getConsultantWebsite() { return consultantWebsite; }
	public String getContactCoS() { return contactCoS; }
	public String getCoSCell() { return cosCell; }
	public String getCoSDirectPhone() { return cosDirectPhone; }
	public String getCoSEmail() { return cosEmail; }
	public String getCoSFax() { return cosFax; }
	public String getCoSPersonalEmail() { return cosPersonalEmail; }
	public String getDCCCExt() { return dcccExt; }
	public String getDCCCLD() { return dcccLD; }
	public String getDCCCLeadership() { return dcccLeadership; }
	public String getDCCCPhone() { return dcccPhone; }
	public String getDCCCVmail() { return dcccVmail; }
	public String getDCHomePhone() { return dcHomePhone; }
	public int getDistrict() { return district; }
	public String getDistOffAddress() { return distOffAddress; }
	public String getDistOffCity() { return distOffCity; }
	public String getDistOffFax() { return distOffFax; }
	public String getDistOffPhone() { return distOffPhone; }
	public String getDistOffState() { return distOffState; }
	public int getDistOffZip() { return distOffZip; }
	public String getEducationWorkforce() { return educationWorkforce; }
	public String getEnergyCommerce() { return energyCommerce; }
	public String getElectionResults2012() { return electionResults2012; }
	public String getEthicsSoOC() { return ethicsSoOC; }
	public String getFinanceDirector() { return financeDirector; }
	public String getFinanceDirectorEmail() { return financeDirectorEmail; }
	public String getFinanceDirectorPhone() { return financeDirectorPhone; }
	public String getFinancialServices() { return financialServices; }
	public String getFirst() { return first; }
	public String getForeignAffairsIR() { return foreignAffairsIR; }
	public String getFreshman() { return freshman; }
	public String getFrontline() { return frontline; }
	public String getHillFax() { return hillFax; }
	public String getHillPhone() { return hillPhone; }
	public String getHOBAddress() { return hobAddress; }
	public String getHomelandSecurity() { return homelandSecurity; }
	public String getHon() { return hon; }
	public String getHouseAdministration() { return houseAdministration; }
	public String getHouseLeadership() { return houseLeadership; }
	public String getJudiciary() { return judiciary; }
	public String getLast() { return last; }
	public String getLeadershipPACName() { return leadershipPACName; }
	public String getLDCellPhone() { return ldCellPhone; }
	public String getLDDirectPhone() { return ldDirectPhone; }
	public String getLDFax() { return ldFax; }
	public String getLDHillEmail() { return ldHillEmail; }
	public String getLDName() { return ldName; }
	public String getLDPersonalEmail() { return ldPersonalEmail; }
	public String getLPAC() { return lpac; }
	public String getMaritialStatus() { return maritialStatus; }
	public String getMemberCell() { return memberCell; }
	public String getMemberEmail() { return memberEmail; }
	public String getMemberName() { return memberName; }
	public String getMemberOtherPhone() { return memberOtherPhone; }
	public String getMentors() { return mentors; }
	public String getNaturalResources() { return naturalResources; }
	public String getNDC() { return ndc; }
	public String getNickname() { return nickname; }
	public String getNonSpecialty() { return nonSpecialty; }
	public String getOversightGovtReform() { return oversightGovtReform; }
	public String getPACFundraiser() { return pacFundraiser; }
	public String getPACFundraiserEmail() { return pacFundraiserEmail; }
	public String getPACFundraiserFax() { return pacFundraiserFax; }
	public String getPACFundraiserPhone() { return pacFundraiserPhone; }
	public String getPACWkBkPass() { return pacWkBkPass; }
	public String getPACWkBkUser() { return pacWkBkUser; }
	public String getPressSecCellPhone() { return pressSecCellPhone; }
	public String getPressSecDirectPhone() { return pressSecDirectPhone; }
	public String getPressSecEmail() { return pressSecEmail; }
	public String getPressSecFax() { return pressSecFax; }
	public String getPressSecName() { return pressSecName; }
	public String getPressSecPersonalEmail() { return pressSecPersonalEmail; }
	public String getPrimaryHomeAddress() { return primaryHomeAddress; }
	public String getPrimaryHomeCity() { return primaryHomeCity; }
	public String getPrimaryHomePhone() { return primaryHomePhone; }
	public String getPrimaryHomeState() { return primaryHomeState; }
	public String getPrimaryHomeZip() { return primaryHomeZip; }
	public String getProDem() { return proDem; }
	public String getRankingMember() { return rankingMember; }
	public String getReligion() { return religion; }
	public String getRules() { return rules; }
	public String getScheduler() { return scheduler; }
	public String getSchedulerCell() { return schedulerCell; }
	public String getSchedulerDirectPhone() { return schedulerDirectPhone; }
	public String getSchedulerEmail() { return schedulerEmail; }
	public String getSchedulerFax() { return schedulerFax; }
	public String getSchedulerPersonalEmail() { return schedulerPersonalEmail; }
	public String getScience() { return science; }
	public String getSEEC() { return seec; }
	public String getSelectIntelligence() { return selectIntelligence; }
	public String getSmallBusiness() { return smallBusiness; }
	public String getSpouse() { return spouse; }
	public String getState() { return state; }
	public String getTEAMBuilders() { return teamBuilders; }
	public int getTerm() { return term; }
	public String getTI() { return ti; }
	public String getVA() { return va; }
	public String getWaysMeans() { return waysMeans; }
	public String getWomen() { return women; }
	public int getZip() { return zip; }
	
	public void setAgriculture(String agriculture) { this.agriculture = agriculture; }
	public void setAppropriations(String appropriations) { this.appropriations = appropriations; }
	public void setAppropsCardinals(String appropsCardinals) { this.appropsCardinals = appropsCardinals; }
	public void setArmedServices(String armedServices) { this.armedServices = armedServices; }
	public void setBlDogs(String blDogs) { this.blDogs = blDogs; }
	public void setBoard(String board) { this.board = board; }
	public void setBudget(String budget) { this.budget = budget; }
	public void setCampaignContact(String campaignContact) { this.campaignContact = campaignContact; }
	public void setCampaignContactTitle(String campaignContactTitle) { this.campaignContactTitle = campaignContactTitle; }
	public void setCampContactEmail(String campContactEmail) { this.campContactEmail = campContactEmail; }
	public void setCampContactPhone(String campContactPhone) { this.campContactPhone = campContactPhone; }
	public void setCampOfficeAddress(String campOfficeAddress) { this.campOfficeAddress = campOfficeAddress; }
	public void setCampOfficeFax(String campOfficeFax) { this.campOfficeFax = campOfficeFax; }
	public void setCBC(String cbc) { this.cbc = cbc; }
	public void setCHAPAC(String chapac) { this.chapac = chapac; }
	public void setCHC(String chc) { this.chc = chc; }
	public void setCityState(String cityState) { this.cityState = cityState; }
	public void setConsultantWebsite(String consultantWebsite) { this.consultantWebsite = consultantWebsite; }
	public void setContactCoS(String contactCoS) { this.contactCoS = contactCoS; }
	public void setCoSCell(String cosCell) { this.cosCell = cosCell; }
	public void setCoSDirectPhone(String cosDirectPhone) { this.cosDirectPhone = cosDirectPhone; }
	public void setCoSEmail(String cosEmail) { this.cosEmail = cosEmail; }
	public void setCoSFax(String cosFax) { this.cosFax = cosFax; }
	public void setCoSPersonalEmail(String cosPersonalEmail) { this.cosPersonalEmail = cosPersonalEmail; }
	public void setDCCCExt(String dcccExt) { this.dcccExt = dcccExt; }
	public void setDCCCLD(String dcccLD) { this.dcccLD = dcccLD; }
	public void setDCCCLeadership(String dcccLeadership) { this.dcccLeadership = dcccLeadership; }
	public void setDCCCPhone(String dcccPhone) { this.dcccPhone = dcccPhone; }
	public void setDCCCVmail(String dcccVmail) { this.dcccVmail = dcccVmail; }
	public void setDCHomePhone(String dcHomePhone) { this.dcHomePhone = dcHomePhone; }
	public void setDistrict(int District) { this.district = district; }
	public void setDistOffAddress(String distOffAddress) { this.distOffAddress = distOffAddress; }
	public void setDistOffCity(String distOffCity) { this.distOffCity = distOffCity; }
	public void setDistOffFax(String distOffFax) { this.distOffFax = distOffFax; }
	public void setDistOffPhone(String distOffPhone) { this.distOffPhone = distOffPhone; }
	public void setDistOffState(String distOffState) { this.distOffState = distOffState; }
	public void setDistOffZip(int distOffZip) { this.distOffZip = distOffZip; }
	public void setEducationWorkforce(String educationWorkforce) { this.educationWorkforce = educationWorkforce; }
	public void setElectionResults2012(String electionResults2012) { this.electionResults2012 = electionResults2012; }
	public void setEnergyCommerce(String energyCommerce) { this.energyCommerce = energyCommerce; }
	public void setEthicsSoOC(String ethicsSoOC) { this.ethicsSoOC = ethicsSoOC; }
	public void setFinanceDirector(String financeDirector) { this.financeDirector = financeDirector; }
	public void setFinanceDirectorEmail(String financeDirectorEmail) { this.financeDirectorEmail = financeDirectorEmail; }
	public void setFinanceDirectorPhone(String financeDirectorPhone) { this.financeDirectorPhone = financeDirectorPhone; }
	public void setFinancialServices(String financialServices) { this.financialServices = financialServices; }
	public void setFirst(String first) { this.first = first; }
	public void setForeignAffairsIR(String foreignAffairsIR) { this.foreignAffairsIR = foreignAffairsIR; }
	public void setFreshman(String freshman) { this.freshman = freshman; }
	public void setFrontline(String frontline) { this.frontline = frontline; }
	public void setHillFax(String hillFax) { this.hillFax = hillFax; }
	public void setHillPhone(String hillPhone) { this.hillPhone = hillPhone; }
	public void setHOBAddress(String hobAddress) { this.hobAddress = hobAddress; }
	public void setHomelandSecurity(String homelandSecurity) { this.homelandSecurity = homelandSecurity; }
	public void setHon(String hon) { this.hon = hon; }
	public void setHouseAdministration(String houseAdministration) { this.houseAdministration = houseAdministration; }
	public void setHouseLeadership(String houseLeadership) { this.houseLeadership = houseLeadership; }
	public void setJudiciary(String judiciary) { this.judiciary = judiciary; }
	public void setLast(String last) { this.last = last; }
	public void setLeadershipPACName(String leadershipPACName) { this.leadershipPACName = leadershipPACName; }
	public void setLDCellPhone(String ldCellPhone) { this.ldCellPhone = ldCellPhone; }
	public void setLDDirectPhone(String ldDirectPhone) { this.ldDirectPhone = ldDirectPhone; }
	public void setLDFax(String ldFax) { this.ldFax = ldFax; }
	public void setLDHillEmail(String ldHillEmail) { this.ldHillEmail = ldHillEmail; }
	public void setLDName(String ldName) { this.ldName = ldName; }
	public void setLDPersonalEmail(String ldPersonalEmail) { this.ldPersonalEmail = ldPersonalEmail; }
	public void setLPAC(String lpac) { this.lpac = lpac; }
	public void setMaritialStatus(String maritialStatus) { this.maritialStatus = maritialStatus; }
	public void setMemberCell(String memberCell) { this.memberCell = memberCell; }
	public void setMemberEmail(String memberEmail) { this.memberEmail = memberEmail; }
	public void setMemberName(String memberName) { this.memberName = memberName; }
	public void setMemberOtherPhone(String memberOtherPhone) { this.memberOtherPhone = memberOtherPhone; }
	public void setMentors(String mentors) { this.mentors = mentors; }
	public void setNaturalResources(String naturalResources) { this.naturalResources = naturalResources; }
	public void setNDC(String ndc) { this.ndc = ndc; }
	public void setNickname(String nickname) { this.nickname = nickname; }
	public void setNonSpecialty(String nonSpecialty) { this.nonSpecialty = nonSpecialty; }
	public void setOversightGovtReform(String oversightGovtReform) { this.oversightGovtReform = oversightGovtReform; }
	public void setPACFundraiser(String pacFundraiser) { this.pacFundraiser = pacFundraiser; }
	public void setPACFundraiserEmail(String pacFundraiserEmail) { this.pacFundraiserEmail = pacFundraiserEmail; }
	public void setPACFundraiserFax(String pacFundraiserFax) { this.pacFundraiserFax = pacFundraiserFax; }
	public void setPACFundraiserPhone(String pacFundraiserPhone) { this.pacFundraiserPhone = pacFundraiserPhone; }
	public void setPACWkBkPass(String pacWkBkPass) { this.pacWkBkPass = pacWkBkPass; }
	public void setPACWkBkUser(String pacWkBkUser) { this.pacWkBkUser = pacWkBkUser; }
	public void setPressSecCellPhone(String pressSecCellPhone) { this.pressSecCellPhone = pressSecCellPhone; }
	public void setPressSecDirectPhone(String pressSecDirectPhone) { this.pressSecDirectPhone = pressSecDirectPhone; }
	public void setPressSecEmail(String pressSecEmail) { this.pressSecEmail = pressSecEmail; }
	public void setPressSecFax(String pressSecFax) { this.pressSecFax = pressSecFax; }
	public void setPressSecName(String pressSecName) { this.pressSecName = pressSecName; }
	public void setPressSecPersonalEmail(String pressSecPersonalEmail) { this.pressSecPersonalEmail = pressSecPersonalEmail; }
	public void setPrimaryHomeAddress(String primaryHomeAddress) { this.primaryHomeAddress = primaryHomeAddress; }
	public void setPrimaryHomeCity(String primaryHomeCity) { this.primaryHomeCity = primaryHomeCity; }
	public void setPrimaryHomePhone(String primaryHomePhone) { this.primaryHomePhone = primaryHomePhone; }
	public void setPrimaryHomeState(String primaryHomeState) { this.primaryHomeState = primaryHomeState; }
	public void setPrimaryHomeZip(String primaryHomeZip) { this.primaryHomeZip = primaryHomeZip; }
	public void setProDem(String proDem) { this.proDem = proDem; }
	public void setRankingMember(String rankingMember) { this.rankingMember = rankingMember; }
	public void setReligion(String religion) { this.religion = religion; }
	public void setRules(String rules) { this.rules = rules; }
	public void setScheduler(String scheduler) { this.scheduler = scheduler; }
	public void setSchedulerCell(String schedulerCell) { this.schedulerCell = schedulerCell; }
	public void setSchedulerDirectPhone(String schedulerDirectPhone) { this.schedulerDirectPhone = schedulerDirectPhone; }
	public void setSchedulerEmail(String schedulerEmail) { this.schedulerEmail = schedulerEmail; }
	public void setSchedulerFax(String schedulerFax) { this.schedulerFax = schedulerFax; }
	public void setSchedulerPersonalEmail(String schedulerPersonalEmail) { this.schedulerPersonalEmail = schedulerPersonalEmail; }
	public void setScience(String science) { this.science = science; }
	public void setSEEC(String seec) { this.seec = seec; }
	public void setSelectIntelligence(String selectIntelligence) { this.selectIntelligence = selectIntelligence; }
	public void setSmallBusiness(String smallBusiness) { this.smallBusiness = smallBusiness; }
	public void setSpouse(String spouse) { this.spouse = spouse; }
	public void setState(String state) { this.state = state; }
	public void setTEAMBuilders(String teamBuilders) { this.teamBuilders = teamBuilders; }
	public void setTerm(int term) { this.term = term; }
	public void setTI(String ti) { this.ti = ti; }
	public void setVA(String va) { this.va = va; }
	public void setWaysMeans(String waysMeans) { this.waysMeans = waysMeans; }
	public void setWomen(String women) { this.women = women; }
	public void setZip(int zip) { this.zip = zip; }
	
}
