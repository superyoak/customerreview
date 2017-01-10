package de.hybris.platform.customerreview;

import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

public abstract interface FilteredCustomerReviewService 
{
    //create customer review with rating and comments validations
    public abstract CustomerReviewModel createCustomerReview(Double paramDouble, String paramString1, String paramString2, UserModel paramUserModel, ProductModel paramProductModel);

    //return the Number of CustomerReviewModel within rating range [ratingLow, ratingHigh] 
    public abstract Integer getNumOfReviewsInRatingRange(ProductModel paramProductModel, Double ratingLow, Double ratingHigh);
}

