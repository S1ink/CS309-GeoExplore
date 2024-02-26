package hb403.geoexplore.controllers;

import java.util.ArrayList;
import java.util.List;

import hb403.geoexplore.datatype.map.items.EventEntity;
import hb403.geoexplore.datatype.map.items.EventRepository;
import hb403.geoexplore.datatype.map.items.ReportEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class EventController {
	
	@Autowired
	protected EventRepository db_events;


	/** [C]rudl - Add a new event to the database */
	@PostMapping(path = "geomap/events")
	public @ResponseBody EventEntity.JsonFormat saveEvent(@RequestBody EventEntity.JsonFormat event_json) {
		if(event_json != null) {
			// successful parse --> convert and insert to repo (DB)
			EventEntity e = EventEntity.fromJson(event_json);
			e.nullId();
			final EventEntity saved = this.db_events.save(e);
			return EventEntity.formatJson(saved);
		}
		return null;
	}
	/** c[R]udl - Get an event from the database by its id */
	@GetMapping(path = "geomap/events/{id}")
	public @ResponseBody EventEntity.JsonFormat getEventById(@PathVariable Long id) {
		if(id != null) {
			try {
				return EventEntity.formatJson(this.db_events.findById(id).get());
			} catch(Exception e) {
				// continue >>> (return null)
			}
		}
		return null;
	}
	/** cr[U]dl - Update an event already in the database by it's id */
	@PutMapping(path = "geomap/events/{id}")
	public @ResponseBody EventEntity.JsonFormat updateEventById(@PathVariable Long id, @RequestBody EventEntity.JsonFormat event_json) {
		if(id != null && event_json != null) {
			final EventEntity e = EventEntity.fromJson(event_json);
			e.setId(id);	// repository will overwrite whatever entity already exists with this id --> optionally enforce only updating entities that already exist?
			return EventEntity.formatJson(this.db_events.save(e));
		}
		return null;
	}
	/** cru[D]l - Delete an event in the database by it's id */
	@DeleteMapping(path = "geomap/events/{id}")
	public @ResponseBody EventEntity.JsonFormat deleteEventById(@PathVariable Long id) {
		if(id != null) {
			try {
				final EventEntity.JsonFormat ref = this.getEventById(id);
				this.db_events.deleteById(id);
				return ref;
			} catch(Exception e) {
				// continue >>> (return null)
			}
		}
		return null;
	}

	/** crud[L] - Get a list of all the events in the database */
	@GetMapping(path = "geomap/events")
	public @ResponseBody List<EventEntity.JsonFormat> getAllEvents() {
		final List<EventEntity> events = this.db_events.findAll();
		final ArrayList<EventEntity.JsonFormat> formatted_list = new ArrayList<>();
		for(EventEntity e : events) {
			formatted_list.add(EventEntity.formatJson(e));
		}
		return formatted_list;
	}


}
