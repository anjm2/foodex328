package food.domain;

import food.RiderApplication;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Delivery_table")
@Data
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String orderId;

    private String address;

    private String status;

    @PostPersist
    public void onPostPersist() {}

    public static DeliveryRepository repository() {
        DeliveryRepository deliveryRepository = RiderApplication.applicationContext.getBean(
            DeliveryRepository.class
        );
        return deliveryRepository;
    }

    public void pickFood() {
        FoodPicked foodPicked = new FoodPicked(this);
        foodPicked.publishAfterCommit();
    }

    public void confirm() {
        DeliveryConfirmed deliveryConfirmed = new DeliveryConfirmed(this);
        deliveryConfirmed.publishAfterCommit();
    }

    public static void loadDeliveryInfo(CookStarted cookStarted) {
        /** Example 1:  new item 
        Delivery delivery = new Delivery();
        repository().save(delivery);

        */


        repository().findById(cookStarted.getOrderId()).ifPresent(delivery->{
            
            delivery.setStatus("요리시작됨");
            repository().save(delivery);


         });

    }

    public static void loadDeliveryInfo(OrderPlaced orderPlaced) {
      Delivery delivery = new Delivery();
      delivery.setOrderId(String.valueOf(orderPlaced.getId()));
      delivery.setAddress(orderPlaced.getAddress());
      delivery.setStatus("배송대기중");
      repository().save(delivery);

    }
}
