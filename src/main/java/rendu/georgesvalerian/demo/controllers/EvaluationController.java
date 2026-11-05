package rendu.georgesvalerian.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import rendu.georgesvalerian.demo.dto.AddEvaluationDto;
import rendu.georgesvalerian.demo.dto.AddEvaluationResponseDto;
import rendu.georgesvalerian.demo.dto.GetEvaluationsResponseDto;
import rendu.georgesvalerian.demo.dto.UpdateEvaluationDto;
import rendu.georgesvalerian.demo.exceptions.NotFoundRestaurantException;
import rendu.georgesvalerian.demo.services.EvaluationService;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController

public class EvaluationController {
    private final EvaluationService evaluationService;

    //sur cette page les jwt sont récupérer pour permettre de différencier les ADMIN des USER
    //je récupère aussi les claims pour permettre aux services de différencier les USER des uns des autres
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @PostMapping("/restaurant/{id}/evaluation")
    public AddEvaluationResponseDto AddEvaluation(@RequestBody AddEvaluationDto evaluationBody, @PathVariable(name="id") int id,Authentication principal){
        Jwt jwt = (Jwt) principal.getPrincipal();
        String nom = jwt.getClaimAsString("name");

        return AddEvaluationResponseDto.fromEntity(
                this.evaluationService.AddEvaluation(
                        id,nom, evaluationBody.getCommentary(), evaluationBody.getNote()));
    }

    //effacer une evaluation
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @DeleteMapping("/restaurant/{id}/evaluation/{evaluationid}")
    public ResponseEntity<Integer> DeleteEvaluation(@PathVariable(name="evaluationid") int evaluationId, Authentication principal){
        Jwt jwt = (Jwt) principal.getPrincipal();
        String nom = jwt.getClaimAsString("name");
        String role = principal.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(this.evaluationService.DeleteEvaluation(evaluationId,role,nom));
    }

    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @PutMapping("/restaurant/{id}/evaluation/{evaluationid}")
    public ResponseEntity<Integer> UpdateEvaluation(@PathVariable(name="evaluationid")Integer id, @RequestBody UpdateEvaluationDto evaluationBody, Authentication principal){
        Jwt jwt = (Jwt) principal.getPrincipal();
        String nom = jwt.getClaimAsString("name");
        String role = principal.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(this.evaluationService.UpdateEvaluation(id,evaluationBody,role,nom));
    }

    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @GetMapping("/evaluations")
    public ResponseEntity<List<GetEvaluationsResponseDto>> GetEvaluations(Authentication principal){
        Jwt jwt = (Jwt) principal.getPrincipal();
        String nom = jwt.getClaimAsString("name");
        return ResponseEntity.ok(this.evaluationService.GetEvaluations(nom));
    }

    //upload en précisant le nombre d'image à upload
    @PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
    @PutMapping("/restaurant/{id}/evaluation/{evaluationid}/img")
    public ResponseEntity<List<String>> GetImgUploadUrl(@PathVariable(name="evaluationid")Integer id, @RequestBody Integer nbImg,Authentication principal){
        Jwt jwt = (Jwt) principal.getPrincipal();
        String nom = jwt.getClaimAsString("name");
        List<String> Urls = this.evaluationService.GetImgUploadUrl(id,nbImg,nom);

        return ResponseEntity.ok(Urls);
    }

    //consultation d'image
    @GetMapping("/restaurant/{id}/evaluation/{evaluationid}/img/{imgid}")
    public ResponseEntity<String> GetImgUrl(@PathVariable(name="evaluationid") Integer evaluationid,@PathVariable(name="evaluationid") Integer imgid){
        return ResponseEntity.ok(this.evaluationService.GetImgUrl(evaluationid,imgid));
    }
}
