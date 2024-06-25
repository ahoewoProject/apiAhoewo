package com.memoire.apiAhoewo.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class PaydunyaConfig {
    @Autowired
    private Environment environment;

//    @Bean
//    public PaydunyaSetup paydunyaSetup() {
//        PaydunyaSetup setup = new PaydunyaSetup();
//        setup.setMasterKey(environment.getProperty("paydunya.masterKey"));
//        setup.setPublicKey(environment.getProperty("paydunya.publicKey"));
//        setup.setPrivateKey(environment.getProperty("paydunya.privateKey"));
//        setup.setToken(environment.getProperty("paydunya.token"));
//        setup.setMode(environment.getProperty("paydunya.mode"));
//        return setup;
//    }
//
//    @Bean
//    public PaydunyaCheckoutStore paydunyaStore() {
//        PaydunyaCheckoutStore store = new PaydunyaCheckoutStore();
//        store.setName("ahoewo");
//        store.setLogoUrl("src/main/resources/ahoe-min.png");
//        return store;
//    }
}

