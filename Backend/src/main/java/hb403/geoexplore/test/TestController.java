package hb403.geoexplore.test;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;


@RestController
public class TestController {
	
	@PostMapping(path = "/test/add")
	public @ResponseBody TestData testPost(@RequestBody TestData data) {
		if(data == null) {
			System.out.println("Test Serialization failed!");
			return null;
		} else {
			System.out.println("Test Serialization succeeded!:\n" + data.toString());
			return data;
		}
	}

	@GetMapping(path = "/test/generate")
	public @ResponseBody TestData testGet() {
		final TestData x = new TestData();
		x.latitude = x.longitude = 100.0;
		x.title = "test data";
		x.description = "test description";
		x.creator_uid = -1;
		return x;
	}


}
