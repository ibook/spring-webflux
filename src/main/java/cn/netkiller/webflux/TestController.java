package cn.netkiller.webflux;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

import org.reactivestreams.Publisher;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@RestController
public class TestController {

	public TestController() {

	}

	@GetMapping("/")
	@ResponseBody
	public Publisher<String> index() {
		return Mono.just("Hello world!");
	}

	@GetMapping("/randomNumbers")
	public Flux<ServerSentEvent<Integer>> randomNumbers() {
		return Flux.interval(Duration.ofSeconds(1)).map(new Function<Long, Object>() {
			public Object apply(Long seq) {
				return Tuples.of(seq, ThreadLocalRandom.current().nextInt());
			}
		}).map(new Function<Object, ServerSentEvent<Integer>>() {
			public ServerSentEvent<Integer> apply(Object data) {
				return ServerSentEvent.<Integer>builder().event("random").id(Long.toString(((Tuple2<Long, Integer>) data).getT1())).data(((Tuple2<Long, Integer>) data).getT2()).build();
			}
		});
	}

}
