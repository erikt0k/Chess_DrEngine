package ru.sfedu.chess.api;

import org.kie.api.KieServices;
import org.kie.api.builder.*;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;


public class RuleManager {
    public KieSession createSession() {
        KieServices kieServices = KieServices.Factory.get();
        Resource dt = ResourceFactory.newClassPathResource("game.drl");
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem().write(dt);
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieBuilder.buildAll();
        KieRepository kieRepository = kieServices.getRepository();
        ReleaseId krDefaultReleaseId = kieRepository.getDefaultReleaseId();
        KieContainer kieContainer
                = kieServices.newKieContainer(krDefaultReleaseId);
        return kieContainer.newKieSession();
    }
}