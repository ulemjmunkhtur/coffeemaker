package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class APIRecipeTest {

	@Autowired
	private RecipeService service;

	@Autowired
	private MockMvc mvc;
	private Ingredient coffee;
	private Ingredient milk;
	private Ingredient sugar;
	private Ingredient chocolate;

	@Test
	@Transactional
	public void ensureRecipe() throws Exception {
		service.deleteAll();

		final Recipe r = new Recipe();
		coffee = new Ingredient("coffee", 3);
		milk = new Ingredient("milk", 4);
		sugar = new Ingredient("sugar", 8);
		chocolate = new Ingredient("chocolate", 5);
		r.setPrice(10);
		r.setName("Mocha");
		r.addIngredient(coffee);
		r.addIngredient(milk);
		r.addIngredient(sugar);
		r.addIngredient(chocolate);
		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r)))
				.andExpect(status().isOk());

	}

	@Test
	@Transactional
	public void testValidGetRecipeAPI() throws Exception {
		service.deleteAll();
		final Recipe r = new Recipe();
		coffee = new Ingredient("coffee", 3);
		milk = new Ingredient("milk", 4);
		sugar = new Ingredient("sugar", 8);
		chocolate = new Ingredient("chocolate", 5);
		r.setPrice(10);
		r.setName("Mocha");
		r.addIngredient(coffee);
		r.addIngredient(milk);
		r.addIngredient(sugar);
		r.addIngredient(chocolate);

		service.save(r);
		mvc.perform(get("/api/v1/recipes/Mocha").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	/**
	 * Tests getting all of the recipes using GET/recipes
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testValidGetAllRecipesAPI() throws Exception {
		service.deleteAll();

		// Create a recipe
		final Recipe r = new Recipe();
		coffee = new Ingredient("coffee", 3);
		milk = new Ingredient("milk", 4);
		sugar = new Ingredient("sugar", 8);
		chocolate = new Ingredient("chocolate", 5);
		r.setPrice(10);
		r.setName("Mocha");
		r.addIngredient(coffee);
		r.addIngredient(milk);
		r.addIngredient(sugar);
		r.addIngredient(chocolate);
		service.save(r);

		// Perform the get all recipes API call
		mvc.perform(get("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	@Transactional
	public void tesNotFoundGetRecipeAPI() throws Exception {
		service.deleteAll();
		ResultActions result = mvc.perform(get("/api/v1/recipes/Mocha").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		String errorMessage = result.andReturn().getResponse().getContentAsString();
		Assertions.assertEquals("{\"status\":\"failed\",\"message\":\"No recipe found with name Mocha\"}",
				errorMessage);
	}

	@Test
	@Transactional
	public void testRecipeAPI() throws Exception {

		service.deleteAll();

		service.deleteAll();

		final Recipe recipe = new Recipe();
		coffee = new Ingredient("coffee", 1);
		milk = new Ingredient("milk", 20);
		sugar = new Ingredient("sugar", 5);
		chocolate = new Ingredient("chocolate", 10);
		recipe.setPrice(50);
		recipe.setName("Mocha");
		recipe.addIngredient(coffee);
		recipe.addIngredient(milk);
		recipe.addIngredient(sugar);
		recipe.addIngredient(chocolate);

		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(recipe)));

		Assertions.assertEquals(1, (int) service.count());

	}

	@Test
	@Transactional
	public void testCreateRecipeTooMany() throws Exception {
		service.deleteAll();

		/* Tests a recipe with a duplicate name to make sure it's rejected */

		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");

		final Recipe r1 = new Recipe();
		coffee = new Ingredient("coffee", 3);
		milk = new Ingredient("milk", 1);
		sugar = new Ingredient("sugar", 1);
		chocolate = new Ingredient("chocolate", 2);
		r1.setPrice(50);
		r1.setName("Mocha");
		r1.addIngredient(coffee);
		r1.addIngredient(milk);
		r1.addIngredient(sugar);
		r1.addIngredient(chocolate);

		final Recipe r2 = new Recipe();
		coffee = new Ingredient("coffee", 3);
		milk = new Ingredient("milk", 1);
		sugar = new Ingredient("sugar", 1);
		chocolate = new Ingredient("chocolate", 2);
		r2.setPrice(50);
		r2.setName("Coffee");
		r2.addIngredient(coffee);
		r2.addIngredient(milk);
		r2.addIngredient(sugar);
		r2.addIngredient(chocolate);

		service.save(r1);
		service.save(r2);

		final Recipe r3 = new Recipe();
		coffee = new Ingredient("coffee", 3);
		milk = new Ingredient("milk", 1);
		sugar = new Ingredient("sugar", 1);
		chocolate = new Ingredient("chocolate", 2);
		r3.setPrice(50);
		r3.setName("another coffees");
		r3.addIngredient(coffee);
		r3.addIngredient(milk);
		r3.addIngredient(sugar);
		r3.addIngredient(chocolate);

		service.save(r3);

		final Recipe r4 = new Recipe();
		coffee = new Ingredient("coffee", 3);
		milk = new Ingredient("milk", 1);
		sugar = new Ingredient("sugar", 1);
		chocolate = new Ingredient("chocolate", 2);
		r4.setPrice(50);
		r4.setName("too much cofffee");
		r4.addIngredient(coffee);
		r4.addIngredient(milk);
		r4.addIngredient(sugar);
		r4.addIngredient(chocolate);

		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r4)))
				.andExpect(status().isInsufficientStorage());

		Assertions.assertEquals(3, service.findAll().size(), "There should only 3 recipes in the CoffeeMaker");
	}

	@Test
	@Transactional
	public void testAddRecipe2() throws Exception {
		service.deleteAll();

		/* Tests a recipe with a duplicate name to make sure it's rejected */

		Assertions.assertEquals(0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker");

		final Recipe r1 = new Recipe();
		coffee = new Ingredient("coffee", 3);
		milk = new Ingredient("milk", 1);
		sugar = new Ingredient("sugar", 1);
		chocolate = new Ingredient("chocolate", 2);
		r1.setPrice(50);
		r1.setName("Mocha");
		r1.addIngredient(coffee);
		r1.addIngredient(milk);
		r1.addIngredient(sugar);
		r1.addIngredient(chocolate);

		final Recipe r2 = new Recipe();
		coffee = new Ingredient("coffee", 3);
		milk = new Ingredient("milk", 1);
		sugar = new Ingredient("sugar", 1);
		chocolate = new Ingredient("chocolate", 2);
		r2.setPrice(50);
		r2.setName("Mocha");
		r2.addIngredient(coffee);
		r2.addIngredient(milk);
		r2.addIngredient(sugar);
		r2.addIngredient(chocolate);

		service.save(r1);

		mvc.perform(post("/api/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(r2)))
				.andExpect(status().is4xxClientError());

		Assertions.assertEquals(1, service.findAll().size(), "There should only one recipe in the CoffeeMaker");
	}

	/**
	 * Tests that the API Endpoint DELETE/recipes successfully removes a recipe of
	 * the given name. If no recipe exists, returns a 400 error response.
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testDeleteRecipe() throws Exception {
		service.deleteAll();

		//Create and save Mocha recipe
		Recipe mocha = new Recipe();
		mocha.setName("Mocha");
		mocha.setPrice(350);
		coffee = new Ingredient("coffee", 50);
		milk = new Ingredient("milk", 1);
		chocolate = new Ingredient("chocolate", 5);
		mocha.addIngredient(coffee);
		mocha.addIngredient(milk);
		mocha.addIngredient(chocolate);
		service.save(mocha);

		//Create and save Latte recipe
		Recipe latte = new Recipe();
		latte.setName("Latte");
		coffee = new Ingredient("coffee", 50);
		milk = new Ingredient("milk", 5);
		chocolate = new Ingredient("chocolate", 1);
		latte.addIngredient(coffee);
		latte.addIngredient(milk);
		latte.addIngredient(chocolate);
		latte.setPrice(450);
		service.save(latte);

		// ensure that both are saved
		List<Recipe> dbRecipes = (List<Recipe>) service.findAll();
		Assertions.assertEquals(2, dbRecipes.size());

		// delete operation on latte
		mvc.perform(delete("/api/v1/recipes/Latte").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		// get the updated list of recipes after the delete operation.
		dbRecipes = (List<Recipe>) service.findAll();
		Assertions.assertEquals(1, dbRecipes.size());

		// remaining recipe should be mocha
		Recipe remainingRecipe = dbRecipes.get(0);
		Assertions.assertEquals("Mocha", remainingRecipe.getName());

		// delete operation on mocha
		mvc.perform(delete("/api/v1/recipes/Mocha").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
		
		// Test an invalid delete recipe call
		// Try to delete a recipe that does not exist
		mvc.perform(delete("/api/v1/recipes/WeirdRecipeName").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());

		// Confirm that no recipes are in the system
		Assertions.assertEquals(0, service.findAll().size(), "There should not be any recipes in the CoffeeMaker");
	}
	/**
	 * Tests that the API Endpoint PUT/recipes/{name} successfully removes a recipe of
	 * the given name. If no recipe exists, returns a 400 error response.
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testUpdateRecipe() throws Exception {
	    service.deleteAll();

	    // create an original recipe
	    Recipe mocha = new Recipe();
		mocha.setName("Mocha");
		mocha.setPrice(350);
		coffee = new Ingredient("coffee", 50);
		mocha.addIngredient(coffee);
		//assuming this functionality is working properly
		service.save(mocha);

	    // new updated recipe for mocha
	    Recipe updatedRecipe = new Recipe();
	    updatedRecipe.setName("Mocha");
	    updatedRecipe.setPrice(250); // Updated price
	    coffee = new Ingredient("coffee", 60); // Updated coffee quantity
	    milk = new Ingredient("milk", 5); // New ingredient added
	    updatedRecipe.addIngredient(coffee);
	    updatedRecipe.addIngredient(milk);

	    // Perform the update API call
	    mvc.perform(put("/api/v1/recipes/Mocha")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(TestUtils.asJsonString(updatedRecipe)))
	            .andExpect(status().isOk());

	    // get the updated recipe from the db
	    Recipe fetchedRecipe = service.findByName("Mocha");
	    Assertions.assertEquals(250, fetchedRecipe.getPrice(), "The price should be updated to 250");
	    Assertions.assertEquals(2, fetchedRecipe.getIngredients().size(), "There should be two ingredients now");

	    // test updating a non-existing recipe
	    Recipe nonExistingRecipe = new Recipe();
	    nonExistingRecipe.setName("Latte");
	    nonExistingRecipe.setPrice(300);
	    nonExistingRecipe.addIngredient(new Ingredient("milk", 10));

	    // try to update recipe that is not found and expect to get an error message
	    mvc.perform(put("/api/v1/recipes/Latte")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(TestUtils.asJsonString(nonExistingRecipe)))
	            .andExpect(status().isNotFound());

	    // Confirm the error message for non-existing recipe update
	    ResultActions resultActions = mvc.perform(put("/api/v1/recipes/Latte")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(TestUtils.asJsonString(nonExistingRecipe)))
	            .andExpect(status().isNotFound());
	    String errorMessage = resultActions.andReturn().getResponse().getContentAsString();
	    Assertions.assertEquals("{\"status\":\"failed\",\"message\":\"No recipe found with name Latte\"}",
	            errorMessage);


	}

	/**
	 * Tests that the API Endpoint PUT/recipes/{name} handles the alternative flows
	 * (invalid prices, units, and missing ingredients)
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testUpdateRecipeExceptions() throws Exception {
	    service.deleteAll();


	    // mocha recipe
	    Recipe mocha = new Recipe();
	    mocha.setName("Mocha");
	    mocha.setPrice(300);
	    Ingredient coffee = new Ingredient("coffee", 30);
	    mocha.addIngredient(coffee);
	    service.save(mocha);

	    String jsonWithNegativePrice = "{\"name\":\"Mocha\",\"price\":-1,\"ingredients\":[{\"name\":\"coffee\",\"amount\":30}]}";
        String jsonWithNegativeUnit = "{\"name\":\"Mocha\",\"price\":300,\"ingredients\":[{\"name\":\"coffee\",\"amount\":-1}]}";
        String jsonWithNoIngredients = "{\"name\":\"Mocha\",\"price\":300,\"ingredients\":[]}";

        // 1) try to update with negative price
        mvc.perform(put("/api/v1/recipes/Mocha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithNegativePrice))
                .andExpect(status().isBadRequest());

        // 2) try to update with a negative unit for an ingredient
        mvc.perform(put("/api/v1/recipes/Mocha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithNegativeUnit))
                .andExpect(status().isBadRequest());

        // 3) try to update with no ingredients
        mvc.perform(put("/api/v1/recipes/Mocha")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithNoIngredients))
                .andExpect(status().isBadRequest());

	    // make sure that the original recipe is not altered due to the attempted updates
	    Recipe retrievedRecipe = service.findByName("Mocha");
	    Assertions.assertEquals(300, retrievedRecipe.getPrice(), "price should remain unchanged: 300");
	    Assertions.assertEquals(1, retrievedRecipe.getIngredients().size(), "ingredient count should remain unchanged: 1");


	}


}