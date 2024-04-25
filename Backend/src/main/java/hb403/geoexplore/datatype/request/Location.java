package hb403.geoexplore.datatype.request;


public class Location {
	
	public Double
		latitude,
		longitude;

	public boolean isValid() {
		return (
			this.latitude != null &&
			this.longitude != null
		);
	}
	public boolean isInvalid() {
		return (
			this.latitude == null ||
			this.longitude == null
		);
	}

}
