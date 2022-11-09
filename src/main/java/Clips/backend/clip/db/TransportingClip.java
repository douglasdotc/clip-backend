package Clips.backend.clip.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TransportingClip {
    @JsonProperty("uid")
    private final String uid;
    @JsonProperty("displayName")
    private final String displayName;
    @JsonProperty("title")
    private final String title;
    @JsonProperty("fileName")
    private final String fileName;
    @JsonProperty("url")
    private final String url;
    @JsonProperty("screenshotURL")
    private final String screenshotUrl;
    @JsonProperty("screenshotFileName")
    private final String screenshotFileName;
}
