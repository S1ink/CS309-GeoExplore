package hb403.geoexplore.test;

import org.springframework.core.style.ToStringCreator;


public class TestData {
	
	public double
		latitude,
		longitude;
	public String
		title,
		description;
	public int
		creator_uid;

	@Override
	public String toString() {
		return new ToStringCreator(this)
			.append("lat", this.latitude)
			.append("long", this.longitude)
			.append("title", this.title)
			.append("desc", this.description)
			.append("creator_id", this.creator_uid)
		.toString();
	}

}
