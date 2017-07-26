package io.oasp.application.mtsj.bookingmanagement.dataaccess.api;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import io.oasp.application.mtsj.bookingmanagement.common.api.Booking;
import io.oasp.application.mtsj.bookingmanagement.common.api.datatype.BookingType;
import io.oasp.application.mtsj.general.common.api.validation.EmailExtended;
import io.oasp.application.mtsj.general.dataaccess.api.ApplicationPersistenceEntity;
import io.oasp.application.mtsj.ordermanagement.dataaccess.api.OrderEntity;
import io.oasp.application.mtsj.usermanagement.dataaccess.api.UserEntity;

@Entity
@javax.persistence.Table(name = "Booking")
public class BookingEntity extends ApplicationPersistenceEntity implements Booking {

  @NotNull
  private String name;

  private String bookingToken;

  private String comment;

  @NotNull
  @Future
  private Timestamp bookingDate;

  private Timestamp expirationDate;

  private Timestamp creationDate;

  @NotNull
  @EmailExtended
  private String email;

  private Boolean canceled;

  private BookingType bookingType;

  private TableEntity table;

  private Long orderId;

  private UserEntity user;

  @LazyCollection(LazyCollectionOption.FALSE)
  private List<InvitedGuestEntity> invitedGuests;

  private List<OrderEntity> orders;

  @Min(value = 1, message = "Assistants must be greater than 0")
  @Digits(integer = 2, fraction = 0)
  private Integer assistants;

  private static final long serialVersionUID = 1L;

  public BookingEntity() {

    super();
    this.canceled = false;
  }

  /**
   * @return name
   */
  @Override
  public String getName() {

    return this.name;
  }

  /**
   * @param name new value of {@link #getName}.
   */
  @Override
  public void setName(String name) {

    this.name = name;
  }

  /**
   * @return bookingToken
   */
  @Override
  public String getBookingToken() {

    return this.bookingToken;
  }

  /**
   * @param bookingToken new value of {@link #getBookingToken}.
   */
  @Override
  public void setBookingToken(String bookingToken) {

    this.bookingToken = bookingToken;
  }

  /**
   * @return comments
   */
  @Override
  public String getComment() {

    return this.comment;
  }

  /**
   * @param comments new value of {@link #getComment}.
   */
  @Override
  public void setComment(String comments) {

    this.comment = comments;
  }

  /**
   * @return bookingDate
   */
  @Override
  public Timestamp getBookingDate() {

    return this.bookingDate;
  }

  /**
   * @param bookingDate new value of {@link #getBookingDate}.
   */
  @Override
  public void setBookingDate(Timestamp bookingDate) {

    this.bookingDate = bookingDate;
  }

  /**
   * @return expirationDate
   */
  @Override
  public Timestamp getExpirationDate() {

    return this.expirationDate;
  }

  /**
   * @param expirationDate new value of {@link #getExpirationDate}.
   */
  @Override
  public void setExpirationDate(Timestamp expirationDate) {

    this.expirationDate = expirationDate;
  }

  /**
   * @return creationDate
   */
  @Override
  public Timestamp getCreationDate() {

    return this.creationDate;
  }

  /**
   * @param creationDate new value of {@link #getCreationDate}.
   */
  @Override
  public void setCreationDate(Timestamp creationDate) {

    this.creationDate = creationDate;
  }

  /**
   * @return canceled
   */
  @Override
  public Boolean getCanceled() {

    return this.canceled;
  }

  /**
   * @param canceled new value of {@link #getCanceled}.
   */
  @Override
  public void setCanceled(Boolean canceled) {

    this.canceled = canceled;
  }

  /**
   * @return table
   */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idTable")
  public TableEntity getTable() {

    return this.table;
  }

  /**
   * @param table new value of {@link #getTable}.
   */
  public void setTable(TableEntity table) {

    this.table = table;
  }

  /**
   * @return invitedGuests
   */
  @OneToMany(mappedBy = "booking"/* , fetch = FetchType.EAGER */)
  public List<InvitedGuestEntity> getInvitedGuests() {

    return this.invitedGuests;
  }

  /**
   * @param invitedGuests new value of {@link #getInvitedGuests}.
   */
  public void setInvitedGuests(List<InvitedGuestEntity> invitedGuests) {

    this.invitedGuests = invitedGuests;
  }

  /**
   * @return type
   */
  @Override
  public BookingType getBookingType() {

    return this.bookingType;
  }

  /**
   * @param bookingType new value of {@link #getBookingType}.
   */
  @Override
  public void setBookingType(BookingType bookingType) {

    this.bookingType = bookingType;
  }

  @Override
  public String getEmail() {

    return this.email;
  }

  @Override
  public void setEmail(String email) {

    this.email = email;

  }

  @Override
  @Transient
  public Long getTableId() {

    if (this.table == null) {
      return null;
    }
    return this.table.getId();
  }

  @Override
  public void setTableId(Long tableId) {

    if (tableId == null) {
      this.table = null;
    } else {
      TableEntity tableEntity = new TableEntity();
      tableEntity.setId(tableId);
      this.table = tableEntity;
    }
  }

//  /**
//   * @return order
//   */
//  @OneToOne(fetch = FetchType.EAGER)
//  @JoinColumn(name = "idOrder")
//  public OrderEntity getOrder() {
//
//    return this.order;
//  }
//
//  /**
//   * @param order new value of {@link #getOrder}.
//   */
//  public void setOrder(OrderEntity order) {
//
//    this.order = order;
//  }

  @Override
  //@Transient
  @Column(name = "orderId")
  public Long getOrderId() {

    if (this.orderId == null) {
      return null;
    }
    return this.orderId;
  }

  @Override
  public void setOrderId(Long orderId) {

    this.orderId = orderId;
  }

  /**
   * @return orders
   */
  @OneToMany(mappedBy = "booking", fetch = FetchType.EAGER)
  public List<OrderEntity> getOrders() {

    return this.orders;
  }

  /**
   * @param orders new value of {@link #getOrders}.
   */
  public void setOrders(List<OrderEntity> orders) {

    this.orders = orders;
  }

  /**
   * @return assistants
   */
  public Integer getAssistants() {

    return this.assistants;
  }

  /**
   * @param assistants new value of {@link #getAssistants}.
   */
  public void setAssistants(Integer assistants) {

    this.assistants = assistants;
  }

  /**
   * @return user
   */
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "idUser")
  public UserEntity getUser() {

    return this.user;
  }

  /**
   * @param user new value of {@link #getUser}.
   */
  public void setUser(UserEntity user) {

    this.user = user;
  }

  @Override
  @Transient
  public Long getUserId() {

    if (this.user == null) {
      return null;
    }
    return this.user.getId();
  }

  @Override
  public void setUserId(Long userId) {

    if (userId == null) {
      this.user = null;
    } else {
      UserEntity userEntity = new UserEntity();
      userEntity.setId(userId);
      this.user = userEntity;
    }
  }

}
