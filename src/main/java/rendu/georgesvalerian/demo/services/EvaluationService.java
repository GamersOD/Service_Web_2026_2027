package rendu.georgesvalerian.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rendu.georgesvalerian.demo.dto.AddEvaluationDto;
import rendu.georgesvalerian.demo.dto.GetEvaluationsResponseDto;
import rendu.georgesvalerian.demo.dto.UpdateEvaluationDto;
import rendu.georgesvalerian.demo.entity.EvaluationEntity;
import rendu.georgesvalerian.demo.entity.RestaurantEntity;
import rendu.georgesvalerian.demo.exceptions.NotFoundRestaurantException;
import rendu.georgesvalerian.demo.repository.EvaluationRepository;
import rendu.georgesvalerian.demo.repository.RestaurantRepository;
import rendu.georgesvalerian.demo.repository.S3Repository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class EvaluationService {
    private final EvaluationRepository evaluationRepository;
    private final RestaurantRepository restaurantRepository;
    private final S3Repository s3Repository;
    private final String BUCKET = "evaluation";

    //ajout d'une evaluation en fonction de l'utilisateur connecté
    public EvaluationEntity AddEvaluation(Integer restaurantid,String reviewername, String commentary, Integer note){

        if(restaurantRepository.existsById(restaurantid)) {
            EvaluationEntity evaluationEntity = new EvaluationEntity();
            evaluationEntity.setReviewername(reviewername);
            evaluationEntity.setCommentary(commentary);
            evaluationEntity.setNote(note);

            RestaurantEntity restaurant = this.restaurantRepository.findById(restaurantid).orElse(null);
            evaluationEntity.setRestaurant(restaurant);

            this.evaluationRepository.save(evaluationEntity);
            return evaluationEntity;
        }else throw new NotFoundRestaurantException("Restaurant pour ajout d'evaluation introuvable");
    }

    //suppression d'un commentaire si on est un ADMIN sans vérification , si l'on ne l'est pas alors
    //utilise le nom depuis le JWT token pour confirmer qu'on est bien le propriétaire et ensuite supprime
    //sinon annule
    public Integer DeleteEvaluation(Integer evaluationid,String role,String nom){
        if(evaluationRepository.existsById(evaluationid)){
            if(role.equals("ADMIN")) {
                evaluationRepository.deleteById(evaluationid);
            }else {
                EvaluationEntity evaluation = evaluationRepository.findById(evaluationid).orElse(null);
                if(evaluation.getReviewername().equals(nom)){
                    evaluationRepository.deleteById(evaluationid);
                }
            }
            return 1;
        }else throw new NotFoundRestaurantException("Delete evaluation, cette evaluation n'existe pas");

    }

    //modification d'un commentaire si on est un ADMIN sans vérification , si l'on ne l'est pas alors
    //utilise le nom depuis le JWT token pour confirmer qu'on est bien le propriétaire et ensuite supprime
    //sinon annule
    public Integer UpdateEvaluation(Integer evaluationid, UpdateEvaluationDto UpdateEvaluationDto, String role , String nom){
        if(evaluationRepository.existsById(evaluationid)){
            EvaluationEntity evaluation = this.evaluationRepository.findById(evaluationid).orElse(null);
            if(role.equals("ADMIN")) {
                evaluation.setNote(UpdateEvaluationDto.getNote());
                evaluation.setCommentary(UpdateEvaluationDto.getCommentary());
                evaluationRepository.save(evaluation);
            }else {
                if(evaluation.getReviewername().equals(nom)){
                    evaluation.setNote(UpdateEvaluationDto.getNote());
                    evaluation.setCommentary(UpdateEvaluationDto.getCommentary());
                    evaluationRepository.save(evaluation);
                }else throw new NotFoundRestaurantException("Vous n'êtes pas le propriétaire de cette evaluation");
            }
            return 1;
        }else throw new NotFoundRestaurantException("Delete evaluation, cette evaluation n'existe pas");
    }

    //pas réussie à update pour les admin mais on peut récupérer toute les evaluations de l'utilisateur authentifié
    public List<GetEvaluationsResponseDto> GetEvaluations(String nom){

        List<EvaluationEntity> evaluations = evaluationRepository.findAllByReviewername(nom);
        List<GetEvaluationsResponseDto> getEvaluationsResponseDtos = new ArrayList<>();

            if (!evaluations.isEmpty()) {
                    for (EvaluationEntity evaluationEntity : evaluations) {
                        getEvaluationsResponseDtos.add(GetEvaluationsResponseDto.fromEntity(evaluationEntity));
                    }
                    return getEvaluationsResponseDtos;
            } else throw new NotFoundRestaurantException("Aucune Evaluation trouvé pour cet utilisateur");
    }

    //récupère le lien pour upload une image en vérifiant si l'evaluation existe
    public List<String> GetImgUploadUrl(Integer id,Integer nbImg, String nom){

        if(this.evaluationRepository.existsById(id)){
            EvaluationEntity evaluation = this.evaluationRepository.findById(id).orElse(null);
            if(nom.equals(evaluation.getReviewername())) {
                List<String> filename = evaluation.getFilename();
                Integer fileNumber;
                String fileid;
                String filePath;
                List<String> Urls = new ArrayList<>();

                for (int i = 0; i < nbImg; i++) {

                    fileNumber = filename.size() + 1;
                    fileid = id + fileNumber.toString();
                    filename.add(fileid);

                    filePath = "georgesvalerian/" + BUCKET + fileid;
                    Urls.add(s3Repository.getUploadFileUrl(filePath, BUCKET));
                }

                evaluation.setFilename(filename);
                this.evaluationRepository.save(evaluation);

                return Urls;
            }else throw new NotFoundRestaurantException("Evaluation utilisé n'appartient pas à cet user");
        }else throw new NotFoundRestaurantException("Aucune evaluation trouvé pour l'insertion de fichier");
    }

    //récupère l'URL d'une image
    public String GetImgUrl(Integer evaluationid,Integer imgId){

        if(this.evaluationRepository.existsById(evaluationid)){
            EvaluationEntity evaluation = this.evaluationRepository.findById(evaluationid).orElse(null);

            if(!evaluation.getFilename().isEmpty()){

                String filePath = "georgesvalerian/" + BUCKET + evaluationid + imgId.toString();

                if(s3Repository.doesObjectExist(BUCKET,filePath)){
                    return s3Repository.getFileUrl(filePath, BUCKET);
                }else throw new NotFoundRestaurantException("Ce fichier n'existe pas dans cette evaluation");

            }else throw new NotFoundRestaurantException("Aucun fichier relié à cette evaluation");

        }else throw new NotFoundRestaurantException("Aucune evaluation trouvé pour la récupération de fichier");
    }



}
