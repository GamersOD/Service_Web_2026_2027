package rendu.georgesvalerian.demo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

//DTO pour l'ajoute d'evaluation a la BDD
public class AddEvaluationDto {
    private String reviewername;
    private String commentary;
    private Integer note;
}
