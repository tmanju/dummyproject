package com.thirdpillar.codify.loanpath.model;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Transient;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;

@XStreamLookupCollectionByOGNL.List(
	    {
	    	@XStreamLookupCollectionByOGNL(
	           name = "byRefNumber",
	           keys = { "refNumber" }
	        ),
	        @XStreamLookupCollectionByOGNL(
	 	           name = "byServicingIdentifier",
	 	           keys = { "servicingIdentifier" }
	 	        )
	    }
	)
public class DebtorCustomer extends BaseDataObject{
	
	@Transient
	private boolean duplicateDebtor = false;
	
	@Transient
	private Set<PartyDba> facilityDBASet = null;
	
	@Transient
	private String duplicateDBAName;
	
	@Transient
	private String duplicateDebtorName;
	
	public boolean updateDebtorInfoAllowed(){
		boolean match = false;
		User user = (User)ContextHolder.getContext().getUser();
		List<Team> teams = user.getTeams();
		for(Team team : teams){
			if("credit decision team".equals(team.getName().toLowerCase())){
				match = true;
				break;
			}
			if(Constants.DEBTOR_PENDING_STATUS_KEY.equals(getWfStatus().getStatusKey()) && "credit analyst team".equals(team.getName().toLowerCase())){
				match = true;
				break;
			}
		}
		return match;
	}
	
	public boolean debtorCustomerAllowed(){
		if(this.getFacilityCustomerRole() == null || this.getFacilityCustomerRole().getCustomer() == null){
			return false;
		}
		return true;
	}
	
	public boolean  debtorDupCustomerAllowed(){
		if(this.getFacilityCustomerRole() != null && this.getFacilityCustomerRole().getCustomer() != null && this.getFacilityCustomerRole().getCustomer().getDuns() != null && this.getFacility() != null){
			EntityService es = new EntityService();
			for(DebtorCustomer dc : this.getFacility().getDebtors()){
				if(this.getRefNumber().equals(dc.getRefNumber())){
					continue;
				}
				if(this.getId() == null && dc.getFacilityCustomerRole().getCustomer().getDuns()!= null && this.getFacilityCustomerRole().getCustomer().getDuns() != null && dc.getFacilityCustomerRole().getCustomer().getDuns().equals(this.getFacilityCustomerRole().getCustomer().getDuns())){
					duplicateDebtor = true;
				}
			}
		}
		return duplicateDebtor;
	}
	
	public boolean duplicateDbaName(){
		boolean match = true;
		HashSet<String> set = new HashSet<>();
		if(this.getDbas() !=null){
			for(PartyDba partyDba : this.getDbas()){
				if(partyDba.getDbaName() != null && partyDba.getDebtorCustomer() != null){
					match = set.add(partyDba.getDbaName().toLowerCase()+"-"+partyDba.getDebtorCustomer().toString().toLowerCase());
				}
			}
		}
		return match;
	}
		
	public boolean isDuplicateDebtor() {
		return duplicateDebtor;
	}

	public void setDuplicateDebtor(boolean duplicateDebtor) {
		this.duplicateDebtor = duplicateDebtor;
	}
	
	public boolean isDebtorDuplicate(){
		boolean flag = true;
				
		//List of all Party DBA's currently in model
		List<PartyDba> modelFacilityDBAList = this.getFacility().getFacilityDBAList();
		
		if( modelFacilityDBAList != null && !modelFacilityDBAList.isEmpty() ){
			EntityService entityService = new EntityService<BaseDataObject>();
			String[] queryParams = new String[] { "dbaName", "refNumber" };
			
			//Iterate on model Party DBA's to verify if any of the Model DBA Party entered is duplicate in Database or model 
			for( PartyDba p : modelFacilityDBAList ){
				Object[] queryValues = new Object[] { p.getDbaName(),this.getFacility().getRefNumber() };
				List<PartyDba> existingPartyDbas = entityService.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.PartyDba.byDBANameAndFacility",queryParams, queryValues);
				if (existingPartyDbas != null && existingPartyDbas.size() > 0) {
						for(PartyDba partyDBA:existingPartyDbas){
							if(!partyDBA.getRefNumber().equals(p.getRefNumber())){
								duplicateDBAName= partyDBA.getDbaName();
								duplicateDebtorName= partyDBA.getCustomer().getLegalName();
								flag = false;
							}
						}
					}else{
						//check from model
						Set<PartyDba> set = new TreeSet<>(new Comparator<PartyDba>() {

							@Override
							public int compare(PartyDba dbaParty1,  PartyDba dbaParty2) {
								return dbaParty1.getDbaName().compareTo(dbaParty2.getDbaName());
							}
						});
						
						for(PartyDba partyDBA : modelFacilityDBAList){
							if(!set.add(partyDBA)){
								duplicateDBAName= partyDBA.getDbaName();
								duplicateDebtorName= partyDBA.getCustomer().getLegalName();
								flag = false;
							}
						}
					}
			}
		}
		
		return flag;
	}
	
	public String duplicateDbaNameValueDer(){
		return this.getDuplicateDBAName();
	}
	
	public String dbaNameValueDer(){
		return this.getDuplicateDebtorName();
	}
	
	public Set<PartyDba> getFacilityDBASet() {
		return facilityDBASet;
	}

	public void setFacilityDBASet(Set<PartyDba> facilityDBASet) {
		this.facilityDBASet = facilityDBASet;
	}

	public String getDuplicateDBAName() {
		return duplicateDBAName;
	}

	public void setDuplicateDBAName(String duplicateDBAName) {
		this.duplicateDBAName = duplicateDBAName;
	}

	public String getDuplicateDebtorName() {
		return duplicateDebtorName;
	}

	public void setDuplicateDebtorName(String duplicateDebtorName) {
		this.duplicateDebtorName = duplicateDebtorName;
	}
}
