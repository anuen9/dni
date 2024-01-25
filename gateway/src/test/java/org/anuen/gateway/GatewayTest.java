package org.anuen.gateway;

import org.anuen.gateway.utils.JwtTool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GatewayTest {
    @Autowired
    private JwtTool jwtTool;

    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1c2VyVWlkIjoiYTQ0ZGNhMGYtMDkzZS00MzgzLTlkYjEtYzMzMzYxMTFjYzhmIiwiaWF0IjoxNzA2MTY2NTE1LCJleHAiOjE3MDYxNjgzMTV9.qSa9uOqUYZcS6bqM1ZX8SX6_a2D_mDYobn5EY7_n8fbTqGr6lgIbtGLmIUFi01WtJi5eyx488ndAL3Il9CPpxrjNhpgXewaD1SKMdz_wLiLMoQAHYEfV_wdbG5-IW2f-HYBudodVdpJl94Rk7LCsp_5YGxBnr5gWJkJOd-khTBt2uU8buUhZwi0u2tApWwNioc3pZjPXXdSn-VobhIPirW3yZ3AqY8BIn3q_elsQy_MGxbDJHhW4vWHKZomX4PuYVB_LayC0yA_RaSN09yZT9EZGYk2CZoeCv-ur5gU1h3R-WDPMw63X0vDdKpvcNsxxRakZAI4hDi-mb4MZ6HDphw";


    private String errorToken = "eyJ0eXAiOiJKV1iOiJSUzI1NiJ9.eyJVc2VyVWlkIjoiYTQ0ZGNhMGYtMDkzZS00MzgzLTlkYjEtYzMzMzYxMTFjYzhmIiwiaWF0IjoxNzA2MTYzOTUyLCJleHAiOjE3MDYxNjU3NTJ9.B4EwXfClWLNSHcsYEfiA4EWUN8BEmfKjHmjtv0klcusZaKW84CtsrNVCyRIkXA9xQXImNbveaRZKDkHbqZ7TefBw0YEomIJdA-Ffb6g5pFgiUFnRm6Txehv9PsWY0Gg8lyg4GkZACQF3N3hELUOOho0m8_d_rspU66KasXVKA3lcH8OK5_3EwFNbneb9imVwfGC-VgVXre4mBQzsDbRYw93er-S-Lk3uSts7WeWXyi5M_oHtFF0wRNwv1afdz-iiJ_H1MdXeFzNettuerENuoZYXNzMALCU0JfJXiBtkQDrQLmmDUEP5XBRaX-NFPcqv91FPOE1RMaMpZ-sA-nEaWw";

    @Test
    public void testJwtTool() {
        String token = jwtTool.generateToken("a44dca0f-093e-4383-9db1-c3336111cc8f");
        System.out.println("token = " + token);
    }

    @Test
    public void test2() {
        boolean tokenExpired = jwtTool.isTokenExpired(token);
        System.out.println("tokenExpired = " + tokenExpired);
    }

    @Test
    public void test3() {
        boolean tokenExpired = jwtTool.isTokenExpired(errorToken);
        System.out.println("tokenExpired = " + tokenExpired);
    }

    @Test
    public void test4() {
        String uid = jwtTool.parseToken(errorToken);
        System.out.println("uid = " + uid);
    }



}
