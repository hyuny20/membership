package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private CheckBox adminCheckBox;
	@FXML
	private TextField useridTextField;
	@FXML
	private PasswordField passwordPasswordField;
	@FXML
	private Button loginButton;
	@FXML
	private Button registrationButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Button closeButton;
	@FXML
	private Label loginMessageLabel;
	
	boolean isUserLogin = false;
	
	
	public void closeButtonOnAction(ActionEvent e) {
		System.out.println("창닫기");
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
	
	public void loginButtonOnAction(ActionEvent e) {
		loginMessageLabel.setText("사용자 아이디와 암호를 검사합니다...");
		
		if(loginButton.getText().equals("로그인") == true) {
			if(useridTextField.getText().isBlank() == false && passwordPasswordField.getText().isBlank() == false) {
			
				if(adminCheckBox.isSelected() == true) {
					//loginMessageLabel.setText("관리자 로그인...");
					validateAdminLogin();
				} else {
					//loginMessageLabel.setText("사용자 로그인...");
					validateMemberLogin();
				}
			} else {
				loginMessageLabel.setText("아이디와 암호를 입력해주세요");
			}	
		} else if(loginButton.getText().equals("로그아웃") == true) {
			loginMessageLabel.setText("로그아웃...");
			logout();
		}
		
	}
	
	public void adminCheckBoxOnAction(ActionEvent e) {
		if(adminCheckBox.isSelected() == true) {
			registrationButton.setText("관리자 회원관리");
			registrationButton.setDisable(true);
			useridTextField.setText("");
			passwordPasswordField.setText("");
		} else {
			registrationButton.setText("회원가입");
			registrationButton.setDisable(false);
			useridTextField.setText("");
			passwordPasswordField.setText("");
		}
	}
	
	public void registrationButtonOnAction(ActionEvent e) {
			try {
				if(adminCheckBox.isSelected() == true) {
					Parent root1 = FXMLLoader.load(getClass().getResource("membership.fxml"));
					Stage membershipStage = new Stage();
					membershipStage.setTitle("관리자 회원관리 모듈");
					membershipStage.setScene(new Scene(root1, 600, 400));
					membershipStage.getIcons().add(new Image("application/instagram"));
					membershipStage.show();
				} else {
					if(isUserLogin == true) {
						Parent root2 = FXMLLoader.load(getClass().getResource("modification.fxml"));
						Stage modificationStage = new Stage();
						modificationStage.setTitle("사용자 회원정보 수정 모듈");
						modificationStage.setScene(new Scene(root2, 600, 400));
						modificationStage.getIcons().add(new Image("application/instagram"));
						modificationStage.show();
					} else {
						Parent root3 = FXMLLoader.load(getClass().getResource("registration.fxml"));
						Stage registrationStage = new Stage();
						registrationStage.setTitle("사용자 회원가입 모듈");
						registrationStage.setScene(new Scene(root3, 600, 400));
						registrationStage.getIcons().add(new Image("application/instagram"));
						registrationStage.show();
					}
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
	}
	
	public void cancelButtonOnAction(ActionEvent e) {
		adminCheckBox.setSelected(false);
		useridTextField.setText("");
		passwordPasswordField.setText("");
	}
	
	public boolean isAdminLogin() {
		DBConnection connDB = new DBConnection();
		Connection conn = connDB.getConnection();
		
		String sql = "SELECT admin_id, admin_password" +
					 " FROM admin_accounts" + 
					 " WHERE admin_id=?" + 
					 " AND admin_password=?";
		
		boolean result = false;
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, useridTextField.getText());
			pstmt.setString(2, passwordPasswordField.getText());
			pstmt.executeUpdate();
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("admin_id").equals(useridTextField.getText())) {
					
					loginMessageLabel.setText("관리자 로그인 성공");
					result = true;
				} 
			}
			
			pstmt.close();
			rs.close();
			conn.close();
		} catch(Exception error) {
			
		}
		return result;
	}
	
	public boolean isMemberLogin() {
		DBConnection connDB = new DBConnection();
		Connection conn = connDB.getConnection();
		
		String sql = "SELECT user_id, user_password" +
					 " FROM member_accounts" + 
					 " WHERE user_id=?" + 
					 " AND user_password=?";
		
		boolean result = false;
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, useridTextField.getText());
			pstmt.setString(2, passwordPasswordField.getText());
			pstmt.executeUpdate();
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = true;
				isUserLogin = true;
				Main.global_user_id = useridTextField.getText();
			}
			
			
//			while(rs.next()) {
//				if(rs.getString("user_id").equals(useridTextField.getText())) {
//					
//					loginMessageLabel.setText("관리자 로그인 성공");
//					result = true;
//				} 
//			}
			
			pstmt.close();
			rs.close();
			conn.close();
		} catch(Exception error) {
			
		}
		return result;
	}
	void validateAdminLogin() {
		if(isAdminLogin() == true) {
			loginMessageLabel.setText("관리자 로그인 성공 환영합니다!!");
			useridTextField.setText("");
			passwordPasswordField.setText("");
			registrationButton.setDisable(false);
			loginButton.setText("로그아웃");
		} else {
			loginMessageLabel.setText("관리자 아이디 또는 암호가 잘못됐습니다");
		}
	}
	
	void validateMemberLogin() {
		if(isMemberLogin() == true) {
			loginMessageLabel.setText("사용자 로그인 성공 환영합니다!!");
			useridTextField.setText("");
			passwordPasswordField.setText("");
			loginButton.setText("로그아웃");
			registrationButton.setText("회원정보 수정");
		} else {
			loginMessageLabel.setText("사용자 아이디 또는 암호가 잘못됐습니다");
		}
	}
	void logout() {
		loginButton.setText("로그인");
		registrationButton.setText("회원가입");
		adminCheckBox.setSelected(false);
		isUserLogin = false;
		Main.global_user_id = null;
	}
}
