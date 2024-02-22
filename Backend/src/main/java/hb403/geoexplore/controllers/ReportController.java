package hb403.geoexplore.controllers;

import java.util.ArrayList;
import java.util.List;

import hb403.geoexplore.datatype.map.items.ReportEntity;
import hb403.geoexplore.datatype.map.items.ReportRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class ReportController {
	
	@Autowired
	protected ReportRepository db_reports;


	/** [C]rudl - Add a new report to the database */
	@PostMapping(path = "geomap/reports/add")
	public @ResponseBody ReportEntity.JsonFormat saveReport(@RequestBody ReportEntity.JsonFormat report_json) {
		if(report_json != null) {
			// successful parse --> convert and insert to repo (DB)
			final ReportEntity saved = db_reports.save(ReportEntity.fromJson(report_json));
			return ReportEntity.formatJson(saved);
		}
		return null;
	}
	/** c[R]udl - Get a report from the database by it's id */
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
	/** cr[U]dl - Update a report already in the database by it's id */
	@PutMapping(path = "geomap/reports/{id}/update")
	public @ResponseBody ReportEntity.JsonFormat updateReportById(@PathVariable Long id, @RequestBody ReportEntity.JsonFormat report_json) {
		// custom query? :|
		return null;
	}
	/** cru[D]l - Delete an evetn in the database by it's id */
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

	/** crud[L] - Get a list of all the reports in the database */
	@GetMapping(path = "geomap/reports")
	public @ResponseBody List<ReportEntity.JsonFormat> getAllReports() {
		final List<ReportEntity> reports = db_reports.findAll();
		final ArrayList<ReportEntity.JsonFormat> formatted_list = new ArrayList<>();
		for(ReportEntity r : reports) {
			formatted_list.add(ReportEntity.formatJson(r));
		}
		return formatted_list;
	}


}
