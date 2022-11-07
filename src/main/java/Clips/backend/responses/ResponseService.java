package Clips.backend.responses;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public class ResponseService {
    public Response ResponseBuilder(Map<?, ?> receivedData, String receivedMessage, HttpStatus status) {
        return Response.builder()
            .timeStamp(now())
            .data(receivedData)
            .message(receivedMessage)
            .httpStatus(status)
            .statusCode(status.value())
            .build();
    }
}
