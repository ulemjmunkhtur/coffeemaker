package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;

import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * This is the controller that holds the REST endpoints that handle add and
 * update operations for the Ingredient.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
public class APIIngredientController extends APIController {

	/**
	 * InventoryService object, to be autowired in by Spring to allow for
	 * manipulating the inventory model. Used for the POST/Ingredients endpoint.
	 */
	@Autowired
	private InventoryService inventoryService;

	/**
	 * REST API endpoint to provide update access to CoffeeMaker's singleton
	 * Inventory. This will update the Inventory of the CoffeeMaker by adding
	 * amounts from the Inventory provided to the CoffeeMaker's stored inventory
	 *
	 * @param ingredient amounts to add to inventory
	 * @return response to the request
	 */
	@PostMapping(BASE_PATH + "/ingredients")
	public ResponseEntity addIngredient(@RequestBody final Ingredient ingredient) {
		// Get name of the ingredient being added
		String ingredientName = ingredient.getName();
		// Check to see if an ingredient of the specified name already exists
		Inventory existingInventory = inventoryService.getInventory();
		for (Ingredient ing : existingInventory.getIngredients()) {
			if (ing.getName().equals(ingredientName)) {
				return new ResponseEntity(errorResponse( "Ingredient with the name " + ing.getName() + " already exists" ),
	                    HttpStatus.CONFLICT );
			}
		}
		// Add the ingredient to the inventory
		// Create list containing the single ingredient being added
		List<Ingredient> ingredientList = new ArrayList<Ingredient>();
		ingredientList.add(ingredient);
		// Create a new inventory object for adding
		Inventory newInventory = new Inventory(ingredientList);
		// Add the new ingredient to the existing inventory
		existingInventory.addIngredients(newInventory);
		// Save the inventory
		inventoryService.save(existingInventory);
		return new ResponseEntity(existingInventory, HttpStatus.OK);
	}
}
