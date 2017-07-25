package io.oasp.application.mtsj.dishmanagement.logic.api.to;

import java.util.List;

import io.oasp.application.mtsj.general.common.api.to.AbstractCto;
import io.oasp.application.mtsj.general.logic.api.to.BinaryObjectEto;


/**
 * Composite transport object of Dish
 */
public class DishCto extends AbstractCto {

  private static final long serialVersionUID = 1L;

  private DishEto dish;

  private BinaryObjectEto image;

  private List<IngredientEto> extras;

  private List<CategoryEto> categories;

  public DishEto getDish() {

    return this.dish;
  }

  public void setDish(DishEto dish) {

    this.dish = dish;
  }

  public List<IngredientEto> getExtras() {

    return this.extras;
  }

  public void setExtras(List<IngredientEto> extras) {

    this.extras = extras;
  }

  public List<CategoryEto> getCategories() {

    return this.categories;
  }

  public void setCategories(List<CategoryEto> categories) {

    this.categories = categories;
  }

  /**
   * @return image
   */
  public BinaryObjectEto getImage() {

    return image;
  }

  /**
   * @param image new value of {@link #getImage}.
   */
  public void setImage(BinaryObjectEto image) {

    this.image = image;
  }

}
