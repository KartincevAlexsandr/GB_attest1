import Controller.AnimalController;
import Model.Animal;
import Services.IRepository;
import Services.AnimalRepository;
import UserInterface.ConsoleMenu;

public class Program {
    public static void main(String[] args) throws Exception {

        IRepository <Animal> myFarm = new AnimalRepository();
        AnimalController controller = new AnimalController(myFarm);
        new ConsoleMenu (controller).start();
    }
}    