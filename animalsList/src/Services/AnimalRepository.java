package Services;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.time.LocalDate;
import Model.*;


public class AnimalRepository implements IRepository<Animal> {

    private Creator animalCreator;
    private Statement sqlSt;
    private ResultSet resultSet;
    private String SQLstr;
    
    public AnimalRepository() {
        this.animalCreator = new AnimalCreator();
    };

    @Override
    public List<Animal> getAll() {
        List<Animal> farm = new ArrayList<Animal>();
        Animal animal;
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection dbConnection = DriverManager.getConnection("jdbc:sqlite:dblocal.db")) {
                sqlSt = dbConnection.createStatement();
                SQLstr = "SELECT  Genus_id,  Id, AnimalName,  Birthday  FROM animals ORDER BY Id";
                resultSet = sqlSt.executeQuery(SQLstr);
                while (resultSet.next()) {

                    AnimalType type = AnimalType.getType(resultSet.getInt(1));
                    int id = resultSet.getInt(2);
                    String name = resultSet.getString(3);
                    LocalDate birthday = resultSet.getDate(4).toLocalDate();
                    
                    animal = animalCreator.createAnimal(type, name, birthday);
                    animal.setAnimalId(id);
                    farm.add(animal);
                }
                return farm;
            } 
        } catch (ClassNotFoundException  | SQLException ex) {
            Logger.getLogger(AnimalRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }           
    }

    @Override
    public Animal getById(int animalId) {
        Animal animal = null;
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection dbConnection = DriverManager.getConnection("jdbc:sqlite:dblocal.db")) {

                SQLstr = "SELECT Genus_id, Id, animalName, Birthday FROM animals WHERE Id = ?";
                PreparedStatement prepSt = dbConnection.prepareStatement(SQLstr);
                prepSt.setInt(1, animalId);
                resultSet = prepSt.executeQuery();

                if (resultSet.next()) {

                    AnimalType type = AnimalType.getType(resultSet.getInt(1));
                    int id = resultSet.getInt(2);
                    String name = resultSet.getString(3);
                    LocalDate birthday = resultSet.getDate(4).toLocalDate();

                    animal = animalCreator.createAnimal(type, name, birthday);
                    animal.setAnimalId(id);
                } 
                return animal;
            } 
        } catch (ClassNotFoundException  | SQLException ex) {
            Logger.getLogger(AnimalRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public int create(Animal animal) {
        int rows;
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection dbConnection = DriverManager.getConnection("jdbc:sqlite:dblocal.db")) {

                SQLstr = "INSERT INTO animals (AnimalName, Birthday, Genus_id) SELECT ?, ?, (SELECT Id FROM animal_type WHERE Genus_name = ?)";
                PreparedStatement prepSt = dbConnection.prepareStatement(SQLstr);
                prepSt.setString(1, animal.getName());
                prepSt.setDate(2, Date.valueOf(animal.getBirthdayDate())); 
                prepSt.setString(3, animal.getClass().getSimpleName());

                rows = prepSt.executeUpdate();
                return rows;
            }
        } catch (ClassNotFoundException  | SQLException ex) {
            Logger.getLogger(AnimalRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } 
    }

    public void train (int id, String command){
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection dbConnection = DriverManager.getConnection("jdbc:sqlite:dblocal.db")) {
                String SQLstr = "INSERT INTO animal_to_commands (AnimalId, CommandId) SELECT ?, (SELECT Id FROM commands WHERE Command_name = ?)";
                PreparedStatement prepSt = dbConnection.prepareStatement(SQLstr);
                prepSt.setInt(1, id);
                prepSt.setString(2, command);

                prepSt.executeUpdate();
            }
        } catch (ClassNotFoundException  | SQLException ex) {
            Logger.getLogger(AnimalRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } 
    }

    public List<String> getCommandsById (int animalId, int commands_type){   
        
        // commands type = 1  - получить команды, выполняемые питомцем, 2 - команды, выполнимые животным того рода, к которому относится питомец

        List <String> commands = new ArrayList <>();
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection dbConnection = DriverManager.getConnection("jdbc:sqlite:dblocal.db")) {
                PreparedStatement prepSt = dbConnection.prepareStatement("SELECT Command_name FROM animal_to_commands pc JOIN commands c ON pc.CommandId = c.Id WHERE pc.animalId = ?");
                prepSt.setInt(1, animalId);
                resultSet = prepSt.executeQuery();
                while (resultSet.next()) {
                    commands.add(resultSet.getString(1));
                }
                return commands;
            }
        } catch (ClassNotFoundException  | SQLException ex) {
            Logger.getLogger(AnimalRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } 
    }

    @Override
    public int update(Animal animal) {
        int rows;
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection dbConnection = DriverManager.getConnection("jdbc:sqlite:dblocal.db")) {
                SQLstr = "UPDATE animals SET AnimalName = ?, Birthday = ? WHERE Id = ?";
                PreparedStatement prepSt = dbConnection.prepareStatement(SQLstr);

                prepSt.setString(1, animal.getName());
                prepSt.setDate(2, Date.valueOf(animal.getBirthdayDate())); 
                prepSt.setInt(3,animal.getAnimalId());
                
                rows = prepSt.executeUpdate();
                return rows;
            }
        } catch (ClassNotFoundException  | SQLException ex) {
            Logger.getLogger(AnimalRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } 
    }

    @Override
    public void delete (int id) {
        try {
            Class.forName("org.sqlite.JDBC");
            try (Connection dbConnection = DriverManager.getConnection("jdbc:sqlite:dblocal.db")) {
                SQLstr = "DELETE FROM animals WHERE Id = ?";
                PreparedStatement prepSt = dbConnection.prepareStatement(SQLstr);
                prepSt.setInt(1,id);
                prepSt.executeUpdate();
            }
        } catch (ClassNotFoundException  | SQLException ex) {
            Logger.getLogger(AnimalRepository.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        } 
    }

}
