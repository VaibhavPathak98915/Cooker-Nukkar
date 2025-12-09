package resources;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

class ImageTransferable implements Transferable {
    private Image image;

    public ImageTransferable(Image image) {
        this.image = image;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { DataFlavor.imageFlavor };
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.imageFlavor.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return image;
    }
}


public class OpenRecipe extends JFrame {
    String Title;
    String Ingredients;
    String Instructions;
    String Image_Path;
    SQLInteractor db;
    public OpenRecipe(SQLInteractor db, int recipe_id){
        this.db=db;
        try{
            ResultSet Recipe = db.getRecipeById(recipe_id);
            Title=Recipe.getString("Title");
            Ingredients=toBulletsBySpace(Recipe.getString("Ingredients"));
            Instructions=toBulletsByDot(Recipe.getString("Instructions"));
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
        JMenuItem removeBookmarkItem = new JMenuItem("Remove Bookmark");
        JMenuItem ingredientSubstitute = new JMenuItem("Substitute Ingredient");
        fileMenu.add(addBookmarkItem);
        fileMenu.add(removeBookmarkItem);
        fileMenu.add(ingredientSubstitute);

        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem closeRecipe = new JMenuItem("Close");
        JMenuItem helpItem = new JMenuItem("Help");
        toolsMenu.add(closeRecipe);
        toolsMenu.add(helpItem);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        setJMenuBar(menuBar);

        // Actions for demonstration
        addBookmarkItem.addActionListener(e -> {
            try{
                db.addBookmark(recipe_id);
                JOptionPane.showMessageDialog(this, "Bookmark to this recipe added");
                addBookmarkItem.setEnabled(false);
                removeBookmarkItem.setEnabled(true);

            }
            catch(SQLException err){
                System.out.println("SQLException occured:"+err.getMessage());
            }
            catch(Exception err){
                System.out.println("A Random Error occured:"+err.getMessage());
            }
        });
        removeBookmarkItem.addActionListener(e -> {
            try{
                db.removeBookmark(recipe_id);
                JOptionPane.showMessageDialog(this, "Bookmark to this recipe removed");
                removeBookmarkItem.setEnabled(false);
                addBookmarkItem.setEnabled(true);
            }
            catch(SQLException err){
                System.out.println("SQLException occured:"+err.getMessage());
            }
            catch(Exception err){
                System.out.println("A Random Error occured:"+err.getMessage());
            }
        });
        ingredientSubstitute.addActionListener(e -> openIngredientSubstitute());
        helpItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Help clicked. Instructions here."));
        closeRecipe.addActionListener(e -> {this.dispose();});

        // ----------- UI CONTENT BELOW MENUBAR -----------
        ImagePanel imagePanel = new ImagePanel(Image_Path);
        imagePanel.setBorder(BorderFactory.createTitledBorder("Image:"));
        JTextArea Ingredients_ta = new JTextArea((this.getWidth())/3,(this.getHeight())/3);
        Ingredients_ta.setEditable(false);
        Ingredients_ta.setLineWrap(true);
        Ingredients_ta.setWrapStyleWord(true);
        Ingredients_ta.setFont(new Font("Times New Roman",Font.PLAIN,14));
        Ingredients_ta.setBackground(new Color(245, 245, 245, 200));
        Ingredients_ta.setForeground(new Color(44, 62, 80));
        Ingredients_ta.setBorder(BorderFactory.createTitledBorder("Ingredients:"));
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
        Instructions_ta.setBorder(BorderFactory.createTitledBorder("Instructions:"));
        Instructions_ta.setText(Instructions);
        JScrollPane ins_ScrollPane = new JScrollPane(Instructions_ta);
        ins_ScrollPane.setBorder(BorderFactory.createEmptyBorder());
        ins_ScrollPane.setMaximumSize(new Dimension(700, 220));
        ins_ScrollPane.setPreferredSize(new Dimension(620, 200));
        ins_ScrollPane.setOpaque(false);
        ins_ScrollPane.getViewport().setOpaque(false);
        //Copy Buttons
        JButton copyIngredients = createButton("Copy Ingredients", new Color(66, 133, 244));
        JButton copyInstructions = createButton("Copy Instructions", new Color(66, 133, 244));
        JButton copyImage = createButton("Copy Image", new Color(66, 133, 244));
        ing_ScrollPane.add(copyIngredients);
        ins_ScrollPane.add(copyInstructions);
        imagePanel.add(copyImage);
        this.add(ing_ScrollPane);
        this.add(ins_ScrollPane);
        this.add(imagePanel);
        
        //ActionListners
        copyIngredients.addActionListener(e ->{
            copyToClipboard(Ingredients);
        });
        copyInstructions.addActionListener(e ->{
            copyToClipboard(Instructions);
        });
        copyImage.addActionListener(e ->{
            Image imageToCopy;
            try{
                ImageIcon icon = new ImageIcon(Image_Path);
                imageToCopy= icon.getImage();
                if (imageToCopy != null) {
                    copyToClipboard(imageToCopy);
                }
            }
            catch(Exception err){System.out.println("Failed to load Image");}
        });

        setSize(1020, 680);
        setVisible(true);
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 22));
        return btn;
    }

    // ingredients: split by ', '
    private String toBulletsBySpace(String text) {

        String[] parts = text.split("', '");// split by one or more spaces [web:96][web:99][web:102]
        parts[0]=parts[0].replace("['","");
        parts[(parts.length)-1]=parts[(parts.length)-1].replace("']","");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            String trimmed = p.trim();
            if (!trimmed.isEmpty()) {
                sb.append("• ").append(trimmed).append('\n');
            }
        }
        return sb.toString();
    }

    // instructions: split by '.'
    private String toBulletsByDot(String text) {
        String[] parts = text.split("\\."); // split by dot [web:78][web:79]
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            String trimmed = p.trim();
            if (!trimmed.isEmpty()) {
                sb.append("• ").append(trimmed).append(".\n");
            }
        }
        return sb.toString();
    }
    private void copyToClipboard(String text){
        StringSelection str = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(str,null);
    }

    private void copyToClipboard(Image image){
        ImageTransferable transferable = new ImageTransferable(image);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(transferable,null);
    }

    private void openIngredientSubstitute(){
        SwingUtilities.invokeLater(() -> {
            new IngredientSubstitute(db).setVisible(true);
        });
    }
}