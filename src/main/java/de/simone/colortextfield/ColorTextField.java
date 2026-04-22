package de.simone.colortextfield;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.component.textfield.TextField;

@CssImport("./colortextfield/color-text-field.css")
public class ColorTextField extends TextField {

    private Popover colorPickerPopover;
    private Span colorPreview;
    private Map<String, String> webColorMap;
    private HorizontalLayout historyLayout;
    private HorizontalLayout subThemeColorsLayout;
    private HorizontalLayout themeColorsLayout;
    private List<String> history;

    private String[] themeColors = { "#ffffff", "#000000", "#eeece1", "#1f497d", "#4f81bd", "#c0504d", "#9bbb59",
            "#8064a2", "#4bacc6", "#f79646" };

    private String[] subThemeColors = { "#7f7f7f", "#0c0c0c", "#1d1b10", "#0f243e", "#244061", "#632423", "#4f6228",
            "#3f3151", "#205867", "#974806" };

    private String[] standardColors = { "#c00000", "#ff0000", "#ffc000", "#ffff00", "#92d050", "#00b050", "#00b0f0",
            "#0070c0", "#002060", "#7030a0" };

    private String[][] webColors = {
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

    String[][] monoColors = {
            { "#ffffff", "#ebebeb", "#d7d7d7", "#c3c3c3", "#afafaf", "#9b9b9b", "#878787", "#737373", "#5f5f5f",
                    "#4b4b4b", "#373737", "#232323", "#0f0f0f" },
            { "#f5f5f5", "#e1e1e1", "#cdcdcd", "#b9b9b9", "#a5a5a5", "#919191", "#7d7d7d", "#696969", "#555555",
                    "#414141", "#2d2d2d", "#191919", "#050505" }
    };

    private String transColor = "#0000ffff";

    public ColorTextField() {
        webColorMap = Utils.getWebColors();
        history = new ArrayList<>();

        this.colorPreview = getColorButton(transColor, false);
        colorPreview.setWidth("30px");
        colorPreview.getStyle().set("margin-right", "var(--lumo-space-s)");
        colorPreview.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");
        setPrefixComponent(colorPreview);

        Icon icon = VaadinIcon.PALETTE.create();
        icon.getStyle().set("cursor", "default");
        icon.addClickListener(e -> colorPickerPopover.open());
        setSuffixComponent(icon);

        TabSheet tabSheet = new TabSheet();
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_SMALL,
                TabSheetVariant.LUMO_NO_PADDING);
        tabSheet.add("Colors", createColorLayout());
        tabSheet.add("Custom", createCustomColorLayout());

        colorPickerPopover = new Popover();
        colorPickerPopover.setTarget(icon);
        colorPickerPopover.add(tabSheet);
        colorPickerPopover.addThemeVariants(PopoverVariant.ARROW);
        colorPickerPopover.addOpenedChangeListener(e -> refreshHistory());

        setClearButtonVisible(true);
        addValueChangeListener(e -> setValue(e.getValue()));
        setValue(null);
    }

    public ColorTextField(String label) {
        this();
        setLabel(label);
    }

    private VerticalLayout createCustomColorLayout() {
        VerticalLayout layout = Utils.getCompactVerticalLayout();
        layout.setWidth("220px");
        layout.getStyle().set("margin", "var(--lumo-space-s)");
        // layout.setSizeFull();

        // hexagon pattern
        for (String[] row : webColors) {
            HorizontalLayout colorRow = Utils.getCompactHorizontalLayout();
            colorRow.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            colorRow.setWidthFull();
            layout.add(colorRow);
            for (String color : row) {
                Span colorButton = getColorButton(color, true);
                colorButton.setWidth("16px");
                colorButton.setHeight("14px");
                colorButton.getStyle().set("margin", "0");
                colorRow.add(colorButton);
            } 
        }

        // monochrome patternddd
        boolean isFirst = true;
        for (String[] row : monoColors) {
            HorizontalLayout colorRow = Utils.getCompactHorizontalLayout();
            colorRow.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
            colorRow.setWidthFull();
            if (isFirst) {
                colorRow.getStyle().set("margin-top", "var(--lumo-space-m)");
                isFirst = false;
            }
            layout.add(colorRow);
            for (String color : row) {
                Span colorButton = getColorButton(color, true);
                colorButton.setWidth("16px");
                colorButton.setHeight("16px");
                colorButton.getStyle().set("margin", "0");
                colorRow.add(colorButton);
            }
        }
        return layout;
    }

    /**
     * Adds a color value to the history of selected colors. If the value already
     * exists in the history, it is moved to the front. The history is limited to 10
     * entries.
     * 
     * @param color - the color
     */
    public void addHistory(String color) {
        history.remove(color);
        history.add(0, color);
        refreshHistory();
    }

