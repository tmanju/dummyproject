/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.lookup;

import com.thirdpillar.codify.loanpath.model.AttributeChoice;
import com.thirdpillar.foundation.lookup.LookupProvider;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.model.EffectiveDateRange;
import com.thirdpillar.foundation.rules.MvelExecutor;
import com.thirdpillar.foundation.service.EntityService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.ArrayList;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
@Configurable public class AttributeChoiceLookupProvider<T extends BaseDataObject>
    implements LookupProvider<AttributeChoiceLookup, AttributeChoice> {
	
    //~ Instance fields ------------------------------------------------------------------------------------------------

    @Autowired private EntityService<AttributeChoice> service;
    private AttributeChoiceLookup attributeChoiceLookup;
    private T entity;

    //~ Methods --------------------------------------------------------------------------------------------------------
    
    public AttributeChoiceLookupProvider(T entity) {
    	this.entity = entity;
    }

    public void initialize(AttributeChoiceLookup attributeChoiceLookup) {
        this.attributeChoiceLookup = attributeChoiceLookup;

        // HACK aspectj doesn't work because of relcoations of spring fiels to WEB-INF
        service = new EntityService<AttributeChoice>();
    }

    public List<AttributeChoice> lookupList() {
    	String key = attributeChoiceLookup.key();
    	
    	List<AttributeChoice>  values = lookupByKey(key);
    	
    	values = applyEffectiveDateRange(values);
    	
    	values = applyGroupMvel(values);
    	    		
    	return values;       	
    }
    
    private List<AttributeChoice> applyEffectiveDateRange(List<AttributeChoice> values) {
		boolean effective = attributeChoiceLookup.effective();
		if(effective && EffectiveDateRange.class.isAssignableFrom(AttributeChoice.class)){
			List<AttributeChoice> filteredValues = new ArrayList<AttributeChoice>();
			for (AttributeChoice attributeChoice : values) {
				if( ((EffectiveDateRange)attributeChoice).getEffective()){
					filteredValues.add(attributeChoice);
				}
			}
			return filteredValues;
		}
		return values;
	}

	private List<AttributeChoice> applyGroupMvel(List<AttributeChoice> values) {
		String groupMvel = attributeChoiceLookup.groupMvel();
    	if (!StringUtils.isEmpty(groupMvel)) {
    		String groupValue = (String) MvelExecutor.eval(groupMvel, MvelExecutor.IMMIDIATE_PARENT_ENTITY, entity);
    		if (!StringUtils.isEmpty(groupValue)) {
    			String[] groupKeys = groupValue.split(",");
	    		List<AttributeChoice> filteredValues = new ArrayList<AttributeChoice>();
	    		for (AttributeChoice choice : values) {
	    			for(String groupKey : groupKeys){
	    				groupKey = groupKey.trim();
	    				if (choice.isInGroup(groupKey)) {
	    					filteredValues.add(choice);
	    					break;
	    				}
	    				
	    			}
	    		}
	    		
	    		return filteredValues;
    		}
    	}
    	return values;
	}

    public AttributeChoice lookupValue(String... keys) {
        String attributeKey = attributeChoiceLookup.key();
        List<AttributeChoice> choices = service.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.AttributeChoice.byKeyAndAttributeKey",new String[]{"key","attribute_key"},
        			new Object[]{keys[0],attributeKey}); 
        if (choices.isEmpty()) {
            return choices.get(0);
        } else {
            return null;
        }
    }
    
    private List<AttributeChoice> lookupByKey(String key) {
        

        return service.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.AttributeChoice.byAttributeKey","attribute_key",key);
    	
    }
}
