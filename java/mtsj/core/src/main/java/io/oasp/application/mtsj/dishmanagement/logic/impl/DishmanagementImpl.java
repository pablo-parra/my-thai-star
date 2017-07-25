package io.oasp.application.mtsj.dishmanagement.logic.impl;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import io.oasp.application.mtsj.general.logic.api.to.BinaryObjectEto;
import io.oasp.application.mtsj.general.logic.base.UcManageBinaryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import io.oasp.application.mtsj.dishmanagement.common.api.Category;
import io.oasp.application.mtsj.dishmanagement.dataaccess.api.CategoryEntity;
import io.oasp.application.mtsj.dishmanagement.dataaccess.api.DishEntity;
import io.oasp.application.mtsj.dishmanagement.dataaccess.api.IngredientEntity;
import io.oasp.application.mtsj.dishmanagement.dataaccess.api.dao.CategoryDao;
import io.oasp.application.mtsj.dishmanagement.dataaccess.api.dao.DishDao;
import io.oasp.application.mtsj.dishmanagement.dataaccess.api.dao.IngredientDao;
import io.oasp.application.mtsj.dishmanagement.logic.api.Dishmanagement;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.CategoryEto;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.CategorySearchCriteriaTo;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.DishCto;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.DishEto;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.DishSearchCriteriaTo;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.IngredientEto;
import io.oasp.application.mtsj.dishmanagement.logic.api.to.IngredientSearchCriteriaTo;
import io.oasp.application.mtsj.general.logic.base.AbstractComponentFacade;
import io.oasp.module.jpa.common.api.to.PaginatedListTo;

/**
 * Implementation of component interface of dishmanagement
 */
@Named
@Transactional
public class DishmanagementImpl extends AbstractComponentFacade implements Dishmanagement {

  /**
   * Logger instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(DishmanagementImpl.class);

  /**
   * @see #getCategoryDao()
   */
  @Inject
  private CategoryDao categoryDao;

  /**
   * @see #getDishDao()
   */
  @Inject
  private DishDao dishDao;

  /**
   * @see #getIngredientDao()
   */
  @Inject
  private IngredientDao ingredientDao;

  @Inject
  private UcManageBinaryObject binaryObject;

  /**
   * The constructor.
   */
  public DishmanagementImpl() {

    super();
  }

  @Override
  public CategoryEto findCategory(Long id) {

    LOG.debug("Get Category with id {} from database.", id);
    return getBeanMapper().map(getCategoryDao().findOne(id), CategoryEto.class);
  }

  @Override
  public PaginatedListTo<CategoryEto> findCategoryEtos(CategorySearchCriteriaTo criteria) {

    criteria.limitMaximumPageSize(MAXIMUM_HIT_LIMIT);
    PaginatedListTo<CategoryEntity> categorys = getCategoryDao().findCategorys(criteria);
    return mapPaginatedEntityList(categorys, CategoryEto.class);
  }

  @Override
  public boolean deleteCategory(Long categoryId) {

    CategoryEntity category = getCategoryDao().find(categoryId);
    getCategoryDao().delete(category);
    LOG.debug("The category with id '{}' has been deleted.", categoryId);
    return true;
  }

  @Override
  public CategoryEto saveCategory(CategoryEto category) {

    Objects.requireNonNull(category, "category");
    CategoryEntity categoryEntity = getBeanMapper().map(category, CategoryEntity.class);

    // initialize, validate categoryEntity here if necessary
    CategoryEntity resultEntity = getCategoryDao().save(categoryEntity);
    LOG.debug("Category with id '{}' has been created.", resultEntity.getId());

    return getBeanMapper().map(resultEntity, CategoryEto.class);
  }

  /**
   * Returns the field 'categoryDao'.
   *
   * @return the {@link CategoryDao} instance.
   */
  public CategoryDao getCategoryDao() {

    return this.categoryDao;
  }

  @Override
  public DishCto findDish(Long id) {

    LOG.debug("Get Dish with id {} from database.", id);
    DishEntity dish = getDishDao().findOne(id);
    DishCto cto = new DishCto();
    cto.setCategories(getBeanMapper().mapList(dish.getCategories(), CategoryEto.class));
    cto.setImage(this.binaryObject.findBinaryObject(dish.getImageId()));
    cto.setDish(getBeanMapper().map(dish, DishEto.class));
    cto.setExtras(getBeanMapper().mapList(dish.getExtras(), IngredientEto.class));
    return cto;
  }

  @Override
  public PaginatedListTo<DishCto> findDishCtos(DishSearchCriteriaTo criteria) {

    criteria.limitMaximumPageSize(MAXIMUM_HIT_LIMIT);
    List<DishCto> ctos = new ArrayList<>();
    PaginatedListTo<DishEntity> searchResult = getDishDao().findDishs(criteria);

    for (DishEntity dish : searchResult.getResult()) {
      DishCto cto = new DishCto();
      cto.setDish(getBeanMapper().map(dish, DishEto.class));
      cto.setImage(this.binaryObject.findBinaryObject(dish.getImageId()));
      cto.setCategories(getBeanMapper().mapList(dish.getCategories(), CategoryEto.class));
      cto.setExtras(getBeanMapper().mapList(dish.getExtras(), IngredientEto.class));
      ctos.add(cto);
    }

    if (!criteria.getCategories().isEmpty()) {
      ctos = categoryFilter(ctos, criteria.getCategories());
    }

    return new PaginatedListTo<>(ctos, searchResult.getPagination());
  }

