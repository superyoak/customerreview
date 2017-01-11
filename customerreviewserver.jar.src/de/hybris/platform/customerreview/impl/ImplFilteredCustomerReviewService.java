package de.hybris.platform.customerreview.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.CustomerReviewService;
import de.hybris.platform.customerreview.dao.CustomerReviewDao;
import de.hybris.platform.customerreview.jalo.CustomerReview;
import de.hybris.platform.customerreview.jalo.CustomerReviewManager;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;




public class ImplFilteredCustomerReviewService
      extends AbstractBusinessService
      implements FilteredCustomerReviewService
{
  private CustomerReviewService defaultService;
  private String[] cursewords;
  private Double zerovalue;
  
  public CustomerReviewModel createCustomerReview(Double rating, String headline, String comment, UserModel user, ProductModel product)
  {
      //Check if rating is less than 0.  Throws if null || < 0
      if (rating == null || rating.doubleValue() < 0)
      {
          throw new JaloInvalidParameterException(Localization.getLocalizedString("error.customerreview.invalidrating", 
                  new Object[] { (rating == null) ? "null" : rating, 
                               new Double(CustomerReviewConstants.getInstance().MINRATING), 
                               new Double(CustomerReviewConstants.getInstance().MAXRATING) 
                             }), 0);
      }
      
      if(comment != null)
      {
          //Check if comment contains any of the curse words. compare uppercase only
          String strUpperComment = comment.toUpperCase();
          if(Arrays.asList(cursewords).stream().anyMatch(cw -> strUpperComment.contains(cw)))
          {
              throw new JaloInvalidParameterException(comment + " is not allowed to contain curse words", 0);
          }
      }
      
      return defaultService.createCustomerReview(rating, headline, comment, user, product);
  }


  public Integer getNumOfReviewsInRatingRange(ProductModel paramProductModel, Double ratingLow, Double ratingHigh);
  {
      if(ratingLow == null) 
      {
          ratingLow = new Double(CustomerReviewConstants.getInstance().MINRATING);
      }
      
      if(ratingHigh == null)
      {
          ratingHigh = new Double(CustomerReviewConstants.getInstance().MAXRATING);
      }
      
      if(ratingHigh.doubleValue() < ratingLow.doubleValue())
      {
          throw new JaloInvalidParameterException("Wrong rating range provided. ratingLow should not be greater than ratingHigh!", 0);
      }
 
      //get all reviews
      List<CustomerReview> reviews = CustomerReviewManager.getInstance().getAllReviews(
        (Product)getModelService().getSource(product));
      
      //get count within range
      return (int)(reviews.stream().filter(cr -> cr.getRating().doubleValue() > max(0, ratingLow.doubleValue() - zerovalue.doubleValue()) &&
                                           cr.getRating().doubleValue() < ratingHigh.doubleValue() + zerovalue.doubleValue()).count());
  }
  
  @Required
  public void setDefaultservice(CustomerReviewService defaultService)
  {
      this.defaultService = defaultService;
  }
  
  protected CustomerReviewService getDefaultservice()
  {
      return defaultservice;
  }

  @Required
  public void setCursewords(String[] cursewords) {
     this.cursewords = cursewords;
  }
  public String[] getCursewords() {
     return cursewords;
  }
  
  @Required
  public void setZerovalue(Double zerovalue )
  {
      this.zerovalue = zerovalue;
  }
  
  public Double getZerovalue() {
      return zerovalue;
  }
}
  