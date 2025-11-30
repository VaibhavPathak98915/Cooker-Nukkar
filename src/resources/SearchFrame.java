package resources;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchFrame extends JFrame {
    JTextField searchField = new JTextField(22);
    JButton searchBtn = new JButton("Search");
    JTextArea resultArea = new JTextArea(8, 36);
    String username;

    public SearchFrame(String username,SQLInteractor db) {
        this.username = username;
        setTitle("Recipe Search");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // ------------ MENUBAR -------------
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem addBookmarkItem = new JMenuItem("Add Bookmark");
        JMenuItem addRecipeItem = new JMenuItem("Add Recipe");
        fileMenu.add(addBookmarkItem);
        fileMenu.add(addRecipeItem);

        JMenu viewMenu = new JMenu("View");
        JMenuItem bookmarksItem = new JMenuItem("Bookmarks");
        JMenuItem historyItem = new JMenuItem("History");
        JMenuItem userProfileItem = new JMenuItem("User Profile");
        viewMenu.add(bookmarksItem);
        viewMenu.add(historyItem);
        viewMenu.add(userProfileItem);

        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem quitItem = new JMenuItem("Quit");
        JMenuItem helpItem = new JMenuItem("Help");
        toolsMenu.add(quitItem);
        toolsMenu.add(helpItem);

        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(toolsMenu);
        setJMenuBar(menuBar);

        // Actions for demonstration
        addBookmarkItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Add Bookmark clicked"));
        addRecipeItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Add Recipe clicked"));
        bookmarksItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Bookmarks menu selected"));
        historyItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "History menu selected"));
        userProfileItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "User Profile menu selected"));
        helpItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Help clicked. Instructions here."));
        quitItem.addActionListener(e -> {db.close();System.exit(0);});

        // ----------- UI CONTENT BELOW MENUBAR -----------
        ImageIcon imgIcon = new ImageIcon(getClass().getResource("logo.png"));
        BackgroundPanel bgPanel = new BackgroundPanel(imgIcon.getImage());
        bgPanel.setLayout(new GridBagLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setMaximumSize(new Dimension(800, Integer.MAX_VALUE));

        JLabel greet = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        greet.setFont(new Font("Arial", Font.BOLD, 36));
        greet.setForeground(new Color(33, 33, 99));
        greet.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(28));
        centerPanel.add(greet);
        centerPanel.add(Box.createVerticalStrut(32));

        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchBarPanel.setOpaque(false);
        searchField.setFont(new Font("Arial", Font.PLAIN, 22));
        searchField.setPreferredSize(new Dimension(340, 38));
        searchBtn.setFont(new Font("Arial", Font.BOLD, 22));
        searchBtn.setBackground(new Color(66, 133, 244));
        searchBtn.setForeground(Color.WHITE);
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchBtn);

        centerPanel.add(searchBarPanel); // Search bar is always above results
        centerPanel.add(Box.createVerticalStrut(22));

        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 20));
        resultArea.setBackground(new Color(245, 245, 245, 200));
        resultArea.setForeground(new Color(44, 62, 80));
        resultArea.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setMaximumSize(new Dimension(700, 220));
        scrollPane.setPreferredSize(new Dimension(620, 200));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        centerPanel.add(scrollPane); // Results area, always just below search bar

        bgPanel.add(centerPanel, new GridBagConstraints());

        Runnable doSearch = () -> {
            String query = searchField.getText().trim();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search term.", "No query", JOptionPane.WARNING_MESSAGE);
            } else {
                showResults(query,db);
            }
        };
        searchBtn.addActionListener(e -> doSearch.run());
        searchField.addActionListener(e -> doSearch.run());

        setContentPane(bgPanel);
        setVisible(true);
    }

    private void showResults(String query,SQLInteractor db) {
        String temp_str="";
        try{
            ResultSet temp_rs = db.searchByTitle(query);
            int i=1;
            while(temp_rs.next()){
                temp_str=temp_str+"Recipe"+i+":"+temp_rs.getString("Title")+"\n------\n";
                i++;
            }
            resultArea.setText(temp_str);
        }
        catch(SQLException e){
            temp_str=temp_str+"\nPROBLEM LOADING THE SEARCH RESULTS!!";
            resultArea.setText(temp_str);
            System.out.println("An SQLException occured:"+e.getMessage());
        }
        catch(Exception e){
            temp_str=temp_str+"\nPROBLEM LOADING THE SEARCH RESULTS!!";
            resultArea.setText(temp_str);
            System.out.println("A Random Exception occured:"+e.getMessage());
        }
    }
}
