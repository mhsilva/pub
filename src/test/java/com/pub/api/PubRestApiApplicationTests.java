package com.pub.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pub.api.main.PubRestApiApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PubRestApiApplication.class)
@WebAppConfiguration
public class PubRestApiApplicationTests {

	@Test
	public void contextLoads() {
	}

}
