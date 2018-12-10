import javafx.application.*;
import javafx.event.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ClientGUI extends Application {
    
    private TextArea textArea;
    private TextField textField;
    private Button button;
    private Client client;
    private boolean firstMessage;
    
    public void start(Stage primaryStage) {
        firstMessage = true;
        
        client = new Client((String packet)->{
            Platform.runLater(()->{
                textArea.appendText(packet + "\n");
            });
        });
        
        button = new Button("Connect");
        button.setLayoutY(220);
        button.setLayoutX(430);
        button.setOnAction((ActionEvent e)->{
            if (!firstMessage) {
                String message = textField.getText();
                client.send(message);
                textArea.appendText("Tu: " + message + "\n");
                textField.setText("");
            } else {
                String[] params = textField.getText().trim().split(":");
                textArea.setText("");
                client.connectTo(params[0], Integer.parseInt(params[1]));
                button.setText("Send");
                firstMessage = false;
                textField.setText("");
            }
        });
        
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setMinWidth(200);
        textArea.setLayoutY(20);
        textArea.setLayoutX(10);
        textArea.appendText("Inserire IP:PORTA del server e premere Connect" +
                            System.lineSeparator());
        
        textField = new TextField();
        textField.setLayoutY(220);
        textField.setLayoutX(10);
        textField.setMinWidth(400);
        
        Group root = new Group(textArea, textField, button);
        
        Scene scene = new Scene(root, 500, 272);
        
        primaryStage.setTitle("Client!");
        primaryStage.setScene(scene);
        primaryStage.show();
        new Thread(()->{
            while(!client.hasStopped())
                try {
                    Thread.sleep(2);
                } catch(InterruptedException exception) {
                    break;
                }
            Platform.runLater(()->{
                textArea.appendText("---- Ti sei disconnesso dal server. ----");
            });
        }).start();
    }
    
    public void stop() {
        System.exit(0);
    }
    
}