    /**
     * Sets the history of selected colors based on a list of hexadecimal color
     * strings. The method takes the first 10 colors from the provided list and
     * updates the history accordingly.
     * 
     * @param history - the history
     */
    public void setHistory(List<String> history) {
        this.history = history;
        refreshHistory();
    }

    private void refreshHistory() {
        if (history.isEmpty()) {
            return;
        }
        historyLayout.removeAll();
        for (int i = 0; i < 10; i++) {
            String color = history.get(i);
            historyLayout.add(getColorButton(color, true));
        }
    }

    /**
     * Sets the theme colors based on a list of hexadecimal color strings. The
     * method takes the first 10 colors from the provided list and updates the theme
     * colors accordingly.
     * 
     * @param themeColors - the colors
     */
    public void setThemeColors(List<String> themeColors) {
        List<String> themeColors2 = themeColors.subList(0, Math.min(themeColors.size(), 10));
        this.themeColors = themeColors2.toArray(new String[0]);
        refreshThemeColors();
    }

    private void refreshThemeColors() {
        themeColorsLayout.removeAll();
        for (String color : themeColors) {
            themeColorsLayout.add(getColorButton(color, true));
        }
    }

    /**
     * Sets the sub-theme colors based on a list of hexadecimal color strings. The
     * method takes the first 10 colors from the provided list and updates the
     * sub-theme colors accordingly. The elements in this list must be a list of
     * darker versions of the theme colors. This method will generate lighter
     * colors based on the provided colors.
     * 
     * @param subThemeColors - the colors
     */
    public void setSubThemeColors(List<String> subThemeColors) {
        List<String> subThemeColors2 = subThemeColors.subList(0, Math.min(subThemeColors.size(), 10));
        this.subThemeColors = subThemeColors2.toArray(new String[0]);
        refreshSubThemeColorsLayout();
    }

    private void refreshSubThemeColorsLayout() {
        subThemeColorsLayout.removeAll();
        for (String color : subThemeColors) {
            float[] hslColor = Utils.hexToHsl(color);
            List<String> generatedColors = Utils.generateHslColors(hslColor);
            VerticalLayout colorGroup = Utils.getCompactVerticalLayout();
            for (int i = generatedColors.size() - 1; i >= 0; i--) {
                colorGroup.add(getColorButton(generatedColors.get(i), true));
            }
            subThemeColorsLayout.add(colorGroup);
        }
    }

    private VerticalLayout createColorLayout() {
        themeColorsLayout = Utils.getCompactHorizontalLayout();
        themeColorsLayout.getStyle().set("margin-bottom", "var(--lumo-space-m)");
        refreshThemeColors();

        subThemeColorsLayout = Utils.getCompactHorizontalLayout();
        refreshSubThemeColorsLayout();

        HorizontalLayout standardColorslayout = Utils.getCompactHorizontalLayout();
        for (String color : standardColors) {
            standardColorslayout.add(getColorButton(color, true));
        }

        historyLayout = Utils.getCompactHorizontalLayout();
        historyLayout.getStyle().set("margin-bottom", "var(--lumo-space-m)");
        refreshHistory();

        VerticalLayout layout = Utils.getCompactVerticalLayout();
        layout.add(Utils.getSeparator("Theme colors"), themeColorsLayout, subThemeColorsLayout,
                Utils.getSeparator("Standard colors"), standardColorslayout, Utils.getSeparator("History"),
                historyLayout);
        return layout;
    }

    private Span getColorButton(String color, boolean addClickListener) {
        Span button = new Span();
        button.addClassName("color-text-field-button");
        button.getStyle().set("background-color", color);
        button.setWidth("20px");
        button.setHeight("20px");

        if (addClickListener) {
            button.getStyle().set("margin", "0 2px 0 2px");
            button.getStyle().set("cursor", "pointer");
            button.addClickListener(e -> {
                setValue(color);
                colorPickerPopover.close();
            });
        }

        return button;
    }

    @Override
    public void setValue(String value) {
        colorPreview.removeClassName("stripes");
        colorPreview.getStyle().remove("background-color");
        if (value == null || value.trim().isEmpty()) {
            super.setValue("");
            colorPreview.addClassName("stripes");
            return;
        }

        String value2 = value.trim();

        // if a color name?
        if (webColorMap.containsKey(value2.toUpperCase())) {
            value2 = webColorMap.get(value2.toUpperCase());
        }

        // is valid hex color?
        if (!value2.matches("#[0-9a-fA-F]{6}")) {
            setErrorMessage("Invalid value. Expected value are #rrggbb or a valid CSS color name.");
            setInvalid(true);
            return;
        }

        super.setValue(value2);
        colorPreview.getStyle().set("background-color", value2);
        addHistory(value2);
    }
}
