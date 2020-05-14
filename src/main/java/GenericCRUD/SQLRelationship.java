package GenericCRUD;

public class SQLRelationship {
    private SQLTable relTable;
    private SQLTable firstTable;
    private SQLTable secondTable;
    private String firstTableIdRefName;
    private String secondTableIdRefName;
    private RelationshipType relationshipType;

    public SQLRelationship(SQLTable relTable, SQLTable firstTable, SQLTable secondTable, String firstTableIdRefName, String secondTableIdRefName, RelationshipType relationshipType) {
        this.relTable = relTable;
        this.firstTable = firstTable;
        this.secondTable = secondTable;
        this.firstTableIdRefName = firstTableIdRefName;
        this.secondTableIdRefName = secondTableIdRefName;
        this.relationshipType = relationshipType;
    }

    public SQLTable getRelTable() {
        return relTable;
    }

    public void setRelTable(SQLTable relTable) {
        this.relTable = relTable;
    }

    public SQLTable getFirstTable() {
        return firstTable;
    }

    public void setFirstTable(SQLTable firstTable) {
        this.firstTable = firstTable;
    }

    public SQLTable getSecondTable() {
        return secondTable;
    }

    public void setSecondTable(SQLTable secondTable) {
        this.secondTable = secondTable;
    }

    public String getFirstTableIdRefName() {
        return firstTableIdRefName;
    }

    public void setFirstTableIdRefName(String firstTableIdRefName) {
        this.firstTableIdRefName = firstTableIdRefName;
    }

    public String getSecondTableIdRefName() {
        return secondTableIdRefName;
    }

    public void setSecondTableIdRefName(String secondTableIdRefName) {
        this.secondTableIdRefName = secondTableIdRefName;
    }

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    public SQLRelationship createReverseRelationship() {
        return new SQLRelationship(this.relTable, this.secondTable, this.firstTable, this.secondTableIdRefName, this.firstTableIdRefName, this.relationshipType.getOpposite());
    }
}
