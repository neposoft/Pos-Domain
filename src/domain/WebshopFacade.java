package domain;

import java.util.List;
import java.util.Properties;

import db.DBtypes;
import domain.discount.Discount;
import domain.discount.DiscountService;
import domain.person.Person;
import domain.person.PersonService;
import domain.person.Role;
import domain.product.Product;
import domain.product.ProductService;
import domain.product.ShoppingCartProduct;

public class WebshopFacade {
	private final PersonService personService;
	private final ProductService productService;
	private final ShoppingCartService shoppingCartService;
	private final DiscountService discountService;

	public WebshopFacade(Properties properties) {
		personService = new PersonService(DBtypes.LOCALDB, properties);
		productService = new ProductService(DBtypes.LOCALDB, properties);
		shoppingCartService = new ShoppingCartService();
		discountService = new DiscountService(DBtypes.LOCALDB, properties);
		
	}

	// product things

	public Product getProduct(int productId) {
		return productService.getProduct(productId);
	}

	public List<Product> getProducts() {
		return productService.getProducts();
	}

	public void addProduct(Product product) {
		productService.addProduct(product);
	}

	public void updateProduct(Product product) {
		productService.updateProduct(product);
	}

	public void deleteProduct(int productId) {
		productService.deleteProduct(productId);
	}

	// person things

	public Person getPerson(String personId) {
		return personService.getPerson(personId);
	}

	public List<Person> getPersons() {
		return personService.getPersons();
	}

	public boolean hasPerson(Person person) {
		return personService.hasPerson(person);
	}

	public boolean canHaveAsPerson(Person person) {
		return personService.canHaveAsPerson(person);
	}

	public void addPerson(Person person) {
		if (canHaveAsPerson(person))
			personService.addPerson(person);
	}

	public void updatePerson(Person person) {
		personService.updatePerson(person);
	}

	public void deletePerson(String id) {
		personService.deletePerson(id);
	}

	public Person getAuthenticatedUser(String personId, String password) {
		return personService.getAuthenticatedUser(personId, password);
	}

	public Role getRole(String userId) {
		return personService.getRole(userId);
	}

	// cart things

	public ShoppingCart createCart(String userId) {
		return shoppingCartService.createCart(userId);
	}

	public ShoppingCart getCart(int cartId) {
		return shoppingCartService.getCart(cartId);
	}

	public ShoppingCart getCartFromUser(String userId) {
		return shoppingCartService.getCartFromUser(userId);
	}

	public void deleteCart(int cartId) {
		shoppingCartService.removeCart(cartId);
	}

	public double getTotalFromCart(int cartId) {
		return shoppingCartService.getTotalPrice(cartId);
	}

	public void addProductToCart(int cartId, Product product) {
		addProductToCart(cartId, product, 1);
	}

	public void addProductToCart(int cartId, Product product, int quantity) {
		ShoppingCartProduct prd = new ShoppingCartProduct(product, quantity);
		shoppingCartService.addProduct(cartId, prd);
	}

	public void addProductToCartFromUser(String userId, Product product,
			int quantity) {
		ShoppingCartProduct prd = new ShoppingCartProduct(product, quantity);
		shoppingCartService.addProductToCartFromUser(userId, prd);
	}

	public int getTotalQty(int cartId) {
		return shoppingCartService.getTotalQty(cartId);
	}

	public int getTotalQtyFromUser(String userId) {
		return shoppingCartService.getTotalQtyFromUser(userId);
	}


	// discount things
	public void addDiscountToCart(int cartId, String code) {
		shoppingCartService.setDiscount(cartId, getDiscount(code));
	}

	private Discount getDiscount(String code){
		return discountService.getDiscount(code);
	}

	public String getDiscountCode(int cartId) {
		return shoppingCartService.getDiscountCode(cartId);
	}

}