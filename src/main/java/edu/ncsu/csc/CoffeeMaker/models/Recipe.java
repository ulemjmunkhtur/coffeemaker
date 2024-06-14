package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long    id;

    /** Recipe name */
    @NotBlank
    private String  name;

    /** Recipe price */
    @Min ( 0 )
    private Integer price;
    
    /**
     * List of ingredients in recipe
     */
    @NotEmpty
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ingredient> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        ingredients = new ArrayList<Ingredient>();
    }
    
    /**
     * Adds ingredient to the recipe
     * 
     * @param ingredient to add
     * @throws IAE if ingredient amount is zero
     */
    public void addIngredient(Ingredient ingredient) {
    	
    	if (ingredient.getAmount() == 0) {
    		throw new IllegalArgumentException("Ingredient " + ingredient.getName() + " must have an amount greater than zero");
    	}
    	
    	ingredients.add(ingredient);
    	
    }
    
    /**
     * Gets ingredients
     * 
     * @return list of ingredients
     */
    public List<Ingredient> getIngredients () {
    	return ingredients;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }


    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }


    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice(Integer price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be a positive integer");
        }
        this.price = price;
    }
    /**
     * Updates the recipe with a new recipe object. 
     * It takes the price & ingredient list, validated them and sets accordingly 
     *
     * @param Recipe updatedRecipe
     *            The new recipe object to update
     */
    public void updateRecipe(Recipe updatedRecipe) {
    	  // setting price
        setPrice(updatedRecipe.getPrice());

        // check if the updated recipe has ingredients
        List<Ingredient> newIngredients = updatedRecipe.getIngredients();
        if (newIngredients == null || newIngredients.isEmpty()) {
            throw new IllegalArgumentException("A recipe must contain at least one ingredient");
        }

        // map so there's a quicker look up
        Map<String, Ingredient> currentIngredients = new HashMap<>();
        for (Ingredient ingredient : this.ingredients) {
            currentIngredients.put(ingredient.getName(), ingredient);
        }

        // update the ingredients
        for (Ingredient newIngredient : newIngredients) {
        	// checking to see if it's a current ingredient by looking it up in the map
            Ingredient existingIngredient = currentIngredients.get(newIngredient.getName());
            if (existingIngredient != null) {
                // if it is the update the amount
                existingIngredient.setAmount(newIngredient.getAmount());
            } else {
                // if not then you create a new ingredient object
                addIngredient(new Ingredient(newIngredient.getName(), newIngredient.getAmount()));
            }
        }
    	
    	
    }

    /**
     * Returns a string describing the current contents of the recipe
     * 
     * @return string representing recipe
     */
    @Override
	public String toString() {
		return "Recipe [id=" + id + ", name=" + name + ", price=" + price + ", ingredients=" + ingredients + "]";
	}

    /**
     * overrides hashcode method
     * 
     * @return int hashscode
     */
	@Override
	public int hashCode() {
		final int prime = 31;
		Integer result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * Overrides equals method. Recipes are equal if they have the same name.
	 * 
	 * @return boolean representing equality.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Recipe other = (Recipe) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

}
