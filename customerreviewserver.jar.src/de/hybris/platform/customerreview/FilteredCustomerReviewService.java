package de.hybris.platform.customerreview;

import de.hybris.platform.core.model.product.ProductModel;
import java.util.List;

public abstract interface FilteredCustomerReviewService extends CustomerReviewService
{
	//return the Number of CustomerReviewModel within rating range [ratingLow, ratingHigh] 
	public abstract Integer getNumOfReviewsInRatingRange(ProductModel paramProductModel, Double ratingLow, Double ratingHigh);
}

