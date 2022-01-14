/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*ww  w.j ava2  s .co  m*/
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class Main extends JPanel implements ActionListener {
  int newNodeSuffix = 1;
  static String ADD_COMMAND = "add";
  static String REMOVE_COMMAND = "remove";
  static String CLEAR_COMMAND = "clear";
  DynamicTree  treePanel = new DynamicTree();

  public Main() {
    super(new BorderLayout());
    populateTree(treePanel);
    JButton addButton = new JButton("Add");
    addButton.setActionCommand(ADD_COMMAND);
    addButton.addActionListener(this);
    JButton removeButton = new JButton("Remove");
    removeButton.setActionCommand(REMOVE_COMMAND);
    removeButton.addActionListener(this);
    JButton clearButton = new JButton("Clear");
    clearButton.setActionCommand(CLEAR_COMMAND);
    clearButton.addActionListener(this); 
    treePanel.setPreferredSize(new Dimension(300, 150));
    add(treePanel, BorderLayout.CENTER);
    JPanel panel = new JPanel(new GridLayout(0, 3));
    panel.add(addButton);
    panel.add(removeButton);
    panel.add(clearButton);
    add(panel, BorderLayout.SOUTH);
  }

  public void populateTree(DynamicTree treePanel) {
    String p1Name = "Parent 1";
    String p2Name = "Parent 2";
    String c1Name = "Child 1";
    String c2Name = "Child 2";
    DefaultMutableTreeNode p1, p2;
    p1 = treePanel.addObject(null, p1Name);
    p2 = treePanel.addObject(null, p2Name);
    treePanel.addObject(p1, c1Name);
    treePanel.addObject(p1, c2Name);
    treePanel.addObject(p2, c1Name);
    treePanel.addObject(p2, c2Name);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String command = e.getActionCommand();
    if (ADD_COMMAND.equals(command)) {
      treePanel.addObject("New Node " + newNodeSuffix++);
    } else if (REMOVE_COMMAND.equals(command)) {
      treePanel.removeCurrentNode();
    } else if (CLEAR_COMMAND.equals(command)) { 
      treePanel.clear();
    }
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
    Main newContentPane = new Main();
    frame.setContentPane(newContentPane); 
    frame.pack();
    frame.setVisible(true);
  }
}

class DynamicTree extends JPanel {
  DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root Node");
  DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
  JTree tree;

  public DynamicTree() {
    super(new GridLayout(1, 0));
    treeModel.addTreeModelListener(new MyTreeModelListener());
    tree = new JTree(treeModel);
    tree.setEditable(true);
    tree.getSelectionModel().setSelectionMode(
        TreeSelectionModel.SINGLE_TREE_SELECTION);
    tree.setShowsRootHandles(true);
    JScrollPane scrollPane = new JScrollPane(tree);
    add(scrollPane);
  }

  public void clear() {
    rootNode.removeAllChildren();
    treeModel.reload();
  }

  public void removeCurrentNode() {
    TreePath currentSelection = tree.getSelectionPath();
    if (currentSelection != null) {
      DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection
          .getLastPathComponent());
      MutableTreeNode parent = (MutableTreeNode) (currentNode.getParent());
      if (parent != null) {
        treeModel.removeNodeFromParent(currentNode);
        return;
      }
    }
  }

  public DefaultMutableTreeNode addObject(Object child) {
    DefaultMutableTreeNode parentNode = null;
    TreePath parentPath = tree.getSelectionPath();
    if (parentPath == null) {
      parentNode = rootNode;
    } else {
      parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
    }
    return addObject(parentNode, child, true);
  }

  public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
      Object child) {
    return addObject(parent, child, false);
  }

  public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
      Object child, boolean shouldBeVisible) {
    DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
    if (parent == null) {
      parent = rootNode;
    }
    treeModel.insertNodeInto(childNode, parent, parent.getChildCount());
    if (shouldBeVisible) {
      tree.scrollPathToVisible(new TreePath(childNode.getPath()));
    }
    return childNode;
  }

  class MyTreeModelListener implements TreeModelListener {

    @Override
    public void treeNodesChanged(TreeModelEvent e) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) (e.getTreePath()
          .getLastPathComponent()); 
      int index = e.getChildIndices()[0];
      node = (DefaultMutableTreeNode) (node.getChildAt(index));
      System.out.println("New value NodesChanged: " + node.getUserObject());
    }

    @Override
    public void treeNodesInserted(TreeModelEvent e) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) (e.getTreePath()
          .getLastPathComponent()); 
      int index = e.getChildIndices()[0];
      node = (DefaultMutableTreeNode) (node.getChildAt(index));
      System.out.println("New value NodesInserted : " + node.getUserObject());
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent e) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) (e.getTreePath()
          .getLastPathComponent()); 
      int index = e.getChildIndices()[0];
      node = (DefaultMutableTreeNode) (node.getChildAt(index));
      System.out.println("New value NodesRemoved : " + node.getUserObject());
    }

    @Override
    public void treeStructureChanged(TreeModelEvent e) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) (e.getTreePath()
          .getLastPathComponent());
      int index = e.getChildIndices()[0];
      node = (DefaultMutableTreeNode) (node.getChildAt(index));
      System.out.println("New value StructureChanged : " + node.getUserObject());
    }
  }
}