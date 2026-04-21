package de.simone.colortextfield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import mil.nga.color.Color;
import mil.nga.color.ColorUtils;

/**
 * Utility class for color conversions and UI component creation related to the
 * ColorTextField.
 */
public class Utils {

    /**
     * Converts an HSL color to its hexadecimal representation.
     * 
     * @param color the HSL color array
     * @return the value
     */
    static String hslToHex(float[] color) {
        Color hslColor = new Color();
        hslColor.setColorByHSL(color[0], color[1], color[2]);
        return hslColor.getColorHex();
    }

    /**
     * Converts a hexadecimal color string to an HSL color array.
     * 
     * @param hexColor the color
     * @return the value
     */
    static float[] hexToHsl(String hexColor) {
        Color color = new Color(hexColor);
        float[] hsl = ColorUtils.toHSL(color.getRed(), color.getGreen(), color.getBlue());
        return hsl;
    }

    /**
     * Generates a list of 4 HSL colors with increasing lightness based on the given
     * starting HSL color.
     * 
     * @param startHslColor the starting HSL color array
     * @return the colors
     */
    static List<String> generateHslColors(float[] startHslColor) {
        final float distance = 0.14f;
        final int outputCount = 4;

        int normalized = Math.round(startHslColor[0]) % 360;
        float hue = normalized < 0 ? normalized + 360 : normalized;
        float saturation = Math.max(0f, Math.min(1f, startHslColor[1]));
        float lightness = Math.max(0f, Math.min(1f, startHslColor[2]));

        List<String> colors = new ArrayList<>(outputCount);
        for (int i = 1; i <= outputCount; i++) {
            float nextLightness = Math.max(0f, Math.min(1f, lightness + (i * distance)));
            colors.add(hslToHex(new float[] { hue, saturation, nextLightness }));
        }
        return colors;
    }

    /**
     * Returns a Span with the given label styled as a separator.
     * 
     * @param label the label
     * @return the styled Span
     */
    static Span getSeparator(String label) {
        Span separator = new Span(label);
        separator.getStyle().set("color", "gray");
        separator.getStyle().set("font-weight", "bold");
        separator.getStyle().set("margin", "8px 0 4px 0");
        return separator;
    }

    /**
     * Returns a HorizontalLayout with no padding and no spacing.
     * 
     * @return the HorizontalLayout
     */
    static HorizontalLayout getCompactHorizontalLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);
        return layout;
    }

    /**
     * Returns a VerticalLayout with no padding and no spacing.
     * 
     * @return the VerticalLayout
     */
    static VerticalLayout getCompactVerticalLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);
        layout.setSpacing(false);
        return layout;
    }

    /**
     * Returns a map of web color names and their corresponding hex values.
     * 
     * @return the map
     */
    static Map<String, String> getWebColors() {
        Map<String, String> webColors = new HashMap<>();
        webColors.put("ALICEBLUE", "#f0f8ff");
        webColors.put("ANTIQUEWHITE", "#faebd7");
        webColors.put("AQUA", "#00ffff");
        webColors.put("AQUAMARINE", "#7fffd4");
        webColors.put("AZURE", "#f0ffff");
        webColors.put("BEIGE", "#f5f5dc");
        webColors.put("BISQUE", "#ffe4c4");
        webColors.put("BLACK", "#000000");
        webColors.put("BLANCHEDALMOND", "#ffebcd");
        webColors.put("BLUE", "#0000ff");
        webColors.put("BLUEVIOLET", "#8a2be2");
        webColors.put("BROWN", "#a52a2a");
        webColors.put("BURLYWOOD", "#deb887");
        webColors.put("CADETBLUE", "#5f9ea0");
        webColors.put("CHARTREUSE", "#7fff00");
        webColors.put("CHOCOLATE", "#d2691e");
        webColors.put("CORAL", "#ff7f50");
        webColors.put("CORNFLOWERBLUE", "#6495ed");
        webColors.put("CORNSILK", "#fff8dc");
        webColors.put("CRIMSON", "#dc143c");
        webColors.put("CYAN", "#00ffff");
        webColors.put("DARKBLUE", "#00008b");
        webColors.put("DARKGRAY", "#a9a9a9");
        webColors.put("DARKGREEN", "#006400");
        webColors.put("DARKRED", "#8b0000");
        webColors.put("DEEPSKYBLUE", "#00bfff");
        webColors.put("DODGERBLUE", "#1e90ff");
        webColors.put("FUCHSIA", "#ff00ff");
        webColors.put("GOLD", "#ffd700");
        webColors.put("GOLDENROD", "#daa520");
        webColors.put("GRAY", "#808080");
        webColors.put("GREEN", "#008000");
        webColors.put("GREY", "#808080");
        webColors.put("HOTPINK", "#ff69b4");
        webColors.put("INDIGO", "#4b0082");
        webColors.put("IVORY", "#fffff0");
        webColors.put("KHAKI", "#f0e68c");
        webColors.put("LAVENDER", "#e6e6fa");
        webColors.put("LIGHTBLUE", "#add8e6");
        webColors.put("LIGHTGRAY", "#d3d3d3");
        webColors.put("LIGHTGREEN", "#90ee90");
        webColors.put("LIGHTYELLOW", "#ffffe0");
        webColors.put("LIME", "#00ff00");
        webColors.put("LIMEGREEN", "#32cd32");
        webColors.put("MAGENTA", "#ff00ff");
        webColors.put("MAROON", "#800000");
        webColors.put("MEDIUMBLUE", "#0000cd");
        webColors.put("MEDIUMSEAGREEN", "#3cb371");
        webColors.put("NAVY", "#000080");
        webColors.put("OLIVE", "#808000");
        webColors.put("ORANGE", "#ffa500");
        webColors.put("ORANGERED", "#ff4500");
        webColors.put("ORCHID", "#da70d6");
        webColors.put("PERU", "#cd853f");
        webColors.put("PINK", "#ffc0cb");
        webColors.put("PLUM", "#dda0dd");
        webColors.put("PURPLE", "#800080");
        webColors.put("RED", "#ff0000");
        webColors.put("ROYALBLUE", "#4169e1");
        webColors.put("SALMON", "#fa8072");
        webColors.put("SEAGREEN", "#2e8b57");
        webColors.put("SIENNA", "#a0522d");
        webColors.put("SILVER", "#c0c0c0");
        webColors.put("SKYBLUE", "#87ceeb");
        webColors.put("SLATEGRAY", "#708090");
        webColors.put("STEELBLUE", "#4682b4");
        webColors.put("TAN", "#d2b48c");
        webColors.put("TEAL", "#008080");
        webColors.put("TOMATO", "#ff6347");
        webColors.put("TURQUOISE", "#40e0d0");
        webColors.put("VIOLET", "#ee82ee");
        webColors.put("WHEAT", "#f5deb3");
        webColors.put("WHITE", "#ffffff");
        webColors.put("YELLOW", "#ffff00");
        webColors.put("YELLOWGREEN", "#9acd32");
        return webColors;
    }
}
