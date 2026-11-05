package rendu.georgesvalerian.demo.dto;

import lombok.*;
import rendu.georgesvalerian.demo.entity.EvaluationEntity;
import rendu.georgesvalerian.demo.entity.RestaurantEntity;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter

public class GetRestaurantResponseDto {
    private Integer id;
    private String name;
    private String address;
    private List<GetEvaluationsByRestaurantDto> evaluations;
    private Integer moyenne;
    private Integer filename;

    public static GetRestaurantResponseDto fromEntity(RestaurantEntity restaurantEntity){

        Integer temp = 0;

        GetRestaurantResponseDto response = new GetRestaurantResponseDto();
        response.setId(restaurantEntity.getId());
        response.setName(restaurantEntity.getName());
        response.setAddress(restaurantEntity.getAddress());
        response.setFilename(restaurantEntity.getFilename());

        List<EvaluationEntity> evaluationEntities = restaurantEntity.getEvaluations();
        List<GetEvaluationsByRestaurantDto> getEvaluationsByRestaurantDtos = new ArrayList<>();

        for (EvaluationEntity evaluationEntity : evaluationEntities){
            getEvaluationsByRestaurantDtos.add(GetEvaluationsByRestaurantDto.fromEntity(evaluationEntity));
            temp += evaluationEntity.getNote();
        }

        if(!getEvaluationsByRestaurantDtos.isEmpty()) {
            temp = temp / getEvaluationsByRestaurantDtos.size();
            response.setMoyenne(temp);
        }else response.setMoyenne(-1);

        response.setEvaluations(getEvaluationsByRestaurantDtos);

        return response;

    }

}
