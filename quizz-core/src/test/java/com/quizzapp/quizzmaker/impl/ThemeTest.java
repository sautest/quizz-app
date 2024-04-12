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
        String bgColor = "bg_color";
        theme.setBgColor(bgColor);
        assertEquals(bgColor, theme.getBgColor());
    }

    @Test
    public void testBgImage() {
        String bgImage = "bg_image_data";
        theme.setBgImage(bgImage);
        assertEquals(bgImage, theme.getBgImage());
    }

    @Test
    public void testQuestionColor() {
        String questionColor = "question_color";
        theme.setQuestionColor(questionColor);
        assertEquals(questionColor, theme.getQuestionColor());
    }

    @Test
    public void testOptionBgColor() {
        String optionBgColor = "option_bg_color";
        theme.setOptionBgColor(optionBgColor);
        assertEquals(optionBgColor, theme.getOptionBgColor());
    }

    @Test
    public void testOptionTextColor() {
        String optionTextColor = "option_text_color";
        theme.setOptionTextColor(optionTextColor);
        assertEquals(optionTextColor, theme.getOptionTextColor());
    }

    @Test
    public void testButtonBgColor() {
        String buttonBgColor = "button_bg_color";
        theme.setButtonBgColor(buttonBgColor);
        assertEquals(buttonBgColor, theme.getButtonBgColor());
    }

    @Test
    public void testButtonTextColor() {
        String buttonTextColor = "button_text_color";
        theme.setButtonTextColor(buttonTextColor);
        assertEquals(buttonTextColor, theme.getButtonTextColor());
    }
}
