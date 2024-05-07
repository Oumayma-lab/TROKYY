package com.example.trokyy.controllers;
import com.example.trokyy.controllers.User.CaptureImageDialogController;
import javafx.collections.FXCollections;
import com.example.trokyy.models.ImageCollector;
import com.example.trokyy.services.UserDao;
import com.example.trokyy.models.Utilisateur;
import com.example.trokyy.tools.EmailService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.bytedeco.opencv.opencv_core.Mat;
import org.controlsfx.control.Notifications;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import org.controlsfx.control.textfield.TextFields;
import org.json.JSONObject; // Import for JSON handling
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SignUpController implements Initializable {

    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField useremail;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Label passwordStrengthLabel;
    @FXML
    private Button registerButton;
    @FXML
    private TextField phoneNumber;
    @FXML
    private TextField address;
    private final UserDao userDao = new UserDao();
    @FXML
    private Pane passwordParent; // Add this field to reference the parent container of the password field in your FXML
    @FXML
    private Pane confirmPasswordParent; // Add this field to reference the parent container of the confirmPassword field in your FXML
    @FXML
    private Pane FormContainer;

    @FXML
    private Label countryCodeValueLabel;

    @FXML
    private Rectangle strengthIndicator;

    @FXML
    private Label suggestPasswordIcon;
    private boolean passwordVisible = false;
    @FXML
    private ImageView imageView;

    private ImageCollector imageCollector;

    private int imagesTaken = 0;

    private Stage captureImageStage;

    private static final double CRITERIA_WEIGHT = 0.25; // Each criteria contributes 25% to the progress
    private static final double MAX_WIDTH = 200; // Maximum width for the strength indicator

    private static final List<String> TUNISIAN_GOVERNORATES = Arrays.asList(

    );


    private final List<String> addresses = Arrays.asList(
            "Ariana", "Beja", "Ben Arous", "Bizerte", "Gabes", "Gafsa", "Jendouba",
            "Kairouan", "Kasserine", "Kebili", "Kef", "Mahdia", "Manouba", "Medenine",
            "Monastir", "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", "Tataouine",
            "Tozeur", "Tunis", "Zaghouan"
    );

    public SignUpController() {
    }
    private boolean updatingText = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        imageCollector = new ImageCollector();



        address.textProperty().addListener((observable, oldValue, newValue) -> {
            if (updatingText) {
                return;
            }

            String input = newValue.toLowerCase();
            String foundAddress = null;

            // Find the first address that starts with the input from the first two letters
            if (input.length() >= 2) {
                for (String addr : addresses) {
                    if (addr.toLowerCase().startsWith(input.substring(0, 2))) {
                        foundAddress = addr;
                        break;
                    }
                }
            }

            // If found, update the address field
            if (foundAddress != null) {
                updatingText = true;
                address.setText(foundAddress);
                address.positionCaret(input.length());
                updatingText = false;
            }
        });

        // Add a listener to reset the updatingText flag when user manually edits the text field
        address.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!updatingText) {
                updatingText = false;
            }
        });


        // Add a listener to reset the updatingText flag when user manually edits the text field
        address.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!updatingText) {
                updatingText = false;
            }
        });


        if (FormContainer == null) {
            System.err.println("FormContainer is null. Check FXML binding.");
        } else {
            System.out.println("FormContainer is initialized successfully.");
        }


        firstName.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(firstName, newValue);
        });

        lastName.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(lastName, newValue);
        });

        useremail.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(useremail, newValue);
        });

        password.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(password, newValue);
        });

        confirmPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(confirmPassword, newValue);
        });
        phoneNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(phoneNumber, newValue);
        });
        address.textProperty().addListener((observable, oldValue, newValue) -> {
            validateField(address, newValue);
        });

        String countryCode = getCountryCodeFromIPAddress();
        countryCodeValueLabel.setText(countryCode);

        strengthIndicator.setStyle("-fx-background-color: #E8F0E6; -fx-background-radius: 30px; -fx-border-color: #006400; -fx-border-radius: 30px; -fx-pref-height: 2px;");
        strengthIndicator.setVisible(false);
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                strengthIndicator.setVisible(false);
            } else {
                strengthIndicator.setVisible(true);
                updateStrengthIndicator(newValue);
            }
        });
    }

    private void updateStrengthIndicator(String password) {
        int numCriteriaMet = calculateCriteriaMet(password);
        double progress = numCriteriaMet * CRITERIA_WEIGHT;
        strengthIndicator.setStyle("-fx-background-color: #E8F0E6; -fx-background-radius: 30px; -fx-border-color: #006400; -fx-border-radius: 30px; -fx-pref-height: 2px;");
        double maxWidth = Math.min(password.length() * 15, MAX_WIDTH);
        strengthIndicator.setWidth(maxWidth);
        if (progress == 0) {
            strengthIndicator.setFill(Color.RED);
        } else if (progress < 0.25) {
            strengthIndicator.setFill(Color.RED);
        } else if (progress < 0.5) {
            strengthIndicator.setFill(Color.ORANGE);
        } else if (progress < 0.75) {
            strengthIndicator.setFill(Color.YELLOW);
        } else {
            strengthIndicator.setFill(Color.GREEN);
        }
    }

    private int calculateCriteriaMet(String password) {
        int criteriaMet = 0;
        if (password.matches(".*[a-z].*")) {
            criteriaMet++;
        }
        if (password.matches(".*[A-Z].*")) {
            criteriaMet++;
        }
        if (password.matches(".*\\d.*")) {
            criteriaMet++;
        }
        if (password.matches(".*[!@#$%^&*()-_=+\\|\\[{\\]};:'\",<.>/?`~].*")) {
            criteriaMet++;
        }
        return criteriaMet;
    }



    @FXML
    public void registerUser(ActionEvent actionEvent) throws SQLException {
        String nom = firstName.getText().trim();
        String prenom = lastName.getText().trim();
        String email = useremail.getText();
        String mdp = password.getText();
        String confirmUserPassword = confirmPassword.getText();
        String phoneNumberText = phoneNumber.getText().trim();
        String adresse = address.getText();
        UserDao userDao = new UserDao();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty() ||
                confirmUserPassword.isEmpty() || phoneNumberText.isEmpty() || adresse.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        int phoneNumberValue;
        try {
            phoneNumberValue = Integer.parseInt(phoneNumberText);
        } catch (NumberFormatException e) {
            // Handle invalid phone number format
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid phone number format.");
            return;
        }
        validateField(firstName, firstName.getText());
        validateField(lastName, lastName.getText());
        validateField(useremail, useremail.getText());
        validateField(password, password.getText());
        validateField(confirmPassword, confirmPassword.getText());
        validateField(phoneNumber, phoneNumber.getText());
        validateField(address, address.getText());
        ValidationResult validationResult = validateFields(nom, prenom, email, mdp, confirmUserPassword, Integer.parseInt(phoneNumberText), adresse);
        if (!validationResult.isValid()) {
            showAlert(Alert.AlertType.ERROR, "Error", validationResult.getMessage());
            return;
        }
        LocalDateTime dateInscription = LocalDateTime.now();
        Utilisateur user = new Utilisateur(nom, prenom, email, mdp, phoneNumberValue, adresse) ; // Create user object
        try {


                UserDao.createUser(user, LocalDateTime.now()); // Register user with hashed password
            System.out.println("User created successfully."); // Debug message
            showNotification("Registration Successful!", "Welcome " + prenom + ". Please log in using your credentials.");
            showLoginForm(null);
            EmailService.sendEmail(email);


        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to register user.");
        }

    }



    private void showNotification(String title, String message) {
        FontAwesomeIconView successIcon = new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE);
        successIcon.setSize("36");
        successIcon.setFill(Color.web("#4CAF50"));
        Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .graphic(successIcon)
                .darkStyle()
                .show();
    }

    private void validateField(TextField field, String value){
        System.out.println("Validating field: " + field.getId());
        if (value.isEmpty()) {
            field.setStyle("-fx-border-color: red; -fx-border-radius: 30px;");
        } else {
            field.setStyle("-fx-border-color: inherit;");
        }
    }

    public static boolean isValidTunisianAddress(String address) {
        return TUNISIAN_GOVERNORATES.contains(address);
    }
    private void checkPasswordMatch(String confirmPassword) {
        String passwordText = password.getText();
        boolean match = passwordText.equals(confirmPassword);
        if (match) {
            passwordParent.setStyle("-fx-background-color: #FFFFFF;"); // White background for match
            confirmPasswordParent.setStyle("-fx-background-color: #FFFFFF;"); // White background for match
        } else {
            passwordParent.setStyle("-fx-background-color: #FF0000;"); // Red background for mismatch
            confirmPasswordParent.setStyle("-fx-background-color: #FF0000;"); // Red background for mismatch
        }
    }


    private ValidationResult validateFields(String nom, String prenom, String email, String mdp, String confirmUserPassword, int phoneNumber, String adresse){
        ValidationResult validationResult = new ValidationResult();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty() || confirmUserPassword.isEmpty() || adresse.isEmpty()) {
            validationResult.addMessage("Please fill in all fields.");
        }

        if (!isValidEmail(email)) {
            validationResult.addMessage("Invalid email. Please enter a valid address.");
        }

        if (userDao.emailExists(email)) {
            validationResult.addMessage("Email is already registered.");
        }

        if (!isValidPassword(mdp)) {
            validationResult.addMessage("Password must be at least 8 characters long and contain at least one digit.");
        }

        if (!mdp.equals(confirmUserPassword)) {
            validationResult.addMessage("Passwords do not match.");
        }

        if (nom.length() < 2 || nom.length() > 12) {
            validationResult.addMessage("Last name must be between 2 and 12 characters long.");
        }

        if (prenom.length() < 2 || prenom.length() > 12) {
            validationResult.addMessage("First name must be between 2 and 12 characters long.");
        }

        if (String.valueOf(phoneNumber).isEmpty() || String.valueOf(phoneNumber).length() != 8) {
            validationResult.addMessage("Invalid phone number format.");
        }

        //if (!adresse.isEmpty()) {
        //   if (!isValidTunisianAddress(adresse)) {
        //        validationResult.addMessage("Please enter a valid Tunisian governorate.");
        //    }
        // }
         return validationResult;
    }

    private boolean isValidEmail(String email) {
        // Check if email is valid format
        String emailRegex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        // Password pattern: at least 8 characters long and contain at least one digit
        String passwordRegex = "^(?=.*[0-9]).{8,}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

    public void handleAutoComplete(KeyEvent keyEvent) {
    }


    private static class ValidationResult {
        private final StringBuilder messageBuilder = new StringBuilder();
        public void addMessage(String message) {
            if (messageBuilder.length() > 0) {
                messageBuilder.append("\n");
            }
            messageBuilder.append("• ").append(message); // Add bullet points for better readability
        }
        public boolean isValid() {
            return messageBuilder.length() == 0;
        }
        public String getMessage() {
            return messageBuilder.toString() ;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoMessage(String title, String headerText, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void showLoginForm(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/User/login1.fxml"));
            Parent loginForm = loader.load();
            if (FormContainer != null) {
                animateTransition(FormContainer, loginForm);

            } else {
                System.err.println("FormContainer is null. Check FXML binding.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading login form FXML file.");
        }
    }

    private void animateTransition(Pane container, Parent newContent) {
        Node currentContent = container.getChildren().isEmpty() ? null : container.getChildren().get(0);

        if (currentContent != null) {
            // Create a rotate transition for the current content (flip out)
            RotateTransition rotateOut = new RotateTransition(Duration.seconds(0.5), currentContent);
            rotateOut.setAxis(javafx.geometry.Point3D.ZERO.add(0, 1, 0)); // Rotate around Y-axis
            rotateOut.setFromAngle(0);
            rotateOut.setToAngle(90);
            rotateOut.setInterpolator(Interpolator.EASE_BOTH);

            // Create a rotate transition for the new content (flip in)
            RotateTransition rotateIn = new RotateTransition(Duration.seconds(0.5), newContent);
            rotateIn.setAxis(javafx.geometry.Point3D.ZERO.add(0, 1, 0)); // Rotate around Y-axis
            rotateIn.setFromAngle(-90);
            rotateIn.setToAngle(0);
            rotateIn.setInterpolator(Interpolator.EASE_BOTH);

            // Play both transitions
            rotateOut.setOnFinished(event -> {
                container.getChildren().setAll(newContent);
                rotateIn.play();
            });

            rotateOut.play();
        } else {
            container.getChildren().setAll(newContent);
        }
    }


    public void setFormContainer(Pane container) {
        this.FormContainer = container;
    }

    private String getCountryCodeFromIPAddress() {
        String countryCallingCode = "";
        try {
            URL url = new URL("https://ipinfo.io/json");
            Scanner scanner = new Scanner(url.openStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();
            JSONObject jsonObject = new JSONObject(response.toString());
            String countryCode = jsonObject.getString("country");
            switch (countryCode) {
                case "TN":
                    countryCallingCode = "+216";
                    break;
                case "DZ":
                    countryCallingCode = "+213";
                    break;
                case "FR":
                    countryCallingCode = "+33";
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return countryCallingCode;
    }


    @FXML
    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            password.setPromptText(password.getText());
            password.setText("");
            suggestPasswordIcon.setText("🔓");
        } else {
            password.setText(password.getPromptText());
            password.setPromptText(null);
            suggestPasswordIcon.setText("🔑");
        }
    }
    
    @FXML
    private void suggestPassword() {
        String suggestedPassword = generateRandomPassword();
        password.setText(suggestedPassword);
        suggestPasswordIcon.setText("✅"); // Change icon to indicate suggested password
        suggestPasswordIcon.setStyle("-fx-cursor: default;"); // Disable further clicks on the icon
    }

    private String generateRandomPassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = upperCaseLetters.toLowerCase();
        String numbers = "0123456789";
        String symbols = "!@#$%^&*()-_+=";

        String allCharacters = upperCaseLetters + lowerCaseLetters + numbers + symbols;

        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            password.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
        }

        return password.toString();
    }




    @FXML
    private void captureImage() {
        Image capturedImage = imageCollector.captureImage();
        if (capturedImage != null) {
            openCaptureImageDialog(capturedImage);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to capture image.");
            alert.showAndWait();
        }
    }

    @FXML
    private void saveImage() {
        imageCollector.saveImage();
    }


    private void openCaptureImageDialog(Image capturedImage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trokyy/FrontOffice/User/captureImageDialog.fxml"));
            Scene scene = new Scene(loader.load());
            captureImageStage = new Stage();
            captureImageStage.setTitle("Captured Image");
            captureImageStage.setScene(scene);
            captureImageStage.initModality(Modality.APPLICATION_MODAL);
            CaptureImageDialogController controller = loader.getController();
            controller.setImage(capturedImage);
            captureImageStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
