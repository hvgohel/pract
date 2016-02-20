package com.dw.pract.model.paging;

import java.util.List;

import com.dw.pract.model.Entity;

public class PagedResultsWrapper<T extends Entity> {

  private List<T> results;
  private int pageSize;
  private Long totalResults;

  public List<T> getResults() {
    return results;
  }

  public void setResults(List<T> results) {
    this.results = results;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public Long getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(Long totalResults) {
    this.totalResults = totalResults;
  }
}
