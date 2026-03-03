package pharmacie.service;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import pharmacie.entity.Medicament;
import pharmacie.dao.MedicamentRepository;
import pharmacie.dto.EmailRequest;
import pharmacie.entity.Fournisseur;
import pharmacie.entity.Categorie;
import pharmacie.rest.EmailController;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ReapprovisionnementService {

    @Autowired
    private MedicamentRepository medicamentRepository;
    @Autowired
    private EmailController emailController;

    public Map<String, String> lancerReapprovisionnement() {

        List<Medicament> medicamentsAReappro =
            medicamentRepository.medicamentsReappro();

        if (medicamentsAReappro.isEmpty()) {
            return Map.of("message", "Aucun médicament à réapprovisionner.");
        }

        // Grouper les médicaments par catégorie
        Map<Categorie, List<Medicament>> parCategorie = medicamentsAReappro.stream()
        .collect(Collectors.groupingBy(Medicament::getCategorie));


        Map<Fournisseur, Map<Categorie, List<Medicament>>> parFournisseur = new HashMap<>();

        for (Map.Entry<Categorie, List<Medicament>> entry : parCategorie.entrySet()) {
            Categorie categorie = entry.getKey();
            List<Medicament> meds = entry.getValue();
            for (Fournisseur fournisseur : categorie.getFournisseurs()) {
                parFournisseur
                    .computeIfAbsent(fournisseur, k -> new HashMap<>())
                    .put(categorie, meds);
            }
        }

        // Envoyer un seul mail par fournisseur
        Map<String, String> resultats = new HashMap<>();
        for (Map.Entry<Fournisseur, Map<Categorie, List<Medicament>>> entry
                : parFournisseur.entrySet()) {

            Fournisseur fournisseur = entry.getKey();
            String corps = construireCorpsMail(fournisseur, entry.getValue());

            EmailRequest req = new EmailRequest(
                fournisseur.getAdresseelec(),
                "Demande de devis de réapprovisionnement",
                corps
            );

            ResponseEntity<Map<String, String>> resp = emailController.sendEmail(req);
            resultats.put(fournisseur.getAdresseelec(),
                resp.getStatusCode().is2xxSuccessful() ? "Envoyé" : "Échec");
        }

        return resultats;
    }

    private String construireCorpsMail(Fournisseur f, Map<Categorie, List<Medicament>> parCategorie) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bonjour ").append(f.getNom()).append(",\n\n");
        sb.append("Nous sollicitons un devis pour les médicaments suivants :\n\n");

        for (Map.Entry<Categorie, List<Medicament>> entry : parCategorie.entrySet()) {
            sb.append("-> Catégorie : ").append(entry.getKey().getLibelle()).append(" \n");
            for (Medicament m : entry.getValue()) {
                sb.append("  - ").append(m.getNom())
                  .append(" | Stock : ").append(m.getUnitesEnStock())
                  .append(" | Seuil : ").append(m.getNiveauDeReappro())
                  .append("\n");
            }
            sb.append("\n");
        }

        sb.append("Merci de nous transmettre votre devis.\nCordialement.");
        return sb.toString();
    }
}