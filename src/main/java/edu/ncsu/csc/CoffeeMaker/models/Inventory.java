package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;


/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {

	/** id for inventory entry */
	@Id
	@GeneratedValue
	private Long id;
	/** list of ingredients in the inventory */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private final List<Ingredient> ingredients;

	/**
	 * Creates an empty inventory
	 */
	public Inventory() {
		this.ingredients = new ArrayList<Ingredient>();
	}

	/**
	 * Use this to create inventory with specified amounts of each ingredient.
	 *
	 * @param ingredients stored in the inventory
	 */
	public Inventory(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	/**
	 * Returns the ID of the entry in the DB
	 *
	 * @return long
	 */
	@Override
	public Long getId() {
		return id;
	}

	/**
	 * Returns the quantity for the specified ingredient.
	 * 
	 * @param ingredientName the name of the ingredient quantity being searched for
	 * @return the integer quantity in the inventory, or null if no ingredient is
	 *         found.
	 */
	public Integer getQuantity(String ingredientName) {
		for (Ingredient ingredient : this.ingredients) {
			// Loop through the loop of ingredients. If one is found, return its stored
			// quantity.
			if (ingredient.getName().equals(ingredientName)) {
				return ingredient.getAmount();
			}
		}
		return null;
	}

	/**
	 * adds a list of Ingredient objects from an inventory to the Inventory, by looping through the list
	 * @param addInventory inventory being added
	 */
	public void addIngredients(Inventory addInventory) {
		// Loop through the inventory being added. For each name, if the ingredient is
		// in the system
		// (i.e. the current ingredients list has the ingredient) add the quantity of
		// the ingredient in the new inventory to the quantity already associated with that ingredient,
		// otherwise just add the ingredient.
		for (Ingredient newIngredient : addInventory.getIngredients()) {
			// Indicator used to keep track of whether the ingredient was added already or
			// not
			boolean ingredientAdded = false;
			int idx = 0;
			// Iterate through current ingredients list
			while (idx < ingredients.size() && !ingredientAdded) {
				if (ingredients.get(idx).getName().equals(newIngredient.getName())) {
					// Set the new quantity to be the sum of the existing quantity with the new
					// quantity
					int newAmount = ingredients.get(idx).getAmount() + newIngredient.getAmount();
					Ingredient modifiedNewIngredient = new Ingredient(newIngredient.getName(), newAmount);
					ingredients.set(idx, modifiedNewIngredient);
					ingredientAdded = true;
				}
				idx++;
			}
			// If the ingredient wasn't already in the system, add it.
			if (!ingredientAdded) {
				ingredients.add(newIngredient);
			}
		}
	}

	/**
	 * Returns the list of ingredients in the inventory.
	 * 
	 * @return a list containing the ingredient objects.
	 */
	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	/**
	 * Updates the units in the inventory to reflect a recipe being made.
	 * 
	 * @param r the recipe being brewed.
	 * @return void if the units are successfully removed
	 */
	public boolean useIngredients(Recipe r) {
		// Check to make sure there are enough units in the inventory
		if (!enoughIngredients(r)) {
			return false;
		}
		// Iterate through the inventory and remove the quantities specified in the
		// recipe
		for (Ingredient recipeIngredient : r.getIngredients()) {
			for (int idx = 0; idx < ingredients.size(); idx++) {
				// Find the inventory ingredient that matches the item in the recipe
				if (ingredients.get(idx).getName().equals(recipeIngredient.getName())) {
					// Calculate the new units in the inventory
					int newUnits = ingredients.get(idx).getAmount() - recipeIngredient.getAmount();
					ingredients.get(idx).setAmount(newUnits);
				}
			}
		}
		return true;
	}

	/**
	 * Checks to make sure that all the quantities specified in the recipe are less
	 * than the amount of units currently in the inventory for each ingredient.
	 * 
	 * @param r the recipe being brewed.
	 * @return boolean returns false if there is an ingredient in the recipe with a
	 *         quantity greater than the units in the inventory.
	 */
	public boolean enoughIngredients(Recipe r) {
		for (Ingredient recipeIngredient : r.getIngredients()) {
			boolean enough = false;
			for (int idx = 0; idx < ingredients.size(); idx++) {
				// Find the inventory ingredient that matches the item in the recipe
				if (ingredients.get(idx).getName().equals(recipeIngredient.getName())) {
					// Check to make sure the quantity being added is not more than the amount in
					// the inventory
					if (ingredients.get(idx).getAmount() > recipeIngredient.getAmount()) {
						enough = true;
					}
				}
			}
			if (!enough) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns a string describing the current contents of the inventory.
	 *
	 * @return String
	 */
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		for (Ingredient ingredient : ingredients) {
			buf.append(ingredient.getName());
			buf.append(": " + ingredient.getAmount() + "\n");
		}
		return buf.toString();
	}
	
	   /**
     * Sets the amount for a given ingredient. If the ingredient is not found,
     * it adds the ingredient
     * @param ingredientToUpdate the ingredient object with updated amount
     */
    public void setIngredient(Ingredient ingredientToUpdate) {
        boolean found = false;
        for (int idx = 0; idx < ingredients.size(); idx++) {
            Ingredient ingredient = ingredients.get(idx);
            if (ingredient.getName().equals(ingredientToUpdate.getName())) {
                ingredients.set(idx, new Ingredient(ingredient.getName(), ingredientToUpdate.getAmount()));
                found = true;
                break;
            }
        }
        if (!found) {
            ingredients.add(new Ingredient(ingredientToUpdate.getName(), ingredientToUpdate.getAmount()));
        }


    }
    
}
