package latexhover;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.jface.text.AbstractInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IInformationControlExtension2;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.widgets.MessageBox;


/**
 * Image-based implementation of {@link org.eclipse.jface.text.IInformationControl}.
 * <p>
 * Displays LaTeX in a widget.
 */
public class LaTeXHoverInformationControl extends AbstractInformationControl  implements IInformationControlExtension2    {
    
    private LaTexWidget tooltip;
    private Composite parent;

    /**
     * Creates a LaTeXHoverInformationControl with the given shell as parent.
     *
     * @param parent the parent shell
     */
    public LaTeXHoverInformationControl(Shell parent) {
        super(parent, (String) null);
        create();
    }

    protected void createContent(Composite parent) {

        try {
        	this.parent = parent;
//        	Text text = new Text(parent,SWT.BORDER);
//        	text.setText("Hello World");
//            tooltip = new LaTexWidget(text);
        } 
        catch (SWTError e) {
            MessageBox messageBox = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            messageBox.setMessage("Tooltip cannot be initialized."); //$NON-NLS-1$
            messageBox.setText("Error");                             //$NON-NLS-1$
            messageBox.open();
        }
    }
    
    /*
     * @see IInformationControl#setInformation(String)
     */
    public void setInformation(String content) {
//    	Text text = new Text(parent,SWT.BORDER);
//    	text.setText(content);
        tooltip = new LaTexWidget(parent);
        tooltip.setLatexstring(content);
        tooltip.setShift(new Point(-5, -5));
        tooltip.createToolTipContentArea(null, parent);
    }


    /*
     * @see IInformationControl#computeSizeHint()
     */
    public Point computeSizeHint() {
        // see: https://bugs.eclipse.org/bugs/show_bug.cgi?id=117602
        final int widthHint= SWT.DEFAULT;
        
        return getShell().computeSize(widthHint, SWT.DEFAULT, true);
    }
    

    /*
     * @see IInformationControlExtension#hasContents()
     */
    public boolean hasContents() {
        return tooltip.getLatexstring().length() > 0;
    }


    /*
     * @see org.eclipse.jface.text#setInput()
     * The input object may be a String, or any object that returns a displayable String from its toString() implementation.
     */    
    public void setInput(Object input) {
        // Assume that the input is marked-up LaTeX in a comment
        final String inputString;

        if (input instanceof String) {
            // Treat the String as marked-up text to be displayed.
            inputString = (String) input;
        }
        else {
            // For any other kind of object, just use its string 
            // representation as text to be displayed.
            inputString = input.toString();
        }
        setInformation(inputString);
    }


	public static boolean isAvailable(Shell parent) {
		// Always available ?
		return true;
	}

}
