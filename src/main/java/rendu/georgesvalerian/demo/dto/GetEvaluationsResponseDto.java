package rendu.georgesvalerian.demo.dto;

import lombok.Getter;
import lombok.Setter;
import rendu.georgesvalerian.demo.entity.EvaluationEntity;

import java.util.List;

@Getter
@Setter

//DTO pour le retour de la recupération des evaluations
public class GetEvaluationsResponseDto {
    private Integer id;
    private String reviewername;
    private String commentary;
    private Integer note;
    private List<String> filename;

    public static GetEvaluationsResponseDto fromEntity(EvaluationEntity evaluation) {
        GetEvaluationsResponseDto response = new GetEvaluationsResponseDto();
        response.id = evaluation.getId();
        response.reviewername = evaluation.getReviewername();
        response.commentary = evaluation.getCommentary();
        response.note = evaluation.getNote();
        response.filename = evaluation.getFilename();

        return response;
    }
}
