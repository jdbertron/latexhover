package latexhover;

import org.eclipse.jdt.ui.text.java.hover.IJavaEditorTextHover;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;

/**
 * LaTeXHover - Interprets Java comments with $latex .. $ delimiters as LaTeX and renders them.
 * Normally, only implementing IJavaEditorTextHover is sufficient. Because we want to inject our own Control
 * we have to also implement ITextHoverExtension to provide the control.
 * @author jbertron
 *
 */
public class LaTeXHover implements IJavaEditorTextHover, ITextHoverExtension, ITextHoverExtension2 {

	
	// Inner classes that will provide the control
    private IInformationControlCreator fHoverControlCreator;	
    private IInformationControlCreator fPresenterControlCreator;

    public IInformationControlCreator getInformationPresenterControlCreator() {
        if (fPresenterControlCreator == null)
            fPresenterControlCreator= new PresenterControlCreator();
        return fPresenterControlCreator;
    }

    public IInformationControlCreator getHoverControlCreator() {
        if (fHoverControlCreator == null)
            fHoverControlCreator= new HoverControlCreator(getInformationPresenterControlCreator());
        return fHoverControlCreator;
    }


	/**
	 * Method declared and deprecated in ITextHover. (Since we need to implement ITextHover, we need to implement this method.)
     * @param textViewer 
	 * @param hoverRegion 
	 * @return String to render
	 * @deprecated
     */
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		if (hoverRegion != null) {
			try {
				if (hoverRegion.getLength() > -1) {
					IDocument doc = textViewer.getDocument();
					int offset = hoverRegion.getOffset();
					int lineNumber = doc.getLineOfOffset(offset);
					IRegion line = doc.getLineInformation(lineNumber);
					return doc.get(line.getOffset(), line.getLength());
				}
			} catch (BadLocationException x) {
			}
		}
		return null; 
	}

	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {

	    // Start with the string returned by the older getHoverInfo()
	    final String selection = getHoverInfo(textViewer, hoverRegion);
	    if (selection.length() < 1) {
	    	return null;
	    }
	    else {
	    	// Strip comment markers and retain only the latex part delimited by $latex and matching $
	    	return selection.replaceAll(".*\\$latex", "").replaceAll("\\$.*","");
	    }
	}

	
	
    public static final class PresenterControlCreator extends AbstractReusableInformationControlCreator {

        public IInformationControl doCreateInformationControl(Shell parent) {
            if (LaTeXHoverInformationControl.isAvailable(parent)) {
                LaTeXHoverInformationControl iControl= new LaTeXHoverInformationControl(parent);
                return iControl;

            } else {
                return new DefaultInformationControl(parent, true);
            }
        }
    }


    public static final class HoverControlCreator extends AbstractReusableInformationControlCreator {

        private final IInformationControlCreator fInformationPresenterControlCreator;

        public HoverControlCreator(IInformationControlCreator informationPresenterControlCreator) {
        	fInformationPresenterControlCreator= informationPresenterControlCreator;
        }

        public IInformationControl doCreateInformationControl(Shell parent) {
            if (LaTeXHoverInformationControl.isAvailable(parent)) {
                LaTeXHoverInformationControl iControl= new LaTeXHoverInformationControl(parent) {
                    public IInformationControlCreator getInformationPresenterControlCreator() {
                        return fInformationPresenterControlCreator;
                    }
                };
                return iControl;
            } else {
                return new DefaultInformationControl(parent, true);
            }
        }

        public boolean canReuse(IInformationControl control) {
            return false;
        }
    }


	@Override
	public IRegion getHoverRegion(ITextViewer arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEditor(IEditorPart arg0) {
		// TODO Auto-generated method stub
		
	}
}