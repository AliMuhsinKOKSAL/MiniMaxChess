package game_ui;

import javax.swing.*;

import obj.PieceColor;

import java.awt.*;

public class CheckBoxWindow{

	public JCheckBox checkBoxSelectedW = new JCheckBox("Selected", true);
	public JCheckBox checkBoxValidW = new JCheckBox("getValid", true);
	public JCheckBox checkBoxThreatenedW = new JCheckBox("Threatened", true);
	public JCheckBox checkBoxProtectedW = new JCheckBox("Protected", true);

	public JCheckBox checkBoxSelectedB = new JCheckBox("Selected", true);
	public JCheckBox checkBoxValidB = new JCheckBox("getValid", true);
	public JCheckBox checkBoxThreatenedB = new JCheckBox("Threatened", true);
	public JCheckBox checkBoxProtectedB = new JCheckBox("Protected", true);

	JWindow cFrameW;
	JWindow cFrameB;

	public CheckBoxWindow() {
		white();
		black();
	}

	public void white() {
		cFrameW = new JWindow();
		cFrameW.setSize(100, 125);
		cFrameW.setLayout(new FlowLayout());

		cFrameW.add(checkBoxSelectedW);
		cFrameW.add(checkBoxValidW);
		cFrameW.add(checkBoxThreatenedW);
		cFrameW.add(checkBoxProtectedW);

		cFrameW.setVisible(true);
	}

	public void black() {
		cFrameB = new JWindow();
		cFrameB.setSize(85, 125);
		cFrameB.setLayout(new FlowLayout());

		cFrameB.add(checkBoxSelectedB);
		cFrameB.add(checkBoxValidB);
		cFrameB.add(checkBoxThreatenedB);
		cFrameB.add(checkBoxProtectedB);

		cFrameB.setVisible(true);
	}

	public boolean isSelectedCBSelected(PieceColor color) {
		if (color != null) {
			if (color == PieceColor.white) {
				return checkBoxSelectedW.isSelected();
			} else {
				return checkBoxSelectedB.isSelected();
			}
		}
		return false;
	}

	public boolean isSelectedCBValid(PieceColor color) {
		if (color != null) {
			if (color == PieceColor.white) {
				return checkBoxValidW.isSelected();
			} else {
				return checkBoxValidB.isSelected();
			}
		}
		return false;
	}

	public boolean isSelectedCBThreatened(PieceColor color) {
		if (color != null) {
			if (color == PieceColor.white) {
				return checkBoxThreatenedW.isSelected();
			} else {
				return checkBoxThreatenedB.isSelected();
			}
		}
		return false;
	}

	public boolean isSelectedCBProtected(PieceColor color) {
		if (color != null) {
			if (color == PieceColor.white) {
				return checkBoxProtectedW.isSelected();
			} else {
				return checkBoxProtectedB.isSelected();
			}
		}
		return false;
	}
}
