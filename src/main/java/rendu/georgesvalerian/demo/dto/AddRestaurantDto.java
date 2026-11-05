package rendu.georgesvalerian.demo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

//DTO pour l'ajout de restaurant à la BDD
public class AddRestaurantDto {
    private String name;
    private String address;
}
