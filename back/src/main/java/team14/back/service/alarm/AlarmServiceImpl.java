package team14.back.service.alarm;

import lombok.AllArgsConstructor;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import team14.back.enumerations.LogAction;
import team14.back.repository.LogRepository;

@Service
@AllArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final LogRepository logRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    private KieContainer getKieContainer(){
        KieServices ks = KieServices.Factory.get();
        return ks.newKieClasspathContainer();
    }

    @Override
    public void checkForRedundantCSRRequest() {
        KieSession ksession = getKieContainer().newKieSession("cepKsession");
        ksession.setGlobal("logActionType", LogAction.CREATING_NEW_CSR_REQUEST);
        ksession.setGlobal("simpMessagingTemplate", simpMessagingTemplate);
        this.logRepository.findAll().forEach(ksession::insert);
        ksession.fireAllRules();
    }
}
