package cl.perfulandia.ms_orders_bff;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
	"ms-orders-bs.url=http://localhost:8081"
})
class MsOrdersBffApplicationTests {

	@Test
	void contextLoads() {
		// This test ensures that the Spring context loads properly
		// with all the configurations and dependencies
	}

}
