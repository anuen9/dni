package org.anuen.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anuen.common.entity.ResponseEntity;
import org.anuen.common.enums.ResponseStatus;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * You can parse the list-type data in the response body
     * By calling this method and passing in the response body and element type.
     *
     * @param response   response entity from remote service invoke
     * @param elementClz element type of list-type data in response
     * @param <T>        auto
     * @return a list of elementClz
     */
    @Nullable
    public <T> List<T> getRespDataOfList(@NonNull ResponseEntity<?> response, @NonNull Class<T> elementClz) {
        if (!ResponseStatus.SUCCESS.getCode().equals(response.getCode())) {
            log.error("---> Remote invoke fail!\n");
            return null;
        }

        Object data = response.getData();
        if (Objects.nonNull(data)) {
            List<T> res = new ArrayList<>();
            if (data instanceof List<?>) {
                for (Object element : (List<?>) data) {
                    res.add(elementClz.cast(element));
                }
            }
            return res;
        }

        return null;
    }

    /**
     * You can tell whether the remote service call was successful
     * By calling this method and passing in the response。
     *
     * @param response response entity from remote provider call
     * @return false -> fail / true -> success
     */
    @NonNull
    public Boolean isInvokeSuccess(@NonNull ResponseEntity<?> response) {
        try {
            if (!ResponseStatus.SUCCESS.getCode().equals(response.getCode())) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error("""
                    ---> remote invoke fail
                    """);
            return Boolean.FALSE;
        }
    }
}
