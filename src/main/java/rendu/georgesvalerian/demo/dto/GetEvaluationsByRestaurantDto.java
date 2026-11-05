package rendu.georgesvalerian.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rendu.georgesvalerian.demo.entity.EvaluationEntity;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor

//DTO pour la recuperation des evaluations
public class GetEvaluationsByRestaurantDto {
    private Integer id;
    private String reviewername;
    private String commentary;
    private Integer note;
    private List<String> filename;

    public static GetEvaluationsByRestaurantDto fromEntity(EvaluationEntity evaluation) {
        GetEvaluationsByRestaurantDto response = new GetEvaluationsByRestaurantDto();
        response.id = evaluation.getId();
        response.reviewername = evaluation.getReviewername();
        response.commentary = evaluation.getCommentary();
        response.note = evaluation.getNote();
        response.filename = evaluation.getFilename();

        return response;
    }
}
