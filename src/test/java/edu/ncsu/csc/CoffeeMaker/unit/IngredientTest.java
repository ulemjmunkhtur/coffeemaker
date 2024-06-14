package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;

public class IngredientTest {
	
	private Ingredient ingredient;
	
	@BeforeEach
	public void setup () {
		ingredient = new Ingredient();
	}
	
	@Test
	public void testIngredientConstructorValid() {
		
		Ingredient ingredient = new Ingredient("Coffee", 5);
		
		Assertions.assertEquals("Coffee", ingredient.getName(), "Name should be Coffee, but isn't");
		
		Assertions.assertEquals(5, ingredient.getAmount(), "Amount was not 5");
	}
	
	@Test
	public void testIngredientConstructorInvalid() {
		
		try {
			ingredient = new Ingredient("", 5);
			Assertions.fail("Creating ingredient with empty name should throw IAE, but didn't");
		} catch (IllegalArgumentException e) {
			//carry on
		}
		
		try {
			ingredient = new Ingredient(null, 5);
			Assertions.fail("Creating ingredient with null name should throw IAE, but didn't");
		} catch (IllegalArgumentException e) {
			//carry on
		}
		
		try {
			ingredient = new Ingredient("Coffee", -1);
			Assertions.fail("Creating ingredient with negative amount should throw IAE, but didn't");
		} catch (IllegalArgumentException e) {
			//carry on
		}
		
		
	}
	
	@Test
	public void testSetAmount() {
		
		ingredient.setAmount(5);
		
		Assertions.assertEquals(5, ingredient.getAmount(), "Amount was not 5 as expected");
		
		try {
			ingredient.setAmount(-5);
			Assertions.fail("Setting negative amount should throw IAE, but didn't");
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
		}
	}
	
	@Test
	public void testSetName() {
		
		ingredient.setName("Coffee");
		
		Assertions.assertEquals("Coffee", ingredient.getName(), "Name should be Coffee, but isn't");
		
		try {
			ingredient.setName("");
			Assertions.fail("setting empty name should throw IAE, but didn't");
		} catch (IllegalArgumentException e) {
			//carry on
		}
		
		try {
			ingredient.setName(null);
			Assertions.fail("Setting null name should throw IAE, but didn't");
		} catch (IllegalArgumentException e) {
			//carry on
		}
		
		
	}
	
	
	

}
