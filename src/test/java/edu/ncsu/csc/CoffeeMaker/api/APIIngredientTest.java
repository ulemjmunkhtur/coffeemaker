package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

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
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * Class used for testing the APIIngredientController. Tests to verify that
 * ingredients can successfully be added to the inventory.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class APIIngredientTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private InventoryService iService;
	private Ingredient coffee;

	/**
	 * Tests to confirm that a valid ingredient can be added through the
	 * POST/ingredients end point.
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testValidAddIngredient() throws Exception {
		iService.deleteAll();

		coffee = new Ingredient("Coffee", 10);
		mvc.perform(post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(coffee))).andExpect(status().isOk());
	}

	/**
	 * Tests to confirm that an invalid ingredient is rejected in the
	 * POST/ingredients end point. An ingredient is invalid if another ingredient of
	 * the same name already exists.
	 * 
	 * @throws Exception
	 */
	@Test
	@Transactional
	public void testInvalidAddIngredient() throws Exception {
		iService.deleteAll();

		coffee = new Ingredient("Coffee", 10);
		
		mvc.perform(post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString(coffee))).andExpect(status().isOk());
		
		Ingredient coffee2 = new Ingredient("Coffee", 20);
		
		mvc.perform(post("/api/v1/ingredients").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(coffee2)))
				.andExpect(status().isConflict()); 

	}
}
