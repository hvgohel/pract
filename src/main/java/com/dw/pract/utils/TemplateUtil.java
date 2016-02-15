package com.dw.pract.utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

/**
 * Utility functions used for template (Velocity) engine features.
 */
@Named
public class TemplateUtil {

    /**
     * Returns context-data as Map which are common to all the templates.
     * 
     * @return
     */
    public Map<String, Object> getCommonTemplateData() {
        Map<String, Object> data = new HashMap<String, Object>();

        // TODO: put your common template data here

        return data;
    }

    /**
     * Returns the context data as a Map by merging common template data. <br>
     * Notes: <br>
     * 1. Returned map will always be the new reference of the Map. <br>
     * 2. If passed data contains a key which is also present in the common template data, then the data passed in the
     * argument will be finally visible.
     * 
     * @param data
     * @return
     */
    public Map<String, Object> mergeCommonTemplateData(Map<String, Object> data) {
        Map<String, Object> mergedData = new HashMap<String, Object>();

        mergedData.putAll(data);
        mergedData.putAll(getCommonTemplateData());

        return mergedData;
    }
}
