package Clips.backend.clip;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class NewClipRequest {
    @JsonProperty("uid")
    private final String uid;
    @JsonProperty("display_name")
    private final String displayName;
    @JsonProperty("title")
    private final String title;
    @JsonProperty("file_name")
    private final String fileName;
    @JsonProperty("url")
    private final String url;
    @JsonProperty("screenshot_url")
    private final String screenshotUrl;
    @JsonProperty("screenshot_file_name")
    private final String screenshotFileName;
}
