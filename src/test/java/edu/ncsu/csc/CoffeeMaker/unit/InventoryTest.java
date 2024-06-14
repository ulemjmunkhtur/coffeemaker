package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.ArrayList;
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

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {

    @Autowired
    private InventoryService inventoryService;

    @BeforeEach
    public void setup () {

    	inventoryService.deleteAll();
    	final Inventory ivt = inventoryService.getInventory();
        
        Ingredient chocolate = new Ingredient("Chocolate", 20);
        Ingredient coffee = new Ingredient("Coffee", 10);
        Ingredient milk = new Ingredient("Milk", 10);
        Ingredient cinnamon = new Ingredient("Cinnamon", 20);
        
        List<Ingredient> addToInventory = new ArrayList<>();
        addToInventory.add(coffee);
        addToInventory.add(milk);
        addToInventory.add(chocolate);
        addToInventory.add(cinnamon);

        Inventory toAdd = new Inventory(addToInventory);
        ivt.addIngredients(toAdd);

        inventoryService.save( ivt );
    }
    
    @Test
    @Transactional
    public void testConsumeInventory () {
        final Inventory i = inventoryService.getInventory();
        Ingredient chocolate = new Ingredient("Chocolate", 2);
        Ingredient coffee = new Ingredient("Coffee", 1);
        Ingredient milk = new Ingredient("Milk", 5);
        Ingredient cinnamon = new Ingredient("Cinnamon", 3);

        final Recipe recipe = new Recipe();
        recipe.setName( "Spicy Coffee" );
        recipe.addIngredient(chocolate);
        recipe.addIngredient(coffee);
        recipe.addIngredient(milk);
        recipe.addIngredient(cinnamon);

        recipe.setPrice( 5 );

        i.useIngredients( recipe );

        /*
         * Make sure that all of the inventory fields are now properly updated
         */

        Assertions.assertEquals( 18, (int) i.getQuantity("Chocolate") );
        Assertions.assertEquals( 17, (int) i.getQuantity("Cinnamon") );
        Assertions.assertEquals( 9, (int) i.getQuantity("Coffee"));
        Assertions.assertEquals( 5, (int) i.getQuantity("Milk") );
    }

    @Test
    @Transactional
    public void testAddIngredients () {
        Inventory ivt = inventoryService.getInventory();
        Ingredient chocolate = new Ingredient("Chocolate", 20);
        Ingredient coffee = new Ingredient("Coffee", 10);
        Ingredient milk = new Ingredient("Milk", 10);
        Ingredient cinnamon = new Ingredient("Cinnamon", 20);
        
        List<Ingredient> addToInventory = new ArrayList<>();
        addToInventory.add(coffee);
        addToInventory.add(milk);
        addToInventory.add(chocolate);
        addToInventory.add(cinnamon);

        Inventory toAdd = new Inventory(addToInventory);
        ivt.addIngredients(toAdd);
        

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 20, (int) ivt.getQuantity("Coffee"),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 40, (int) ivt.getQuantity("Chocolate"),
                "Adding to the inventory should result in correctly-updated values for milk" );
        Assertions.assertEquals( 20, (int) ivt.getQuantity("Milk"),
                "Adding to the inventory should result in correctly-updated values sugar" );
        Assertions.assertEquals( 40, (int) ivt.getQuantity("Cinnamon"),
                "Adding to the inventory should result in correctly-updated values chocolate" );

    }
    
    
    /**
     * Tests the toString method for the inventory. 
     * Makes sure the method returns the correct string representation for the current inventory contents.
     * 
     * @author capolinsky
     */
    @Test
    @Transactional
    public void testToString() {
    	final Inventory ivt = inventoryService.getInventory();
    	
    	String expectedString = "Coffee: 10\nMilk: 10\nChocolate: 20\nCinnamon: 20\n";
    	String resultString = ivt.toString();
    	
    	Assertions.assertEquals(expectedString, resultString, "Expected: " + expectedString + "\nBut got: " + resultString);	
    }
    

    
    /**
     * Tests to make sure the enoughIngredients method works. 
     * 
     * 
     */
    @Test
    @Transactional
    public void testEnoughIngredients() {
    	
    	Recipe r1 = new Recipe();
		r1.setName("Mocha");
		r1.setPrice(1);
		Ingredient coffee = new Ingredient("Coffee", 1);
		Ingredient milk = new Ingredient("Milk", 1);
		Ingredient sugar = new Ingredient("Sugar", 1);
		Ingredient chocolate = new Ingredient("Chocolate", 1);
		r1.addIngredient(coffee);
		r1.addIngredient(milk);
		r1.addIngredient(sugar);
		r1.addIngredient(chocolate);
		
		/*sufficient ingredients*/
		Inventory ivt1 = inventoryService.getInventory();
		ivt1.setIngredient(new Ingredient("Coffee",2));
		ivt1.setIngredient(new Ingredient("Milk",2));
		ivt1.setIngredient(new Ingredient("Sugar",2));
		ivt1.setIngredient(new Ingredient("Chocolate",2));
		Assertions.assertTrue(ivt1.enoughIngredients(r1));
		
		
		
		/*insufficient ingredients*/
		
		//creating an inventory with insufficient ingredients
		ivt1.setIngredient(new Ingredient("Coffee",0));
		ivt1.setIngredient(new Ingredient("Milk",1));
		ivt1.setIngredient(new Ingredient("Sugar",1));
		ivt1.setIngredient(new Ingredient("Chocolate",1));
		Assertions.assertFalse(ivt1.enoughIngredients(r1),"Insufficient coffee in inventory, should return false but did not.");
   
		ivt1.setIngredient(new Ingredient("Coffee",1));
		ivt1.setIngredient(new Ingredient("Milk",0));
		ivt1.setIngredient(new Ingredient("Sugar",1));
		ivt1.setIngredient(new Ingredient("Chocolate",1));
		Assertions.assertFalse(ivt1.enoughIngredients(r1),"Insufficient milk in inventory, should return false but did not.");
 
		ivt1.setIngredient(new Ingredient("Coffee",1));
		ivt1.setIngredient(new Ingredient("Milk",1));
		ivt1.setIngredient(new Ingredient("Sugar",0));
		ivt1.setIngredient(new Ingredient("Chocolate",1));
		Assertions.assertFalse(ivt1.enoughIngredients(r1),"Insufficient sugar in inventory, should return false but did not.");
 
		ivt1.setIngredient(new Ingredient("Coffee",1));
		ivt1.setIngredient(new Ingredient("Milk",1));
		ivt1.setIngredient(new Ingredient("Sugar",1));
		ivt1.setIngredient(new Ingredient("Chocolate",0));
		Assertions.assertFalse(ivt1.enoughIngredients(r1),"Insufficient chocolate in inventory, should return false but did not.");
    }
}