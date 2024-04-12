package com.quizzapp.quizzmaker.impl;

import com.quizzapp.quizzmaker.persistence.entities.Settings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SettingsTest {

    private Settings settings;

    @BeforeEach
    public void setUp() {
        settings = new Settings();
    }

    @Test
    public void testId() {
        settings.setId(1);
        assertEquals(1, settings.getId());
    }

    @Test
    public void testLogo() {
        String logoData = "logo_data";
        settings.setLogo(logoData);
        assertEquals(logoData, settings.getLogo());
    }

    @Test
    public void testEnableTimeLimit() {
        settings.setEnableTimeLimit(true);
        assertTrue(settings.isEnableTimeLimit());
    }


    @Test
    public void testEnableAnswerNotInOrder() {
        settings.setEnableAnswerNotInOrder(true);
        assertTrue(settings.isEnableAnswerNotInOrder());
    }

    @Test
    public void testEnableAskForBasicUserInfo() {
        settings.setEnableAskForBasicUserInfo(true);
        assertTrue(settings.isEnableAskForBasicUserInfo());
    }

    @Test
    public void testEnableProgressBar() {
        settings.setEnableProgressBar(true);
        assertTrue(settings.isEnableProgressBar());
    }

    @Test
    public void testEnableRandomizeQuestions() {
        settings.setEnableRandomizeQuestions(true);
        assertTrue(settings.isEnableRandomizeQuestions());
    }

    @Test
    public void testEnablePublic() {
        settings.setEnablePublic(true);
        assertTrue(settings.isEnablePublic());
    }

    @Test
    public void testEnableShowAnswersAtTheEnd() {
        settings.setEnableShowAnswersAtTheEnd(true);
        assertTrue(settings.isEnableShowAnswersAtTheEnd());
    }

    @Test
    public void testMin() {
        settings.setMin(10);
        assertEquals(10, settings.getMin());
    }

    @Test
    public void testHours() {
        settings.setHours(2);
        assertEquals(2, settings.getHours());
    }

    @Test
    public void testQuestionsPerPage() {
        settings.setQuestionsPerPage(20);
        assertEquals(20, settings.getQuestionsPerPage());
    }

}
