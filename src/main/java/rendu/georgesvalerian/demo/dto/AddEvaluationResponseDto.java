package rendu.georgesvalerian.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rendu.georgesvalerian.demo.entity.EvaluationEntity;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//DTO pour le renvoie d'information apres l'ajout d'evaluation la BDD
public class AddEvaluationResponseDto {
    private Integer id;

    public static AddEvaluationResponseDto fromEntity(EvaluationEntity evaluation){
        AddEvaluationResponseDto response = new AddEvaluationResponseDto();
        response.id = evaluation.getId();

        return response;
    }
}
