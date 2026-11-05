package rendu.georgesvalerian.demo.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rendu.georgesvalerian.demo.dto.GetRestaurantResponseDto;
import rendu.georgesvalerian.demo.entity.RestaurantEntity;
import rendu.georgesvalerian.demo.exceptions.NotFoundRestaurantException;
import rendu.georgesvalerian.demo.repository.RestaurantRepository;
import rendu.georgesvalerian.demo.repository.S3Repository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final S3Repository s3Repository;
    private final String BUCKET = "restaurant";

    //ajoute un restaurant
    public RestaurantEntity AddRestaurant(String name, String address){

        RestaurantEntity restaurantEntity = new RestaurantEntity();
        restaurantEntity.setAddress(address);
        restaurantEntity.setName(name);

        this.restaurantRepository.save(restaurantEntity);
        return restaurantEntity;
    }

    //récupère un restaurant en fonction de l'ID
    public RestaurantEntity GetRestaurant(Integer id){
        RestaurantEntity restaurant = this.restaurantRepository.findById(id).orElse(null);

        if(restaurant!= null) {
            return restaurant;

        }else throw new NotFoundRestaurantException("Restaurant non répertorié");

    }

    //récupère tout les restaurants
    public List<GetRestaurantResponseDto> GetRestaurants(){
        List<RestaurantEntity> restaurants = this.restaurantRepository.findAll();
        List<GetRestaurantResponseDto> getRestaurantResponseDto = new ArrayList<>();

        if(!restaurants.isEmpty()){
            for (RestaurantEntity restaurantEntity : restaurants){
                getRestaurantResponseDto.add(GetRestaurantResponseDto.fromEntity(restaurantEntity));
            }

            return getRestaurantResponseDto;
        }else throw new NotFoundRestaurantException("Aucun Restaurant dans la liste");
    }

    //Update un restaurant en fonction de l'ID
    public RestaurantEntity UpdateRestaurant(Integer id, String name, String address){
        RestaurantEntity restaurant = this.restaurantRepository.findById(id).orElse(null);

        if(restaurant != null){
           restaurant.setName(name);
           restaurant.setAddress(address);

           return this.restaurantRepository.save(restaurant);

        }else throw new NotFoundRestaurantException("Aucun restaurant trouvé pour la modification");
    }

    //crée l'URL pour une image
    public String GetImgUploadUrl(Integer id){

        String filePath = "georgesvalerian/" + BUCKET + id;
        RestaurantEntity restaurant = this.restaurantRepository.findById(id).orElse(null);

        if(this.restaurantRepository.existsById(id)){

            restaurant.setFilename(id);
            this.restaurantRepository.save(restaurant);
            return s3Repository.getUploadFileUrl(filePath,BUCKET);

        }else throw new NotFoundRestaurantException("Aucun restaurant trouvé pour l'insertion de fichier");

    }

    //récupère le lien d'une image
    public String GetImgUrl(Integer id){

        if(this.restaurantRepository.existsById(id)) {
            RestaurantEntity restaurant = this.restaurantRepository.findById(id).orElse(null);

            if(restaurant.getFilename() != null) {

                String filePath = "georgesvalerian/" + BUCKET + id;
                return s3Repository.getFileUrl(filePath, BUCKET);

            }else throw new NotFoundRestaurantException("Aucun fichier relié à ce restaurant");

        }else throw new NotFoundRestaurantException("Aucun restaurant trouvé pour la récupération de fichier");
    }
}
