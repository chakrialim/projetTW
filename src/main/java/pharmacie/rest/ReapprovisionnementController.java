package pharmacie.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pharmacie.dao.MedicamentRepository;
import pharmacie.entity.Medicament;
import pharmacie.service.ReapprovisionnementService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reapprovisionnement")
public class ReapprovisionnementController {

    @Autowired
    private ReapprovisionnementService reapprovisionnementService;

    @Autowired
    private MedicamentRepository medicamentRepository;

    /**
     * Déclenche la vérification des stocks et l'envoi des mails aux fournisseurs.
     * GET /api/reapprovisionnement/lancer
     */
    @GetMapping("/lancer")
    public ResponseEntity<Map<String, String>> lancerReapprovisionnement() {
        Map<String, String> resultats = 
            reapprovisionnementService.lancerReapprovisionnement();
        return ResponseEntity.ok(resultats);
    }

    /**
     * Consulter la liste des médicaments à réapprovisionner sans envoyer de mail.
     * GET /api/reapprovisionnement/medicaments
     */
    @GetMapping("/medicaments")
    public ResponseEntity<List<Medicament>> getMedicamentsAReapprovisionner() {
        return ResponseEntity.ok(
            medicamentRepository.medicamentsReappro()
        );
    }
}

