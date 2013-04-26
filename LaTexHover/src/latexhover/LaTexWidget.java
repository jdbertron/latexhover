package latexhover;

import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;


public class LaTexWidget extends org.eclipse.jface.window.ToolTip {

	private static final int TRANSPARENT_COLOR = 0x123456;
	private static final PaletteData PALETTE_DATA = new PaletteData(0xFF0000,0xFF00, 0xFF);
	private Shell parentShell;
	private String latexstring;

	    public LaTexWidget(Control control) {
	        super(control);
	        this.parentShell = control.getShell();
	    }

	    protected Composite createToolTipContentArea(Event event, Composite parent) {
	        Composite comp = new Composite(parent,SWT.NONE);
	        comp.setLayout(new FillLayout());
	        Label label = new Label(comp, SWT.NONE);
		    label.setAlignment(SWT.CENTER);

			// create a formula
			TeXFormula formula = new TeXFormula(latexstring);
			// render the formla to an icon of the same size as the formula.
			TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
			// insert a border 
			icon.setInsets(new Insets(5, 5, 5, 5));

			// now create an actual image of the rendered equation
			BufferedImage awtImage = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D g2 = awtImage.createGraphics();
			g2.setColor(java.awt.Color.white);
			g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
			JLabel jl = new JLabel();
			jl.setForeground(new java.awt.Color(0, 0, 0));
			icon.paintIcon(jl, g2, 0, 0);
			// at this point the image is created, you could also save it with ImageIO
			
			ImageData swtImageData = new ImageData(awtImage.getWidth(), awtImage.getHeight(), 24, PALETTE_DATA);
			swtImageData.transparentPixel = TRANSPARENT_COLOR;
			Image swtImage = transferPixels(swtImageData, awtImage,  0, 0, icon.getIconWidth(), icon.getIconHeight());
		    label.setImage(swtImage);
	        
	        return comp;
	    }

		/**
		 * Transfer a rectangular region from the AWT image to the SWT image.
		 * @return 
		 */
		private Image transferPixels(ImageData swtImageData, BufferedImage awtImage, int clipX, int clipY, int clipW, int clipH) {
			int[] awtPixels = new int[awtImage.getWidth() * awtImage.getHeight()];
			int step = swtImageData.depth / 8;
			byte[] data = swtImageData.data;
			awtImage.getRGB(clipX, clipY, clipW, clipH, awtPixels, 0, clipW);
			for (int i = 0; i < clipH; i++) {
				int idx = (clipY + i) * swtImageData.bytesPerLine + clipX * step;
				for (int j = 0; j < clipW; j++) {
					int rgb = awtPixels[j + i * clipW];
					for (int k = swtImageData.depth - 8; k >= 0; k -= 8) {
						data[idx++] = (byte) ((rgb >> k) & 0xFF);
					}
				}
			}
			return new Image(Display.getDefault(), swtImageData);
		}

	

	
	public void setLatexstring(String latexstring) {
		this.latexstring = latexstring;
	}

	public String getLatexstring() {
		return latexstring;
	}

}
