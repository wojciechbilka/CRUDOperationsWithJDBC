import GenericCRUD.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    private static final String URL = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=CONNECTIS;integratedSecurity=true;";
    private static final String driverPath = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static Connection connection = new SQLConnection().getConnection();
    private static Transaction transaction = new Transaction(connection);
    private static SQLOperations operations = new SQLOperations();

    private static void selectExample() {
        List<String> carColumns = new ArrayList<>();
        carColumns.add("Name");
        carColumns.add("Model");
        carColumns.add("RegistryDate");

        SQLTable cars = new SQLTable("Car", "ID", carColumns);

        operations.addOperation(OperationType.SELECT, cars);
        transaction.start(operations);
    }

    private static void insertExample() {
        List<String> carColumns = new ArrayList<>();
        carColumns.add("Name");
        carColumns.add("Model");
        carColumns.add("RegistryDate");

        SQLTable cars = new SQLTable("Car", "ID", carColumns);

        Map<String, String> carsInsertColumnValueMap = new HashMap<>();
        carsInsertColumnValueMap.put("Name", "'Mustang'");
        carsInsertColumnValueMap.put("Model", "'Ford'");
        carsInsertColumnValueMap.put("RegistryDate", "'2000-02-12'");

        operations.addOperation(OperationType.INSERT, cars, carsInsertColumnValueMap);
        operations.addOperation(OperationType.SELECT, cars);
        transaction.start(operations);
    }

    private static void updateExample() {
        List<String> carColumns = new ArrayList<>();
        carColumns.add("Name");
        carColumns.add("Model");
        carColumns.add("RegistryDate");
        SQLTable cars = new SQLTable("Car", "ID", carColumns);

        Map<String, String> carsUpdateColumnValueMap = new HashMap<>();
        carsUpdateColumnValueMap.put("RegistryDate", "'1996-08-23'");

        operations.addOperation(OperationType.UPDATE, cars, 3, carsUpdateColumnValueMap);
        operations.addOperation(OperationType.SELECT, cars);
        transaction.start(operations);
    }

    private static void deleteExample() {
        List<String> carColumns = new ArrayList<>();
        carColumns.add("Name");
        carColumns.add("Model");
        carColumns.add("RegistryDate");
        SQLTable cars = new SQLTable("Car", "ID", carColumns);

        operations.addOperation(OperationType.DELETE, cars, 6);
        transaction.start(operations);
    }

    private static void cascadeDeleteOneToMany() {
        List<String> cityColumns = new ArrayList<>();
        cityColumns.add("ID");
        cityColumns.add("Name");
        cityColumns.add("Population");
        SQLTable cityTable = new SQLTable("City", "ID", cityColumns);

        List<String> buildingsColumns = new ArrayList<>();
        buildingsColumns.add("ID");
        buildingsColumns.add("Name");
        SQLTable buildingsTable = new SQLTable("Buildings", "ID", buildingsColumns);

        List<String> cityBuildingsColumns = new ArrayList<>();
        cityBuildingsColumns.add("CityID");
        cityBuildingsColumns.add("BuildingsID");
        SQLTable cityBuildingsTable = new SQLTable("CityBuildingsFK", cityBuildingsColumns);

        SQLRelationship cityBuildingsRel = new SQLRelationship(cityBuildingsTable, cityTable, buildingsTable, "CityID", "BuildingsID", RelationshipType.ONE_TO_MANY);
        cityTable.addRelationship(cityBuildingsRel);
        SQLRelationship buildingsCityRel = cityBuildingsRel.createReverseRelationship();
        buildingsTable.addRelationship(buildingsCityRel);

        operations.addOperation(OperationType.SELECT, cityTable);
        operations.addOperation(OperationType.SELECT, buildingsTable);
        operations.addOperation(OperationType.CASCADE_DELETE, cityTable, 1);
        operations.addOperation(OperationType.SELECT, cityTable);
        operations.addOperation(OperationType.SELECT, buildingsTable);
        transaction.start(operations);
    }

    private static void cascadeDeleteManyToMany() {
        List<String> cityColumns = new ArrayList<>();
        cityColumns.add("ID");
        cityColumns.add("Name");
        cityColumns.add("Population");
        SQLTable cityTable = new SQLTable("City", "ID", cityColumns);

        List<String> buildingsColumns = new ArrayList<>();
        buildingsColumns.add("ID");
        buildingsColumns.add("Name");
        SQLTable buildingsTable = new SQLTable("Buildings", "ID", buildingsColumns);

        List<String> cityBuildingsColumns = new ArrayList<>();
        cityBuildingsColumns.add("CityID");
        cityBuildingsColumns.add("BuildingsID");
        SQLTable cityBuildingsTable = new SQLTable("CityBuildingsFK", cityBuildingsColumns);

        SQLRelationship cityBuildingsRel = new SQLRelationship(cityBuildingsTable, cityTable, buildingsTable, "CityID", "BuildingsID", RelationshipType.MANY_TO_MANY);
        cityTable.addRelationship(cityBuildingsRel);
        SQLRelationship buildingsCityRel = cityBuildingsRel.createReverseRelationship();
        buildingsTable.addRelationship(buildingsCityRel);

        operations.addOperation(OperationType.SELECT, cityTable);
        operations.addOperation(OperationType.SELECT, buildingsTable);
        operations.addOperation(OperationType.CASCADE_DELETE, cityTable, 3);
        operations.addOperation(OperationType.SELECT, cityTable);
        operations.addOperation(OperationType.SELECT, buildingsTable);
        transaction.start(operations);
    }

    private static void cascadeDeleteOneToManyComplex() {
        List<String> cityColumns = new ArrayList<>();
        cityColumns.add("ID");
        cityColumns.add("Name");
        cityColumns.add("Population");
        SQLTable cityTable = new SQLTable("City", "ID", cityColumns);

        List<String> buildingsColumns = new ArrayList<>();
        buildingsColumns.add("ID");
        buildingsColumns.add("Name");
        SQLTable buildingsTable = new SQLTable("Buildings", "ID", buildingsColumns);

        List<String> visitorsColumns = new ArrayList<>();
        cityColumns.add("ID");
        cityColumns.add("Name");
        cityColumns.add("Surname");
        SQLTable visitorsTable = new SQLTable("Visitors", "ID", visitorsColumns);

        List<String> cityBuildingsColumns = new ArrayList<>();
        cityBuildingsColumns.add("CityID");
        cityBuildingsColumns.add("BuildingsID");
        SQLTable cityBuildingsTable = new SQLTable("CityBuildingsFK", cityBuildingsColumns);

        List<String> buildingsVisitorsColumns = new ArrayList<>();
        buildingsVisitorsColumns.add("BuildingsID");
        buildingsVisitorsColumns.add("VisitorsID");
        SQLTable buildingsVisitorsTable = new SQLTable("BuildingsVisitorsFK", buildingsVisitorsColumns);

        SQLRelationship cityBuildingsRel = new SQLRelationship(cityBuildingsTable, cityTable, buildingsTable, "CityID", "BuildingsID", RelationshipType.ONE_TO_MANY);
        cityTable.addRelationship(cityBuildingsRel);
        SQLRelationship buildingsCityRel = cityBuildingsRel.createReverseRelationship();
        buildingsTable.addRelationship(buildingsCityRel);

        SQLRelationship buildingsVisitorsRel = new SQLRelationship(buildingsVisitorsTable, buildingsTable, visitorsTable, "BuildingsID", "VisitorsID", RelationshipType.ONE_TO_MANY);
        buildingsTable.addRelationship(buildingsVisitorsRel);
        SQLRelationship visitorsBuildingsRel = buildingsVisitorsRel.createReverseRelationship();
        visitorsTable.addRelationship(visitorsBuildingsRel);

        operations.addOperation(OperationType.SELECT, cityTable);
        operations.addOperation(OperationType.SELECT, buildingsTable);
        operations.addOperation(OperationType.SELECT, visitorsTable);
        operations.addOperation(OperationType.CASCADE_DELETE, cityTable, 1);
        operations.addOperation(OperationType.SELECT, cityTable);
        operations.addOperation(OperationType.SELECT, buildingsTable);
        operations.addOperation(OperationType.SELECT, visitorsTable);

        transaction.start(operations);
    }

    private static void rollbackExample() {
        List<String> carColumns = new ArrayList<>();
        carColumns.add("Name");
        carColumns.add("Model");
        carColumns.add("RegistryDate");

        SQLTable cars = new SQLTable("Car", "ID", carColumns);

        operations.addOperation(OperationType.SELECT, cars);
        operations.addOperation(OperationType.DELETE, cars, 13);
        operations.addOperation(OperationType.DELETE, cars, "62");

        transaction.start(operations);
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        //selectExample();
        //insertExample();
        //updateExample();
        //deleteExample();
        //cascadeDeleteManyToMany();
        cascadeDeleteOneToManyComplex();
        //rollbackExample();

    }
}
