package com.dw.pract.model;

import java.util.List;

public class FacetNode
{
    private boolean selected;

    private List<FacetNode> nodes;

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public List<FacetNode> getNodes()
    {
        return nodes;
    }

    public void setNodes(List<FacetNode> nodes)
    {
        this.nodes = nodes;
    }
}
