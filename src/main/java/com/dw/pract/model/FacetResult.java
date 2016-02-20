package com.dw.pract.model;

import java.util.List;

/**
 *
 * @author cbrown
 */
public class FacetResult {
  private String type;

  private List<FacetNode> facetNodes;

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<FacetNode> getFacetNodes() {
    return facetNodes;
  }

  public void setFacetNodes(List<FacetNode> facetNodes) {
    this.facetNodes = facetNodes;
  }
}
