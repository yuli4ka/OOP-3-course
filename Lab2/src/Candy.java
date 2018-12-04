import java.util.ArrayList;

public class Candy {
    private int id;
    private String name;
    private int energy;
    private ArrayList type = new ArrayList();
    private ArrayList ingredients = new ArrayList();
    private String production;
    //public class Value{
        private int protein;
        private int fats;
        private int carbohydrates;

        void setValueProtein(int valueProtein){
            this.protein = valueProtein;
        }

    public int getProtein() {
        return protein;
    }

    void setValueFats(int valueFats){
            this.fats = valueFats;
        }

    public int getFats() {
        return fats;
    }

    void setValueCarbohydrates(int valueCarbohydrates){
            this.carbohydrates = valueCarbohydrates;
        }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    //}

    void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    void setType(String type) {
        this.type.add(type);
    }

    String getType() {
        if (type.size() == 0)
            return "no";
        String tt = String.valueOf(type.get(0));
        for (int i=1; i < type.size(); i++){
            tt = tt + ", " + String.valueOf(type.get(i));
        }
        return tt;
    }

    void setIngredients(String ingredients) {
        this.ingredients.add(ingredients);
    }

    String getIngredients() {
        if (ingredients.size() == 0)
            return "no";
        String tt = String.valueOf(ingredients.get(0));
        for (int i=1; i < ingredients.size(); i++){
            tt = tt + ", " + String.valueOf(ingredients.get(i));
        }
        return tt;
    }

    void setProduction(String production) {
        this.production = production;
    }

    public String getProduction() {
        return production;
    }

    @Override
    public String toString() {
        return "Candy:: ID: " + this.getId()+";\n        Name: " + this.getName() +
                ";\n        Type: " + this.getType() + ";\n        Energy: " + this.getEnergy() +
                "kkal;\n        Ingredients: " + this.getIngredients() + ";\n        Production: "
                + this.getProduction() +
                ";\n        Value:\n            Carbohydrates: " + this.getCarbohydrates()
                + "g\n            Protein: " + this.getProtein()
                + "g\n            Fats: " + this.getFats() + "g\n";
    }

}
