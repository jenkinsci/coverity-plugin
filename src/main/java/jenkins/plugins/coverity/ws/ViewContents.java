/*******************************************************************************
 * Copyright (c) 2018 Synopsys, Inc
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Synopsys, Inc - initial implementation and documentation
 *******************************************************************************/
package jenkins.plugins.coverity.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class ViewContents {
    private final Long totalRows;
    private final Long rowsOffset;
    private final List<String> columns;
    private final List<Map<String, Object>> rows;

    public ViewContents(JSONObject viewContentsV1) {
        totalRows = (Long)viewContentsV1.get("totalRows");
        rowsOffset = (Long)viewContentsV1.get("offset");

        columns = new ArrayList<>();
        final JSONArray viewColumns = (JSONArray)viewContentsV1.get("columns");
        for (Object column : viewColumns) {
            JSONObject jsonColumns = (JSONObject)column;
            columns.add((String)jsonColumns.get("name"));
        }

        rows = new ArrayList<>();
        final JSONArray viewRows = (JSONArray)viewContentsV1.get("rows");
        for (Object row : viewRows) {
            JSONObject jsonRow = (JSONObject)row;
            Map<String, Object> rowDetail = new HashMap<>();
            for (Iterator iterator = jsonRow.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry)iterator.next();
                rowDetail.put(String.valueOf(entry.getKey()), entry.getValue());
            }
            rows.add(rowDetail);
        }
    }

    public Long getTotalRows() {
        return totalRows;
    }

    public Long getRowsOffset() {
        return rowsOffset;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }
}
