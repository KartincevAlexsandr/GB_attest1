package Model;

public enum AnimalType {
	Horse,
    Cat,
    Dog,
    Mice;

    public static AnimalType getType (int typeId){
        switch (typeId){
	        case 1:
	        	return AnimalType.Horse;
            case 2:
                return AnimalType.Cat;
            case 3:
                return AnimalType.Dog;
            case 4:
                return AnimalType.Mice;
            default:
                return null;
        }
    }
}
