package com.hashtagplus;

import com.hashtagplus.controller.IndexController;
import com.hashtagplus.model.HtplUserDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = {App.class})
//@WebIntegrationTest("server.port = 8093")
public class AppTest {

    @Autowired
    private EmbeddedWebApplicationContext webApplicationContext;

    @Autowired
    private TestRestTemplate restTemplate;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username="simon", password="password",roles={"USER","ADMIN"})
    public void exampleTest()  throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/api/messages/2")
                        .with(user("simonn").roles("USER","ADMIN")).param("hashtags", "dog")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());



        //String body = this.restTemplate.getForObject("/api/messages/2", String.class);
       // assertThat(body).isEqualTo("Hello World");
    }



    @Test
    @WithMockUser(username="simon", password="password",roles={"USER","ADMIN"})
    public void testLogout()  throws Exception {
        mockMvc.perform(formLogin("/login").user("simonn").password("password"));
    }


}