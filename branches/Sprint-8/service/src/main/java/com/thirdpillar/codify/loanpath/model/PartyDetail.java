package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.DocumentAware;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.codify.loanpath.model.Relationship;
import java.util.ArrayList;
import java.util.List;

public class PartyDetail extends BaseDataObject {

	public PartyDetailQuestionaire getPartyQuestionaire() {

		if ((getPartyDetailQuestionnaires() != null)
				&& (getPartyDetailQuestionnaires().size() > 0)) {
			return getPartyDetailQuestionnaires().get(0);
		}

		return null;
	}

	public void setPartyQuestionaire(
			PartyDetailQuestionaire partyDetailQuestionaire) {
		addToPartyDetailQuestionnaires(partyDetailQuestionaire);
	}
	
	public List<Facility> getFacilities() {
        List<Facility> facilities = new ArrayList<Facility>();

        if ((getSites() != null) && (getSites().size() >= 1)) {

            for (PartySite partySite : getSites()) {

                if ((partySite.getRequest() != null) && (partySite.getRequest().getAllFacilities() != null)) {
                    facilities.addAll(partySite.getRequest().getAllFacilities());
                }
            }
        }

        return facilities;
    }
	
	public List<Relationship> getRelationships() {
        ArrayList<Relationship> relationships = new ArrayList<Relationship>();

        if ((getAllRelationshipParties() != null) && (getAllRelationshipParties().size() > 0)) {

            for (RelationshipParty relationshipParty : getAllRelationshipParties()) {

                if ((relationshipParty.getRelationship() != null) &&
                        !(isRelationshipExist(relationships, relationshipParty.getRelationship()))) {
                    relationships.add(relationshipParty.getRelationship());
                }
            }
        }

        return relationships;
    }
	
	/**
     * Check whether relationship exists in relationships list
     *
     * @return
     */
    private boolean isRelationshipExist(List<Relationship> relationships, Relationship rel) {

        for (Relationship relationship : relationships) {

            if (relationship.getId().equals(rel.getId())) {
                return true;
            }
        }

        return false;
    }
    
    /**
     * @return  String containing "lastName, FirstName" if party is individual and legalName otherwise.
     */
    public String getCustomerName() {

        if ((getPartyDetailType() != null) &&
                "CUSTOMER_TYPE_INDIVIDUAL".equalsIgnoreCase(getPartyDetailType().getKey())) {
            return getLastName() + ", " + getFirstName();
        } else {
            return getLegalName();
        }
    }
    
	

}
