package application;
	
import java.beans.EventHandler;
import java.io.IOException;
import java.lang.Class;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


public class Main extends Application {
    TabPane tabPane=new TabPane();
    Tab firstTab=new Tab("New Tab");
    Tab newTab=new Tab();

    public Main() throws IOException {
    }
	@Override  
    public void start(Stage primaryStage) throws IOException {  
        init(primaryStage);  
        primaryStage.show();  
    }  
    private void init(Stage primaryStage) throws IOException{  
        BorderPane root=new BorderPane();
        root.setPrefSize(1280,720);
        Scene scene=new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setTitle("CHEN BROWSER v1.0");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        
        //getTabPaneView(addListener
        tabPane.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
        	public void changed(ObservableValue<? extends Tab> ov,Tab t,Tab newSelectedTab) {
        		if(tabPane.getTabs().size()==1)
        			Platform.exit();
        		if(newSelectedTab==newTab) {
        			Platform.runLater(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							addNewTab(tabPane, newTab);
						}
					});
        		}
        	}
		});
        newTab.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("add.png"))));
          
        //加载主页
        //跳转网页后更新地址栏   
        //跳转网页更新标题栏为当前页Title
       
        //设置按钮及其监听器
       
        //容器添加控件       
        
        //添加新标签页
        firstTab=new TabUI().getNewTab();      
        tabPane.getTabs().addAll(firstTab,newTab);
        tabPane.setTabClosingPolicy(TabClosingPolicy.SELECTED_TAB);
        root.setCenter(tabPane);         
    }  
    public void addNewTab(TabPane tabPane,Tab newTab) {
		Tab tab=new TabUI().getNewTab();
		ObservableList<Tab> tabs=tabPane.getTabs();
		Platform.runLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				tabs.add(tabs.size()-1,tab);
				SingleSelectionModel<Tab> selectedTab=tabPane.getSelectionModel();
				selectedTab.select(tab);
			}
		});
	}
    
    public static void main(String[] args) {  
        launch(args);  
    }  
}
