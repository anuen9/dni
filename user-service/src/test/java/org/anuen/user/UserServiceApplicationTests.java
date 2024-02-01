package org.anuen.user;

import org.anuen.api.client.AuthClient;
import org.anuen.common.entity.ResponseEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceApplicationTests {

	@Autowired
	private AuthClient authClient;

	@Test
	void contextLoads() {
		ResponseEntity<?> resp = authClient.token("a1608b8f-7be3-4005-be56-5412d2f97402");
		String token = resp.getData().toString();
		System.out.println("token = " + token);
	}

}
