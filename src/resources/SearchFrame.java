package resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchFrame extends JFrame {
    JTextField searchField = new JTextField(22);
    JButton searchBtn = new JButton("Search");
    
    DefaultListModel<ListItem> resultModel = new DefaultListModel<>();
    JList<ListItem> resultList = new JList<>(resultModel);

    String username;
    SQLInteractor db;

    public SearchFrame(String username, SQLInteractor db) {
        this.username = username;
        this.db = db;

        setMinimumSize(new Dimension(1020, 680));

        setTitle("Recipe Search");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // ------------ MENUBAR -------------
        JMenuBar menuBar = new JMenuBar();

        JMenu viewMenu = new JMenu("View");
        JMenuItem userProfileItem = new JMenuItem("User Profile");
        viewMenu.add(userProfileItem);

        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem quitItem = new JMenuItem("Quit");
        JMenuItem helpItem = new JMenuItem("Help");
        toolsMenu.add(quitItem);
        toolsMenu.add(helpItem);

        menuBar.add(viewMenu);
        menuBar.add(toolsMenu);
        setJMenuBar(menuBar);

        // Actions for demonstration
        userProfileItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "User name:"+username));
        helpItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Help clicked. Instructions here."));
        quitItem.addActionListener(e -> { db.close(); System.exit(0); });

        // ----------- UI CONTENT BELOW MENUBAR -----------
        ImageIcon imgIcon = new ImageIcon(getClass().getResource("logo.png"));
        BackgroundPanel bgPanel = new BackgroundPanel(imgIcon.getImage());
        bgPanel.setLayout(new GridBagLayout());

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setMaximumSize(new Dimension(800, Integer.MAX_VALUE));

        JLabel greet = new JLabel("Welcome " + username + "!", SwingConstants.CENTER);
        greet.setFont(new Font("Arial", Font.BOLD, 30));
        greet.setForeground(new Color(33, 33, 99));
        greet.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(Box.createVerticalStrut(28));
        centerPanel.add(greet);
        centerPanel.add(Box.createVerticalStrut(15));

        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        searchBarPanel.setOpaque(false);
        searchField.setFont(new Font("Arial", Font.PLAIN, 22));
        searchField.setPreferredSize(new Dimension(340, 45));
        searchBtn.setFont(new Font("Arial", Font.BOLD, 22));
        searchBtn.setBackground(new Color(66, 133, 244));
        searchBtn.setForeground(Color.WHITE);
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchBtn);

        centerPanel.add(searchBarPanel);
        centerPanel.add(Box.createVerticalStrut(22));

        // Configure JList as results area
        resultList.setFont(new Font("SansSerif", Font.PLAIN, 20));
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultList.setVisibleRowCount(8);
        resultList.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JScrollPane scrollPane = new JScrollPane(resultList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setMaximumSize(new Dimension(700, 420));
        scrollPane.setPreferredSize(new Dimension((this.getWidth())/2, 400));
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        centerPanel.add(scrollPane);

        bgPanel.add(centerPanel, new GridBagConstraints());

        // Search action
        Runnable doSearch = () -> {
            String query = searchField.getText().trim();
            if (query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search term.", "No query", JOptionPane.WARNING_MESSAGE);
            } else {
                showResults(query);
            }
        };
        searchBtn.addActionListener(e -> doSearch.run());
        searchField.addActionListener(e -> doSearch.run());

        // Double-click on result = open detail
        resultList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // double-click
                    int index = resultList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        ListItem item = resultModel.getElementAt(index);
                        int recipe_id = item.getId();
                        if (recipe_id >= 0) {
                            openRecipeDetail(recipe_id);
                        }
                    }
                }
            }
        });

        setContentPane(bgPanel);
        setVisible(true);
    }

    private void showResults(String query) {
        resultModel.clear();
        try {
            ResultSet rs = db.searchByTitle(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("Title");
                ListItem temp=new ListItem(id, title);
                resultModel.addElement(temp);
            }
            if (resultModel.isEmpty()) {
                ListItem temp = new ListItem("No recipes found");
                resultModel.addElement(temp);
            }
        } catch (SQLException e) {
            ListItem temp = new ListItem("PROBLEM LOADING THE SEARCH RESULTS!!");
            resultModel.addElement(temp);
            System.out.println("An SQLException occurred: " + e.getMessage());
        } catch (Exception e) {
            ListItem temp = new ListItem("PROBLEM LOADING THE SEARCH RESULTS!!");
            resultModel.addElement(temp);
            System.out.println("A Random Exception occurred: " + e.getMessage());
        }
    }

    private void openRecipeDetail(int id) {
        SwingUtilities.invokeLater(()->new OpenRecipe(db,id));
    }
}
