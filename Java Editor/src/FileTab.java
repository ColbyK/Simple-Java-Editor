import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;

// File object for individual tabs created
class FileTab {
	public String fileName;
	public String content;
	public File file;
	public JScrollPane tabComponent;
	public boolean isProjectFile;
	// Input buffer, will only take specified number of characters to load as String content
	private final int charBuffer = 65536;
	public FileTab(File inputFile, boolean isPF) {
		file = inputFile;
		fileName = file.getName();
		isProjectFile = isPF;
		getInput(file);
	}
	// Gets String data for a file
	private void getInput(File inputFile) {
		if(inputFile != null) {
			try {
				FileInputStream fis = new FileInputStream(inputFile);
		        InputStreamReader in = new InputStreamReader(fis, Charset.forName("UTF-8"));
		        char[] buffer = new char[charBuffer];
		        int n = in.read(buffer);
		        content = new String(buffer, 0, n+1);
		        in.close();
			}
			catch(Exception exc){
				exc.printStackTrace();
			}
		}
	}

	// get the inner component of the scrollpane
	public JTextPane getTextPane() {
		JViewport viewport = tabComponent.getViewport();
		return (JTextPane)viewport.getView();
	}
	
	// saves the file with the current text content in the scrollpane
	public void saveFile() {
		try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            String currentContent = getTextPane().getText();
            writer.write(currentContent);
            writer.close();
            content = currentContent;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
	}
	// gets unsaved changes to this tab, returns true if there are unsaved changes
	public boolean unsavedChanges()
	{
		return content.compareTo(getTextPane().getText()) != 0;
	}
}
