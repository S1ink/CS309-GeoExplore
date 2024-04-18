package hb403.geoexplore;

import hb403.geoexplore.UserStorage.LocationSharing;
import hb403.geoexplore.UserStorage.entity.User;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.boot.test.web.server.LocalServerPort;	// SBv3


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class SamSystemTest {
	
	@LocalServerPort
	int port;

	private User test_user = null;
	private long test_user_id = -1L;
	private Response post_test_response = null;

	@Before
	public void setUp() {

		RestAssured.port = port;
		RestAssured.baseURI = "http://localhost";

		this.test_user = new User();
		this.test_user.setId(-1L);
		this.test_user.setName("NEW USER (AUTOMATED TEST)");
		this.test_user.setEmailId("email@iastate.edu");
		this.test_user.setPassword("87fdj203dxcjk392");
		this.test_user.setLocation_privacy(LocationSharing.PUBLIC);
		this.test_user.setIo_latitude(10.0);
		this.test_user.setIo_longitude(-10.0);
		this.test_user.setLast_location_update(new Date());

		// we have to pre-post since we need to user id for subsequent accesses
		this.post_test_response = 
			RestAssured
				.given()
				.contentType("application/json")
				.body(this.test_user)
				.post("/user/create");

		try {
			this.test_user_id = this.post_test_response.getBody().as(User.class).getId();
		} catch(Exception e) {
			this.test_user_id = -1L;	// fail
		}

	}


	private static void assertUsersEqual(User a, User b) {
		assertTrue(		a.getName()					.equals( b.getName() )					);
		assertTrue(		a.getEmailId()				.equals( b.getEmailId() )				);
		assertTrue(		a.getPassword()				.equals( b.getPassword() )				);
		assertEquals(	a.getLocation_privacy(), 	b.getLocation_privacy()					);
		assertEquals(	a.getIo_latitude(), 		b.getIo_latitude()						);
		assertEquals(	a.getIo_longitude(), 		b.getIo_longitude()						);
		// assertTrue(		a.getLast_location_update()	.equals( b.getLast_location_update() )	);
	}

	// @Test
	public void postUserTest() {

		assertEquals(200, this.post_test_response.getStatusCode());
		// System.out.println(this.post_test_response.getBody().asPrettyString());
		final User u = this.post_test_response.getBody().as(User.class);

		assertTrue(this.test_user_id >= 0);
		SamSystemTest.assertUsersEqual(this.test_user, u);

	}

	// @Test
	public void getUserTest() {

		assertTrue(this.test_user_id >= 0);

		final Response resp =
			RestAssured.get("/user/{id}", this.test_user_id);

		assertEquals(200, resp.getStatusCode());
		final User u = resp.getBody().as(User.class);

		assertEquals(this.test_user_id, u.getId());
		SamSystemTest.assertUsersEqual(this.test_user, u);

	}

	// @Test
	public void updateUserTest() {

		assertTrue(this.test_user_id >= 0);

		this.test_user.setName("UPDATED USER (AUTOMATED TEST)");
		this.test_user.setEmailId("updated@iastate.edu");
		this.test_user.setPassword("f908fds098f90d");
		this.test_user.setLocation_privacy(LocationSharing.GROUP);
		this.test_user.setIo_latitude(-25.0);
		this.test_user.setIo_longitude(25.0);
		this.test_user.setLast_location_update(new Date());

		final Response resp =
			RestAssured
				.given()
				.contentType("application/json")
				.body(this.test_user)
				.put("/user/{id}/update", this.test_user_id);

		assertEquals(200, resp.getStatusCode());
		// System.out.println(resp.getBody().asPrettyString());
		final User u = resp.getBody().as(User.class);

		assertEquals(this.test_user_id, u.getId());
		SamSystemTest.assertUsersEqual(this.test_user, u);

	}

	// @Test
	public void deleteUserTest() {

		assertTrue(this.test_user_id >= 0);

		final Response resp =
			RestAssured.delete("/user/{id}/delete", this.test_user_id);

		assertEquals(200, resp.getStatusCode());
		final User u = resp.getBody().as(User.class);

		assertEquals(this.test_user_id, u.getId());
		SamSystemTest.assertUsersEqual(this.test_user, u);

		final Response verify =
			RestAssured.get("/user/{id}", this.test_user_id);

		assertEquals(200, verify.getStatusCode());
		try {
			final User v = verify.getBody().as(User.class);
			assertEquals(null, v);
		} catch(Exception e) {
			// this is also fine
		}

	}


	@Test
	public void testSequential() {
		this.postUserTest();
		this.getUserTest();
		this.updateUserTest();
		this.deleteUserTest();
	}


}
