package application;
import java.beans.EventHandler;
import java.io.IOException;
import java.lang.Class;
import java.security.GeneralSecurityException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXButton.ButtonType;

import org.controlsfx.control.PopOver;
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
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
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
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
public class TabUI {
	public static final String defaultURL="https://www1.szu.edu.cn";//默认加载的网址
    
	JFXButton backward=new JFXButton();
	JFXButton forward=new JFXButton();
	JFXButton refresh=new JFXButton();
	JFXButton download=new JFXButton();
	JFXButton mark=new JFXButton();
	JFXButton menu=new JFXButton();
	JFXButton email=new JFXButton();
	JFXButton bookmarks=new JFXButton();
//	JFXSnackbar hamburger=new JFXSnackbar();
	JFXProgressBar progressBar=new JFXProgressBar();
	JFXSpinner spinner=new JFXSpinner();
	String folder;
	String title;
	ObservableList<String> options;
	
	JFXTextField addrField=new JFXTextField(defaultURL);
	JFXTextField searchField=new JFXTextField();
    JFXButton search=new JFXButton();  
    WebView webView=new WebView();  
    WebEngine engine=  webView.getEngine();
    WebHistory history=engine.getHistory();
    
    VBox sideBar=new VBox();
    VBox navigationBar=new VBox();
    BorderPane borderPane=new BorderPane();
    Tab newTab=new Tab("New Tab");
    
