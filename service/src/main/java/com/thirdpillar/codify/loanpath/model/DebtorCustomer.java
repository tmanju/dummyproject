package com.thirdpillar.codify.loanpath.model;

import java.util.HashSet;
import java.util.List;

import javax.persistence.Transient;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.model.WorkflowAware;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;
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
				if(this.getId() == null && dc.getFacilityCustomerRole().getCustomer().getDuns().equals(this.getFacilityCustomerRole().getCustomer().getDuns())){
					duplicateDebtor = true;
				}
			}
		}
		return duplicateDebtor;
	}
	
	public boolean duplicateDbaName(){
		boolean match = true;
		HashSet<String> set = new HashSet<>();
		if(this.getDbas() != null){
			for(PartyDba partyDba : this.getDbas()){
				if(partyDba.getDbaName() != null && partyDba.getDebtorCustomer() != null){
					match = set.add(partyDba.getDbaName()+"-"+partyDba.getDebtorCustomer().toString());
				}
			}
		}
		return match;
	}
	
	public String duplicateDbaNameValueDer(){
		boolean match = true;
		String duplicateDbaNameValue= null;
		HashSet<String> set = new HashSet<>();
		if(this.getDbas() !=null){
			for(PartyDba partyDba : this.getDbas()){
				if(partyDba.getDbaName() != null && partyDba.getDebtorCustomer() != null){
					match = set.add(partyDba.getDbaName()+"-"+partyDba.getDebtorCustomer().toString());
				}
				
				if(!match){
					if(partyDba.getDebtorCustomer() !=null && partyDba.getDebtorCustomer().getFacilityCustomerRole() !=null && partyDba.getDebtorCustomer().getFacilityCustomerRole().getCustomer() !=null && partyDba.getDebtorCustomer().getFacilityCustomerRole().getCustomer().getLegalName() !=null){
						duplicateDbaNameValue =  partyDba.getDebtorCustomer().getFacilityCustomerRole().getCustomer().getLegalName();
					}
				}
			}
		}
		return duplicateDbaNameValue;
	}
	
	public String dbaNameValueDer(){
		boolean match = true;
		String dbaNameValue= null;
		HashSet<String> set = new HashSet<>();
		if(this.getDbas() !=null){
			for(PartyDba partyDba : this.getDbas()){
				if(partyDba.getDbaName() != null && partyDba.getDebtorCustomer() != null){
					match = set.add(partyDba.getDbaName()+"-"+partyDba.getDebtorCustomer().toString());
				}
				
				if(!match){
					if(partyDba.getDebtorCustomer() !=null && partyDba.getDebtorCustomer().getFacilityCustomerRole() !=null && partyDba.getDebtorCustomer().getFacilityCustomerRole().getCustomer() !=null && partyDba.getDebtorCustomer().getFacilityCustomerRole().getCustomer().getLegalName() !=null){
						dbaNameValue =  partyDba.getDbaName();
					}
				}
			}
		}
		return dbaNameValue;
	}
	
	public boolean isDuplicateDebtor() {
		return duplicateDebtor;
	}

	public void setDuplicateDebtor(boolean duplicateDebtor) {
		this.duplicateDebtor = duplicateDebtor;
	}
}