  @Override
  public boolean deleteDish(Long dishId) {

    DishEntity dish = getDishDao().find(dishId);
    getDishDao().delete(dish);
    LOG.debug("The dish with id '{}' has been deleted.", dishId);
    return true;
  }

  @Override
  public DishEto saveDish(DishEto dish) {

    Objects.requireNonNull(dish, "dish");
    DishEntity dishEntity = getBeanMapper().map(dish, DishEntity.class);

    // initialize, validate dishEntity here if necessary
    DishEntity resultEntity = getDishDao().save(dishEntity);
    LOG.debug("Dish with id '{}' has been created.", resultEntity.getId());

    return getBeanMapper().map(resultEntity, DishEto.class);
  }

  public PaginatedListTo<DishCto> findDishesByCategory(DishSearchCriteriaTo criteria, String categoryName) {

    List<DishCto> ctos = new ArrayList<>();
    PaginatedListTo<DishCto> searchResult = findDishCtos(criteria);
    for (DishCto dish : searchResult.getResult()) {
      for (CategoryEto category : dish.getCategories()) {
        if (category.getName().equals(categoryName)) {
          ctos.add(dish);
        }
      }
    }
    return new PaginatedListTo<>(ctos, searchResult.getPagination());
  }

  private List<DishCto> categoryFilter(List<DishCto> dishes, List<CategoryEto> categories) {

    List<DishCto> dishFiltered = new ArrayList<>();
    for (DishCto dish : dishes) {

      List<CategoryEto> entityCats = dish.getCategories();
      for (Category entityCat : entityCats) {
        for (Category category : categories) {
          if (category.getId() == entityCat.getId()) {
            if (!dishAlreadyAdded(dishFiltered, dish)) {
              dishFiltered.add(dish);
              break;
            }

          }
        }
      }

    }

    return dishFiltered;
  }

  private boolean dishAlreadyAdded(List<DishCto> dishEntitiesFiltered, DishCto dishEntity) {

    boolean result = false;
    for (DishCto entity : dishEntitiesFiltered) {
      if (entity.getDish().getId() == dishEntity.getDish().getId()) {
        result = true;
        break;
      }
    }
    return result;
  }

  /**
   * Returns the field 'dishDao'.
   *
   * @return the {@link DishDao} instance.
   */
  public DishDao getDishDao() {

    return this.dishDao;
  }

  @Override
  public IngredientEto findIngredient(Long id) {

    LOG.debug("Get Ingredient with id {} from database.", id);
    return getBeanMapper().map(getIngredientDao().findOne(id), IngredientEto.class);
  }

  @Override
  public PaginatedListTo<IngredientEto> findIngredientEtos(IngredientSearchCriteriaTo criteria) {

    criteria.limitMaximumPageSize(MAXIMUM_HIT_LIMIT);
    PaginatedListTo<IngredientEntity> ingredients = getIngredientDao().findIngredients(criteria);
    return mapPaginatedEntityList(ingredients, IngredientEto.class);
  }

  @Override
  public boolean deleteIngredient(Long ingredientId) {

    IngredientEntity ingredient = getIngredientDao().find(ingredientId);
    getIngredientDao().delete(ingredient);
    LOG.debug("The ingredient with id '{}' has been deleted.", ingredientId);
    return true;
  }

  @Override
  public IngredientEto saveIngredient(IngredientEto ingredient) {

    Objects.requireNonNull(ingredient, "ingredient");
    IngredientEntity ingredientEntity = getBeanMapper().map(ingredient, IngredientEntity.class);

    // initialize, validate ingredientEntity here if necessary
    IngredientEntity resultEntity = getIngredientDao().save(ingredientEntity);
    LOG.debug("Ingredient with id '{}' has been created.", resultEntity.getId());

    return getBeanMapper().map(resultEntity, IngredientEto.class);
  }

  /**
   * Returns the field 'ingredientDao'.
   *
   * @return the {@link IngredientDao} instance.
   */
  public IngredientDao getIngredientDao() {

    return this.ingredientDao;
  }

  @Override
  public BinaryObjectEto findDishImage(Long dishId){
    DishCto dish = findDish(dishId);
    if (dish != null){
      return getUcManageBinaryObject().findBinaryObject(dish.getImage().getId());
    }else{
      return null;
    }
  }

  public Blob findDishImageBlob(Long dishId){
    DishCto dish = findDish(dishId);
    if (dish != null){
      return getUcManageBinaryObject().getBinaryObjectBlob(dish.getImage().getId());
    }else{
      return null;
    }
  }


  /**
   * @return ucManageBinaryObject
   */
  public UcManageBinaryObject getUcManageBinaryObject() {

    return this.binaryObject;
  }
}
