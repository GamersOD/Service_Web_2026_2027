package rendu.georgesvalerian.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rendu.georgesvalerian.demo.entity.RestaurantEntity;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

//DTO pour la response d'un ajout de restaurant à la bdd
public class AddRestaurantResponseDto {
    private Integer id;

    public static AddRestaurantResponseDto fromEntity(RestaurantEntity restaurant){
        AddRestaurantResponseDto response = new AddRestaurantResponseDto();
        response.id = restaurant.getId();
        System.out.println(response.id);

        return response;
    }
}
