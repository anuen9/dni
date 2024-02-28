package org.anuen.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.enums.ResponseStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class RPCRespResolver {

    /**
     * you can provide a ResponseEntity and a Class into this method
     * then you will get a result type of Class from response.
     *
     * @param response responseEntity from remote invoke
     * @param clz      the result type you want
     * @param <T>      auto
     * @return data in resp type of clz
     */
    @Nullable
    public <T> T getRespData(@NonNull ResponseEntity<?> response, @NonNull Class<T> clz) {
        if (!ResponseStatus.SUCCESS.getCode().equals(response.getCode())) { // check rpc are success?
            log.error("---> Remote invoke fail!\n");
            return null;
        }
        Object data = response.getData();
        if (Objects.nonNull(data)) {
            try { // trying to cast data -> clz
                return clz.cast(data);
            } catch (Exception e) {
                log.error("---> Cast exception\n", e);
                return null;
            }
        }
        return null;
    }
}
