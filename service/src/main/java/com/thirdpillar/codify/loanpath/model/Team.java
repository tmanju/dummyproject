package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.codify.loanpath.lookup.AttributeChoiceLookup;

import com.thirdpillar.foundation.model.Auto;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.model.DataObjectHelper;
import com.thirdpillar.foundation.model.WorkflowAware;
import com.thirdpillar.foundation.xstream.lookup.XStreamNamedQueryLookup;
import com.thirdpillar.foundation.model.DocumentAware;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Index;

import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * Model class for Team
 * 
 * @author Jagdeep Singh
 * @version 1.0
 * @since 1.0
 */
public class Team extends BaseDataObject {

	// ~ Methods
	// --------------------------------------------------------------------------------------------------------
	/**
	 * Method to get email addresses of all users assigned to the team.
	 * 
	 * @return userList
	 */
	public String getEmailAddressForAllUsers() {

		List<User> users = getUsers();
		StringBuffer userListBuffer = new StringBuffer();
		String userList = "";
		if (users != null && !users.isEmpty()) {
			for (User user : users) {
				if (user.getContact() != null
						&& user.getContact().getEmail() != null
						&& user.getContact().getEmail().length() > 0) {
					userListBuffer = userListBuffer.append(
							user.getContact().getEmail()).append(";");
				}
			}

		}
		if (userListBuffer.length() > 0) {
			userList = userListBuffer.substring(0, userListBuffer.length() - 1);
		}
		return userList;
	}

	public List<User> getUsersDer() {
		List<User> usersDer = new ArrayList<User>();
		if (getUsers() != null) {
			usersDer.addAll(getUsers());
		}
		return usersDer;
	}
}