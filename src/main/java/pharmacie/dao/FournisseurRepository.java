package pharmacie.dao;

import org.springframework.data.jpa.repository.Query;

import pharmacie.entity.Fournisseur;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FournisseurRepository extends JpaRepository<Fournisseur, Long> {
    
    // Trouver les fournisseurs capables de fournir une catégorie donnée
    @Query("SELECT f FROM Fournisseur f JOIN f.categories c WHERE c.id = :categorieId")
    List<Fournisseur> findByCategorieId(Long categorieId);
}
