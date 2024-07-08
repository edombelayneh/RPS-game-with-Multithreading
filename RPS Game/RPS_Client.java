/*
 * @File Name: RPS_Client.java
 * @Name: Edom Belayneh
 * @Homework #: 5
 * @Due Date: March 19, 2024 @ 11:59 pm
 * @Description: GUI of Client Side Rock Paper Scissors Game
 */


import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;	
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RPS_Client extends Application 
implements RPS_Constants {
	// Indicate whether the player has the turn
	private boolean myTurn = false;

	private static int player;
	// Create and initialize a title label
	private static Label titleLbl1 = new Label("Player 1");
	private static Label titleLbl2 = new Label("Player 2");
	private static Button playAgainBt1 = new Button("Play Again");
	private static HBox titleBox1 = new HBox(80);

	// Create and initialize a status label
	private static Label lblStatus1; 
	//private static Label lblStatus2; 

	//choice
	public static int choice1;
	public static int choice2;
	public static int c1;
	public static int c2;


	//Buttons
	private static Button[] buttons1;
	private static Button rockBt1;
	private static Button paperBt1;
	private static Button scissorsBt1;

	private static Button[] buttons2;
	private static Button rockBt2;
	private static Button paperBt2;
	private static Button scissorsBt2;

	private static int[] rowArr; 
	private static int[] columnArr;

	// Input and output streams from/to server
	private DataInputStream fromServer;
	private DataOutputStream toServer;

	// Continue to play?
	private boolean continueToPlay = true;


	// Host name or ip address
	private String host = "localhost";

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {

		titleBox();
		playerStage1();

		// Connect to the server
		connectToServer();
	}
	/**
	 * Set up the title box.
	 */
	private static void titleBox() {
		titleLbl1.setFont(Font.font("", FontWeight.BOLD, 24));
		titleLbl2.setFont(Font.font("", FontWeight.BOLD, 24));

		titleBox1.getChildren().addAll(titleLbl1, playAgainBt1, titleLbl2);
		titleBox1.setPadding(new Insets(0, 60, 0, 75)); 
	}

	// Create a method to generate player boxes
	private static void buttonImages() {

		// Create buttons for rock, paper, and scissors
		rockBt1 = new Button();
		rockBt1.setGraphic(new ImageView("http://people.se.cmich.edu/liao1q/classes/rock.jpg"));
		rockBt1.setBackground(Background.fill(Color.WHITESMOKE));
		rockBt1.setBorder(Border.stroke(Color.DARKGRAY));
		rockBt1.setOnAction(ae -> buttonHandlers(0, 0));

		paperBt1 = new Button();
		paperBt1.setGraphic(new ImageView("http://people.se.cmich.edu/liao1q/classes/paper.jpg"));
		paperBt1.setBackground(Background.fill(Color.WHITESMOKE));
		paperBt1.setBorder(Border.stroke(Color.DARKGRAY));
		paperBt1.setOnAction(ae -> buttonHandlers(0, 1));

		scissorsBt1 = new Button();
		scissorsBt1.setGraphic(new ImageView("http://people.se.cmich.edu/liao1q/classes/scissors.jpg"));
		scissorsBt1.setBackground(Background.fill(Color.WHITESMOKE));
		scissorsBt1.setBorder(Border.stroke(Color.DARKGRAY));
		scissorsBt1.setOnAction(ae -> buttonHandlers(0, 2));

		rockBt2 = new Button();
		rockBt2.setGraphic(new ImageView("http://people.se.cmich.edu/liao1q/classes/rock.jpg"));
		rockBt2.setBackground(Background.fill(Color.WHITESMOKE));
		rockBt2.setBorder(Border.stroke(Color.DARKGRAY));
		rockBt2.setOnAction(ae -> buttonHandlers(2, 0));

		paperBt2 = new Button();
		paperBt2.setGraphic(new ImageView("http://people.se.cmich.edu/liao1q/classes/paper.jpg"));
		paperBt2.setBackground(Background.fill(Color.WHITESMOKE));
		paperBt2.setBorder(Border.stroke(Color.DARKGRAY));
		paperBt2.setOnAction(ae -> buttonHandlers(2, 1));

		scissorsBt2 = new Button();
		scissorsBt2.setGraphic(new ImageView("http://people.se.cmich.edu/liao1q/classes/scissors.jpg"));
		scissorsBt2.setBackground(Background.fill(Color.WHITESMOKE));
		scissorsBt2.setBorder(Border.stroke(Color.DARKGRAY));
		scissorsBt2.setOnAction(ae -> buttonHandlers(2, 2));

		buttons1 = new Button[3];
		buttons1[0] = rockBt1;
		buttons1[1] = paperBt1;
		buttons1[2] = scissorsBt1;

		buttons2 = new Button[3];
		buttons2[0] = rockBt2;
		buttons2[1] = paperBt2;
		buttons2[2] = scissorsBt2;
	}

	/**
	 * Handle button click events.
	 *
	 * @param row    The row index of the button.
	 * @param column The column index of the button.
	 */
	private static void buttonHandlers(int row, int column) {
		rowArr = new int[6];
		columnArr = new int[6];

		for(int i = 0; i < 6; i++) {
			rowArr[i] = 1000;
		}

		for(int i = 0; i < 6; i++) {
			if(rowArr[i] == 1000 ) {
				rowArr[i] = row;
				columnArr[i] = column;
				break;
			}
		}
	}

	/**
	 * Set up the GUI for player stage 1.
	 */
	private void playerStage1() {
		lblStatus1 = new Label(" ");

		buttonImages();
		Stage primaryStage1 = new Stage();

		// Pane to hold cell
		GridPane pane = new GridPane(); 
		//pane.setGridLinesVisible(true);
		pane.setPadding(new Insets(0, 10, 0, 10));

		// Create 3 rows and 3 columns
		for (int i = 0; i < 3; i++) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100.0 / 3);
			pane.getRowConstraints().add(row);
		}

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(100.0 / 3); 
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(40.0 / 3); // Half the width of other columns
		ColumnConstraints col3 = new ColumnConstraints();
		col3.setPercentWidth(100.0 / 3);
		pane.getColumnConstraints().addAll(col1, col2, col3);

		//implement a method where the pane adds the buttons based on the player
		pane.add(buttons1[0], 0, 0);
		pane.add(buttons1[1], 0, 1);
		pane.add(buttons1[2], 0, 2);

		pane.add(buttons2[0], 2, 0);
		pane.add(buttons2[1], 2, 1);
		pane.add(buttons2[2], 2, 2);


		HBox boxStatus = new HBox();
		boxStatus.getChildren().add(lblStatus1);
		boxStatus.setBackground(Background.fill(Color.WHITESMOKE));
		boxStatus.setPadding(new Insets(0, 10, 0, 10));

		BorderPane borderPane = new BorderPane();
		borderPane.setTop(titleBox1);
		borderPane.setCenter(pane);
		borderPane.setBottom(boxStatus);
		borderPane.setBackground(Background.fill(Color.LIGHTGREY));

		// Create a scene and place it in the stage
		Scene scene = new Scene(borderPane,542, 700);
		primaryStage1.setTitle("Rock Paper Scissors Client"); // Set the stage title
		primaryStage1.setScene(scene); // Place the scene in the stage
		primaryStage1.show(); // Display the stage 
	}


	/**
	 * Establishes connection to the server.
	 */
	private void connectToServer() {

		try {
			// Create a socket to connect to the server
			Socket socket = new Socket(host, 8000);

			// Create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());

			// Create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		// Control the game on a separate thread
		new Thread(() -> {
			try {
				// Get notification from the server
				player = fromServer.readInt();
				System.out.println("1st Client readInt: "+ player);

				// Am I player 1 or 2?
				if (player == PLAYER1) {
					Platform.runLater(() -> {
						updateUIForPlayer1();
						hide(buttons2);
					});
					fromServer.readInt();
					Platform.runLater(() -> {
						updateUIForPlayer1Connected();
					});


					// It is my turn
					myTurn = true;
				}
				else if (player == PLAYER2) {
					Platform.runLater(() -> {
						updateUIForPlayer2();
						hide(buttons1);       
					});
					fromServer.readInt();
				}

				// Continue to play
				while (continueToPlay) {

					if (player == PLAYER1) {
						waitForPlayerAction(buttons1, PLAYER1); // Wait for player 1 to move
					} 
					else if (player == PLAYER2) {
						waitForPlayerAction(buttons2, PLAYER2); // Wait for player 2 to move
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	/**
	 * Wait for player action.
	 *
	 * @param buttons The array of buttons to wait for actions on.
	 * @param player  The player whose action is being waited for.
	 */
	private void waitForPlayerAction(Button[] buttons, int player) {
		for (Button button : buttons) {
			button.setOnAction(event -> {
				try {
					int choice;
					if (button.equals(rockBt1) || button.equals(rockBt2)) {
						choice = ROCK;
					} else if (button.equals(paperBt1) || button.equals(paperBt2)) {
						choice = PAPER;
					} else if (button.equals(scissorsBt1) || button.equals(scissorsBt2)) {
						choice = SCISSORS;
					} else {
						// Handle other buttons if needed
						return;
					}

					if (player == PLAYER1) {
						choice1 = choice;
						c1 = choice;
						try {
							displayChoice(buttons1, choice1);

						} catch (Exception e) {

							e.printStackTrace();
						}
					} 

					else if (player == PLAYER2) {
						choice2 = choice;
						c2 = choice;
						try {
							displayChoice(buttons2, choice2);

						} catch (Exception e) {

							e.printStackTrace();
						}

					}

					sendMove(choice);

					int result = receiveInfoFromServer(); // Receive info from the server
					if (player == PLAYER1) {
						displayChoiceOpponent(buttons2, result);
					}
					else if (player == PLAYER2) {
						displayChoiceOpponent(buttons1, result);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		// Optionally, handle other buttons like playAgainBt1
		playAgainBt1.setOnAction(ae -> reset());


	}

	/**
	 * Reset the game.
	 */
	private void reset() {
		// Reset game state variables
		continueToPlay = true; // Assuming this flag controls the game loop
		choice1 = -1; // Or some default value indicating no choice
		choice2 = -1;
		lblStatus1.setText(""); // Clear any previous messages

		if(player == PLAYER1) {
			for (int i = 0; i < 3; i++) {
				buttons1[i].setVisible(true);
				buttons1[i].setDisable(false); // Re-enable buttons
			}
		}
		else if(player == PLAYER2) {
			for (int i = 0; i < 3; i++) {
				buttons2[i].setVisible(true);
				buttons2[i].setDisable(false); // Re-enable buttons
			}
		}
		//reset values somewhere...
		connectToServer();
	}

	/**
	 * Display the chosen button and hide others.
	 *
	 * @param buttonArr The array of buttons to manipulate.
	 * @param choice    The choice made.
	 * @throws Exception If an error occurs.
	 */
	private void displayChoice(Button[] buttonArr, int choice) throws Exception {
		System.out.println("Choice1: " + choice);
		switch(choice) {
		case 6:
			choice = 0;
			break;
		case 7:
			choice = 1;
			break;
		case 8: 
			choice = 2;
			break;
		default:
			throw new Exception("Error");
		}

		for (int i = 0; i < 3; i++) {
			if(i != choice) {
				buttonArr[i].setVisible(false);
			}
		}
	}

	/**
	 * Display the opponent's choice.
	 *
	 * @param buttonArr The array of buttons to manipulate.
	 * @param result    The opponent's choice.
	 * @throws Exception If an error occurs.
	 */
	private void displayChoiceOpponent(Button[] buttonArr, int result) throws Exception {
		System.out.println("Choice2: " + result);

		System.out.println("Player: " + player);
		switch(result) {
		case 1:
			if(player == PLAYER1) {
				System.out.println("Im here 1");
				//if it is player 1 wins
				//chooses rock then p2 chose scissors
				if(choice1 == 6) {
					buttonArr[2].setVisible(true);
				}
				//paper then p2 is rock
				else if(choice1 == 7) {
					buttonArr[0].setVisible(true);
				}
				//scissors then p2 is paper
				else if(choice1 == 8) {
					buttonArr[1].setVisible(true);
				}
			}
			if(player == PLAYER2) {
				System.out.println("Im here 2");
				//if it is player 2's plane and player two lost
				//rock then p1 chose paper
				if(choice2 == 6) {
					buttonArr[1].setVisible(true);
				}
				//paper then p1 is 
				else if(choice2 == 7) {
					buttonArr[2].setVisible(true);
				}
				//scissors then p1 is paper
				else if(choice2 == 8) {
					buttonArr[0].setVisible(true);
				}
			}
			break;
		case 2:
			//player2 won
			if(player == PLAYER1) {
				//if it is player 1 wins
				//chooses rock then p2 chose scissors
				if(choice1 == 6) {
					buttonArr[1].setVisible(true);
				}
				//paper then p2 is rock
				else if(choice1 == 7) {
					buttonArr[2].setVisible(true);
				}
				//scissors then p2 is paper
				else if(choice1 == 8) {
					buttonArr[0].setVisible(true);
				}
			}
			if(player == PLAYER2) {
				//if it is player 2's plane and player two lost
				//rock then p1 chose paper
				if(choice2 == 6) {
					buttonArr[2].setVisible(true);
				}
				//paper then p1 is 
				else if(choice2 == 7) {
					buttonArr[0].setVisible(true);
				}
				//scissors then p1 is paper
				else if(choice2 == 8) {
					buttonArr[1].setVisible(true);
				}
			}
			break;
		case 3: 
			if(player == PLAYER1) {
				//if it is player 2's plane and player two lost
				//rock then p1 chose paper
				if(choice1 == 6) {
					buttonArr[0].setVisible(true);
				}
				//paper then p1 is 
				else if(choice1 == 7) {
					buttonArr[1].setVisible(true);
				}
				//scissors then p1 is paper
				else if(choice1 == 8) {
					buttonArr[2].setVisible(true);
				}
			}
			if(player == PLAYER2) {
				//if it is player 2's plane and player two lost
				//rock then p1 chose paper
				if(choice2 == 6) {
					buttonArr[0].setVisible(true);
				}
				//paper then p1 is 
				else if(choice2 == 7) {
					buttonArr[1].setVisible(true);
				}
				//scissors then p1 is paper
				else if(choice2 == 8) {
					buttonArr[2].setVisible(true);
				}
			}
			break;
		default:
			throw new Exception("Error");
		}
	}

	/**
	 * Receive information from the server.
	 *
	 * @return The status received from the server.
	 * @throws IOException If an I/O error occurs.
	 */
	private int receiveInfoFromServer() throws IOException {
		// Receive game status
		int status = fromServer.readInt();
		System.out.println("2nd Client readInt: " + status);

		if (status == PLAYER1_WON) {
			// Player 1 won, stop playing
			continueToPlay = false;
			if(player == PLAYER1) {
				Platform.runLater(() -> {
					lblStatus1.setText("I won!");
				});
			}
			else if(player == PLAYER2) {
				Platform.runLater(() -> {
					lblStatus1.setText("Player 1 has won!");
				});

			}

		}
		else if (status == PLAYER2_WON) {
			// Player 2 won, stop playing
			continueToPlay = false;

			if(player == PLAYER1) {
				Platform.runLater(() -> {
					lblStatus1.setText("Player 2 has won!");
				});
			}
			else if(player == PLAYER2) {
				Platform.runLater(() -> {
					lblStatus1.setText("I won!");
				});
			}
		}

		else if (status == DRAW) {
			// No winner, game is over
			continueToPlay = false;
			Platform.runLater(() -> {
				lblStatus1.setText("Game is over, no winner!");
			});

			if (player == PLAYER2) {
				Platform.runLater(() -> {
					lblStatus1.setText("Game is over, no winner!");
				});}


		}

		return status;
	}

	// UI update methods
	private void updateUIForPlayer1() {
		lblStatus1.setText("Waiting for player 2 to join");
	}

	/**
	 * Update UI for player 1 when connected.
	 */
	private void updateUIForPlayer1Connected() {
		lblStatus1.setText("Connected to the server. You are now Player 1. Make your Selection.");
	}

	/**
	 * Update UI for player 2.
	 */
	private void updateUIForPlayer2() {
		lblStatus1.setText("Connected to the server. You are now Player 2. Make your Selection.");
	}

	/**
	 * Send the player's move to the server.
	 *
	 * @param move The move to send.
	 * @throws IOException If an I/O error occurs.
	 */
	private void sendMove(int move ) throws IOException {
		try {
			toServer.writeInt(move);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Hide buttons.
	 *
	 * @param buttonArr The array of buttons to hide.
	 */
	private void hide(Button[] buttonArr) {
		for(int i = 0; i < 3; i++) {
			buttonArr[i].setVisible(false);
		}

	}

	/**
	 * The main method is only needed for the IDE with limited
	 * JavaFX support. Not needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}


