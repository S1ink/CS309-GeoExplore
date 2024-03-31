package test.connect.geoexploreapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import test.connect.geoexploreapp.model.ReportMarker;

public interface ReportMarkerApi {

    //[C]rudl - Add a new report to the database
    @POST("geomap/reports")
    Call<ReportMarker> addReport(@Body ReportMarker reportMarker);

    // c[R]udl - Get an report from the database by its id
    @GET("geomap/reports/{id}")
    Call<ReportMarker> getReportById(@Path("id") Long id);

    // cr[U]dl - Update an report already in the database by it's id
    @PUT("geomap/reports/{id}")
    Call<ReportMarker> updateReportById(@Path("id") Long id, @Body ReportMarker reportMarker);

    // cru[D]l - Delete an report in the database by it's id
    @DELETE("geomap/reports/{id}")
    Call<Void> deleteReportById(@Path("id") Long id);

    // crud[L] - Get a list of all the reports in the database
    @GET("geomap/reports")
    Call<List<ReportMarker>> GetAllReportMarker();
}

/*

    Backend operations to reference

	@PostMapping(path = "geomap/reports/add")
	public @ResponseBody ReportEntity.JsonFormat saveReport(@RequestBody ReportEntity.JsonFormat report_json) {
		if(report_json != null) {
			// successful parse --> convert and insert to repo (DB)
			final ReportEntity saved = db_reports.save(ReportEntity.fromJson(report_json));
			return ReportEntity.formatJson(saved);
		}
		return null;
	}

	@GetMapping(path = "geomap/reports/{id}")
	public @ResponseBody ReportEntity.JsonFormat getReportById(@PathVariable Long id) {
		if(id != null) {
			try {
				return ReportEntity.formatJson(this.db_reports.findById(id).get());
			} catch(Exception e) {
				// continue >>>
			}
		}
		return null;
	}

	@PutMapping(path = "geomap/reports/{id}/update")
	public @ResponseBody ReportEntity.JsonFormat updateReportById(@PathVariable Long id, @RequestBody ReportEntity.JsonFormat report_json) {
		// custom query? :|
		return null;
	}

	@DeleteMapping(path = "geomap/reports/{id}/delete")
	public @ResponseBody ReportEntity.JsonFormat deleteReportById(@PathVariable Long id) {
		if(id != null) {
			try {
				final ReportEntity.JsonFormat ref = this.getReportById(id);
				this.db_reports.deleteById(id);
				return ref;
			} catch(Exception e) {
				// continue >>>
			}
		}
		return null;
	}

	@GetMapping(path = "/points")
	public @ResponseBody List<PointEntity> getPoints() {
		return geo_repo.findAll();
	}



	@GetMapping(path = "geomap/reports")
	public @ResponseBody List<ReportEntity.JsonFormat> getAllReports() {
		final List<ReportEntity> reports = db_reports.findAll();
		final ArrayList<ReportEntity.JsonFormat> formatted_list = new ArrayList<>();
		for(ReportEntity r : reports) {
			formatted_list.add(ReportEntity.formatJson(r));
		}
		return formatted_list;
	}

 */