package org.course.handlers;

import com.azure.core.util.FluxUtil;
import org.course.handlers.interfaces.ImageHandler;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Service
public class ImageHandlerImpl implements ImageHandler {

    public static final String FILE = "image-storage/Манипуляторы для работы с химикатами.jpg";

    @Override
    public Mono<ServerResponse> getImage(ServerRequest request){
        return Mono.just(request).flatMap(r -> {
            try {
                return FluxUtil.collectBytesInByteBufferStream(FluxUtil.readFile(AsynchronousFileChannel
                        .open(Path.of(this.getClass().getClassLoader().getResource(FILE).toURI()), StandardOpenOption.READ)));
            } catch (IOException | URISyntaxException e) {
                return Mono.empty();
            }
        }).flatMap(b -> ServerResponse.ok().contentType(MediaType.IMAGE_JPEG).bodyValue(b)).log();
    }

}
