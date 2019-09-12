import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.LinkedList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
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

    public ExtendedJFrame() {
        tabPane = new JTabbedPane();
        tabs = new LinkedList<FileTab>();
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
        fileData.tabComponent = panelScrollPane;
        
        panelTextPane.setText(fileData.content);
        panelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tabPane.add(fileData.fileName, panelScrollPane);

        this.getContentPane().add(tabPane, BorderLayout.CENTER);
        this.setSize(1000, 800);
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
        switch (actionType) {
            case "NewFile":
                System.out.println("NewFile");
                File createFile = fileCreate();
                break;

            case "OpenFile":
                System.out.println("OpenFile");
                File selectFile = fileOpen();
                if (selectFile != null) {
                    FileTab fileOpened = new FileTab(selectFile, false);
                    tabs.add(fileOpened);
                    createFileContentArea(fileOpened);
                }
                break;

            case "SaveFile":
                System.out.println("SaveFile");
                // TODO
                break;

            case "CloseFile":
                System.out.println("CloseFile");
                // TODO
                // Ivan is doing this
                break;

            case "NewProj":
                System.out.println("NewProject");
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
                break;

            case "OpenProj":
                System.out.println("OpenProj");
                // TODO close current project if new is opened
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
                break;

            case "SaveProj":
                System.out.println("SaveProj");
                // TODO
                for (int i = 0; i < tabs.size(); i++) {
                    if (tabs.get(i).isProjectFile) {
                        projFileSave(tabs.get(i).file, tabs.get(i).getTextPane().getText());
                    }
                }
                break;

            case "CloseProj":
                System.out.println("CloseProj");
                for(int i = 0; i < tabs.size(); i++) {
                	if(tabs.get(i).isProjectFile) {
                		tabPane.remove(tabs.get(i).tabComponent);
                		tabs.remove(i);
                		i--;
                	}
                }
                projPath = null;
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

    // create a new file through JFileChooser
    public File fileCreate() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
        chooser.addChoosableFileFilter(new MyFileFilter("Java(.java)", ".java"));
        chooser.addChoosableFileFilter(new MyFileFilter("C(.c)", ".c"));
        chooser.addChoosableFileFilter(new MyFileFilter("C++(.cpp)", ".cpp"));
        chooser.addChoosableFileFilter(new MyFileFilter("Text(.txt)", ".txt"));
        chooser.addChoosableFileFilter(new MyFileFilter("HTML(.html)", ".html"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File newFile;
            File file = chooser.getSelectedFile();
            if (file.getName() == null) return null;
            BufferedWriter bw;
            MyFileFilter filter = (MyFileFilter) chooser.getFileFilter();
            String ends = filter.getEnds();
            if (file.toString().indexOf(ends) != -1) {
                newFile = file;
            } else {
                newFile = new File(file.getAbsolutePath() + ends);
            }
            try {
                bw = new BufferedWriter(new FileWriter(newFile));
                bw.flush();
                bw.close();
                return newFile;
            } catch (IOException e1) {
                e1.printStackTrace();
            }

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

    // Saves a project with String
    public void projFileSave(File file, String content) {
        try {
            PrintWriter writer = new PrintWriter(file, "UTF-8");
            writer.write(content);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // For file filter to save different types of files
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
