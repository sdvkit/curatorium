package com.sdv.kit.server.service;

import com.nimbusds.jose.jwk.RSAKey;

public interface RsaKeyGeneratorService {
    RSAKey generate();
}
