package game_ui;

import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class btnSquare extends JButton {
	
	Icon icon1;
	Icon icon2;

	public void setIcons(Icon xicon1, Icon xicon2) {
		this.icon1 = xicon1;
		this.icon2 = xicon2;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (icon1 != null) {
			icon1.paintIcon(this, g, 0, 0);
		}

		if (icon2 != null) {
			icon2.paintIcon(this, g, 0, 0);
		}
	}
}
