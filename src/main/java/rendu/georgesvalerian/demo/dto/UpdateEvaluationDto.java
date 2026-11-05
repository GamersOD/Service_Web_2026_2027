package rendu.georgesvalerian.demo.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UpdateEvaluationDto {
    private String commentary;
    private Integer note;
}
