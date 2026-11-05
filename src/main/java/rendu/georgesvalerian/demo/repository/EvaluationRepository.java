package rendu.georgesvalerian.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rendu.georgesvalerian.demo.entity.EvaluationEntity;

import java.util.List;


public interface EvaluationRepository extends JpaRepository<EvaluationEntity, Integer> {
    //rajoute une méthode
    List<EvaluationEntity> findAllByReviewername(String reviewername);
}
