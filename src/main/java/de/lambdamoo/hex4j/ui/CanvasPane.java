package de.lambdamoo.hex4j.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Region;

/**
 * This is the wrapper class for the Canvas on which the graphic should be
 * painted
 */
public class CanvasPane extends Region {

	private ObjectProperty<Canvas> canvasViewProperty = new SimpleObjectProperty<Canvas>();

	public ObjectProperty<Canvas> canvasViewProperty() {
		return canvasViewProperty;
	}

	private ChangeListener resizeListener = null;

	public void setOnResizeListener(ChangeListener resizeListener) {
		this.resizeListener = resizeListener;
	}

	public Canvas getCanvas() {
		return canvasViewProperty.get();
	}

	public void setCanvas(Canvas can) {
		this.canvasViewProperty.set(can);
	}

	public CanvasPane() {
		this(new Canvas());
	}

	@Override
	protected void layoutChildren() {
		Canvas canvas = canvasViewProperty.get();
		if (canvas != null) {
			double w = getWidth();
			double h = getHeight();
			canvas.setWidth(w);
			canvas.setHeight(h);
			layoutInArea(canvas, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
			if (resizeListener != null) {
				resizeListener.changed(null, null, null);
			}
		}
		super.layoutChildren();
	}

	public CanvasPane(Canvas can) {
		canvasViewProperty.addListener(new ChangeListener<Canvas>() {
			@Override
			public void changed(ObservableValue<? extends Canvas> arg0, Canvas oldIV, Canvas newIV) {
				if (oldIV != null) {
					getChildren().remove(oldIV);
				}
				if (newIV != null) {
					getChildren().add(newIV);
				}
			}
		});
		this.canvasViewProperty.set(can);
		Canvas c = canvasViewProperty.getValue();
	}

}
