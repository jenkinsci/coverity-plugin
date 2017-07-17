/*******************************************************************************
 * Copyright (c) 2017 Synopsys, Inc
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
import java.util.List;
import java.util.Map;

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
            for (Object key : jsonRow.keySet()) {
                rowDetail.put((String)key, jsonRow.get(key));
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
