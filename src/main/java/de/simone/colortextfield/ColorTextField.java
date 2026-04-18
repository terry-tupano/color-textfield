package de.simone.colortextfield;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverVariant;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.component.textfield.TextField;

import mil.nga.color.Color;
import mil.nga.color.ColorUtils;

@CssImport("./styles/color-text-field.css")
public class ColorTextField extends TextField {

	private Popover colorPickerPopover;
	private Span colorPreview;
	private WebColors webColorMap;

	private String[] history;

	private String[] themeColors = { "#ffffff", "#000000", "#eeece1", "#1f497d", "#4f81bd", "#c0504d", "#9bbb59",
			"#8064a2", "#4bacc6", "#f79646" };

	private String[] subThemeColors = { "#7f7f7f", "#0c0c0c", "#1d1b10", "#0f243e", "#244061", "#632423", "#4f6228",
			"#3f3151", "#205867", "#974806" };

	private String[] standardColors = { "#c00000", "#ff0000", "#ffc000", "#ffff00", "#92d050", "#00b050", "#00b0f0",
			"#0070c0", "#002060", "#7030a0" };

	private String transColor = "#0000ffff";

	public ColorTextField() {
		webColorMap = new WebColors();

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

		colorPickerPopover = new Popover();
		colorPickerPopover.setTarget(icon);
		colorPickerPopover.add(tabSheet);
		colorPickerPopover.addThemeVariants(PopoverVariant.ARROW);


		setClearButtonVisible(true);
		addValueChangeListener(e -> setValue(e.getValue()));
		setValue(null);
	}

	public ColorTextField(String label) {
		this();
		setLabel(label);
	}

	private VerticalLayout createColorLayout() {
		HorizontalLayout themeColorslayout = getCompactHorizontalLayout();
		themeColorslayout.getStyle().set("margin-bottom", "var(--lumo-space-m)");
		for (String color : themeColors) {
			themeColorslayout.add(getColorButton(color, true));
		}

		HorizontalLayout subThemeColorslayout = getCompactHorizontalLayout();
		subThemeColorslayout.getStyle().set("margin-bottom", "var(--lumo-space-m)");
		for (String color : subThemeColors) {
			float[] hslColor = hexToHsl(color);
			List<String> generatedColors = generateHslColors(hslColor);
			VerticalLayout colorGroup = getCompactVerticalLayout();
			for(int i = generatedColors.size() - 1; i >= 0; i--) {
				colorGroup.add(getColorButton(generatedColors.get(i), true));
			}
			subThemeColorslayout.add(colorGroup);
		}

		HorizontalLayout standardColorslayout = getCompactHorizontalLayout();
		standardColorslayout.getStyle().set("margin-bottom", "var(--lumo-space-m)");
		for (String color : standardColors) {
			standardColorslayout.add(getColorButton(color, true));
		}

				VerticalLayout layout = getCompactVerticalLayout();
		layout.add(getSeparator("Theme colors"), themeColorslayout, subThemeColorslayout,
				getSeparator("Standard colors"), standardColorslayout);
		return layout;
	}

	private static VerticalLayout getCompactVerticalLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setPadding(false);
		layout.setSpacing(false);
		return layout;
	}

	public HorizontalLayout getCompactHorizontalLayout() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setPadding(false);
		layout.setSpacing(false);
		return layout;
	}

	private static Span getSeparator(String label) {
		Span separator = new Span(label);
		separator.getStyle().set("color", "gray");
		separator.getStyle().set("font-weight", "bold");
		separator.getStyle().set("margin", "8px 0 4px 0");
		return separator;
	}

	private Span getColorButton(String color, boolean addClickListener) {
		Span button = new Span();
		button.addClassName("color-text-field-button");
		button.getStyle().set("background-color", color);
		button.setWidth("24px");
		button.setHeight("24px");

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
		System.out.println("ColorTextField.setValue() " + value);
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
	}

	private List<String> generateHslColors(float[] startHslColor) {
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

	private String hslToHex(float[] color) {
		Color hslColor = new Color();
		hslColor.setColorByHSL(color[0], color[1], color[2]);
		return hslColor.getColorHex();
	}

	private float[] hexToHsl(String hexColor) {
		Color color = new Color(hexColor);
		float[] hsl = ColorUtils.toHSL(color.getRed(), color.getGreen(), color.getBlue());
		return hsl;
	}
}
