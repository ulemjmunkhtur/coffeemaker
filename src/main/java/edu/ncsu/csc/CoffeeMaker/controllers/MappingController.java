package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for the URL mappings for CoffeeMaker. The controller returns
 * the approprate HTML page in the /src/main/resources/templates folder. For a
 * larger application, this should be split across multiple controllers.
 *
 * @author Kai Presler-Marshall
 */
@Controller
public class MappingController {

    /**
     * On a GET request to /home, the IndexController will return
     * /src/main/resources/templates/homepage.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/home", "/homepage.html" } )
    public String index ( final Model model ) {
        return "homepage";
    }
    
    /**
     * On a GET request to /, the IndexController will return
     * /src/main/resources/templates/homepage.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/home", "/" } )
    public String home ( final Model model ) {
        return "homepage";
    }

    /**
     * On a GET request to /addRecipe, the RecipeController will return
     * /src/main/resources/templates/addRecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addRecipe", "/addRecipe.html" } )
    public String addRecipePage ( final Model model ) {
        return "addRecipe";
    }

    /**
     * On a GET request to /deleterecipe, the DeleteRecipeController will return
     * /src/main/resources/templates/deleterecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/deleterecipe", "/deleterecipe.html" } )
    public String deleteRecipeForm ( final Model model ) {
        return "deleterecipe";
    }

    /**
     * On a GET request to /editrecipe, the EditRecipeController will return
     * /src/main/resources/templates/editrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/editrecipe", "/editrecipe.html" } )
    public String editRecipeForm ( final Model model ) {
        return "editrecipe";
    }

    /**
     * Handles a GET request for inventory. The GET request provides a view to
     * the client that includes the list of the current ingredients in the
     * inventory and a form where the client can enter more ingredients to add
     * to the inventory.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/inventory", "/inventory.html" } )
    public String inventoryForm ( final Model model ) {
        return "inventory";
    }

    /**
     * On a GET request to /makecoffee, the MakeCoffeeController will return
     * /src/main/resources/templates/makecoffee.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/makecoffee", "/makecoffee.html" } )
    public String makeCoffeeForm ( final Model model ) {
        return "makecoffee";
    }
    
    /**
     * On a GET request to /addingredient, the MakeCoffeeController will return
     * /src/main/resources/templates/addingredient.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addIngredient", "/addIngredient.html" } )
    public String addIngredient ( final Model model ) {
        return "addIngredient";
    }
    
    /**
     * On a GET request to /addingredientform, the MakeCoffeeController will return
     * /src/main/resources/templates/addingredientform.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addingredientform", "/addingredientform.html" } )
    public String addIngredientForm ( final Model model ) {
        return "addingredientform";
    }
    
    
    /**
     * Handles a GET request for update inventory. The GET request provides a view
     * to the client that includes the list of the current ingredients in the
     * inventory and a form where the client can enter more stock to the inventory.
     *
     * @param model
     *              underlying UI model
     * @return contents of the page
     */
    @GetMapping({ "/updateinventory", "/updateinventory.html" })
    public String updateinventoryForm(final Model model) {
        return "updateinventory";
    }
    


}
