package rendu.georgesvalerian.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rendu.georgesvalerian.demo.entity.RestaurantEntity;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Integer> {
}
