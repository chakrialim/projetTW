package pharmacie.service;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import pharmacie.dao.*;
import pharmacie.entity.*;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
@Service
@Validated

public class MedicamentService {
    
    @Autowired
    private final MedicamentRepository medicamentDao;

    public MedicamentService(MedicamentRepository medicamentDao) {
        this.medicamentDao = medicamentDao;
    }

    public List<Medicament> medicamentsAReapprovisionnes() {
        return medicamentDao.medicamentsReappro();
    }

    
}
