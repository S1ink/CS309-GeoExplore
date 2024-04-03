package test.connect.geoexploreapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlertMarker {
    private long id;
    private String title;
    private String description;
    private double io_latitude;
    private double io_longitude;
    private User creator;
    private Long time_created;
    private Long time_updated;
    private String meta;
    private List<MarkerTag> tags;

    public AlertMarker() {
        this.tags = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getIo_latitude() {
        return io_latitude;
    }

    public void setIo_latitude(double io_latitude) {
        this.io_latitude = io_latitude;
    }

    public double getIo_longitude() {
        return io_longitude;
    }

    public void setIo_longitude(double io_longitude) {
        this.io_longitude = io_longitude;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Long getTime_created() {
        return time_created;
    }

    public void setTime_created(Long time_created) {
        this.time_created = time_created;
    }

    public Long getTime_updated() {
        return time_updated;
    }

    public void setTime_updated(Long time_updated) {
        this.time_updated = time_updated;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public List<MarkerTag> getTags() {
        return tags;
    }

    public void setTags(List<MarkerTag> tags) {
        this.tags = tags;
    }
}
