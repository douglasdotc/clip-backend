package Clips.backend.clip;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Clip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;

    @JsonProperty("doc_id")
    private String docId;

    @JsonProperty("uid")
    private String uid;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("title")
    private String title;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("url")
    private String url;

    @JsonProperty("screenshot_url")
    private String screenshotUrl;

    @JsonProperty("screenshot_file_name")
    private String screenshotFileName;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    public Clip(
        String uid,
        String displayName,
        String title,
        String fileName,
        String url,
        String screenshotUrl,
        String screenshotFileName
    ) {
        this.uid = uid;
        this.displayName = displayName;
        this.title = title;
        this.fileName = fileName;
        this.url = url;
        this.screenshotUrl = screenshotUrl;
        this.screenshotFileName = screenshotFileName;
    }
}