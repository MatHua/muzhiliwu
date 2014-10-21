package com.muzhiliwu.list;

import java.util.List;

import org.nutz.dao.pager.Pager;

public class PageResult<T> {
	private Pager page;
	private List<T> results;

	public Pager getPage() {
		return page;
	}

	public void setPage(Pager page) {
		this.page = page;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

}
