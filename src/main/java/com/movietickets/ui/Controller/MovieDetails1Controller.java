package com.movietickets.ui.Controller;

import com.movietickets.model.*;
import com.movietickets.repository.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MovieDetails1Controller implements Initializable {
    @FXML
    private Button myButtonADD;
    @FXML
    private ComboBox<String> lettersCombo;
    @FXML
    private Button myback;
    @FXML
    private Button myreservation;
    SeatRepository seatRepo;
    Movie movie1;
    Show show1;
    User user;
    private int showid;
    Reservation reservation;
    ReservationRepository reservationRepo;
    private List<Integer> seatIDs;
    private int hallid;

    public void sethallID(int hallid) {
        this.hallid = hallid;
    }

    public void setUserData(String email, int userId) {
        this.email = email;
        this.userid = userId;
        System.out.println("Email = " + email);
        System.out.println("ID = " + userId);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            user = UserSessionController.getUser();
            MovieRepository movieRepo = new MovieRepository();
            ShowRepository showRepo = new ShowRepository();
            seatRepo = new SeatRepository();

            Optional<Movie> movie1opt = movieRepo.getById(1);
            Movie movie1 = movie1opt.isPresent() ? movie1opt.get() : null;
            if (movie1 == null) {
                System.out.println("show is Null");
                return;
            }

            Optional<Show> show1opt = showRepo.getById(1);
            show1 = show1opt.isPresent() ? show1opt.get() : null;
            if (show1 == null) {
                System.out.println("show is Null");
                return;
            }

            hallid = show1.getHallID().getHallID();

            myMovieID.setText(String.valueOf(movie1.getMovieId()));
            myTitle.setText(movie1.getTitle());
            myDescription.setText(movie1.getDescription());
            myDuration.setText(String.valueOf(movie1.getDuration()));
            myRating.setText(String.valueOf(movie1.getRating()));
            myCategory.setText(movie1.getCategory());
            myShowID.setText(String.valueOf(show1.getShowID()));
            myStartTime.setText(String.valueOf(show1.getStartTime()));
            myHallID.setText(String.valueOf(show1.getHallID().getHallID()));
            myDate.setText(show1.getDate());
            showid = show1.getShowID();
            ShowSessionController.setShow(show1);
            for (char c = 'A'; c <= 'Z'; c++) {
                lettersCombo.getItems().add(String.valueOf(c));
            }

            lettersCombo.setOnAction(event -> showCheckBoxes());
        }catch (Exception e){
            System.out.println("Exception "+e);
        }
    }

    @FXML
    private void showCheckBoxes() {

        numbersBox.getChildren().clear();

        for (int i = 0; i <= 10; i++) {
            CheckBox cb = new CheckBox(String.valueOf(i));
            cb.setStyle("-fx-text-fill: white;");
            numbersBox.getChildren().add(cb);
        }
        myButtonADD.setVisible(true);
        labelCheck.setLayoutX(8);
        labelCheck.setLayoutY(911);
        numbersBox.setVisible(true);
    }

    @FXML
    private VBox numbersBox;
    @FXML
    private Label labelCheck;
    @FXML
    private TextField myMovieID;
    @FXML
    private TextField myTitle;
    @FXML
    private TextField myDescription;
    @FXML
    private TextField myDuration;
    @FXML
    private TextField myRating;
    @FXML
    private TextField myCategory;
    @FXML
    private TextField myShowID;
    @FXML
    private TextField myStartTime;
    @FXML
    private TextField myDate;
    @FXML
    private TextField myHallID;
    @FXML
    private Label labelSetter;
    private String email;
    private int userid;

    @FXML
    private void goReservation(ActionEvent event) throws IOException {
        if (seatIDS == null || seatIDS.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Please choose seats first");
            alert.show();
            return;
        }
        System.out.println("Reservation is complete and ID: " + reservation.getReservationId());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/movietickets/resources/Reservation.fxml"));
        Parent root = loader.load();

        ReservationController reservecontroller = loader.getController();
        reservecontroller.setUserData(email, userid);
        reservecontroller.sethallID(hallid);

        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(scrollPane, 900, 650);

        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(Windowevent -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Logout");
            alert.setHeaderText("Do You Want to Logout from the Programe");

            ButtonType yesBtn = new ButtonType("Yes");
            ButtonType noBtn = new ButtonType("NO");

            alert.getButtonTypes().setAll(yesBtn, noBtn);

            alert.showAndWait().ifPresent(type -> {

                if (type == yesBtn) {
                    UserSessionController.clear();
                    ReservationSessionController.clear();
                    ShowSessionController.clear();
                    PaymentSessionController.clear();
                } else {
                    Windowevent.consume();
                }

            });
        });
    }

    @FXML
    private void goMovie(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/movietickets/resources/Movies.fxml"));
        Parent root = loader.load();

        MoviesController moviescontroller = loader.getController();
        moviescontroller.setUserData(email, userid);

        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(scrollPane, 900, 650);

        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(Windowevent -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Logout");
            alert.setHeaderText("Do You Want to Logout from the Programe");

            ButtonType yesBtn = new ButtonType("Yes");
            ButtonType noBtn = new ButtonType("NO");

            alert.getButtonTypes().setAll(yesBtn, noBtn);

            alert.showAndWait().ifPresent(type -> {

                if (type == yesBtn) {
                    UserSessionController.clear();
                    ReservationSessionController.clear();
                    ShowSessionController.clear();
                    PaymentSessionController.clear();
                } else {
                    Windowevent.consume();
                }

            });
        });
    }

    private List<Integer> seatIDS;

    @FXML
    private void getValues(ActionEvent event) throws IOException {
        try {
            reservationRepo = new ReservationRepository();
            String selectedLetter = lettersCombo.getValue();
            if (selectedLetter == null) return;
            List<Integer> selectedNumbers = new ArrayList<>();
            List<Seat> selectedSeats = new ArrayList<>();

            for (Node node : numbersBox.getChildren()) {
                CheckBox cb = (CheckBox) node;
                if (cb.isSelected()) {
                    int seatNum = Integer.parseInt(cb.getText());
                    for (Seat s : seatRepo.findAll()) {
                        if (s.getNum() == seatNum && s.getRow().equals(selectedLetter)) {
                            selectedSeats.add(s);
                            break;
                        }
                    }
                    selectedNumbers.add(Integer.parseInt(cb.getText()));
                }
            }
            seatIDS = new ArrayList<>();
            for (Seat s : selectedSeats) {
                seatIDS.add(s.getSeatID());
            }
            if (show1.checkAvailable(seatIDS)) {
                show1.reserveSeats(reservation, seatIDS);
                for (Seat s : selectedSeats) {
                    s.reserve(reservation);
                    seatRepo.save(s);
                }
                System.out.print("Seats Selected: ");
                for (int i = 0; i < selectedNumbers.toArray().length; i++) {
                    System.out.print(selectedLetter + selectedNumbers.toArray()[i] + ",");
                }
                reservation = new Reservation(user, show1, seatIDS);
                if (reservation == null) {
                    System.out.println("Reservation is Null");
                    return;
                }
                labelCheck.setVisible(true);
                labelCheck.setText("selected seats are Avaliable");
                ReservationSessionController.setReservation(reservation);
                reservationRepo.save(reservation);
            } else {
                labelCheck.setVisible(true);
                labelCheck.setText("selected seats are Not Avaliable");
            }
        } catch (Exception e) {
            System.out.println("Exception" + e);
        }
    }
}