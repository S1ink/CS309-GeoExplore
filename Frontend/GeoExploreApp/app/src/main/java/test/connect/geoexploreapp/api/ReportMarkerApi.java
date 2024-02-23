package test.connect.geoexploreapp.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import test.connect.geoexploreapp.model.ReportMarker;

public interface ReportMarkerApi {

    @GET("geomap/reports")
    Call<List<ReportMarker>> GetAllReportMarker();

//    @POST("geomap/reports/add")
//    Call<ReportMarker> PostReportMarker()


}

/*

    Backends CRUDL

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