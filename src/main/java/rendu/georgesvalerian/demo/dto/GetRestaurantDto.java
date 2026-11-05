package rendu.georgesvalerian.demo.dto;


import lombok.Getter;
import lombok.Setter;
import rendu.georgesvalerian.demo.entity.RestaurantEntity;

import java.util.UUID;

@Getter
@Setter


public class GetRestaurantDto {
    private Integer id;
    private String name;
    private String address;
}
