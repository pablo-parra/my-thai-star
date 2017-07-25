package io.oasp.application.mtsj.dishmanagement.service.impl.rest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;

import io.oasp.application.mtsj.dishmanagement.logic.api.Dishmanagement;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.CategoryEto;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.CategorySearchCriteriaTo;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.DishCto;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.DishEto;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.DishSearchCriteriaTo;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.IngredientSearchCriteriaTo;
import io.oasp.application.mtsj.dishmanagement.service.api.rest.DishmanagementRestService;
import io.oasp.module.jpa.common.api.to.PaginatedListTo;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * The service implementation for REST calls in order to execute the logic of component {@link Dishmanagement}.
 */
@Named("DishmanagementRestService")
public class DishmanagementRestServiceImpl implements DishmanagementRestService {

  @Inject
  private Dishmanagement dishmanagement;

  @Override
  public CategoryEto getCategory(long id) {

    return this.dishmanagement.findCategory(id);
  }

  @Override
  public CategoryEto saveCategory(CategoryEto category) {

    return this.dishmanagement.saveCategory(category);
  }

  @Override
  public void deleteCategory(long id) {

    this.dishmanagement.deleteCategory(id);
  }

  @Override
  public PaginatedListTo<CategoryEto> findCategorysByPost(CategorySearchCriteriaTo searchCriteriaTo) {

    return this.dishmanagement.findCategoryEtos(searchCriteriaTo);
  }

  @Override
  public DishCto getDish(long id) {

    return this.dishmanagement.findDish(id);
  }

  @Override
  public DishEto saveDish(DishEto dish) {

    return this.dishmanagement.saveDish(dish);
  }

  @Override
  public void deleteDish(long id) {

    this.dishmanagement.deleteDish(id);
  }

  @Override
  public PaginatedListTo<DishCto> findDishsByPost(DishSearchCriteriaTo searchCriteriaTo) {

    return this.dishmanagement.findDishCtos(searchCriteriaTo);
  }

  @Override
  public IngredientEto getIngredient(long id) {

    return this.dishmanagement.findIngredient(id);
  }

  @Override
  public IngredientEto saveIngredient(IngredientEto ingredient) {

    return this.dishmanagement.saveIngredient(ingredient);
  }

  @Override
  public void deleteIngredient(long id) {

    this.dishmanagement.deleteIngredient(id);
  }

  @Override
  public PaginatedListTo<IngredientEto> findIngredientsByPost(IngredientSearchCriteriaTo searchCriteriaTo) {

    return this.dishmanagement.findIngredientEtos(searchCriteriaTo);
  }

  @Override
  public MultipartBody getDishPicture(long dishId) throws SQLException, IOException {
    Blob blob = this.dishmanagement.findDishImageBlob(dishId);
    byte[] data = IOUtils.readBytesFromStream(blob.getBinaryStream());

    List<Attachment> atts = new LinkedList<>();
    atts.add(new Attachment("binaryObjectEto", MediaType.APPLICATION_JSON, this.dishmanagement.findDishImage(dishId)));
    atts.add(new Attachment("blob", MediaType.APPLICATION_OCTET_STREAM, new ByteArrayInputStream(data)));
    return new MultipartBody(atts, true);
  }

}
