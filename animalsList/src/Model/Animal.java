package Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public abstract class Animal {
    
    protected int animalId;
    protected String name;
    protected LocalDate birthday;
    
    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBirthday(LocalDate date) {
        this.birthday = date;
    }

    public LocalDate getBirthdayDate(){
        return birthday;
    }

    public String getBirthday() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return formatter.format(birthday);
    }

    @Override
    public String toString() {
        return String.format("%d. %s: имя: %s, дата рождения: %s ", getAnimalId(), getClass().getSimpleName(), name, getBirthday());
    }
}