    public TabUI() {
    	//加载主页
    	engine.load(defaultURL);  
        //跳转网页后更新地址栏
        engine.locationProperty().addListener(new ChangeListener<String>(){  
            @Override  
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {  
                addrField.setText(newValue);  
            }  
        });    
        //跳转网页更新标题栏为当前页Title
        engine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>(){  
            @Override  
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {  
                if(newValue==Worker.State.SUCCEEDED){  
               //     primaryStage.setTitle(engine.getTitle());  
                    newTab.setText(engine.getTitle());
                    
                    if(history.getCurrentIndex()==0) {
                    	backward.setDisable(true);
                    	forward.setDisable(true);
                    	if(history.getEntries().size()>1)
                    		forward.setDisable(false);
                    }
                    if(history.getCurrentIndex()>0) {
                    	backward.setDisable(false);
                    	forward.setDisable(false);
                    }
                    if((history.getCurrentIndex()+1)==history.getEntries().size())
                    	forward.setDisable(true);
                }  
            }  
        });
        //设置按钮及其监听器
        search.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("search.png"))));
        search.setDefaultButton(true); 
   //     search.setButtonType(ButtonType.RAISED);
        search.setId("search-btn");
   //     search.setStyle("-fx-background-color:#fb7299");
        search.addEventHandler(MouseEvent.MOUSE_CLICKED,(e)->{
        	//搜索按钮后跳转网页更新标题栏为当前页Title
            engine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>(){  
                @Override  
                public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {  
                    if(newValue==Worker.State.SUCCEEDED){  
                     //   primaryStage.setTitle(engine.getTitle()); 
                        addrField.setText(engine.getLocation());
                        newTab.setText(engine.getTitle());
                        
                        if(history.getCurrentIndex()==0) {
                        	backward.setDisable(true);
                        	forward.setDisable(true);
                        	if(history.getEntries().size()>1)
                        		forward.setDisable(false);
                        }
                        if(history.getCurrentIndex()>0) {
                        	backward.setDisable(false);
                        	forward.setDisable(false);
                        }
                        if((history.getCurrentIndex()+1)==history.getEntries().size())
                        	forward.setDisable(true);
                    }  
                }  
            }); 
            engine.load("https://www.baidu.com/s?ie=UTF-8&wd="+searchField.getText());
            searchField.setText(null);
        });
        addrField.setStyle("-fx-background-color:white");
        addrField.setStyle("-fx-border-radius:10");
        addrField.setOnKeyPressed(event->{
        	Pattern p = Pattern.compile("[a-z]*[ ]*[A-Z]*[ ]*[0-9]*[ ]");
			Matcher m = p.matcher(addrField.getText());
			boolean b = m.matches();
			if(event.getCode()==KeyCode.ENTER) {
				if(b) 
					engine.load("https://www.baidu.com/s?ie=UTF-8&wd="+addrField.getText());
				else 
					engine.load(addrField.getText().startsWith("http")?addrField.getText().trim():"http://"+addrField.getText().trim());
				
				if(history.getCurrentIndex()==0) {
					backward.setDisable(true);
					forward.setDisable(true);
					if(history.getEntries().size()>1)
						forward.setDisable(false);
				}
				if(history.getCurrentIndex()>0) {
					backward.setDisable(false);
					forward.setDisable(false);
				}
				if((history.getCurrentIndex()+1)==history.getEntries().size())
					forward.setDisable(true);
			}
			/*搜索历史*/
        });
        searchField.setStyle("-fx-background-color:white");
        searchField.setOnKeyPressed(event->{
        	if(event.getCode()==KeyCode.ENTER) {
        		engine.load("https://www.baidu.com/s?ie=UTF-8&wd="+searchField.getText());
        		searchField.setText(null);
        		if(history.getCurrentIndex()==0) {
					backward.setDisable(true);
					forward.setDisable(true);
					if(history.getEntries().size()>1)
						forward.setDisable(false);
				}
				if(history.getCurrentIndex()>0) {
					backward.setDisable(false);
					forward.setDisable(false);
				}
				if((history.getCurrentIndex()+1)==history.getEntries().size())
					forward.setDisable(true);
        	}
        });
        backward.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("back.png"))));
        backward.setDisable(true);//初始状态不可后退
        forward.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("forward1.png"))));
        forward.setDisable(true);//初始状态不可前进
        refresh.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("refresh.png"))));
        download.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("download.png"))));
        mark.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("bookmark.png"))));
        menu.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("menu.png"))));
        email.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("email.png"))));
        bookmarks.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("bookmarks.png"))));
        progressBar.setPrefWidth(2400);
        progressBar.progressProperty().bind(engine.getLoadWorker().progressProperty());
        backward.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
        	history.go(-1);
        });
        forward.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
        	history.go(1);
        });
        refresh.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
        	engine.reload();
        });
        mark.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
        	Label titleLabel=new Label("ADD BOOKMARK");
        	Label nameLabel=new Label("Name");
        	JFXTextField nameField=new JFXTextField();
        	nameField.setText(engine.getTitle());
      
        	JFXButton save=new JFXButton("SAVE");
        	save.setButtonType(ButtonType.RAISED);
        	save.setStyle("-fx-background-color:whitesmoke");
        	save.setMinSize(120, 35);
        	JFXButton cancel=new JFXButton("CANCEL");
        	cancel.setButtonType(ButtonType.RAISED);
        	cancel.setStyle("-fx-background-color:whitesmoke");
        	cancel.setMinSize(120, 35);
        	
        	HBox buttonBox=new HBox();
        	buttonBox.setSpacing(30);
        	buttonBox.getChildren().addAll(save,cancel);
        	buttonBox.setPadding(new Insets(15, 12, 15, 12));
        	
        	VBox popUpMenu=new VBox();
        	popUpMenu.setMinSize(300,150);
        	popUpMenu.setSpacing(3);
        	popUpMenu.setPadding(new Insets(5,5,5,5));
        	VBox.setMargin(titleLabel, new Insets(5,5,5,5));
        	VBox.setMargin(nameLabel, new Insets(5,5,5,5));
        	VBox.setMargin(nameField, new Insets(5, 5, 5, 5));
			popUpMenu.getChildren().addAll(titleLabel, nameLabel, nameField,buttonBox);
			
			PopOver popOverWin=new PopOver(new JFXButton("YES"));
			popOverWin.setCornerRadius(8);
			popOverWin.setContentNode(popUpMenu);
			popOverWin.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
			popOverWin.show(mark);
			
			cancel.addEventHandler(MouseEvent.MOUSE_CLICKED,event->{
				popOverWin.hide();
			});
			save.addEventHandler(MouseEvent.MOUSE_CLICKED, event->{
				//添加收藏夹
			});
			
        });
        menu.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
        	if(borderPane.getRight()==null)
        		borderPane.setRight(sideBar);
        	else
        		borderPane.setRight(null);
        });
        webView.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
        		borderPane.setRight(null);
        });
        email.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
        	Label titleLabel=new Label("EMAIL");
        	Label toLabel=new Label("To");
        	JFXTextField toField=new JFXTextField();
        	toField.setText("964213879@qq.com");
        	Label fromLabel=new Label("From");
        	JFXTextField fromField=new JFXTextField();
        	fromField.setText("964213879@qq.com");
        	Label subjectLabel=new Label("Subject:");
        	JFXTextField subjectField=new JFXTextField();
        	Label contentLabel=new Label("Content:");
        	JFXTextField contentField=new JFXTextField();
        	
        	JFXButton send=new JFXButton("SEND");
        	send.setButtonType(ButtonType.RAISED);
        	send.setStyle("-fx-background-color:whitesmoke");
        	send.setMinSize(120, 35);
        	JFXButton cancel=new JFXButton("CANCEL");
        	cancel.setButtonType(ButtonType.RAISED);
        	cancel.setStyle("-fx-background-color:whitesmoke");
        	cancel.setMinSize(120, 35);
        	
        	HBox buttonBox=new HBox();
        	buttonBox.setSpacing(30);
        	buttonBox.getChildren().addAll(send,cancel);
        	buttonBox.setPadding(new Insets(15, 12, 15, 12));
        	
        	VBox popUpMenu=new VBox();
        	popUpMenu.setMinSize(300,150);
        	popUpMenu.setSpacing(3);
        	popUpMenu.setPadding(new Insets(5,5,5,5));
        	VBox.setMargin(titleLabel, new Insets(5,5,5,5));
        	VBox.setMargin(toLabel, new Insets(5,5,5,5));
        	VBox.setMargin(toField, new Insets(5, 5, 5, 5));
			popUpMenu.getChildren().addAll(titleLabel, toLabel, toField,fromLabel,fromField,subjectLabel,subjectField,contentLabel,contentField,buttonBox);
			
			PopOver popOverWin=new PopOver(new JFXButton("YES"));
			popOverWin.setCornerRadius(8);
			popOverWin.setContentNode(popUpMenu);
			popOverWin.setArrowLocation(PopOver.ArrowLocation.RIGHT_TOP);
			popOverWin.show(email);
			
			cancel.addEventHandler(MouseEvent.MOUSE_CLICKED,event->{
				popOverWin.hide();
			});
			send.addEventHandler(MouseEvent.MOUSE_CLICKED, event->{
				try {
					SendEmail sendEmail=new SendEmail(toField.getText(), subjectField.getText(), contentField.getText());
					if(sendEmail.state) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("EMAIL");
						alert.setHeaderText(null);
						alert.setContentText("Send Email Successfully!");

						alert.showAndWait();
					}

				} catch (GeneralSecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
        });
          
        //添加JS回调函数。。。
        
        //容器添加控件
        sideBar.setSpacing(10);
        sideBar.getChildren().addAll(bookmarks,email);
        
        HBox hbox=new HBox();  
        hbox.getChildren().addAll(backward,forward,refresh,addrField,search,searchField,mark,download,menu);  
        HBox.setHgrow(addrField, Priority.ALWAYS);     
        
        navigationBar.getChildren().addAll(hbox,progressBar);  
        VBox.setVgrow(hbox, Priority.ALWAYS);
        
        borderPane.setTop(navigationBar);
        borderPane.setCenter(webView);
        newTab.setContent(borderPane);
    }
    public Tab getNewTab() {
    	return newTab;
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
