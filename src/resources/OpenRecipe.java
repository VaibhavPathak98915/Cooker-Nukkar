package resources;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import java.awt.*;


public class OpenRecipe extends JFrame {
    String Title;
    String Ingredients;
    String Instructions;
    String Image_Path;
    public OpenRecipe(SQLInteractor db, int recipe_id){
        try{
            ResultSet Recipe = db.getRecipeById(recipe_id);
            Title=Recipe.getString("Title");
            Ingredients=Recipe.getString("Ingredients");
            Instructions=Recipe.getString("Instructions");
            Image_Path="bin/resources/images/Food Images/"+Recipe.getString("Image_Name")+".jpg";
        }
        catch(SQLException e){
            System.out.println("An SQLException occured:"+e.getMessage());
        }
        catch(Exception e){
            System.out.println("A Random Error occured:"+e.getMessage());
        }
        this.setTitle("Recipe:"+Title);
        this.setLayout(new GridLayout(1,3));//Layout Manager
        
        //MenuBar
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
        JMenuItem closeRecipe = new JMenuItem("Close");
        JMenuItem helpItem = new JMenuItem("Help");
        toolsMenu.add(closeRecipe);
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
        closeRecipe.addActionListener(e -> {this.dispose();});

        // ----------- UI CONTENT BELOW MENUBAR -----------
        ImagePanel imagePanel = new ImagePanel(Image_Path);
        JTextArea Ingredients_ta = new JTextArea((this.getWidth())/3,(this.getHeight())/3);
        Ingredients_ta.setEditable(false);
        Ingredients_ta.setLineWrap(true);
        Ingredients_ta.setWrapStyleWord(true);
        Ingredients_ta.setFont(new Font("Times New Roman",Font.PLAIN,14));
        Ingredients_ta.setBackground(new Color(245, 245, 245, 200));
        Ingredients_ta.setForeground(new Color(44, 62, 80));
        Ingredients_ta.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        Ingredients_ta.setText(Ingredients);
        JScrollPane ing_ScrollPane = new JScrollPane(Ingredients_ta);
        ing_ScrollPane.setBorder(BorderFactory.createEmptyBorder());
        ing_ScrollPane.setMaximumSize(new Dimension(700, 220));
        ing_ScrollPane.setPreferredSize(new Dimension(620, 200));
        ing_ScrollPane.setOpaque(false);
        ing_ScrollPane.getViewport().setOpaque(false);
        JTextArea Instructions_ta = new JTextArea((this.getWidth())/3,this.getHeight()/3);
        Instructions_ta.setEditable(false);
        Instructions_ta.setLineWrap(true);
        Instructions_ta.setWrapStyleWord(true);
        Instructions_ta.setFont(new Font("Times New Roman",Font.PLAIN,14));
        Instructions_ta.setBackground(new Color(245, 245, 245, 200));
        Instructions_ta.setForeground(new Color(44, 62, 80));
        Instructions_ta.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        Instructions_ta.setText(Instructions);
        JScrollPane ins_ScrollPane = new JScrollPane(Instructions_ta);
        ins_ScrollPane.setBorder(BorderFactory.createEmptyBorder());
        ins_ScrollPane.setMaximumSize(new Dimension(700, 220));
        ins_ScrollPane.setPreferredSize(new Dimension(620, 200));
        ins_ScrollPane.setOpaque(false);
        ins_ScrollPane.getViewport().setOpaque(false);
        this.add(ing_ScrollPane);
        this.add(ins_ScrollPane);
        this.add(imagePanel);
        setSize(1020, 680);
        setVisible(true);
    }
}