package com.javaedit;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;


// The entire frame for the application including the menu bar and editor tabs (may break up later)
@SuppressWarnings("serial")
class ExtendedJFrame extends JFrame implements ActionListener {
    //public File selectedFile;
    // get file tabs for storage and save purposes
    public LinkedList<FileTab> tabs;
    // Editor tabs for JTextPane
    public JTabbedPane tabPane;
    // File path for the selected project folder
    public File projPath;
    // Label for tracking number of keywords in current selected tab
    public JLabel keywordsTrack;

    public ExtendedJFrame() {
        tabPane = new JTabbedPane();
        tabs = new LinkedList<FileTab>();
        createKeywordsLabel();
        createMenuBar();
    }

    public void createKeywordsLabel() {
    	keywordsTrack = new JLabel();
        keywordsTrack.setText("Number of Keywords: ");
        this.getContentPane().add(keywordsTrack, BorderLayout.SOUTH);
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
        JMenuItem removeFileMenuItem = new JMenuItem("Remove");
        removeFileMenuItem.setActionCommand("RemoveFile");
        removeFileMenuItem.addActionListener(this);

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
        menuFile.add(removeFileMenuItem);

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
        final JTextPane panelTextPane = new JTextPane();
        panelTextPane.setFont(new Font("default", Font.PLAIN, 13));
        TextLineNumber tln = new TextLineNumber(panelTextPane);
        JScrollPane panelScrollPane = new JScrollPane(panelTextPane);
        panelScrollPane.setRowHeaderView(tln);
        fileData.tabComponent = panelScrollPane;

        panelTextPane.setText(fileData.content);
        panelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panelScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        if(fileData.isProjectFile)
        	tabPane.add(fileData.fileName + " - Project", panelScrollPane);
        else
        	tabPane.add(fileData.fileName, panelScrollPane);

        this.getContentPane().add(tabPane, BorderLayout.CENTER);
        this.setSize(1000, 800);
        this.setVisible(true);

        // Listener for code highlighting
        panelTextPane.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent arg0) {
                EditorTextHighlight eth = new EditorTextHighlight(panelTextPane, keywordsTrack);
                eth.execute();
            }
        });
    }

    // Events for JMenuBar item interactions
    @Override
    public void actionPerformed(ActionEvent ae) {
        String actionType = ae.getActionCommand();
        switch (actionType) {
            case "NewFile":
                System.out.println("NewFile");
                newFile();
                break;

            case "OpenFile":
                System.out.println("OpenFile");
                openFile();
                break;

            case "SaveFile":
                System.out.println("SaveFile");
                saveFile((JScrollPane) tabPane.getSelectedComponent());
                break;

            case "CloseFile":
                System.out.println("CloseFile");
                fileClose();
                break;

            case "RemoveFile":
            	System.out.println("RemoveFile");
            	removeFile();
                break;

            case "NewProj":
                System.out.println("NewProject");
                closeProject();
                newProject();
                break;

            case "OpenProj":
                System.out.println("OpenProj");
                closeProject();
                openProject();
                break;

            case "SaveProj":
                System.out.println("SaveProj");
                saveProject();
                break;

            case "CloseProj":
                System.out.println("CloseProj");
                closeProject();
                break;

            case "Compile":
                System.out.println("Compile");
                compileProject();
                break;

            case "Execute":
                System.out.println("Execute");
                executeProject();
                break;
        }
    }

    public void executeProject() {
    	String basePath = System.getProperty("user.dir");
    	String currentProjectPath = projPath.getAbsolutePath();
    	//String relativePath = new File(projPath.toURI().relativize(new File(basePath).toURI())).getPath();
    	//String otherRelativePath = new File(basePath).toURI().relativize(projPath.toURI()).getPath();
    	Path pbase = Paths.get(basePath);
    	Path pcur = Paths.get(projPath.getAbsolutePath());
    	//Path prel = pbase.relativize(pcur);
    	Path prel2 = pcur.relativize(pbase);
    	//System.out.println(prel);
    	System.out.println(prel2);
    	System.out.println(basePath);
    	System.out.println(currentProjectPath);
    	//System.out.println(relativePath);
    	//System.out.println(otherRelativePath);
    	if(projPath != null) {
    		try {
				Runtime.getRuntime().exec("cmd /c start cmd.exe /K "
										+ "\"cd " + currentProjectPath
										+ " && java -javaagent:" + prel2 + "\\src\\main\\resources\\JEAgent.jar Main\"");
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    //
    public void compileProject() {
    	if(projPath != null) {
	    	String commandBuilder = "";
	    	boolean firstExists = false;
	    	for(int i = 0; i < tabs.size(); i++) {
	    		if(tabs.get(i).isProjectFile) {
	    			if(firstExists) {
	    				commandBuilder += " && ";
	    			}
	    			commandBuilder += "javac " + tabs.get(i).fileName;
	    			firstExists = true;
	    		}
	    	}
	    	System.out.println(commandBuilder);
	    	try {
				Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"cd " + projPath.getAbsolutePath() + " && " + commandBuilder + "\"");
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
    
    public void newProject() {
    	//get new folder name
        String folderName = JOptionPane.showInputDialog("Enter a new folder name");

        //Create the folder
        File folderNameCreate = new File(folderName);

        //Checks if the folder exists
        if (!folderNameCreate.exists()) {
            System.out.println("Creating project folder with name: " + folderName);
            folderNameCreate.mkdir();
        } else {
            JOptionPane.showMessageDialog(null, "There is already a folder with the name of: " + folderName);
        }
        projPath = folderNameCreate;
        
        //Create a Main File
        String mainFile = "Main";
        String javaMainFile = mainFile + ".java";
        File makeMainFile = new File(folderNameCreate, javaMainFile);
        try {
            makeMainFile.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Creating main file in the folder: " + folderName);

        //Opening folder now
        FileTab fileOpened1 = new FileTab(makeMainFile, true);
        tabs.add(fileOpened1);
        createFileContentArea(fileOpened1);
    }

    public void openProject() {
    	File selectFolder = folderOpen();
        projPath = selectFolder;
        if (selectFolder != null) {
            LinkedList<File> javaFiles = getJavaFiles(selectFolder);
            for (int i = 0; i < javaFiles.size(); i++) {
                FileTab fileOpened = new FileTab(javaFiles.get(i), true);
                tabs.add(fileOpened);
                createFileContentArea(fileOpened);
            }
        }
        validateActiveProject();
    }

    public void closeProject() {
    	for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).isProjectFile) {
                if(tabs.get(i).unsavedChanges())
                {
                    System.out.println("Unsaved Changes on file");
                    Object[] options = { "Save", "Cancel", "Don't Save"};
                    String x = tabs.get(i).fileName + " has unsaved changes, do you want to save them?";
                    int result = JOptionPane.showOptionDialog(null,x, "Unsaved Changes", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if(result == 0) {
                    	saveFile(tabs.get(i).tabComponent);
                    }
                    else if(result == 1) {
                    	return;
                    }	
                }
                tabPane.remove(tabs.get(i).tabComponent);
                tabs.remove(i);
                i--;
            }
        }
        projPath = null;
    }

    public void saveProject() {
    	for (int i = 0; i < tabs.size(); i++) {
            if (tabs.get(i).isProjectFile) {
            	tabs.get(i).saveFile();
            }
        }
    }

    public void newFile() {
    	File createFile = fileCreate();
        if (createFile != null) {
        	FileTab fileOpened;
            if(projPath != null && projPath.getAbsolutePath().equals(createFile.getParent())) {
            	fileOpened = new FileTab(createFile, true);
            }
            else {
                fileOpened = new FileTab(createFile, false);
            }
            tabs.add(fileOpened);
            createFileContentArea(fileOpened);
        }
    }
    
    public void openFile() {
    	File selectFile = fileOpen();
        if (selectFile != null) {
            FileTab fileOpened = new FileTab(selectFile, false);
            tabs.add(fileOpened);
            createFileContentArea(fileOpened);
        }
    }
    
    public void removeFile() {
    	JScrollPane selectedComponent = (JScrollPane) tabPane.getSelectedComponent();
    	for (int i = 0; i < tabs.size(); i++) {
    		if(selectedComponent == tabs.get(i).tabComponent) {
    			Object[] options = { "Delete", "Cancel"};
                int result = JOptionPane.showOptionDialog(null,"Are you sure you want to delete " + tabs.get(i).fileName, "Unsaved Changes", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
    			if(result == 0) {
	                if(tabs.get(i).file.delete()) {
	    				tabs.remove(i);
	        			tabPane.remove(selectedComponent);
	        			System.out.println("File deleted successfully");
	        			break;
	    			}
	    			else {
	    				System.err.println("Failed to delete selected file");
	    			}
    			}
    			else {
    				return;
    			}
    		}
    	}
    	validateActiveProject();
    }
    
    public void saveFile(JScrollPane selectedComponent)
    {
        if(tabPane.getTabCount() == 0 || selectedComponent == null)
        {
            System.out.println("Saving failed, No files open");
            return;
        }

        for(int i = 0; i<tabs.size(); i++)
        {
            if(selectedComponent == tabs.get(i).tabComponent)
            {
            	tabs.get(i).saveFile();
                tabs.get(i).content=tabs.get(i).getTextPane().getText();
            }
        }
    }

    // create a new file through JFileChooser
    public File fileCreate() {
        File folder = folderOpen();
        if (folder != null) {
            JTextField file_name = new JTextField();
            Object[] fields = {"Enter a new file name", file_name};
            Object[] obj2 = {".java", ".txt", ".html"};
            String extension = (String) JOptionPane.showInputDialog(null, fields, "Enter a new file name", JOptionPane.PLAIN_MESSAGE, null, obj2, ".java");
            // get new file name
            String filefullname = file_name.getText() + extension;
            System.out.println(filefullname);
            // create new file
            File newfile = new File(folder, filefullname);
            if (filefullname == null || filefullname.equals(""))
                return null;
            if (!newfile.exists()) {
                System.out.println("Creating file with name: " + newfile);
                try {
                    newfile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "There is already a file with the name of: " + newfile);
                return null;
            }
            return newfile;
        }
        return null;
    }

    // Gets a selected file through JFileChooser
    public File fileOpen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }

    //Closes selected tabComponent including files and projects, displays project path of file if its in project
    public void fileClose() {
    	JScrollPane selectedComponent = (JScrollPane) tabPane.getSelectedComponent();
    	for (int i=0; i<tabs.size(); i++) {
    		if (tabs.get(i).tabComponent == selectedComponent) {
                if(tabs.get(i).unsavedChanges())
                {
                    System.out.println("Unsaved Changes on file");
                    Object[] options = { "Save", "Cancel", "Don't Save"};
                    String x = tabs.get(i).fileName + " has unsaved changes, do you want to save them?";
                    int result = JOptionPane.showOptionDialog(null,x, "Unsaved Changes", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if(result == 0) {
                    	saveFile(selectedComponent);
                    }
                    else if(result == 1) {
                    	return;
                    }
                }
    			tabs.remove(i);
    			tabPane.remove(selectedComponent);
    			break;
    		}
    	}
    	validateActiveProject();
    	System.out.println(projPath);
    }

    // Gets the selected folder through JFileChooser
    public File folderOpen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }

    // Gets all java files in a specified directory
    public LinkedList<File> getJavaFiles(File directory) {
        LinkedList<File> javaFiles = new LinkedList<File>();
        File[] allFiles = directory.listFiles();
        for (int i = 0; i < allFiles.length; i++) {
            if (allFiles[i].isFile() && allFiles[i].getName().toLowerCase().contains(".java")) {
                javaFiles.add(allFiles[i]);
            }
        }
        return javaFiles;
    }

    public void validateActiveProject() {
    	boolean isProjectFile = false;
    	for (int j=0; j<tabs.size(); j++) {
    		if (tabs.get(j).isProjectFile) {
    			isProjectFile = true;
    			break;
    		}
    	}
    	if (!isProjectFile) {
    		projPath = null;
    	}
    }

    // For file filter to save different types of files( maybe not useful, you guys can delete it)
    class MyFileFilter extends FileFilter {

        String ends;
        String description;

        public MyFileFilter(String description, String ends) {
            this.ends = ends;
            this.description = description;
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) return true;
            String fileName = f.getName();
            if (fileName.toUpperCase().endsWith(this.ends.toUpperCase())) return true;
            return false;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        public String getEnds() {
            return this.ends;
        }

    }
}
