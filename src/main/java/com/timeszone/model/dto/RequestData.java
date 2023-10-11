package com.timeszone.model.dto;

import java.util.ArrayList;
import java.util.List;

public class RequestData {

    private List<String> selectedValues= new ArrayList<String>();
    private String search ;
    private int page = 0;
    private int size = 2;
	public List<String> getSelectedValues() {
		return selectedValues;
	}
	public void setSelectedValues(List<String> selectedValues) {
		this.selectedValues = selectedValues;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}

}
