package team14.back.config;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class DRoolsConfig {

    @Bean
    public KieContainer kieContainer(){
        KieServices ks = KieServices.Factory.get();
        return ks.newKieClasspathContainer();
    }

    @Bean
    public Map<String, KieSession> kieSessionsMap() {
        return new ConcurrentHashMap<>();
    }

    public KieSession getOrCreateKieSession(String kieSessionName) {
        return kieSessionsMap().computeIfAbsent(kieSessionName, kieContainer()::newKieSession);
    }

    public void clearKieSession(KieSession kieSession) {
        for (FactHandle factHandle : kieSession.getFactHandles()) {
            kieSession.retract(factHandle);
        }
    }
}
