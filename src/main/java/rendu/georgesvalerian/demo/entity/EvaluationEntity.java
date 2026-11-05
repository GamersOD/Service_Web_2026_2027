package rendu.georgesvalerian.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="Evaluation")
public class EvaluationEntity {
    @Id
    @Positive
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Size(max = 50)
    @Column(name = "reviewername")
    private String reviewername;

    @Size(max = 255)
    @Column(name = "commentary")
    private String commentary;

    @Min(0)
    @Max(3)
    @Column(name = "note")
    private Integer note;

    @Column(name = "fileName")
    private List<String> filename = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;

}
