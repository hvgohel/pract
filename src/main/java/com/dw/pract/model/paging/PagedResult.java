package com.dw.pract.model.paging;

import java.util.List;

import com.dw.pract.model.Entity;

/**
 * Data transfer object which can be used to represent paged response of an entity.
 * 
 * @param <T>
 */
public class PagedResult<T extends Entity> {

    private List<T> results;
    private Integer rpp;
    private Integer pageNo;
    private String sortOrder;
    private String sortOn;
    private Long totalResults;

    public PagedResult() {
    }

    public PagedResult(Integer rpp, String sortOrder, String sortOn) {
        super();
        this.rpp = rpp;
        this.sortOrder = sortOrder;
        this.sortOn = sortOn;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public Integer getRpp() {
        return rpp;
    }

    public void setRpp(Integer rpp) {
        this.rpp = rpp;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortOn() {
        return sortOn;
    }

    public void setSortOn(String sortOn) {
        this.sortOn = sortOn;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

}
