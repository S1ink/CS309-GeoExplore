package hb403.geoexplore.datatype;

import hb403.geoexplore.datatype.marker.ObservationMarker;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Observation_images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "file_path")
    @Getter
    @Setter
    private String filePath;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "marker_id")
    private ObservationMarker observation;


}
