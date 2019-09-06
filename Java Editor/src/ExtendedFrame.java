import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

// The entire frame for the application including the menu bar and editor tabs (may break up later)
@SuppressWarnings("serial")
class ExtendedJFrame extends JFrame implements ActionListener{
	//public File selectedFile;
	// TODO - maybe get file tabs for storage and save purposes
	//public LinkedList<FileTab> tabs;
	// Editor tabs for JTextPane
	public JTabbedPane tabPane;
	public ExtendedJFrame() {
		tabPane = new JTabbedPane();
		createMenuBar();
	}
	// Creates the menu bar for application interactions using ActionListener
	public void createMenuBar() {		
		JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuProj = new JMenu("Project");
        JMenu menuSpacer = new JMenu("|");
        JMenu menuProg = new JMenu("Program");
        
        JMenuItem newFileMenuItem = new JMenuItem("New");
        newFileMenuItem.setActionCommand("NewFile");
        newFileMenuItem.addActionListener(this);
        JMenuItem openFileMenuItem = new JMenuItem("Open");
        openFileMenuItem.setActionCommand("OpenFile");
        openFileMenuItem.addActionListener(this);
        JMenuItem saveFileMenuItem = new JMenuItem("Save");
        saveFileMenuItem.setActionCommand("SaveFile");
        saveFileMenuItem.addActionListener(this);
        JMenuItem closeFileMenuItem = new JMenuItem("Close");
        closeFileMenuItem.setActionCommand("CloseFile");
        closeFileMenuItem.addActionListener(this);
        
        JMenuItem newProjMenuItem = new JMenuItem("New");
        newProjMenuItem.setActionCommand("NewProj");
        newProjMenuItem.addActionListener(this);
        JMenuItem openProjMenuItem = new JMenuItem("Open");
        openProjMenuItem.setActionCommand("OpenProj");
        openProjMenuItem.addActionListener(this);
        JMenuItem saveProjMenuItem = new JMenuItem("Save");
        saveProjMenuItem.setActionCommand("SaveProj");
        saveProjMenuItem.addActionListener(this);
        JMenuItem closeProjMenuItem = new JMenuItem("Close");
        closeProjMenuItem.setActionCommand("CloseProj");
        closeProjMenuItem.addActionListener(this);
        
        JMenuItem compileProgMenuItem = new JMenuItem("Compile");
        compileProgMenuItem.setActionCommand("Compile");
        compileProgMenuItem.addActionListener(this);
        JMenuItem executeProgMenuItem = new JMenuItem("Execute");
        executeProgMenuItem.setActionCommand("Execute");
        executeProgMenuItem.addActionListener(this);
        
        menuSpacer.setEnabled(false);
        
        menuFile.add(newFileMenuItem);
        menuFile.add(openFileMenuItem);
        menuFile.add(saveFileMenuItem);
        menuFile.add(closeFileMenuItem);
        
        menuProj.add(newProjMenuItem);
        menuProj.add(openProjMenuItem);
        menuProj.add(saveProjMenuItem);
        menuProj.add(closeProjMenuItem);
        
        menuProg.add(compileProgMenuItem);
        menuProg.add(executeProgMenuItem);
        
        menuBar.add(menuFile);
        menuBar.add(menuProj);
        menuBar.add(menuSpacer);
        menuBar.add(menuProg);
        
        this.setJMenuBar(menuBar);
	}
	// Creates an editor tab for the selected file through *File -> Open*
	public void createFileContentArea(FileTab fileData) {
		JTextPane panelTextPane = new JTextPane();
		JScrollPane panelScrollPane = new JScrollPane(panelTextPane);
		
		panelTextPane.setText(fileData.content);
		panelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		tabPane.add(fileData.fileName, panelScrollPane);
		
		this.getContentPane().add(tabPane, BorderLayout.CENTER);
		this.setSize(1000,800);
		this.setVisible(true);
		
		// Listener for code highlighting
		panelTextPane.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				EditorTextHighlight eth = new EditorTextHighlight(panelTextPane);
				eth.execute();
			}
		});
	}
	// Events for JMenuBar item interactions
	@Override
	public void actionPerformed(ActionEvent ae) {
		String actionType = ae.getActionCommand();
		switch(actionType) {
			case "NewFile":
				System.out.println("NewFile");
				// TODO
				break;
				
			case "OpenFile":
				System.out.println("OpenFile");
				File selectFile = fileOpen();
				FileTab fileOpened = new FileTab(selectFile);
				createFileContentArea(fileOpened);
				break;
				
			case "SaveFile":
				System.out.println("SaveFile");
				// TODO
				break;
				
			case "CloseFile":
				System.out.println("CloseFile");
				// TODO
				break;	
				
			case "NewProj":
				System.out.println("NewProj");
				// TODO
				break;
				
			case "OpenProj":
				System.out.println("OpenProj");
				// TODO
				break;
				
			case "SaveProj":
				System.out.println("SaveProj");
				// TODO
				break;
				
			case "CloseProj":
				System.out.println("CloseProj");
				// TODO
				break;
				
			case "Compile":
				System.out.println("Compile");
				// TODO
				break;
				
			case "Execute":
				System.out.println("Execute");
				// TODO
				break;
		}
	}
	// Gets a selected file through JFileChooser
	public File fileOpen() {
		JFileChooser chooser = new JFileChooser();
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } 
		else {
			return null;
        }
	}
}