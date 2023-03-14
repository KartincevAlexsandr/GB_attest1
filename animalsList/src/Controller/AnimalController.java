package Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import Model.*;
import Services.IRepository;
import Services.AnimalRepository;
import UserInterface.*;

public class AnimalController {
    private IRepository<Animal> animalRepository;
    private Creator animalCreator;
    private final View<Animal> view;


    public AnimalController(IRepository<Animal> animalRepository) {
        this.animalRepository = animalRepository;
        this.animalCreator = new AnimalCreator();
        this.view = new ConsoleView();

    }

    public void createAnimal(AnimalType type) {

        String[] data = new String[] { view.getName(), view.getBirthday() };
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthday = LocalDate.parse(data[1], formatter);
        try {
            int res = animalRepository.create(animalCreator.createAnimal(type, data[0], birthday));
            view.showMessage(String.format("%d добавлено изменение", res));
        } catch (RuntimeException e) {
            view.showMessage(e.getMessage());
        }

    }

    public void updateAminal(int id) {

        Animal animal = getById(id);
        String[] data = new String[] { view.getName(), view.getBirthday() };
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthday = LocalDate.parse(data[1], formatter);
        animal.setName(data[0]);
        animal.setBirthday(birthday);
        try {
            int res = animalRepository.update(animal);
            view.showMessage(String.format("%d изменение произведено \n", res));
        } catch (RuntimeException e) {
            view.showMessage(e.getMessage());
        }

    }

    public void getAllAnimal() {
        try {
            view.printAll(animalRepository.getAll(), Animal.class);
        } catch (RuntimeException e) {
            view.showMessage(e.getMessage());
        }
    }

    public boolean trainAnimal(int id, String command) {
        try {

            if (((AnimalRepository) animalRepository).getCommandsById(id, 1).contains(command))
                view.showMessage("это мы уже умеем");
            else {
                if (!((AnimalRepository) animalRepository).getCommandsById(id, 2).contains(command))
                    view.showMessage("невыполнимая команда");
                else {
                    ((AnimalRepository) animalRepository).train(id, command);
                    view.showMessage("команда разучена\n");
                    return true;
                }
            }
        } catch (RuntimeException e) {
            view.showMessage(e.getMessage());
        }
        return false;
    }

    public Animal getById(int id) {
        try {
            return animalRepository.getById(id);
        } catch (RuntimeException e) {
            view.showMessage(e.getMessage());
        }
        return null;
    }

    public void delete(int id) {
        try {
            animalRepository.delete(id);
            view.showMessage("запись удалена");
        } catch (RuntimeException e) {
            view.showMessage(e.getMessage());
        }
    }

    public void getCommands(int id) {
        try {
            view.printAll(((AnimalRepository) animalRepository).getCommandsById(id, 1), String.class);
        } catch (RuntimeException e) {
            view.showMessage(e.getMessage());
        }
    }
}
