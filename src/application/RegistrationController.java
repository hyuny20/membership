package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class RegistrationController {
	@FXML
	private TextField usernameTextField;
	@FXML
	private TextField useridTextField;
	@FXML
	private TextField hakTextField;
	@FXML
	private TextField banTextField;
	@FXML
	private TextField bunTextField;
	@FXML
	private PasswordField password1PasswordField;
	@FXML
	private PasswordField password2PasswordField;
	@FXML
	private Label registerMessageLabel;
	@FXML
	private Button submitButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Button closeButton;
	
	@FXML
	void submitButtonOnAction() {
		
		boolean checkEmpty = isCheckEmpty();
		boolean checkDuplicatedId = isCheckDuplicatedId();
		boolean checkPasswordSame = isCheckPasswordSame();
		boolean checkNumbers = isCheckNumbers();
		
		if(checkEmpty == true && checkDuplicatedId == true && checkPasswordSame == true && checkNumbers == true) {
			registerMessageLabel.setText("회원 정보를 데이터베이스에 저장합니다....");
			
			DBConnection connNow = new DBConnection();
			Connection conn = connNow.getConnection();
			
			String sql = "INSERT INTO member_accounts "
					+	"(idx, user_name, user_id, user_password, user_hak, user_ban, user_bun)"
					+	"VALUES "
					+	"(member_idx_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)";
			
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, usernameTextField.getText());
				pstmt.setString(2, useridTextField.getText());
				pstmt.setString(3, password1PasswordField.getText());
				pstmt.setString(4, hakTextField.getText());
				pstmt.setString(5, banTextField.getText());
				pstmt.setString(6, bunTextField.getText());
				pstmt.executeUpdate();
				
				pstmt.close();
				conn.close();
				
				registerMessageLabel.setText("데이터베이스에 회원정보 입력 완료!");
				usernameTextField.setText("");
				useridTextField.setText("");
				password1PasswordField.setText("");
				password2PasswordField.setText("");
				hakTextField.setText("");
				banTextField.setText("");
				bunTextField.setText("");
			} catch(Exception e) {
				
			}
		} else {
			if(checkEmpty == false) {
				registerMessageLabel.setText("모든 정보를 입력해주세요");
			} else if(checkDuplicatedId == false) {
				registerMessageLabel.setText("아이디가 중복됩니다. 다른 아이디를 입력해주세요");
			} else if(checkPasswordSame == false) {
				registerMessageLabel.setText("입력한 암호가 동일하지 않습니다. 다시 확인해주세요");
			} else if(checkNumbers == false) {
				registerMessageLabel.setText("학년, 반, 번호를 잘못 입력했습니다");
			}
		}
	}
	
	@FXML
	void cancelButtonOnAction() {
		usernameTextField.setText("");
		useridTextField.setText("");
		hakTextField.setText("");
		banTextField.setText("");
		bunTextField.setText("");
		password1PasswordField.setText("");
		password2PasswordField.setText("");
	}
	
	@FXML
	void closeButtonOnAction() {
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}
	
	boolean isCheckEmpty() {
		boolean result = false;
		if(
			useridTextField.getText().isBlank() == false
			&& password1PasswordField.getText().isBlank() == false
			&& password2PasswordField.getText().isBlank() == false
			&& hakTextField.getText().isBlank() == false
			&& banTextField.getText().isBlank() == false
			&& bunTextField.getText().isBlank() == false
		) {
				result = true;
			}
		return result;
	}
	boolean isCheckDuplicatedId() {
		boolean result = true;
		DBConnection connNow = new DBConnection();
		Connection conn = connNow.getConnection();
		
		String sql = "SELECT COUNT(1) FROM member_accounts"
				+ " WHERE user_id='" + useridTextField.getText() +"'";
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				if(rs.getInt(1) == 1) {
					result = false;
				}
			}
			
			stmt.close();
			conn.close();
		} catch (Exception e) {
			
		}
		return result;
	}
	boolean isCheckPasswordSame() {
		boolean result = false;
		if((password1PasswordField.getText().isBlank() == false && password2PasswordField.getText().isBlank() == false) && (password1PasswordField.getText().equals(password2PasswordField.getText()))) {
			result = true;
		}
		return result;
	}
	boolean isCheckNumbers() {
		boolean result = false;
		int hak = 0;
		int ban = 0;
		int bun = 0;
		try {
			hak = Integer.parseInt(hakTextField.getText());
			ban = Integer.parseInt(banTextField.getText());
			bun = Integer.parseInt(bunTextField.getText());
			
		if(
			(hak >= 1 && hak <=3)
			&& (ban >= 1 && ban <= 15)
			&& (bun >= 1 && bun <= 31)
		) {
			result = true;
		}
			
		return result;
		}catch(NumberFormatException e ) {
			result = false;
			return result;
		}
	}
	
}
