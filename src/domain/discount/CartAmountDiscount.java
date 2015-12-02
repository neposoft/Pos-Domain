package domain.discount;

import java.util.List;

import domain.product.ShoppingCartProduct;

public class CartAmountDiscount extends Discount {
	private double threshold;

	public CartAmountDiscount(String code, Double amount, double threshold) {
		super(code, amount);

		this.threshold = threshold;

	}

	@Override
	public double calcuate(List<ShoppingCartProduct> products) {
		double sum = 0;

		for (ShoppingCartProduct product : products) {
			sum += product.getTotal();
		}

		if (threshold > sum) {
			return sum;
		}

		return sum - this.amount;
	}

	private void setThreshold(double threshold) {
		if (threshold < 0) {
			throw new IllegalArgumentException("Threshold must be 0 or more.");
		}
		this.threshold = threshold;
	}

}