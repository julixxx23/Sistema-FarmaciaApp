package farmacias.AppOchoa.serviceimpl;

import farmacias.AppOchoa.repository.CajaRepository;
import farmacias.AppOchoa.services.CajaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CajaServiceImpl implements CajaService {
    private final CajaRepository repository;

}
