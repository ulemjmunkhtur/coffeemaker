package edu.ncsu.csc.CoffeeMaker;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )


public class TestDatabaseInteraction {
	@Autowired
	private RecipeService recipeService;
    private Ingredient coffee;
    private Ingredient milk;
    private Ingredient sugar;
    private Ingredient chocolate;
    private Ingredient cinnamon;
	private Recipe mocha;
    
    @Transactional
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    	mocha = new Recipe();
        coffee = new Ingredient("coffee", 1);
        milk = new Ingredient("milk", 2);
        sugar = new Ingredient("sugar", 2);
        cinnamon = new Ingredient("cinnamon", 1);
        chocolate = new Ingredient("chocolate", 1);
        mocha.setName( "Mocha" );
        mocha.setPrice( 1 );
        mocha.addIngredient(coffee);
        mocha.addIngredient(chocolate);
        mocha.addIngredient(cinnamon);
	        
	    recipeService.save(mocha);
    }
	
	@Test
	@Transactional
	public void testRecipeIngredient(){  
	    /* this retrieves the ingredients that was added */
	    List<Ingredient> ingredients = this.mocha.getIngredients();
	    
	
	    /* this is retrieving the recipes from the database */
		List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();
		/*confirm that there is only one recipe in the system*/
		Assertions.assertEquals(1, dbRecipes.size());
		/*Get the mocha recipe from the database*/
		Recipe dbRecipe = dbRecipes.get(0);
		/* Get the ingredients */
		List<Ingredient> dbIngredients = dbRecipe.getIngredients();	
		
		/* this is confirming that all the fields are equal */
		Assertions.assertEquals(ingredients, dbIngredients,
				"The ingredients that were persisted to the database should be the same as the ones added to the mocha recipe");
	
		/*Test find recipe by name*/
		Assertions.assertEquals(this.mocha, recipeService.findByName("Mocha"));
	}

	
	@Test
	@Transactional
	public void testDeleteRecipes() {
		
    	Recipe latte = new Recipe();
        coffee = new Ingredient("coffee", 4);
        milk = new Ingredient("milk", 2);
        sugar = new Ingredient("sugar", 2);

        latte.setName( "Mocha" );
        latte.setPrice( 1 );
        latte.addIngredient(coffee);
        latte.addIngredient(milk);
        latte.addIngredient(sugar);
		recipeService.save(latte);
		
		
		List<Recipe> dbRecipes = (List<Recipe>) recipeService.findAll();
		
		/*confirm that there are two recipe in the system*/
		Assertions.assertEquals(2, dbRecipes.size());
		
		recipeService.delete(latte);
		
		dbRecipes = (List<Recipe>) recipeService.findAll();
		
		/*confirm that there is only one recipe in the system*/
		Assertions.assertEquals(1, dbRecipes.size());
		Assertions.assertEquals(this.mocha, recipeService.findByName(this.mocha.getName()));
		
		recipeService.delete(this.mocha);
		
		dbRecipes = (List<Recipe>) recipeService.findAll();
		
		/*confirm that there are no recipes in the system*/
		Assertions.assertEquals(0, dbRecipes.size());	
	}
	

}
