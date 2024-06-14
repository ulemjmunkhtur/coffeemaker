package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {

    @Autowired
    private RecipeService service;
    private Ingredient coffee;
    private Ingredient milk;
    private Ingredient sugar;
    private Ingredient chocolate;
    private Ingredient cinnamon;




    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testAddRecipe () {

        final Recipe r1 = new Recipe();
        coffee = new Ingredient("coffee", 1);
        milk = new Ingredient("milk", 2);
        sugar = new Ingredient("sugar", 2);
        cinnamon = new Ingredient("cinnamon", 1);
        chocolate = new Ingredient("chocolate", 1);
        r1.setName( "Mocha" );
        r1.setPrice( 1 );
        r1.addIngredient(coffee);
        r1.addIngredient(chocolate);
        r1.addIngredient(cinnamon);
        
        service.save( r1 );

        final Recipe r2 = new Recipe();
        coffee = new Ingredient("coffee", 2);
        r2.setName( "Cappucino" );
        r2.setPrice( 1 );
        r2.addIngredient(sugar);
        r2.addIngredient(coffee);
        r2.addIngredient(milk);
        service.save(r2);

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );

        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
    }

    @Test
    @Transactional
    public void testNoRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        coffee = new Ingredient("coffee", 1);
        milk = new Ingredient("milk", 2);
        sugar = new Ingredient("sugar", 2);
        cinnamon = new Ingredient("cinnamon", 1);
        chocolate = new Ingredient("chocolate", 1);
        
        final Recipe r1 = new Recipe();
        r1.setName( "Tasty Drink" );
        r1.setPrice( 12 );
        r1.addIngredient(coffee);
        r1.addIngredient(chocolate);
        r1.addIngredient(cinnamon);

        final Recipe r2 = new Recipe();
        r2.setName( "Cappucino" );
        r2.setPrice(1);
        r2.addIngredient(sugar);
        r2.addIngredient(coffee);
        r2.addIngredient(milk);
       

     

        try {
            Assertions.assertEquals( 0, service.count(),
                    "There should be no recipes if none are saved" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }

    }
    
	@Test
	@Transactional
	public void testGetIngredients() {
		final Recipe recipe = new Recipe();
		recipe.setName("Latte");
		recipe.setPrice(2);

		// Add some ingredients
		Ingredient milk = new Ingredient("milk", 4);
		Ingredient coffee = new Ingredient("coffee", 2);
		recipe.addIngredient(milk);
		recipe.addIngredient(coffee);
		service.save(recipe);

		// Retrieve the saved recipe
		Recipe savedRecipe = service.findById(recipe.getId());

		List<Ingredient> ingredients = savedRecipe.getIngredients();

		// Checks ingredient list
		Assertions.assertNotNull(ingredients);
		Assertions.assertEquals(2, ingredients.size());

		// checks if the right ingredients were added
		Assertions.assertTrue(ingredients.contains(milk));
		Assertions.assertTrue(ingredients.contains(coffee));
	}
	
	@Test
	public void testAddIngredientInvalid() {
		final Recipe recipe = new Recipe();
		recipe.setName("Latte");
		recipe.setPrice(2);
		
		try {
			recipe.addIngredient(new Ingredient("coffee", 0));
			Assertions.fail("Adding ingredient with amount 0 should fail, but didn't");
		}catch (Exception e) {
			//carry on
		}
	}
	
	@Test
	@Transactional
	public void testGetPrice() {
		final Recipe recipe = new Recipe();
		recipe.setName("Latte");
		recipe.setPrice(2);

		// Add some ingredients
		Ingredient milk = new Ingredient("milk", 4);
		Ingredient coffee = new Ingredient("coffee", 2);
		recipe.addIngredient(milk);
		recipe.addIngredient(coffee);
		service.save(recipe);

		// Retrieve the saved recipe
		Recipe savedRecipe = service.findById(recipe.getId());
		int price = savedRecipe.getPrice();
		Assertions.assertEquals(2, price, "price should be 2 but is not");
	}
	
	@Test
	@Transactional
	public void testGetName() {
		final Recipe recipe = new Recipe();
		recipe.setName("Latte");
		recipe.setPrice(2);

		// Add some ingredients
		Ingredient milk = new Ingredient("milk", 4);
		Ingredient coffee = new Ingredient("coffee", 2);
		recipe.addIngredient(milk);
		recipe.addIngredient(coffee);
		service.save(recipe);

		// Retrieve the saved recipe
		Recipe savedRecipe = service.findById(recipe.getId());
		String name = savedRecipe.getName();
		Assertions.assertEquals("Latte", name, "name should be Latte but is not");
	}
	
	@Test
	@Transactional
	public void testHashAndEquals() {
		Recipe recipe1 = new Recipe();
		recipe1.setName("Coffee");

		Recipe recipe2 = new Recipe();
		recipe2.setName("Coffee");

		Recipe recipe3 = new Recipe();
		recipe3.setName("Tea");

		Assertions.assertTrue(recipe1.equals(recipe2),
				"Returns false, but should be true since 1 and 2 are the same recipe");
		Assertions.assertFalse(recipe1.equals(recipe3),
				"Returns true but should be false since recipe 1 and 3 are not equal");

		// Test hashCode method
		Assertions.assertEquals(recipe1.hashCode(), recipe2.hashCode());
		Assertions.assertNotEquals(recipe1.hashCode(), recipe3.hashCode());
	}

	@Test
	@Transactional
	public void testToString() {
		Recipe recipe = new Recipe();
		recipe.setName("Coffee");
		recipe.setPrice(2);
		recipe.addIngredient(new Ingredient("Coffee", 2));
		recipe.addIngredient(new Ingredient("Milk", 1));

		String expectedOutput = "Recipe [id=null, name=Coffee, price=2, ingredients=[Ingredient [id=null, ingredient=Coffee, amount=2], Ingredient [id=null, ingredient=Milk, amount=1]]]";
		Assertions.assertEquals(expectedOutput, recipe.toString(), "To string method does not function as it should");
	}

	@Test
	@Transactional
	public void testUpdateRecipeValid() {
		Recipe initialRecipe = new Recipe();
		Ingredient coffee = new Ingredient("coffee", 1);
		Ingredient milk = new Ingredient("milk", 2);
		initialRecipe.setName("Latte");
		initialRecipe.setPrice(2);
		initialRecipe.addIngredient(coffee);
		initialRecipe.addIngredient(milk);
		service.save(initialRecipe);

		// Retrieving the saved recipe
		List<Recipe> recipes = service.findAll();
		Assertions.assertEquals(1, recipes.size(), "Initially, there should be one recipe in the database");

		Recipe updatedRecipe = new Recipe();
		coffee = new Ingredient("coffee", 2);
		Ingredient cocoa = new Ingredient("cocoa", 2);
		milk = new Ingredient("milk", 2);
		updatedRecipe.setName("Latte");
		updatedRecipe.setPrice(3);
		updatedRecipe.addIngredient(coffee);
		updatedRecipe.addIngredient(cocoa);
		updatedRecipe.addIngredient(milk);
		
		initialRecipe.updateRecipe(updatedRecipe);
		service.save(initialRecipe);
		
		List<Recipe> updatedRecipes = service.findAll();
		Assertions.assertEquals(1, updatedRecipes.size(),
				"After updating, there should still be one recipe in the database");

		Recipe retrievedRecipe = updatedRecipes.get(0);
		Assertions.assertEquals(updatedRecipe.getName(), retrievedRecipe.getName(),
				"The name of the retrieved recipe should match the updated one");
		Assertions.assertEquals(updatedRecipe.getPrice(), retrievedRecipe.getPrice(),
				"The price of the retrieved recipe should match the updated one");
		assertIngredientListEquals(updatedRecipe.getIngredients(), retrievedRecipe.getIngredients());

	}
	@Test
	@Transactional
	public void addIngredientValid() {
	    // new recipe & add ingredients
	    final Recipe recipe = new Recipe();
	    recipe.setName("Americano");
	    recipe.setPrice(3);

	    // valid ingredients and quantities that you can add
	    coffee = new Ingredient("coffee", 3);
	    milk = new Ingredient("milk", 2);
	    recipe.addIngredient(coffee);
	    recipe.addIngredient(milk);

	    // save the recipe to the db
	    service.save(recipe);

	    // retrieve this recipe
	    Recipe savedRecipe = service.findByName(recipe.getName());
	    List<Ingredient> ingredients = savedRecipe.getIngredients();

	    // see whether they were correctly added
	    Assertions.assertNotNull(ingredients, "Ingredients list should not be null");
	    Assertions.assertEquals(2, ingredients.size(), "There should be two ingredients in the recipe");
	    Assertions.assertEquals(recipe.getIngredients(), savedRecipe.getIngredients(),
				"The ingredients of the retrieved recipe should match what was intended to be saved to the db");
	}
	
	@Test
	@Transactional
	public void testUpdateRecipeInvalidEmptyIngredients() {
		Recipe initialRecipe = new Recipe();
		Ingredient coffee = new Ingredient("coffee", 1);
		Ingredient milk = new Ingredient("milk", 2);
		initialRecipe.setName("Latte");
		initialRecipe.setPrice(2);
		initialRecipe.addIngredient(coffee);
		initialRecipe.addIngredient(milk);
		service.save(initialRecipe);

		// Retrieving the saved recipe
		List<Recipe> recipes = service.findAll();
		Assertions.assertEquals(1, recipes.size(), "Initially, there should be one recipe in the database");

		Recipe updatedRecipe = new Recipe();
		updatedRecipe.setName("Latte");
		updatedRecipe.setPrice(3);


		 IllegalArgumentException thrown = Assertions.assertThrows(
			        IllegalArgumentException.class,
			        () -> initialRecipe.updateRecipe(updatedRecipe),
			        "Does not have ingredient so should throw error"
			    );

			    Assertions.assertNotNull(thrown, "The expected exception was not thrown.");

	}
	@Test
	@Transactional
	public void testSetPriceInvalid() {
		Recipe initialRecipe = new Recipe();
		initialRecipe.setName("Latte");



		 IllegalArgumentException thrown = Assertions.assertThrows(
			        IllegalArgumentException.class,
			        () -> initialRecipe.setPrice(-1),
			        "Price is negative so should throw error"
			    );

			    Assertions.assertNotNull(thrown, "The expected exception was not thrown.");

	}

	private void assertIngredientListEquals(List<Ingredient> expected, List<Ingredient> actual) {
	    Assertions.assertEquals(expected.size(), actual.size(), "Ingredient lists are not the same size");
	    for (int i = 0; i < expected.size(); i++) {
	        Ingredient expectedIngredient = expected.get(i);
	        Ingredient actualIngredient = actual.get(i);
	        Assertions.assertEquals(expectedIngredient.getName(), actualIngredient.getName(), "Ingredient names do not match");
	        Assertions.assertEquals(expectedIngredient.getAmount(), actualIngredient.getAmount(), "Ingredient amounts do not match");
	    }
	}
	
	
}




