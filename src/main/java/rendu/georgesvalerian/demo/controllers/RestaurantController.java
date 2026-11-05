package rendu.georgesvalerian.demo.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rendu.georgesvalerian.demo.dto.AddRestaurantDto;
import rendu.georgesvalerian.demo.dto.AddRestaurantResponseDto;
import rendu.georgesvalerian.demo.dto.GetRestaurantResponseDto;
import rendu.georgesvalerian.demo.services.RestaurantService;

import java.util.List;

@RequiredArgsConstructor
@RestController

public class RestaurantController {
    private final RestaurantService restaurantService;

    //route pour ajouter un restaurant
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/restaurant")
    public ResponseEntity<AddRestaurantResponseDto> AddRestaurant(@Valid @RequestBody AddRestaurantDto restaurant, Authentication principal){

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    AddRestaurantResponseDto.fromEntity(
                            this.restaurantService.AddRestaurant(
                                    restaurant.getName(), restaurant.getAddress() )));
    }

    //route pour récupérer un restaurant
    //pas de role nécessaire tout le monde devrait pouvoir voir les restaurants
    @GetMapping("/restaurant/{id}")
    public ResponseEntity<GetRestaurantResponseDto> GetRestaurant(@PathVariable(name="id") Integer id){
        return ResponseEntity.ok(GetRestaurantResponseDto.fromEntity(
                this.restaurantService.GetRestaurant(id) ));
    }

    //route pour récupérer touts les restaurants
    //pas de role nécessaire tout le monde devrait pouvoir voir les restaurants
    @GetMapping("/restaurant")
    public ResponseEntity<List<GetRestaurantResponseDto>> GetRestaurants(){
        return ResponseEntity.ok(this.restaurantService.GetRestaurants());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/restaurant/{id}")
    public ResponseEntity<GetRestaurantResponseDto> UpdateRestaurant(@PathVariable(name="id") Integer id, @RequestBody AddRestaurantDto restaurant, Authentication principal){
        return ResponseEntity.ok(GetRestaurantResponseDto.fromEntity(
                this.restaurantService.UpdateRestaurant(
                        id,restaurant.getName(),restaurant.getAddress() )));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/restaurant/{id}/img")
    public ResponseEntity<String> GetImgUploadUrl(@PathVariable(name="id")Integer id){
            return ResponseEntity.ok(this.restaurantService.GetImgUploadUrl(id));
    }

    //consultation d'image pas nécessaire d'avoir un role
    @GetMapping("/restaurant/{id}/img")
    public ResponseEntity<String> GetImgUrl(@PathVariable(name="id") Integer id){
        return ResponseEntity.ok(this.restaurantService.GetImgUrl(id));
    }
}
