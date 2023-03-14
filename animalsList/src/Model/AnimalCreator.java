package Model;

public class AnimalCreator extends Creator {

    @Override
    protected Animal createNewAnimal (AnimalType type) {

        switch (type) {
	        case Horse:
	            return new Horse();
            case Cat:
                return new Cat();
            case Dog:
                return new Dog();
            case Mice:
                return new Mice();
        }
        return null;
    }
}
