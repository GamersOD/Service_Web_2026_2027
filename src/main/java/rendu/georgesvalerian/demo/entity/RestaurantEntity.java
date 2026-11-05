package rendu.georgesvalerian.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="Restaurant")
public class RestaurantEntity {
    @Id
    @Positive
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Size(max = 90)
    @Column(name="name")
    private String name;

    @Size(max = 255)
    @Column(name="address")
    private String address;

    @Column(name="fileName")
    private Integer filename;

    @OneToMany(mappedBy = "restaurant")
    List<EvaluationEntity> evaluations = new ArrayList<>();

}
