package com.pub.api.main;

import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
	public static final String MESSAGE = "Hello, {0}";
	private final AtomicLong atomicLong = new AtomicLong();

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(atomicLong.incrementAndGet(), MessageFormat.format(GreetingController.MESSAGE, name));
	}
}
