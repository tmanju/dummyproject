package com.thirdpillar.codify.loanpath.model;

import java.util.ArrayList;
import java.util.List;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.ContextHolder;

public class DocumentPackageDetail extends BaseDataObject {

	public String getDocDescription() {
		String docDesc = "";
		if (ContextHolder.getContext() != null
				&& ContextHolder.getContext().getRootContextEntity() != null
				&& (ContextHolder.getContext().getRootContextEntity()) instanceof Request) {
				List<Document> generatedDocList = null;
				List<Document> uploadedDocList = null;
				Request req=(Request)ContextHolder.getContext().getRootContextEntity();
				generatedDocList = ((DocumentGroup)req.getDocumentGroup()).getGeneratedDocuments();
				uploadedDocList = ((DocumentGroup)req.getDocumentGroup()).getUploadedDocuments();
				if((!generatedDocList.isEmpty()) && generatedDocList.contains(getDocument())){
					docDesc =  "Generated Document";
				}else if((!uploadedDocList.isEmpty()) && uploadedDocList.contains(getDocument())){
					docDesc  =  "Uploaded Document";
				}
	               		
			}
			return docDesc;
		}
	
		public List<Document> getFilteredDocuments(){
			List<Document> filteredDocuments = new ArrayList<Document>();
			if(ContextHolder.getContext() != null && ContextHolder.getContext().getRootContextEntity() !=null && (ContextHolder.getContext().getRootContextEntity())instanceof Request){
             	Request req=(Request)ContextHolder.getContext().getRootContextEntity();
             	for(Document document : ((DocumentGroup)req.getDocumentGroup()).getGeneratedDocuments()){
            		if(document != null && document.getUploadedStream() != null){
            			filteredDocuments.add(document);
	                 }	
				}
				for(Document document : ((DocumentGroup)req.getDocumentGroup()).getUploadedDocuments()){
            		if(document != null && document.getUploadedStream() != null){
            			filteredDocuments.add(document);
	                 }	
				}
			}
			return filteredDocuments;
	   }
		
		
}
