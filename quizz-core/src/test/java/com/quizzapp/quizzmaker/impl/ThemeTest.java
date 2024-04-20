package com.quizzapp.quizzmaker.impl;

import com.quizzapp.quizzmaker.persistence.entities.Theme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ThemeTest {

    private Theme theme;

    @BeforeEach
    public void setUp() {
        theme = new Theme();
    }

    @Test
    public void testId() {
        theme.setId(1);
        assertEquals(1, theme.getId());
    }

    @Test
    public void testBgColor() {
        String bgColor = "color";
        theme.setBgColor(bgColor);
        assertEquals(bgColor, theme.getBgColor());
    }

    @Test
    public void testBgImage() {
        String bgImage = "img";
        theme.setBgImage(bgImage);
        assertEquals(bgImage, theme.getBgImage());
    }

    @Test
    public void testQuestionColor() {
        String questionColor = "red";
        theme.setQuestionColor(questionColor);
        assertEquals(questionColor, theme.getQuestionColor());
    }

    @Test
    public void testOptionBgColor() {
        String optionBgColor = "red";
        theme.setOptionBgColor(optionBgColor);
        assertEquals(optionBgColor, theme.getOptionBgColor());
    }

    @Test
    public void testOptionTextColor() {
        String optionTextColor = "red";
        theme.setOptionTextColor(optionTextColor);
        assertEquals(optionTextColor, theme.getOptionTextColor());
    }

    @Test
    public void testButtonBgColor() {
        String buttonBgColor = "red";
        theme.setButtonBgColor(buttonBgColor);
        assertEquals(buttonBgColor, theme.getButtonBgColor());
    }

    @Test
    public void testButtonTextColor() {
        String buttonTextColor = "red";
        theme.setButtonTextColor(buttonTextColor);
        assertEquals(buttonTextColor, theme.getButtonTextColor());
    }
}
