/*-
 * #%L
 * TrendChart Add-on for vaadin Flow
 * %%
 * Copyright (C) 2026 Terry Tupano
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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

    static String[] standardColors = { "#c00000", "#ff0000", "#ffc000", "#ffff00", "#92d050", "#00b050", "#00b0f0",
            "#0070c0", "#002060", "#7030a0" };

    static String[][] webColors = {
            { "#003366", "#336699", "#3366cc", "#003399", "#000099", "#0000cc", "#000066" },
            { "#006666", "#006699", "#0099cc", "#0066cc", "#0033cc", "#0000ff", "#3333ff", "#333399" },
            { "#669999", "#009999", "#33cccc", "#00ccff", "#0099ff", "#0066ff", "#3366ff", "#3333cc", "#666699" },
            { "#339966", "#00cc99", "#00ffcc", "#00ffff", "#33ccff", "#3399ff", "#6699ff", "#6666ff", "#6600ff",
                    "#6600cc" },
            { "#339933", "#00cc66", "#00ff99", "#66ffcc", "#66ffff", "#66ccff", "#99ccff", "#9999ff", "#9966ff",
                    "#9933ff", "#9900ff" },
            { "#006600", "#00cc00", "#00ff00", "#66ff99", "#99ffcc", "#ccffff", "#ccccff", "#cc99ff", "#cc66ff",
                    "#cc33ff", "#cc00ff", "#9900cc" },
            { "#003300", "#009933", "#33cc33", "#66ff66", "#99ff99", "#ccffcc", "#ffffff", "#ffccff", "#ff99ff",
                    "#ff66ff", "#ff00ff", "#cc00cc", "#660066" },
            { "#333300", "#009900", "#66ff33", "#99ff66", "#ccff99", "#ffffcc", "#ffcccc", "#ff99cc", "#ff66cc",
                    "#ff33cc", "#cc0099", "#993399" },
            { "#336600", "#669900", "#99ff33", "#ccff66", "#ffff99", "#ffcc99", "#ff9999", "#ff6699", "#ff3399",
                    "#cc3399", "#990099" },
            { "#666633", "#99cc00", "#ccff33", "#ffff66", "#ffcc66", "#ff9966", "#ff6666", "#ff0066", "#d60094",
                    "#993366" },
            { "#a58800", "#cccc00", "#ffff00", "#ffcc00", "#ff9933", "#ff6600", "#ff0033", "#cc0066", "#660033" },
            { "#996633", "#cc9900", "#ff9900", "#cc6600", "#ff3300", "#ff0000", "#cc0000", "#990033" },
            { "#663300", "#996600", "#cc3300", "#993300", "#990000", "#800000", "#993333" }
    };
    
    static String[][] monoColors = {
            { "#ffffff", "#ebebeb", "#d7d7d7", "#c3c3c3", "#afafaf", "#9b9b9b", "#878787", "#737373", "#5f5f5f",
                    "#4b4b4b", "#373737", "#232323", "#0f0f0f" },
            { "#f5f5f5", "#e1e1e1", "#cdcdcd", "#b9b9b9", "#a5a5a5", "#919191", "#7d7d7d", "#696969", "#555555",
                    "#414141", "#2d2d2d", "#191919", "#050505" }
    };

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
     * Returns a map of common web color names and their corresponding hex values.
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
