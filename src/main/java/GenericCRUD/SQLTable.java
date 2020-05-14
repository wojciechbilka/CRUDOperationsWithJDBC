package GenericCRUD;

import java.util.ArrayList;
import java.util.List;

public class SQLTable {
    private String name;
    private String primaryKeyName;
    private List<String> columnNames;
    private List<SQLRelationship> relationships = new ArrayList<>();

    public SQLTable(String name, List<String> columnNames) {
        this.name = name;
        this.columnNames = columnNames;
    }

    public SQLTable(String name, String primaryKeyName, List<String> columnNames) {
        this.name = name;
        this.primaryKeyName = primaryKeyName;
        this.columnNames = columnNames;
    }

    public SQLTable(String name, String primaryKeyName, List<String> columnNames, SQLRelationship ... relationships) {
        this.name = name;
        this.primaryKeyName = primaryKeyName;
        this.columnNames = columnNames;
        for(SQLRelationship rel : relationships) {
            this.relationships.add(rel);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<SQLRelationship> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<SQLRelationship> relationships) {
        this.relationships = relationships;
    }

    public void addColumnName(String colName) {
        this.columnNames.add(colName);
    }

    public void addRelationship(SQLRelationship relationship) {
        this.relationships.add(relationship);
    }
}
