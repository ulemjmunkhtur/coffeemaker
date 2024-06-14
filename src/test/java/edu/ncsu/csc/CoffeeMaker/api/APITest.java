package edu.ncsu.csc.CoffeeMaker.api;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {
	
	
	@Autowired
	private RecipeService recipeService;
	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST
	 * API
	 */

	private MockMvc               mvc;
	
	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private WebApplicationContext context;
	
    private Ingredient coffee;
    private Ingredient milk;
    private Ingredient sugar;
    private Ingredient chocolate;


	
	/**
	 * Sets up the tests.
	 */
	@BeforeEach
	public void setup () {
	    mvc = MockMvcBuilders.webAppContextSetup( context ).build();
	    
    	List<Ingredient> inventoryIngredients = new ArrayList<>();
        coffee = new Ingredient("coffee", 0);
        milk = new Ingredient("milk", 0);
        sugar = new Ingredient("sugar", 0);
        inventoryIngredients.add(coffee);
        inventoryIngredients.add(milk);
        inventoryIngredients.add(sugar);
        Inventory ivt = new Inventory(inventoryIngredients);
        inventoryService.save( ivt );
	}
	@Transactional
	private Recipe makeMocha() {
		final Recipe r = new Recipe(); 
	    coffee = new Ingredient("coffee", 3);
	    milk = new Ingredient("milk", 4);
	    sugar = new Ingredient("sugar", 8);
	    chocolate  = new Ingredient("chocolate", 5);
	    r.setName( "Mocha" );
	    r.setPrice( 1 );
	    r.addIngredient(coffee);
	    r.addIngredient(milk);
	    r.addIngredient(sugar);
	    r.addIngredient(chocolate);
	        
	    return r;
	}
	
	@Test 
	@Transactional
	public void apiTesting() throws UnsupportedEncodingException, Exception {
		recipeService.deleteAll();
		inventoryService.deleteAll();
		
		String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
		        .andReturn().getResponse().getContentAsString();

		/* Figure out if the recipe we want is present */
		if (!recipe.contains( "Mocha" ) ) {
		    final Recipe r = makeMocha();
		    
		    mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );
		    
		    String recipeTest = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
			        .andReturn().getResponse().getContentAsString();
		    //test getting recipes
		    Assertions.assertTrue(recipeTest.contains("\"Mocha\""));
		    
			List<Ingredient> inventoryIngredients = new ArrayList<>();
	        milk = new Ingredient("milk", 50);
	        coffee = new Ingredient("coffee", 50);
	        sugar = new Ingredient("sugar", 50);
	        chocolate = new Ingredient("chocolate", 50);
	        inventoryIngredients.add(coffee);
	        inventoryIngredients.add(milk);
	        inventoryIngredients.add(sugar);
	        inventoryIngredients.add(chocolate);
	        Inventory i = new Inventory(inventoryIngredients);



	
		    mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString( i) ) ).andExpect( status().isOk() );
		    
		    
			
		    
		    
		    String inventoryTest = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
			        .andReturn().getResponse().getContentAsString();
		    //test if the inventory was added
		    Assertions.assertTrue(inventoryTest.contains("\"milk\",\"amount\":50"));
		    Assertions.assertTrue(inventoryTest.contains("\"coffee\",\"amount\":50"));
		    Assertions.assertTrue(inventoryTest.contains("\"sugar\",\"amount\":50"));
		    Assertions.assertTrue(inventoryTest.contains("\"chocolate\",\"amount\":50"));
		    
		    mvc.perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
		            .content( TestUtils.asJsonString(350) ) ).andExpect( status().isOk() );
		    
		    String inventoryTest2 = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() ).andExpect( status().isOk() )
			        .andReturn().getResponse().getContentAsString();
		    //test if mocha ingredients were successfully removed from inventory.
		    Assertions.assertTrue(inventoryTest2.contains("\"milk\",\"amount\":46"));
		    Assertions.assertTrue(inventoryTest2.contains("\"coffee\",\"amount\":47"));
		    Assertions.assertTrue(inventoryTest2.contains("\"sugar\",\"amount\":42"));
		    Assertions.assertTrue(inventoryTest2.contains("\"chocolate\",\"amount\":45"));

		    
		    

		}
	}
	
}
	

