package com.project.stocker;

import com.project.stocker.service.RankingService;
import com.project.stocker.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("주식 종목 랭킹 시스템")
@SpringBootTest
@AutoConfigureMockMvc
public class RankingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RankingService rankingService;
    @Autowired
    private UserService userService;

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})      //가상의 유저
    public void testGetTop10ByTradeVolume() throws Exception {
        long startTime = System.currentTimeMillis();

        MvcResult result = mockMvc.perform(get("/api/stocks/volume")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        long endTime = System.currentTimeMillis();

        String jsonResponse = result.getResponse().getContentAsString();

        System.out.println("Execution Time: " + (endTime - startTime) + "ms");
        System.out.println("TradVolume top10 Response JSON: " + jsonResponse);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testGetTop10ByIncreaseAmount() throws Exception {
        long startTime = System.currentTimeMillis();
        MvcResult result = mockMvc.perform(get("/api/stocks/increase")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        long endTime = System.currentTimeMillis();
        String jsonResponse = result.getResponse().getContentAsString();

        System.out.println("Execution Time: " + (endTime - startTime) + "ms");
        System.out.println("Increase top10 Response JSON: " + jsonResponse);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    public void testGetTop10ByDecreaseAmount() throws Exception {
        long startTime = System.currentTimeMillis();
        MvcResult result = mockMvc.perform(get("/api/stocks/decrease")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        long endTime = System.currentTimeMillis();
        String jsonResponse = result.getResponse().getContentAsString();

        System.out.println("Execution Time: " + (endTime - startTime) + "ms");
        System.out.println("Decrease top10 Response JSON: " + jsonResponse);
    }

}
