package org.anuen.gateway.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.anuen.common.enums.ExceptionMessage;
import org.anuen.common.exception.UnauthorizedException;
import org.anuen.gateway.config.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
@EnableConfigurationProperties(JwtProperties.class)
public class JwtTool {

    private final JwtProperties jwtProperties;

    private final JWTSigner signer;

    private static final String USER_UID_KEY = "userUid";

    public JwtTool(
            JwtProperties properties,
            KeyPair keyPair) {
        this.jwtProperties = properties;
        this.signer = JWTSignerUtil.createSigner(properties.getAlgorithm(), keyPair);
    }

    public String generateToken(String userUid) {
        return generateToken(new HashMap<>(), userUid);
    }

    /**
     * generate token
     *
     * @param extraClaims extra claims
     * @param userUid     user's uuid
     * @return token
     */
    public String generateToken(Map<String, Object> extraClaims, String userUid) {
        return JWT.create()
                .setPayload(USER_UID_KEY, userUid)
                .addPayloads(extraClaims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getTokenTTL().toMillis()))
                .setSigner(signer)
                .sign();
    }

    /**
     * extract all info from token
     *
     * @param token string token
     * @return jwt
     */
    public JWT extractAll(String token) {
        return JWT.of(token).setSigner(signer);
    }


    /**
     * judge token is valid or not
     *
     * @param token string token
     * @return yes or no
     */
    public String parseToken(String token) {
        // check token is not blank and valid
        if (StrUtil.isBlank(token) || isTokenInvalid(token)) {
            throw new UnauthorizedException(ExceptionMessage.INVALID_TOKEN.getMessage());
        }
        // check token is not expired
        if (isTokenExpired(token)) {
            throw new UnauthorizedException(ExceptionMessage.TOKEN_EXPIRED.getMessage());
        }
        // extract all information from token, and get payload called 'userUid'
        JWT jwt = extractAll(token);
        Object userUid = jwt.getPayload(USER_UID_KEY);
        if (Objects.isNull(userUid)) {
            throw new UnauthorizedException(ExceptionMessage.INVALID_TOKEN.getMessage());
        }
        return userUid.toString();

    }

    /**
     * judge token is invalid or not
     *
     * @param token string token
     * @return yes or no
     */
    public boolean isTokenInvalid(String token) {
        try {
            // extract info from token then check it
            return !extractAll(token).verify();
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * judge token is expired or not
     *
     * @param token string token
     * @return yes or no
     */
    public boolean isTokenExpired(String token) {
        try {
            JWTValidator.of(extractAll(token)).validateDate();
        } catch (Exception e) {
            return true;
        }
        return false;
    }

}
