package hb403.geoexplore.controllers;

import hb403.geoexplore.datatype.map.items.ReportEntity;
import hb403.geoexplore.datatype.map.items.ReportRepository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class ReportController {
	
	@Autowired
	protected ReportRepository db_reports;


	@PostMapping(path = "geomap/reports/add")
	public @ResponseBody ReportEntity.JsonFormat saveReport(@RequestBody ReportEntity.JsonFormat report_json) {
		if(report_json != null) {
			// successful parse --> convert and insert to repo (DB)
			final ReportEntity saved = db_reports.save(ReportEntity.fromJson(report_json));
			return ReportEntity.formatJson(saved);
		}
		return null;
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


}
