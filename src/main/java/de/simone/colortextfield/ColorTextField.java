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
import com.vaadin.flow.function.ValueProvider;

/**
 * A custom Vaadin TextField component that allows users to input and select
 * colors. The ColorTextField provides a color preview, a color picker popover
 * with predefined colors, and a history of selected colors. The
 * component supports both hexadecimal color codes and CSS color names.
 * <p>
 * The ColorTextField includes the following features:
 * <ul>
 * <li>Color preview</li>
 * <li>Color picker popover with predefined colors</li>
 * <li>History of selected colors</li>
 * </ul>
 */
@CssImport("./colortextfield/color-text-field.css")
public class ColorTextField extends TextField {

	private Popover colorPickerPopover;
	private Span colorPreview;
	private Map<String, String> webColorMap;
	private HorizontalLayout historyLayout;
	private HorizontalLayout subThemeColorsLayout;
	private HorizontalLayout themeColorsLayout;
	private ValueProvider<Object, List<String>> historyProvider;
	private List<String> history;
	private boolean showHistory;

    private String[] themeColors = { "#ffffff", "#000000", "#eeece1", "#1f497d", "#4f81bd", "#c0504d", "#9bbb59",
            "#8064a2", "#4bacc6", "#f79646" };

    private String[] subThemeColors = { "#7f7f7f", "#0c0c0c", "#1d1b10", "#0f243e", "#244061", "#632423", "#4f6228",
            "#3f3151", "#205867", "#974806" };


	/**
	 * new Instance
	 */
	public ColorTextField() {
		this(true);
	}

	/**
	 * new Instance
	 * 
	 * @param label - the label
	 */
	public ColorTextField(String label) {
		this(true);
		setLabel(label);
	}

	/**
	 * new Instance
	 * 
	 * @param label       - the label
	 * @param showHistory - whether to show the history of selected colors in the
	 *                    color picker popover
	 */
	public ColorTextField(String label, boolean showHistory) {
		this(showHistory);
		setLabel(label);
	}

	private ColorTextField(boolean showHistory) {
		this.showHistory = showHistory;
		this.webColorMap = Utils.getWebColors();
		this.history = new ArrayList<>();
		this.historyProvider = c -> history;

		this.colorPreview = getColorButton("#ffffff", false);
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

	/**
	 * Adds a color value to the history of selected colors. If the value already
	 * exists in the history, it is moved to the front. The history is limited to 10
	 * entries.
	 * 
	 * @param color - the color
	 */
	public void addToHistory(String color) {
		List<String> history = historyProvider.apply(null);
		history.remove(color);
		history.add(0, color);
	}

	/**
	 * Sets the history value provider. The history value provider is a function
	 * that returns a {@code List<String>} representing the history of selected
	 * colors. The history is limited to 10 entries.
	 * 
	 * @param historyProvider - the history value provider
	 */
	public void setHistoryValueProvider(ValueProvider<Object, List<String>> historyProvider) {
		this.historyProvider = historyProvider;
	}

	/**
	 * Returns the history of selected colors as a list of hexadecimal color
	 * strings.
	 * 
	 * @return - the history
	 */
	public List<String> getHistory() {
		return historyProvider.apply(null);
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
		addToHistory(value2);
	}

	private VerticalLayout createCustomColorLayout() {
		VerticalLayout layout = Utils.getCompactVerticalLayout();
		layout.setWidth("220px");
		layout.getStyle().set("margin", "var(--lumo-space-s)");
		// layout.setSizeFull();

		// hexagon pattern
		for (String[] row : Utils.webColors) {
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

		// monochrome pattern
		boolean isFirst = true;
		for (String[] row : Utils.monoColors) {
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

	private Span getColorButton(String color, boolean addClickListener) {
		Span button = new Span();
		button.addClassName("color-text-field-button");
		button.getStyle().set("background-color", color);
		button.setWidth("20px");
		button.setHeight("20px");

		if (addClickListener) {
			button.getStyle().set("margin", "0 2px 0 2px");
			// button.getStyle().set("cursor", "default");
			button.addClickListener(e -> {
				setValue(color);
				colorPickerPopover.close();
			});
		}

		return button;
	}

	private VerticalLayout createColorLayout() {
		themeColorsLayout = Utils.getCompactHorizontalLayout();
		themeColorsLayout.getStyle().set("margin-bottom", "var(--lumo-space-m)");
		refreshThemeColors();

		subThemeColorsLayout = Utils.getCompactHorizontalLayout();
		refreshSubThemeColorsLayout();

		HorizontalLayout standardColorslayout = Utils.getCompactHorizontalLayout();
		for (String color : Utils.standardColors) {
			standardColorslayout.add(getColorButton(color, true));
		}

		Span historySep = Utils.getSeparator("History");
		historySep.setVisible(showHistory);
		historyLayout = Utils.getCompactHorizontalLayout();
		historyLayout.getStyle().set("margin-bottom", "var(--lumo-space-m)");
		historyLayout.setVisible(showHistory);

		refreshHistory();

		VerticalLayout layout = Utils.getCompactVerticalLayout();
		layout.add(Utils.getSeparator("Theme colors"), themeColorsLayout, subThemeColorsLayout,
				Utils.getSeparator("Standard colors"), standardColorslayout, historySep, historyLayout);
		return layout;
	}

	private void refreshHistory() {
		List<String> history = historyProvider.apply(null);
		if (history.isEmpty())
			return;

		historyLayout.removeAll();
		for (int i = 0; i < Math.min(history.size(), 10); i++) {
			String color = history.get(i);
			historyLayout.add(getColorButton(color, true));
		}
	}

	private void refreshThemeColors() {
		themeColorsLayout.removeAll();
		for (String color : this.themeColors) {
			themeColorsLayout.add(getColorButton(color, true));
		}
	}

	private void refreshSubThemeColorsLayout() {
		subThemeColorsLayout.removeAll();
		for (String color : this.subThemeColors) {
			float[] hslColor = Utils.hexToHsl(color);
			List<String> generatedColors = Utils.generateHslColors(hslColor);
			VerticalLayout colorGroup = Utils.getCompactVerticalLayout();
			for (int i = generatedColors.size() - 1; i >= 0; i--) {
				colorGroup.add(getColorButton(generatedColors.get(i), true));
			}
			subThemeColorsLayout.add(colorGroup);
		}
	}

}
