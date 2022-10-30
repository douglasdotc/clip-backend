package Clips.backend.clip.db;

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

    @JsonProperty("docID")
    private String docId;

    @JsonProperty("uid")
    private String uid;

    @JsonProperty("displayName")
    private String displayName;

    @JsonProperty("title")
    private String title;

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("url")
    private String url;

    @JsonProperty("screenshotURL")
    private String screenshotUrl;

    @JsonProperty("screenshotFileName")
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