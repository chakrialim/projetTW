package pharmacie.entity;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;

import java.util.LinkedList;
import java.util.List;



@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString

public class Fournisseur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic(optional = false)
    @NonNull
    private String nom;

    @Basic(optional = false)
    @NonNull
    private String adresseelec;

    @ToString.Exclude
	@JsonIgnore 
	@ManyToMany( cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name = "fournisseur_categorie",
        joinColumns = @JoinColumn(name = "fournisseur_id"),
        inverseJoinColumns = @JoinColumn(name = "categorie_code"))
    private List<Categorie> categories = new LinkedList<>();
}
