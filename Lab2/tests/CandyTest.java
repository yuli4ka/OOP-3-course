import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class CandyTest {

    private Candy candy;

    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    @Before
    public void setUp() throws Exception {
        candy = new Candy();
    }

    @Test
    public void testValueProtein() {
        candy.setValueProtein(56);
        assertEquals(56, candy.getProtein());
    }

    @Test
    public void testValueFats() {
        candy.setValueFats(78);
        assertEquals(78, candy.getFats());
    }

    @Test
    public void testValueCarbohydrates() {
        candy.setValueCarbohydrates(90);
        assertEquals(90, candy.getCarbohydrates());
    }

    @Test
    public void testId() {
        candy.setId(23);
        assertEquals(23, candy.getId());
    }

    @Test
    public void testName() {
        candy.setName("Cancer");
        assertEquals("Cancer", candy.getName());
    }

    @Test
    public void testEnergy() {
        candy.setEnergy(124);
        assertEquals(124, candy.getEnergy());
    }

    @Test
    public void testNoType() {
        assertEquals("no", candy.getType());
    }

    @Test
    public void testTwoType() {
        candy.setType("chocolate");
        candy.setType("iris");
        assertEquals("chocolate, iris", candy.getType());
    }

    @Test
    public void testNoIngredients() {
        assertEquals("no", candy.getIngredients());
    }

    @Test
    public void testTwoIngredients() {
        candy.setIngredients("water");
        candy.setIngredients("milk");
        assertEquals("water, milk", candy.getIngredients());
    }

    @Test
    public void testProduction() {
        candy.setProduction("prod1");
        assertEquals("prod1", candy.getProduction());
    }

    @Test
    public void testToString() {
        candy.setId(30);
        candy.setName("name");
        candy.setType("type");
        candy.setEnergy(123);
        candy.setIngredients("water");
        candy.setProduction("prod");
        candy.setValueCarbohydrates(20);
        candy.setValueFats(32);
        candy.setValueProtein(45);

        assertEquals("Candy:: ID: " + "30"+";\n        Name: " + "name" +
                ";\n        Type: " + "type" + ";\n        Energy: " + "123" +
                "kkal;\n        Ingredients: " + "water" + ";\n        Production: "
                + "prod" +
                ";\n        Value:\n            Carbohydrates: " + "20"
                + "g\n            Protein: " + "45"
                + "g\n            Fats: " + "32" + "g\n", candy.toString());
    }
}