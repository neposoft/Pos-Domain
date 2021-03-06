package domain.shoppingcart;

import java.util.List;
import java.util.Observable;

import domain.discount.Discount;
import domain.product.Product;
import domain.shoppingcartproduct.ShoppingCartProduct;
import domain.shoppingcartproduct.ShoppingCartProductService;

/**
 * 
 * @author Vijay Sapkota, Milan Sanders
 *
 */
public class ShoppingCart extends Observable {

	private final String userId;
	// can be null
	private final int id;
	private Discount discount;
	private final ShoppingCartProductService shoppingCartProductService;
	private ShoppingCartState state;

	public ShoppingCart(int id, String userid, ShoppingCartProductService shoppingCartProductService) {
		this.userId = userid;
		this.id = id;
		this.shoppingCartProductService = shoppingCartProductService;
		this.setState(new PendingState(this));
	}

	public ShoppingCart(int id, String userid, Discount discount,
			ShoppingCartProductService shoppingCartProductService) {
		this(id, userid, shoppingCartProductService);
		setDiscount(discount);
	}

	public String getUserId() {
		return this.userId;
	}

	public void addProduct(Product product, int quantity) {
		shoppingCartProductService.addToCart(getId(), product, quantity);
		reportChanges();
	}

	public double getTotalPrice() {
		if (this.discount != null) {
			return this.discount.calcuate(this.getProducts());
		} else {
			return shoppingCartProductService.getTotalPriceFromCart(getId());
		}
	}

	public int getTotalQty() {
		return shoppingCartProductService.getQtyFromCart(getId());
	}

	public int getId() {
		return this.id;
	}

	public List<ShoppingCartProduct> getProducts() {
		return shoppingCartProductService.getProductsFromCart(getId());
	}

	public void alterProduct(int productId, int newQuantity) {
		if (newQuantity == 0) {
			shoppingCartProductService.removeProduct(productId);
		} else {
			shoppingCartProductService.alterQty(productId, newQuantity);
		}
	}

	public void reportChanges() {
		setChanged();
		notifyObservers();
	}

	public void setDiscount(Discount discount) {
		this.discount = discount;
		reportChanges();
	}

	public String getDiscountCode() {
		if (discount == null)
			return "";
		return discount.getCode();
	}

	public void pay(double paid) {
		if (this.getTotalPrice() > paid) {
			throw new IllegalArgumentException("Insufficient amount");
		}
		this.state = new CompletedState(this);

	}

	private void setState(ShoppingCartState state) {
		if (state == null) {
			throw new IllegalArgumentException("Invalid state");
		}
		this.state = state;
	}

	public ShoppingCartState getState() {
		return this.state;
	}

}
