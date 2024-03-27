package hb403.geoexplore.controllers;

import hb403.geoexplore.datatype.marker.ReportMarker;
import hb403.geoexplore.datatype.marker.repository.ReportRepository;
import hb403.geoexplore.util.GeometryUtil;

import java.util.*;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class ReportController {
	
	@Autowired
	protected ReportRepository reports_repo;


	/** [C]rudl - Add a new report to the database */
	@Operation(summary = "Add a new report to the database")
	@PostMapping(path = "geomap/reports")
	public @ResponseBody ReportMarker saveReport(@RequestBody ReportMarker report) {
		if(report != null) {
			report.nullifyId();
			report.enforceLocationIO();
			return this.reports_repo.save(report);
		}
		return null;
	}
	/** c[R]udl - Get a report from the database by it's id */
	@Operation(summary = "Get a report from the database from its id")
	@GetMapping(path = "geomap/reports/{id}")
	public @ResponseBody ReportMarker getReportById(@PathVariable Long id) {
		if(id != null) {
			try {
				final ReportMarker r = this.reports_repo.findById(id).get();
				r.enforceLocationTable();
				return r;
			} catch(Exception e) {
				// continue >>> (return null)
			}
		}
		return null;
	}
	/** cr[U]dl - Update a report already in the database by it's id */
	@Operation(summary = "Update a report already in the database by its id")
	@PutMapping(path = "geomap/reports/{id}")
	public @ResponseBody ReportMarker updateReportById(@PathVariable Long id, @RequestBody ReportMarker report) {
		if(id != null && report != null) {
			report.setId(id);
			report.enforceLocationIO();
			return this.reports_repo.save(report);
		}
		return null;
	}
	/** cru[D]l - Delete a report in the database by it's id */
	@Operation(summary = "Delete a report in the database by its id")
	@DeleteMapping(path = "geomap/reports/{id}")
	public @ResponseBody ReportMarker deleteReportById(@PathVariable Long id) {
		if(id != null) {
			try {
				final ReportMarker ref = this.getReportById(id);
				this.reports_repo.deleteById(id);
				ref.enforceLocationTable();
				return ref;
			} catch(Exception e) {
				// continue >>> (return null)
			}
		}
		return null;
	}

	/** crud[L] - Get a list of all the reports in the database */
	@Operation(summary = "Get a list of all the reports in the database")
	@GetMapping(path = "geomap/reports")
	public @ResponseBody List<ReportMarker> getAllReports() {
		final List<ReportMarker> reports = this.reports_repo.findAll();
		for(ReportMarker r : reports) {
			r.enforceLocationTable();
		}
		return reports;
	}



	/** Returns the list of reports within the bounds generated by the provided WKT geometry string */
	@Operation(summary = "Get the list of reports whose locations are bounded by the provided WKT geometry string")
	@GetMapping(path = "geomap/reports/within")
	public @ResponseBody List<ReportMarker> getReportsWithinBounds(@RequestBody String wkt_bounds_geom) {	// takes in 'well known text' for the bounding geometry --> may define special json formats for predefined bounds later
		try {
			final List<ReportMarker> bounded = this.reports_repo.findWithin( GeometryUtil.getGeometry(wkt_bounds_geom) );
			for(ReportMarker r : bounded) {
				r.enforceLocationTable();
			}
			return bounded;
		} catch(Exception e) {
			System.out.println("ReportMarker.getReportsWithinBounds(): Encountered exception! -- " + e.getMessage());
			// continue >>> (return null)
		}
		return null;
	}



	/** TODO:
     * - get marker creator (User) by marker id
     * - get marker tags (MarkerTag[]) by marker id
     * - append [NEW] marker tag to list, accessed by marker id
     * - append [EXISTING] marker tag to list, accessed by marker id and tag id
     * - append User to marker "listed users" by marker id and user id
     */


}
