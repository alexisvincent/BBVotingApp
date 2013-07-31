/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import components.AComponent;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author alexisvincent
 */
public class VotersKeyOverlay extends AComponent {

    public VotersKeyOverlay() {
        
    }
    @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;

                g2d.setPaint(new Color(34, 34, 34, 100));
                g2d.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 15, 15);

                g2d.setPaint(new Color(34, 34, 34, 255));
                g2d.fillRoundRect(100, 100, 100, 100, 15, 15);
            }
}
