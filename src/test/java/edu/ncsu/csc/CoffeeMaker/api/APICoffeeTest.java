package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APICoffeeTest {

    @Autowired
    private MockMvc          mvc;

    @Autowired
    private RecipeService    service;

    @Autowired
    private InventoryService iService;
    private Ingredient coffee;
    private Ingredient milk;
    private Ingredient sugar;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    @Transactional
    public void setup () {

    	iService.deleteAll();
    	
    	List<Ingredient> inventoryIngredients = new ArrayList<>();
        coffee = new Ingredient("coffee", 15);
        milk = new Ingredient("milk", 15);
        sugar = new Ingredient("sugar", 15);
        inventoryIngredients.add(coffee);
        inventoryIngredients.add(milk);
        inventoryIngredients.add(sugar);
        Inventory ivt = new Inventory(inventoryIngredients);

        iService.save( ivt );

        final Recipe recipe = new Recipe();
        recipe.setName( "Coffee" );
        recipe.setPrice( 50 );
        coffee = new Ingredient("coffee", 3);
        milk = new Ingredient("milk", 1);
        sugar = new Ingredient("sugar", 1);
        recipe.addIngredient(coffee);
        recipe.addIngredient(milk);
        recipe.addIngredient(sugar);
        service.save(recipe);
    }

    @Test
    @Transactional
    public void testPurchaseBeverageSufficientFunds() throws Exception {
        final String name = "Coffee";
        int amountPaid = 60;
        int changeExpected = 10;

        mvc.perform(post("/api/v1/makecoffee/" + name)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(amountPaid)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value(changeExpected));
    }
    
    
    
  
    @Test
    @Transactional
    public void testPurchaseBeverageInsufficientFunds() throws Exception {
        final String name = "Coffee";
        int amountPaid = 40;

        mvc.perform(post("/api/v1/makecoffee/" + name)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(amountPaid)))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.message").value("Not enough money paid"));
    }
    /* THIS ONE DOESN'T PASS */
    @Test
    @Transactional
    public void testPurchaseBeverageNotEnoughInventory () throws Exception {
        /* Insufficient inventory */
    	Inventory inventory = iService.getInventory();
        coffee = new Ingredient("coffee", 1);
        inventory.setIngredient(coffee);
        iService.save(inventory);
        

        
       
        final String name = "coffee";
        mvc.perform( post( String.format( "/api/v1/makecoffee/%s", name ) ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString(60) ) ).andExpect( status().is4xxClientError() )
                .andExpect( jsonPath( "$.message" ).value( "Not enough inventory" ) );

    }

}
