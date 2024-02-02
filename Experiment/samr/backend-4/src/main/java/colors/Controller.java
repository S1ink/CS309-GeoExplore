package colors;

import java.util.Random;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/generator")
public class Controller {

	protected static String formatHtmlPageColor(String color) {
		return String.format("<!DOCTYPE html><html><body style=\"background-color:%s;\"></body></html>", color);
	}
	protected static String formatHtmlPageColor(int r, int g, int b) {
		r %= 255;
		g %= 255;
		b %= 255;
		return formatHtmlPageColor(String.format("rgb(%d,%d,%d)", r, g, b));
	}

	
	@GetMapping(path = "/{color}")
	public @ResponseBody String createColorPage(@PathVariable String color) {
		return formatHtmlPageColor(color);
	}

	@GetMapping(path = "/random")
	public @ResponseBody String getRandomColorPage() {
		final Random r = new Random();
		return formatHtmlPageColor(
			r.nextInt(255),
			r.nextInt(255),
			r.nextInt(255)
		);
	}

}
