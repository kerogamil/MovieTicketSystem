package com.movietickets.ui.Controller;

import com.movietickets.model.*;
import com.movietickets.repository.HallRepository;
import com.movietickets.repository.MovieRepository;
import com.movietickets.repository.SeatRepository;
import com.movietickets.repository.ShowRepository;
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
import java.util.ResourceBundle;

public class MovieDetails2Controller implements Initializable{
    @FXML
    private Button myButtonADD;
    @FXML
    private ComboBox<String> lettersCombo;
    @FXML
    private Button myback;
    @FXML
    private Button myreservation;
    SeatRepository seatRepo;
    Movie movie2;
    Show show2;
    Hall hall;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            MovieRepository movieRepo = new MovieRepository();
            ShowRepository showRepo = new ShowRepository();
            seatRepo = new SeatRepository();
            movie2 = movieRepo.getAll().getLast();
            show2 = showRepo.getAll().getLast();
            hall = show2.getHallID();
            myMovieID.setText(String.valueOf(movie2.getMovieId()));
            myTitle.setText(movie2.getTitle());
            myDescription.setText(movie2.getDescription());
            myDuration.setText(String.valueOf(movie2.getDuration()));
            myRating.setText(String.valueOf(movie2.getRating()));
            myCategory.setText(movie2.getCategory());
            myShowID.setText(String.valueOf(show2.getShowID()));
            myStartTime.setText(String.valueOf(show2.getStartTime()));
            myHallID.setText(String.valueOf(show2.getHallID().getHallID()));
            myDate.setText(show2.getDate());
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
    public void setUserData(String email, int userId) {
        this.email = email;
        this.userid = userId;
        System.out.println("Email = " + email);
        System.out.println("ID = " + userId);
    }
    @FXML
    private void goReservation(ActionEvent event)throws IOException{
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
    }

    @FXML
    private void goMovie(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/movietickets/resources/Movies.fxml")
        );

        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(scrollPane, 900, 650);

        stage.setScene(scene);
        stage.show();
    }

    private List<Integer> seatID;
    @FXML
    private void getValues(ActionEvent event) throws IOException {
        String selectedLetter = lettersCombo.getValue();
        //System.out.println("Letter Selected: " + selectedLetter);

        List<Integer> selectedNumbers = new ArrayList<>();
        List<Seat> selectedSeats = new ArrayList<>();
        for (Node node : numbersBox.getChildren()) {
            CheckBox cb = (CheckBox) node;
            if (cb.isSelected()) {
                int seatNum = Integer.parseInt(cb.getText());
                for (Seat s : seatRepo.findAll()) {
                    if (s.getNum() == seatNum && s.getRow().equals(selectedLetter)) {
                        selectedSeats.add(s);
                        s.reserve(reservation);
                        break;
                    }
                }
                selectedNumbers.add(Integer.parseInt(cb.getText()));
            }
        }

        //System.out.println("Numbers Selected: " + selectedNumbers);
        seatID = new ArrayList<>();
        for(Seat s:selectedSeats){
            seatID.add(s.getSeatID());
        }
        if(show2.checkAvailable(seatID)) {
            show2.reserveSeats(reservation,seatID);
            System.out.print("Seats Selected: ");
            for (int i = 0; i < selectedNumbers.toArray().length; i++) {
                System.out.print(selectedLetter + selectedNumbers.toArray()[i] + ",");
            }
        }else{
            labelCheck.setVisible(true);
            labelCheck.setText("selected seats are Not Avaliable");
        }
    }

    private List<Integer> seatIDs;
    public void setseatIDs(List<Integer> seatIDSs){
        this.seatIDs = seatIDSs;
    }
    private Reservation reservation;
    private int hallid;
    public void sethallID(int hallid){
        this.hallid = hallid;
    }
}