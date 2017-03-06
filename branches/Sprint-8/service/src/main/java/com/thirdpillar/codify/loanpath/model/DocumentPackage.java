



package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;




public  class DocumentPackage extends BaseDataObject {
	/** Use serialVersionUID for interoperability. */
	private static final long serialVersionUID = 2880314593607352570L;
	
	public String getDocumentName(){
		if(this.getPackageFileName() != null){
			return this.getPackageFileName();
		}
		return null;
	}

}