package GenericCRUD;

import java.sql.*;
import java.util.*;

//TODO Add validator for where staements and OperationParams objects
public class SQLOperations {

    public LinkedHashMap<String, List<String>> resultMap = new LinkedHashMap<>();

    private List<OperationParams> operations = new ArrayList<>();

    private void select(Connection connection, OperationParams params) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        if (params.columnValueMap == null) {
            query.append("* FROM " + params.table.getName());
        } else {
            int i = 1;
            int j = params.columnValueMap.size();
            for (String col : params.columnValueMap.keySet()) {
                query.append(col);
                if (i < j) {
                    query.append(", ");
                    i++;
                }
            }
            query.append(" FROM " + params.table.getName());
        }
        if (params.id != -1) {
            query.append(" WHERE " + params.table.getPrimaryKeyName() + " = " + params.id);
        } else if (!params.whereCondition.isEmpty()) {
            query.append(" WHERE " + params.whereCondition);
        }
        PreparedStatement statement = connection.prepareStatement(query.toString());
        ResultSet rs = statement.executeQuery();
        System.out.println(query.toString());
        saveQueryResult(query.toString(), rs);
    }

    private void saveQueryResult(String query, ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnsNumber = metaData.getColumnCount();
        List<String> list = new ArrayList<>();
        StringBuilder temp = new StringBuilder();
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) {
                    temp.append(",  ");
                }
                String columnValue = resultSet.getString(i);
                temp.append(metaData.getColumnName(i) + " = " + columnValue);
                System.out.print(temp.toString());
                list.add(temp.toString());
                temp.setLength(0);
            }
            System.out.println("");
        }
        resultMap.put(query, list);
    }

    private void insert(Connection connection, OperationParams params) throws SQLException {
        StringBuilder stringBuilderCol = new StringBuilder(" (");
        StringBuilder stringBuilderVal = new StringBuilder("VALUES (");

        List<String> columnNames = params.table.getColumnNames();
        columnNames.remove(params.table.getPrimaryKeyName());
        for (int i = 0; i < columnNames.size(); i++) {
            String colName = columnNames.get(i);
            String value = params.columnValueMap.get(colName);
            if (value != null) {
                stringBuilderCol.append(colName);
                stringBuilderVal.append(params.columnValueMap.get(colName));
                if (i >= columnNames.size() - 1) {
                    stringBuilderCol.append(") ");
                    stringBuilderVal.append(")");
                } else {
                    stringBuilderCol.append(", ");
                    stringBuilderVal.append(", ");
                }
            } else if (i < columnNames.size() - 1) {
                stringBuilderCol.append(") ");
                stringBuilderVal.append(")");
            }
        }
        System.out.println("INSERT INTO " + params.table.getName() + stringBuilderCol.toString() + stringBuilderVal.toString());
        PreparedStatement statement = connection.prepareStatement("INSERT INTO " + params.table.getName() + stringBuilderCol.toString() + stringBuilderVal.toString());
        statement.execute();
    }

    private void update(Connection connection, OperationParams params) throws SQLException {
        StringBuilder query = new StringBuilder();
        Map<String, String> columnValueMap = params.getColumnValueMap();
        int i = 0;
        int j = columnValueMap.size() - 1;

        for (Map.Entry<String, String> entry : columnValueMap.entrySet()) {
            query.append(entry.getKey() + " = " + entry.getValue());
            if (i < j) {
                query.append(", ");
            }
            i++;
        }
        System.out.println("UPDATE " + params.table.getName() + " SET " + query + " WHERE " + params.table.getPrimaryKeyName() + " = " + params.id);
        PreparedStatement statement = connection.prepareStatement("UPDATE " + params.table.getName() + " SET " + query + " WHERE " + params.table.getPrimaryKeyName() + " = " + params.id);
        statement.execute();
    }

    private void delete(Connection connection, OperationParams params) throws SQLException {
        PreparedStatement statement;
        if (params.id != -1) {
            System.out.println("DELETE FROM " + params.table.getName() + " WHERE " + params.table.getPrimaryKeyName() + " = " + params.id);
            statement = connection.prepareStatement("DELETE FROM " + params.table.getName() + " WHERE " + params.table.getPrimaryKeyName() + " = " + params.id);
            statement.execute();
        } else if (!params.whereCondition.isEmpty()) {
            System.out.println("DELETE FROM " + params.table.getName() + " WHERE " + params.whereCondition);
            statement = connection.prepareStatement("DELETE FROM " + params.table.getName() + " WHERE " + params.whereCondition);
            statement.execute();
        } else {
            System.out.println("Wrong params declaration");
        }
    }

    private void cascadeDelete(Connection connection, OperationParams params) throws SQLException {
        // first table - table we deleting from, second table - table/tables that is associated with first table
        // getting relationships of table deleting from
        SQLTable mainTable = params.table;
        List<SQLRelationship> relationships = mainTable.getRelationships();
        // checking if there are any relationships if no just delete selected record from first table
        if (!relationships.isEmpty()) {
            // iterating every relationship
            for (SQLRelationship rel : relationships) {
                // creating map of columnValue pairs to get only IDs of second table that match selected record from first table (for select operation need only key, that's why value is empty)
                Map<String, String> tempMap = new HashMap<>();
                tempMap.put(rel.getSecondTableIdRefName(), "");
                OperationParams tempParams = new OperationParams(OperationType.SELECT, rel.getRelTable(), tempMap, rel.getFirstTableIdRefName() + " = " + params.id);
                // selecting IDs from second table that are associated with selected record from first table
                select(connection, tempParams);
                // receiving result of SELECT operation
                List<String> listOfValues = getLatestResultList();
                // parsing result to simple string (from: columnName = value, ... to: list of values)
                List<String> listOfValuesFormatted = parse(listOfValues, rel.getSecondTableIdRefName());
                if (listOfValuesFormatted != null) {
                    // deleting all relationship records linked to selected record from first table
                    OperationParams cascadeParamsTabRel = new OperationParams(OperationType.DELETE, rel.getRelTable(), rel.getFirstTableIdRefName() + " = " + params.id);
                    delete(connection, cascadeParamsTabRel);
                    // iterating every ID found
                    for (String val : listOfValuesFormatted) {
                        // if relationship type is one-to-one/many it means record from second table may be deleted if there is no other relationship linked to it (that is why -> cascadeDelete on it)
                        // if relationship type is many-to-one/many it means there can always be record from other table associated with second table record (deleting ONLY relationship then)
                        if ((rel.getRelationshipType() == RelationshipType.ONE_TO_MANY || rel.getRelationshipType() == RelationshipType.ONE_TO_ONE)) {
                            OperationParams cascadeParamsTab2 = new OperationParams(OperationType.CASCADE_DELETE, rel.getSecondTable(), Integer.valueOf(val));
                            cascadeDelete(connection, cascadeParamsTab2);
                        }
                    }
                }
            }
        }
        delete(connection, params);
    }

    private List<String> getLatestResultList() {
        List<Map.Entry<String, List<String>>> tempList = new ArrayList(resultMap.entrySet());
        Map.Entry<String, List<String>> lastEntry = tempList.get(tempList.size() - 1);
        return lastEntry.getValue();
    }

    private static List<String> parse(List<String> resultList, String colName) {
        List<String> returnList = new ArrayList<>();
        for (String s : resultList) {
            if (s.contains(colName)) {
                String value = s.substring(s.lastIndexOf('=') + 1).replace(",", "").trim();
                returnList.add(value);
            }
        }
        return returnList.isEmpty() ? null : returnList;
    }

    public void addOperation(OperationType type, SQLTable table) {
        operations.add(new OperationParams(type, table));
    }

    public void addOperation(OperationType type, SQLTable table, String whereCondition) {
        operations.add(new OperationParams(type, table, whereCondition));
    }

    public void addOperation(OperationType type, SQLTable table, Integer id) {
        operations.add(new OperationParams(type, table, id));
    }

    public void addOperation(OperationType type, SQLTable table, Map<String, String> columnValueMap) {
        operations.add(new OperationParams(type, table, columnValueMap));
    }

    public void addOperation(OperationType type, SQLTable table, Integer id, Map<String, String> columnValueMap) {
        operations.add(new OperationParams(type, table, id, columnValueMap));
    }

    public void addOperation(OperationType type, SQLTable table, Map<String, String> columnValueMap, String whereCondition) {
        operations.add(new OperationParams(type, table, columnValueMap, whereCondition));
    }

    public void execute(Connection connection) throws SQLException {
        for (OperationParams op : operations) {
            switch (op.getType()) {
                case SELECT:
                    select(connection, op);
                    break;
                case INSERT:
                    insert(connection, op);
                    break;
                case UPDATE:
                    update(connection, op);
                    break;
                case DELETE:
                    delete(connection, op);
                    break;
                case CASCADE_DELETE:
                    cascadeDelete(connection, op);
                    break;
            }
        }
        operations.clear();
    }

    private class OperationParams {
        private OperationType type;
        private SQLTable table;
        private Integer id = -1;
        private Map<String, String> columnValueMap;
        private String whereCondition = new String();

        private OperationParams(OperationType type, SQLTable table) {
            this.type = type;
            this.table = table;
        }

        private OperationParams(OperationType type, SQLTable table, String whereCondition) {
            this.type = type;
            this.table = table;
            this.whereCondition = whereCondition;
        }

        private OperationParams(OperationType type, SQLTable table, Integer id) {
            this.type = type;
            this.table = table;
            this.id = id;
        }

        private OperationParams(OperationType type, SQLTable table, Map<String, String> columnValueMap) {
            this.type = type;
            this.table = table;
            this.columnValueMap = columnValueMap;
        }

        public OperationParams(OperationType type, SQLTable table, Integer id, Map<String, String> columnValueMap) {
            this.type = type;
            this.table = table;
            this.id = id;
            this.columnValueMap = columnValueMap;
        }

        public OperationParams(OperationType type, SQLTable table, Map<String, String> columnValueMap, String whereCondition) {
            this.type = type;
            this.table = table;
            this.columnValueMap = columnValueMap;
            this.whereCondition = whereCondition;
        }

        private OperationType getType() {
            return type;
        }

        private void setType(OperationType type) {
            this.type = type;
        }

        public SQLTable getTable() {
            return table;
        }

        public void setTable(SQLTable table) {
            this.table = table;
        }

        private Integer getId() {
            return id;
        }

        private void setId(Integer id) {
            this.id = id;
        }

        private Map<String, String> getColumnValueMap() {
            return columnValueMap;
        }

        private void setColumnValueMap(Map<String, String> columnValueMap) {
            this.columnValueMap = columnValueMap;
        }

        public String getWhereCondition() {
            return whereCondition;
        }

        public void setWhereCondition(String whereCondition) {
            this.whereCondition = whereCondition;
        }
    }
}
