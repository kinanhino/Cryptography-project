package controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;

import edu.rit.Hex;
import home.Launch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class User2Controller {
	@FXML
	private TextArea plainTxt;

	@FXML
	private TextArea encryptedTxt;

	@FXML
	private Button sendBtn;

	@FXML
	private Button encBtn;

	@FXML
	private TextArea dsTxt;

	@FXML
	private Button decBtn;

	@FXML
	private Button clr;

	@FXML
	private TextField verify;

    @FXML
    private Label serpentK2;
    
	//static ArrayList<Byte> encryptedMsg2;
	static String decryptedMsg2, encryptedText2;
	static byte[] dSignature2;
	static byte[] cipherText2,ShaByte2;
	static String plaintext2;
	byte[] bytes2;
	public void initialize() {
		plainTxt.setWrapText(true);
		encryptedTxt.setWrapText(true);
		dsTxt.setWrapText(true);
		serpentK2.setText("Serpent Symmetric Key: " + Hex.HexadecimalToString(Hex.toString(Launch.dhkey)));
		encryptedTxt.setText(User1Controller.encryptedText);
		dsTxt.setText(Hex.HexadecimalToString(Hex.toString(User1Controller.dSignature)));
		dSignature2 = User1Controller.dSignature;
	}

	@FXML
	void encrypt2(ActionEvent event) {
		if (plainTxt.getText() == "") {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error plain text");
			alert.setHeaderText("Invalid plain text!");
			alert.setContentText("Must enter plain text to send.");
			alert.showAndWait();
			return;
		}

		if (Launch.tr1fa2 == false) {
			cipherText2 = Launch.serpent.EncryptMessage(plainTxt.getText());
			encryptedText2= Hex.toString(cipherText2);
			ShaByte2 =User1Controller.eceg2.sha256_Gen(plainTxt.getText().getBytes());
			dSignature2 = User1Controller.eceg1.encrypt(Hex.HexadecimalToString(Hex.toString(ShaByte2)));
			Launch.tr1fa2 = true;
		} else {
			cipherText2 = User1Controller.cipherText;
			encryptedText2 = User1Controller.encryptedText;
			

			dSignature2 = User1Controller.dSignature;
		}
//
//		byte[] bytes = new byte[plaintext2.length];
//		for (int i = 0; i < plaintext2.length; i++)
//			bytes[i] = plaintext2[i];

//		encryptedString2 = new String(bytes);
		encryptedTxt.setText(encryptedText2);
		dsTxt.setText(Hex.HexadecimalToString(Hex.toString(dSignature2)));

		System.out.println("Plain text encrypted. Digital Signature generated.");
		System.out.println("Plain text: " + plainTxt.getText());
		System.out.println("Encrypted text: " + encryptedText2);
//		System.out.println("Digital signature: \n" + Hex.HexadecimalToString(Hex.toString(dSignature2)));
	}

	@FXML
	void decrypt2(ActionEvent event) {
		if (encryptedTxt.getText() == "") {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error encrypted text");
			alert.setHeaderText("Invalid encrypted text!");
			alert.setContentText("Must use encrypted text to decrypt.");
			alert.showAndWait();
			return;
		}

		decryptedMsg2 = Launch.serpent.decryptMessage(User1Controller.cipherText);
		plainTxt.setText(decryptedMsg2);

		System.out.println("Encrypted text has been decrypted.");
		System.out.println("Encrypted text: " + Hex.StringtoHex(encryptedTxt.getText()));
		System.out.println("Decrypted text: " + decryptedMsg2);

		//ArrayList<Byte> c5DecTemp2 = new ArrayList<>();

		String enPtTmp2 = User1Controller.eceg2.decrypt(User1Controller.dSignature);
		//ShaByte2= Hex.toByteArray(Hex.StringtoHex(enPtTmp2));
		ShaByte2 = User1Controller.eceg1.sha256_Gen(decryptedMsg2.getBytes());
	//	System.out.println("\nenPtTmp2: " + enPtTmp2);
	//	System.out.println("\nSHA-256: " + ShaByte2.toString());

//		String str2 = enPtTmp2.substring(1, enPtTmp2.length() - 1);
//		String strtemp2[] = str2.split(", ");
//		String temp2 = Hex.StringtoHex(enPtTmp2);
	//	byte[] bytes2 = Hex.toByteArray(Hex.StringtoHex(enPtTmp2));
//		for (int l = 0; l < strtemp2.length; l++)
//			c5DecTemp2.add(l, Byte.valueOf(strtemp2[l]));
//		
//		byte[] bytes2 = new byte[c5DecTemp2.size()];
//		for (int i = 0; i < c5DecTemp2.size(); i++)
//			bytes2[i] = c5DecTemp2.get(i);

		
	//	String decPT2 = Launch.serpent.decryptMessage(bytes2);
//		Hex.toString(bytes2);
		// decPT2="5"; SIMULATE DS FAIL
		System.out.println("\nDecrypted message SHA-256: " + Hex.toString(ShaByte2));
		System.out.println("\nOriginal message SHA-256: "+ Hex.toString(User1Controller.ShaByte));

		//System.out.println(ShaByte2);
		//System.out.println(User1Controller.ShaByte);

		if (Hex.toString(ShaByte2).equals(Hex.toString(User1Controller.ShaByte))) {
			System.out.println("Digital signature has been decrypted.");
			System.out.println("Digital signature: \n" + dsTxt.getText());
			verify.setText(verify.getText() + "Verified successfully!");
			verify.setStyle("-fx-control-inner-background: #4ec029");
		} else {
			verify.setText(verify.getText() + "Verification FAILED!");
			verify.setStyle("-fx-text-fill: crimson; -fx-control-inner-background: #000");
		}
	}

	@FXML
	void send2(ActionEvent event) throws Exception {
		System.out.println("\nStart User1 SMS window.");
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/user1.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("User1 SMS Screen");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@FXML
	void clearTxt2(ActionEvent event) throws Exception {
		plainTxt.clear();
		encryptedTxt.clear();
		dsTxt.clear();
	}
}